package io.naryo.infrastructure.broadcaster.rabbitmq.producer;

import java.math.BigInteger;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.node.interactor.block.dto.hedera.TransactionName;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterBuilder;
import io.naryo.domain.broadcaster.target.TransactionBroadcasterTargetBuilder;
import io.naryo.domain.common.Destination;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.configuration.broadcaster.BroadcasterCache;
import io.naryo.domain.configuration.broadcaster.rabbitmq.Exchange;
import io.naryo.domain.configuration.broadcaster.rabbitmq.RabbitMqBroadcasterConfiguration;
import io.naryo.domain.event.transaction.eth.EthTransactionEvent;
import io.naryo.domain.event.transaction.hedera.HederaTransactionEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitMqBroadcasterProducerTest {

    private static final String EXCHANGE = "naryo";
    private static final String DESTINATION = "penguin.transactions";
    private static final String PAYLOAD = "{}";

    // transaction_hash as returned by the Hedera mirror node (standard base64 of a 48 byte hash)
    private static final String HEDERA_TRANSACTION_HASH =
            "YQPmgiKA4VDEkiwTEt5w31/mzgtsbei29z2z4kpbqKOiS98632EK++xw6PH+3ESU";
    private static final String ETH_TRANSACTION_HASH =
            "0x663886ddc91691641f50515229d7187097ee511cca381f26c881fc75dc553280";

    @Mock private RabbitTemplate rabbitTemplate;
    @Mock private ObjectMapper objectMapper;

    @Test
    void produce_withHederaTransactionHash_publishesUrlSafeRoutingKey()
            throws JsonProcessingException {
        HederaTransactionEvent event = hederaTransactionEvent(HEDERA_TRANSACTION_HASH);
        doReturn(PAYLOAD).when(objectMapper).writeValueAsString(event);

        RabbitMqBroadcasterProducer producer =
                new RabbitMqBroadcasterProducer(rabbitTemplate, objectMapper);

        assertDoesNotThrow(
                () -> producer.produce(transactionBroadcaster(), configuration(), event));
        verify(rabbitTemplate)
                .convertAndSend(
                        EXCHANGE,
                        DESTINATION
                                + ".YQPmgiKA4VDEkiwTEt5w31_mzgtsbei29z2z4kpbqKOiS98632EK--xw6PH-3ESU",
                        PAYLOAD);
    }

    @Test
    void produce_withEthTransactionHash_publishesRoutingKeyUnchanged()
            throws JsonProcessingException {
        EthTransactionEvent event = ethTransactionEvent(ETH_TRANSACTION_HASH);
        doReturn(PAYLOAD).when(objectMapper).writeValueAsString(event);

        RabbitMqBroadcasterProducer producer =
                new RabbitMqBroadcasterProducer(rabbitTemplate, objectMapper);

        assertDoesNotThrow(
                () -> producer.produce(transactionBroadcaster(), configuration(), event));
        verify(rabbitTemplate)
                .convertAndSend(EXCHANGE, DESTINATION + "." + ETH_TRANSACTION_HASH, PAYLOAD);
    }

    private static Broadcaster transactionBroadcaster() {
        return new BroadcasterBuilder()
                .withTarget(
                        new TransactionBroadcasterTargetBuilder()
                                .withDestination(new Destination(DESTINATION))
                                .build())
                .build();
    }

    private static RabbitMqBroadcasterConfiguration configuration() {
        return new RabbitMqBroadcasterConfiguration(
                UUID.randomUUID(),
                new BroadcasterCache(Duration.ofMinutes(1)),
                new Exchange(EXCHANGE));
    }

    private static HederaTransactionEvent hederaTransactionEvent(String hash) {
        return new HederaTransactionEvent(
                UUID.randomUUID(),
                hash,
                TransactionStatus.CONFIRMED,
                new NonNegativeBlockNumber(BigInteger.valueOf(40930434)),
                BigInteger.valueOf(1790265330),
                "0.0.7791423",
                "0.0.98",
                "0",
                null,
                null,
                0,
                "0.0.7791425",
                List.of(),
                "",
                TransactionName.CONSENSUSSUBMITMESSAGE,
                List.of(),
                "0.0.3",
                0,
                null,
                false,
                List.of(),
                List.of(),
                "0.0.7791423-1790265330-302520166",
                List.of(),
                "120",
                "1790265330.302520166",
                "1790265343.795003362",
                null);
    }

    private static EthTransactionEvent ethTransactionEvent(String hash) {
        return new EthTransactionEvent(
                UUID.randomUUID(),
                hash,
                TransactionStatus.CONFIRMED,
                new NonNegativeBlockNumber(BigInteger.valueOf(40930434)),
                BigInteger.valueOf(1790265330),
                "0x0000000000000000000000000000000000000001",
                "0x0000000000000000000000000000000000000002",
                "0",
                "0x00000000000000000000000000000000000000000000000000000000000000ab",
                new NonNegativeBlockNumber(BigInteger.ONE),
                BigInteger.ZERO,
                "0x",
                "");
    }
}
