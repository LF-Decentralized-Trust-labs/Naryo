package io.naryo.infrastructure.store.event.persistence.entity.block;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.application.node.interactor.block.dto.eth.EthTransaction;
import io.naryo.application.node.interactor.block.dto.hedera.BatchKey;
import io.naryo.application.node.interactor.block.dto.hedera.BatchKeyType;
import io.naryo.application.node.interactor.block.dto.hedera.HederaTransaction;
import io.naryo.application.node.interactor.block.dto.hedera.TransactionName;
import io.naryo.application.node.interactor.block.dto.hedera.Transfer;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.transaction.eth.EthTransactionEvent;
import io.naryo.infrastructure.JpaPersistenceConfig;
import io.naryo.infrastructure.store.event.persistence.entity.transaction.EthTransactionEventEntity;
import io.naryo.infrastructure.store.event.persistence.entity.transaction.TransactionEventEntity;
import io.naryo.infrastructure.store.event.persistence.repository.BlockEventEntityRepository;
import io.naryo.infrastructure.store.event.persistence.repository.TransactionEventEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BlockEventEntityColumnLengthTest.Config.class)
class BlockEventEntityColumnLengthTest {

    @SpringBootConfiguration
    @Import(JpaPersistenceConfig.class)
    static class Config {}

    @Container @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:14.7");

    private static final UUID NODE_ID = UUID.randomUUID();
    private static final BigInteger BLOCK_NUMBER = BigInteger.valueOf(40930462);
    private static final String BLOCK_HASH =
            "0x8f2b0d4e3a1c5b7d9e0f1a2b3c4d5e6f708192a3b4c5d6e7f8091a2b3c4d5e6f";
    private static final String LOGS_BLOOM = hex(512);

    @Autowired private BlockEventEntityRepository blockEventEntityRepository;
    @Autowired private TransactionEventEntityRepository transactionEventEntityRepository;

    @Test
    void savesHederaBlockWithContractResultTransaction() {
        BlockEvent block =
                blockEvent(
                        List.of(
                                hederaTransaction("hcs-" + BLOCK_NUMBER),
                                contractResult(BLOCK_HASH.replace("8f2b", "aaaa"), 136)));

        blockEventEntityRepository.saveAndFlush(BlockEventEntity.fromBlockEvent(block));

        assertThat(blockEventEntityRepository.findByNumber(BLOCK_NUMBER))
                .map(BlockEventEntity::toBlockEvent)
                .get()
                .satisfies(
                        stored -> {
                            assertThat(stored.getTransactions()).hasSize(2);
                            assertThat(stored.getTransactions())
                                    .filteredOn(EthTransaction.class::isInstance)
                                    .map(EthTransaction.class::cast)
                                    .singleElement()
                                    .satisfies(
                                            tx -> {
                                                assertThat(tx.getLogBloom()).hasSize(514);
                                                assertThat(tx.getInput()).hasSize(138);
                                            });
                        });
    }

    @Test
    void savesEthereumBlockWithContractCallInput() {
        EthTransaction contractCall =
                new EthTransaction(
                        BLOCK_HASH.replace("8f2b", "bbbb"),
                        BLOCK_NUMBER,
                        "0x000000000000000000000000000000000060c8c4",
                        "0x00000000000000000000000000000000005f6f0a",
                        "0",
                        null,
                        null,
                        null,
                        BigInteger.ZERO,
                        BigInteger.valueOf(7),
                        BLOCK_HASH,
                        hex(4936),
                        null,
                        null);

        blockEventEntityRepository.saveAndFlush(
                BlockEventEntity.fromBlockEvent(blockEvent(List.of(contractCall))));

        assertThat(blockEventEntityRepository.findByNumber(BLOCK_NUMBER))
                .map(BlockEventEntity::toBlockEvent)
                .get()
                .extracting(BlockEvent::getTransactions)
                .asInstanceOf(org.assertj.core.api.InstanceOfAssertFactories.LIST)
                .singleElement()
                .isInstanceOf(EthTransaction.class)
                .extracting(tx -> ((EthTransaction) tx).getInput())
                .isEqualTo(hex(4936));
    }

