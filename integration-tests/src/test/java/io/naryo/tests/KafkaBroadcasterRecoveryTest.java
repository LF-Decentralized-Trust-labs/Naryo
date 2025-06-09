package io.naryo.tests;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import io.naryo.BaseIntegrationTest;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.ContractEventStatus;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.message.BlockEvent;
import io.naryo.dto.message.ContractEvent;
import io.naryo.dto.message.TransactionEvent;
import io.naryo.dto.transaction.TransactionStatus;
import io.naryo.model.TransactionIdentifierType;
import io.naryo.utils.EventFilterCreator;
import io.naryo.utils.EventVerification;
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
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static io.naryo.utils.KafkaConsumerFactory.createKafkaConsumer;
import static io.naryo.utils.StringManipulation.stringToBytes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles(value = {"mongo", "kafka"})
@ContextConfiguration(initializers = KafkaBroadcasterRecoveryTest.Initializer.class)
public class KafkaBroadcasterRecoveryTest extends BaseIntegrationTest {

    // "BytesValue" in hex
    private static final String BYTES_VALUE_HEX =
            "0x427974657356616c756500000000000000000000000000000000000000000000";
    private static final String CONTRACT_TOPIC = "contract-events-" + UUID.randomUUID();
    private static final String BLOCK_TOPIC = "block-events-" + UUID.randomUUID();
    private static final String TRANSACTION_TOPIC = "transaction-events-" + UUID.randomUUID();
    private static KafkaConsumer<String, String> contractConsumer;
    private static KafkaConsumer<String, String> blockConsumer;
    private static KafkaConsumer<String, String> transactionConsumer;

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                            "broadcaster.enableBlockNotifications=true",
                            "kafka.topic.contractEvents=" + CONTRACT_TOPIC,
                            "kafka.topic.blockEvents=" + BLOCK_TOPIC,
                            "kafka.topic.transactionEvents=" + TRANSACTION_TOPIC)
                    .applyTo(context);
        }
    }

    @BeforeEach
    void beforeAll() {
        contractConsumer =
                createKafkaConsumer(kafkaContainer.getBootstrapServers(), CONTRACT_TOPIC);
        blockConsumer = createKafkaConsumer(kafkaContainer.getBootstrapServers(), BLOCK_TOPIC);
        transactionConsumer =
                createKafkaConsumer(kafkaContainer.getBootstrapServers(), TRANSACTION_TOPIC);
    }

    @Test
    void broadcastMissedBlocksOnStartupAfterFailureTest() throws Exception {
        BigInteger blockNumber = web3j.ethBlockNumber().send().getBlockNumber();
        mineBlocks(1);
        assertTrue(getBlockEvent(BigInteger.ONE.add(blockNumber)).isPresent());

        restartKafka(
                () -> {
                    try {
                        mineBlocks(3);
                    } catch (ExecutionException | InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        assertTrue(getBlockEvent(BigInteger.valueOf(4).add(blockNumber)).isPresent());
    }

    @Test
    void broadcastUnconfirmedEventAfterFailureTest() throws Exception {
        ContractEventFilter eventFilter =
                EventFilterCreator.buildDummyEventFilter(defaultContract.getContractAddress());
        eventFilterCreator.createFilter(eventFilter);

        AtomicReference<TransactionReceipt> txReceipt = new AtomicReference<>();
        restartKafka(
                () -> {
                    try {
                        txReceipt.set(
                                defaultContract
                                        .emitEvent(
                                                stringToBytes("BytesValue"),
                                                BigInteger.TEN,
                                                "StringValue")
                                        .send());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        verifyEvent(txReceipt.get(), eventFilter, ContractEventStatus.UNCONFIRMED);
    }

    @Test
    void broadcastConfirmedEventAfter12BlocksWhenDownTest() throws Exception {
        ContractEventFilter eventFilter =
                EventFilterCreator.buildDummyEventFilter(defaultContract.getContractAddress());
        eventFilterCreator.createFilter(eventFilter);

        AtomicReference<TransactionReceipt> txReceipt = new AtomicReference<>();
        restartKafka(
                () -> {
                    try {
                        txReceipt.set(
                                defaultContract
                                        .emitEvent(
                                                stringToBytes("BytesValue"),
                                                BigInteger.TEN,
                                                "StringValue")
                                        .send());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        verifyEvent(txReceipt.get(), eventFilter, ContractEventStatus.UNCONFIRMED);

        restartKafka(
                () -> {
                    try {
                        mineBlocks(12);
                    } catch (ExecutionException | InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        verifyEvent(txReceipt.get(), eventFilter, ContractEventStatus.CONFIRMED);
    }

    @Test
    void broadcastTransactionUnconfirmedAfterFailureTest() throws Exception {
        final String signedTxHex = createRawSignedTransaction(ZERO_ADDRESS);
        final String txHash = Hash.sha3(signedTxHex);
        assertEquals(txHash, sendRawTransaction(signedTxHex));

        restartKafka(
                () -> {
                    transactionMonitorCreator.createTransactionMonitor(
                            TransactionMonitorCreator.buildTransactionMonitor(
                                    TransactionIdentifierType.HASH, txHash));
                });

        assertTrue(getTransactionEvent(txHash, TransactionStatus.UNCONFIRMED).isPresent());
    }

    private void restartKafka(Runnable runnable) throws Exception {
        kafkaContainer.stop();

        runnable.run();

        Thread.sleep(1000);

        kafkaContainer.setPortBindings(
                List.of(
                        String.format(
                                "%s:%s",
                                KAFKA_PORT_BINDING.getBinding().getHostPortSpec(),
                                KAFKA_PORT_BINDING.getExposedPort().getPort())));
        kafkaContainer.start();
    }

    private void verifyEvent(
            TransactionReceipt txReceipt,
            ContractEventFilter eventFilter,
            ContractEventStatus status) {
        final Optional<ContractEvent> contractEvent =
                getContractEvent(txReceipt.getTransactionHash());
        assertTrue(contractEvent.isPresent());
        final ContractEventDetails eventDetails = contractEvent.get().getDetails();

        EventVerification.verifyDummyEvent(
                eventFilter,
                eventDetails,
                status,
                BYTES_VALUE_HEX,
                Keys.toChecksumAddress(CREDENTIALS.getAddress()),
                BigInteger.TEN,
                "StringValue");
    }

    private Optional<TransactionEvent> getTransactionEvent(
            String transactionHash, TransactionStatus status) {
        return kafkaConsumerFactory.getTransactionMessage(
                transactionConsumer,
                TransactionEvent.class,
                TRANSACTION_TOPIC,
                Durations.ONE_MINUTE,
                transactionHash,
                status);
    }

    private Optional<ContractEvent> getContractEvent(String txHash) {
        return kafkaConsumerFactory.getTransactionalMessage(
                contractConsumer,
                ContractEvent.class,
                CONTRACT_TOPIC,
                Durations.ONE_MINUTE,
                txHash);
    }

    private Optional<BlockEvent> getBlockEvent(BigInteger expectedBlockNumber) {
        return kafkaConsumerFactory.getMessage(
                blockConsumer,
                BlockEvent.class,
                BLOCK_TOPIC,
                Durations.ONE_MINUTE,
                (BlockEvent b) -> b.getDetails().getNumber().equals(expectedBlockNumber));
    }
}
