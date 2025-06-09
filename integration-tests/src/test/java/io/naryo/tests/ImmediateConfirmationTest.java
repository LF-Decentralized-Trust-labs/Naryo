package io.naryo.tests;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

import io.naryo.BaseIntegrationTest;
import io.naryo.dto.event.ContractEventStatus;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.message.ContractEvent;
import io.naryo.dto.message.TransactionEvent;
import io.naryo.dto.transaction.TransactionStatus;
import io.naryo.model.TransactionIdentifierType;
import io.naryo.utils.EventFilterCreator;
import io.naryo.utils.TransactionMonitorCreator;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.awaitility.Durations;
import org.web3j.crypto.Hash;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static io.naryo.utils.KafkaConsumerFactory.createKafkaConsumer;
import static io.naryo.utils.StringManipulation.stringToBytes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles(value = {"mongo", "kafka"})
@ContextConfiguration(initializers = ImmediateConfirmationTest.Initializer.class)
class ImmediateConfirmationTest extends BaseIntegrationTest {

    protected static final String CONTRACT_TOPIC = "contract-events-" + UUID.randomUUID();
    protected static final String TRANSACTION_TOPIC = "transaction-events-" + UUID.randomUUID();
    protected static KafkaConsumer<String, String> contractConsumer;
    protected static KafkaConsumer<String, String> transactionConsumer;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        contractConsumer =
                createKafkaConsumer(kafkaContainer.getBootstrapServers(), CONTRACT_TOPIC);
        transactionConsumer =
                createKafkaConsumer(kafkaContainer.getBootstrapServers(), TRANSACTION_TOPIC);
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                            "kafka.topic.contractEvents=" + CONTRACT_TOPIC,
                            "kafka.topic.transactionEvents=" + TRANSACTION_TOPIC,
                            "broadcaster.event.confirmation.numBlocksToWait=0")
                    .applyTo(context);
        }
    }

    @Test
    void testBroadcastsUnconfirmedEventAfterInitialEmit() throws Exception {
        ContractEventFilter eventFilter =
                EventFilterCreator.buildDummyEventFilter(defaultContract.getContractAddress());
        eventFilterCreator.createFilter(eventFilter);

        TransactionReceipt txReceipt =
                defaultContract
                        .emitEvent(stringToBytes("BytesValue"), BigInteger.TEN, "StringValue")
                        .send();

        final Optional<ContractEvent> contractEvent =
                kafkaConsumerFactory.getTransactionalMessage(
                        contractConsumer,
                        ContractEvent.class,
                        CONTRACT_TOPIC,
                        Durations.ONE_MINUTE,
                        txReceipt.getTransactionHash());

        assertTrue(contractEvent.isPresent());
        assertEquals(ContractEventStatus.CONFIRMED, contractEvent.get().getDetails().getStatus());
    }

    @Test
    void testBroadcastsUnconfirmedTransactionAfterInitialMining() throws Exception {
        final String signedTxHex = createRawSignedTransaction(ZERO_ADDRESS);
        final String txHash = Hash.sha3(signedTxHex);
        assertEquals(txHash, sendRawTransaction(signedTxHex));

        transactionMonitorCreator.createTransactionMonitor(
                TransactionMonitorCreator.buildTransactionMonitor(
                        TransactionIdentifierType.HASH, txHash));

        Optional<TransactionEvent> transaction =
                kafkaConsumerFactory.getTransactionMessage(
                        transactionConsumer,
                        TransactionEvent.class,
                        TRANSACTION_TOPIC,
                        Durations.ONE_MINUTE,
                        txHash,
                        TransactionStatus.CONFIRMED);

        assertTrue(transaction.isPresent());
    }
}
