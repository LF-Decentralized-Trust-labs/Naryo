package io.naryo.infrastructure.store.event.persistence.entity.block;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import io.naryo.application.node.interactor.block.dto.hedera.HederaTransaction;
import io.naryo.application.node.interactor.block.dto.hedera.TransactionName;
import io.naryo.application.node.interactor.block.dto.hedera.Transfer;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.event.block.BlockEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HederaBlockEventTransactionEntityTest {

    private static final UUID NODE_ID = UUID.randomUUID();
    private static final BigInteger BLOCK_NUMBER = BigInteger.valueOf(40902448);
    private static final String BLOCK_HASH =
            "0x382044ad96bf805893584797665f4c525fe824378dd1aceb4a06189e47832a29413002c8a398a4c07c2167896884a96b";
    private static final String ZERO_LOGS_BLOOM = "0x" + "0".repeat(512);
    private static final String NODE_STAKE_UPDATE_HASH =
            "sVo0E8IOphQ9ADjfu9fP9ym4Ffyoc/1F8lHHA4wFcHA+3YjyNcugoxLLqoq9GfBJ";
    private static final String SUBMIT_MESSAGE_HASH =
            "LMREzK1slPPggwJWG58vUifjKYiUsWK8Lg6RvWaaRHMbaPzbdifPc5meoxRT2irF";

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:14.7");

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired private TestEntityManager entityManager;

    @Test
    void persistsBlockWhoseTransactionHasNoSender() {
        BlockEvent block =
                new BlockEvent(
                        NODE_ID,
                        new NonNegativeBlockNumber(BLOCK_NUMBER),
                        BLOCK_HASH,
                        ZERO_LOGS_BLOOM,
                        BigInteger.valueOf(13607),
                        BigInteger.ZERO,
                        BigInteger.valueOf(1790207998),
                        List.of(nodeStakeUpdate(), consensusSubmitMessage()));

        entityManager.persistAndFlush(BlockEventEntity.fromBlockEvent(block));
        entityManager.clear();

        BlockEventEntity stored = entityManager.find(BlockEventEntity.class, BLOCK_HASH);
        assertEquals(2, stored.getTransactions().size());
        HederaBlockEventTransactionEntity nodeStakeUpdate =
                entityManager.find(HederaBlockEventTransactionEntity.class, NODE_STAKE_UPDATE_HASH);
        assertNull(nodeStakeUpdate.getFrom());
        assertEquals(TransactionName.NODESTAKEUPDATE, nodeStakeUpdate.getName());
        HederaBlockEventTransactionEntity submitMessage =
                entityManager.find(HederaBlockEventTransactionEntity.class, SUBMIT_MESSAGE_HASH);
        assertEquals("0.0.5146699", submitMessage.getFrom());
    }

    private static HederaTransaction nodeStakeUpdate() {
        return hederaTransaction(
                NODE_STAKE_UPDATE_HASH,
                null,
                null,
                null,
                "0",
                "1790208000.027803565",
                0,
                null,
                TransactionName.NODESTAKEUPDATE,
                null,
                1,
                List.of());
    }

    private static HederaTransaction consensusSubmitMessage() {
        return hederaTransaction(
                SUBMIT_MESSAGE_HASH,
                "0.0.5146699",
                "0.0.802",
                "339005",
                "200000000",
                "1790208000.027803566",
                339005,
                "0.0.5146701",
                TransactionName.CONSENSUSSUBMITMESSAGE,
                "0.0.7",
                0,
                List.of(
                        new Transfer("0.0.802", BigInteger.valueOf(339005), false),
                        new Transfer("0.0.5146699", BigInteger.valueOf(-339005), false)));
    }

    private static HederaTransaction hederaTransaction(
            String hash,
            String from,
            String to,
            String value,
            String maxFee,
            String consensusTimestamp,
            Integer chargedTxFee,
            String entityId,
            TransactionName name,
            String node,
            Integer nonce,
            List<Transfer> transfers) {
        return new HederaTransaction(
                hash,
                BLOCK_NUMBER,
                from,
                to,
                value,
                maxFee,
                consensusTimestamp,
                "SUCCESS",
                null,
                null,
                chargedTxFee,
                entityId,
                List.of(),
                "",
                name,
                List.of(),
                node,
                nonce,
                null,
                false,
                List.of(),
                List.of(),
                "0.0.5146699-1790207990-410574817",
                transfers,
                null,
                "1790207990.410574817",
                consensusTimestamp);
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EntityScan(basePackageClasses = BlockEventEntity.class)
    static class TestConfiguration {}
}
