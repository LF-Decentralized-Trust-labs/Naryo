package io.naryo.tests;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.naryo.BaseIntegrationTest;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.ContractEventStatus;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.message.ContractEvent;
import io.naryo.utils.EventFilterCreator;
import io.naryo.utils.EventVerification;
import io.naryo.utils.KafkaConsumerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.awaitility.Durations;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static io.naryo.utils.StringManipulation.stringToBytes;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ActiveProfiles(value = {"mongo", "kafka"})
@ContextConfiguration(initializers = NodeRecoveryTest.Initializer.class)
class NodeRecoveryTest extends BaseIntegrationTest {

    // "BytesValue" in hex
    private static final String BYTES_VALUE_HEX =
            "0x427974657356616c756500000000000000000000000000000000000000000000";
    private static final String CONTRACT_TOPIC = "contract-events-" + UUID.randomUUID();
    protected static KafkaConsumer<String, String> contractConsumer;

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            TestPropertyValues.of("kafka.topic.contractEvents=" + CONTRACT_TOPIC).applyTo(context);
        }
    }

    @BeforeAll
    static void beforeAll() {
        contractConsumer =
                KafkaConsumerFactory.createKafkaConsumer(
                        kafkaContainer.getBootstrapServers(), CONTRACT_TOPIC);
    }

    @Test
    void singleNodeFailureRecoveryTest() throws Exception {
        doRestartEventEmissionsAssertion(1000, 1);
    }

    @Test
    void multipleNodeFailuresRecoveryTest() throws Exception {
        doRestartEventEmissionsAssertion(1000, 1);
        doRestartEventEmissionsAssertion(2000, 1);
        doRestartEventEmissionsAssertion(4000, 1);
        doRestartEventEmissionsAssertion(8000, 1);
    }

    @Test
    void quickSuccessionNodeFailuresRecoveryTest() throws Exception {
        doRestartEventEmissionsAssertion(1000, 5);
    }

    private void doRestartEventEmissionsAssertion(int recoveryTime, int numRestarts)
            throws Exception {
        ContractEventFilter eventFilter =
                EventFilterCreator.buildDummyEventFilter(defaultContract.getContractAddress());
        eventFilterCreator.createFilter(eventFilter);

        TransactionReceipt txReceipt =
                defaultContract
                        .emitEvent(stringToBytes("BytesValue"), BigInteger.TEN, "StringValue")
                        .send();

        verifyEvent(txReceipt, eventFilter);

        for (int i = 0; i < numRestarts; i++) {
            restartNode(recoveryTime);
        }

        TransactionReceipt txReceipt2 =
                defaultContract
                        .emitEvent(stringToBytes("BytesValue"), BigInteger.TEN, "StringValue")
                        .send();

        verifyEvent(txReceipt2, eventFilter);
    }

    protected void restartNode(long recoveryTime) throws InterruptedException {
        ganacheContainer.stop();

        Thread.sleep(recoveryTime);

        ganacheContainer.setPortBindings(
                List.of(
                        String.format(
                                "%s:%s",
                                GANACHE_PORT_BINDING.getBinding().getHostPortSpec(),
                                GANACHE_PORT_BINDING.getExposedPort().getPort())));
        ganacheContainer.start();
    }

    private void verifyEvent(TransactionReceipt txReceipt, ContractEventFilter eventFilter) {
        verifyEvent(txReceipt, eventFilter, ContractEventStatus.UNCONFIRMED);
    }

    private void verifyEvent(
            TransactionReceipt txReceipt,
            ContractEventFilter eventFilter,
            ContractEventStatus status) {
        final Optional<ContractEvent> contractEvent =
                kafkaConsumerFactory.getTransactionalMessage(
                        contractConsumer,
                        ContractEvent.class,
                        CONTRACT_TOPIC,
                        Durations.ONE_MINUTE,
                        txReceipt.getTransactionHash());
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
}