    @Test
    void savesEthereumTransactionEventWithContractDeploymentInput() {
        EthTransactionEvent event =
                new EthTransactionEvent(
                        NODE_ID,
                        BLOCK_HASH.replace("8f2b", "cccc"),
                        TransactionStatus.CONFIRMED,
                        new NonNegativeBlockNumber(BLOCK_NUMBER),
                        BigInteger.valueOf(1790265390L),
                        "0x000000000000000000000000000000000060c8c4",
                        "0x00000000000000000000000000000000005f6f0a",
                        "0",
                        BLOCK_HASH,
                        new NonNegativeBlockNumber(BigInteger.valueOf(7)),
                        BigInteger.ZERO,
                        hex(11016),
                        "");

        transactionEventEntityRepository.saveAndFlush(
                TransactionEventEntity.fromTransactionEvent(event));

        assertThat(transactionEventEntityRepository.findByHash(event.getHash()))
                .get()
                .isInstanceOf(EthTransactionEventEntity.class)
                .extracting(entity -> ((EthTransactionEventEntity) entity).getInput())
                .isEqualTo(hex(11016));
    }

    @Test
    void savesHederaTransactionWithMirrorNodeMaximumSizes() {
        BlockEvent block = blockEvent(List.of(hederaTransaction("hcs-max-" + BLOCK_NUMBER)));

        blockEventEntityRepository.saveAndFlush(BlockEventEntity.fromBlockEvent(block));

        assertThat(blockEventEntityRepository.findByNumber(BLOCK_NUMBER))
                .map(BlockEventEntity::toBlockEvent)
                .get()
                .extracting(BlockEvent::getTransactions)
                .asInstanceOf(org.assertj.core.api.InstanceOfAssertFactories.LIST)
                .singleElement()
                .isInstanceOf(HederaTransaction.class)
                .extracting(tx -> ((HederaTransaction) tx).getMemoBase64())
                .isEqualTo("A".repeat(136));
    }

    private static BlockEvent blockEvent(List<? extends Transaction> transactions) {
        return new BlockEvent(
                NODE_ID,
                new NonNegativeBlockNumber(BLOCK_NUMBER),
                BLOCK_HASH,
                LOGS_BLOOM,
                BigInteger.valueOf(8192),
                BigInteger.ZERO,
                BigInteger.valueOf(1790265390L),
                transactions);
    }

    private static EthTransaction contractResult(String hash, int inputHexDigits) {
        return new EthTransaction(
                hash,
                BLOCK_NUMBER,
                "0x000000000000000000000000000000000060c8c4",
                "0x00000000000000000000000000000000005f6f0a",
                "0",
                "0x",
                "1790265390.056902104",
                "0x1",
                BigInteger.ZERO,
                BigInteger.valueOf(7),
                BLOCK_HASH,
                hex(inputHexDigits),
                LOGS_BLOOM,
                null);
    }

    private static HederaTransaction hederaTransaction(String hash) {
        return new HederaTransaction(
                hash,
                BLOCK_NUMBER,
                "0.0.6271551",
                "0.0.800",
                "9",
                "100000000",
                "1790265390.056902104",
                "SUCCESS",
                new BatchKey(BatchKeyType.ED25519, hex(64).substring(2)),
                null,
                8367,
                "0.0.6271554",
                List.of(),
                "A".repeat(136),
                TransactionName.CONSENSUSSUBMITMESSAGE,
                List.of(),
                "0.0.3",
                0,
                null,
                false,
                List.of(),
                List.of(),
                "0.0.6271551-1790265381-705767903",
                List.of(
                        new Transfer("0.0.3", BigInteger.valueOf(451), false),
                        new Transfer("0.0.98", BigInteger.valueOf(7124), false),
                        new Transfer("0.0.800", BigInteger.valueOf(792), false),
                        new Transfer("0.0.6271551", BigInteger.valueOf(-8367), false)),
                "120",
                "1790265381.705767903",
                "1790265390.056902104");
    }

    private static String hex(int digits) {
        return "0x" + "a5".repeat(digits / 2).substring(0, digits);
    }
}
