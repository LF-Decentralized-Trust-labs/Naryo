package io.naryo.tests;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

import io.naryo.BaseIntegrationTest;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.ContractEventStatus;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.message.ContractEvent;
import io.naryo.utils.EventFilterCreator;
import io.naryo.utils.EventVerification;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.awaitility.Durations;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static io.naryo.utils.KafkaConsumerFactory.createKafkaConsumer;
import static io.naryo.utils.StringManipulation.stringToBytes;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles(value = {"mongo", "kafka"})
@ContextConfiguration(initializers = AsciiBytesTest.Initializer.class)
class AsciiBytesTest extends BaseIntegrationTest {

    private static final String FILTER_ID = String.valueOf(UUID.randomUUID());
    private static final String TOPIC = "contract-events-" + FILTER_ID;
    private static KafkaConsumer<String, String> consumer;
    private ContractEventFilter eventFilter;

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                            "kafka.topic.contractEvents=" + TOPIC, "broadcaster.bytesToAscii=true")
                    .applyTo(context);
        }
    }

    @BeforeAll
    static void beforeAll() {
        consumer = createKafkaConsumer(kafkaContainer.getBootstrapServers(), TOPIC);
    }

    @BeforeEach
    void beforeEach() {
        eventFilter =
                EventFilterCreator.buildDummyEventFilter(
                        FILTER_ID, defaultContract.getContractAddress());
        eventFilterCreator.createFilter(eventFilter);
    }

    @Test
    void testAsciiBytes() throws Exception {
        TransactionReceipt txReceipt =
                defaultContract
                        .emitEvent(stringToBytes("BytesValue"), BigInteger.TEN, "StringValue")
                        .send();
        final Optional<ContractEvent> contractEvent =
                kafkaConsumerFactory.getTransactionalMessage(
                        consumer,
                        ContractEvent.class,
                        TOPIC,
                        Durations.ONE_MINUTE,
                        txReceipt.getTransactionHash());
        assertTrue(contractEvent.isPresent());
        final ContractEventDetails eventDetails = contractEvent.get().getDetails();

        EventVerification.verifyDummyEvent(
                eventFilter,
                eventDetails,
                ContractEventStatus.UNCONFIRMED,
                "BytesValue",
                Keys.toChecksumAddress(CREDENTIALS.getAddress()),
                BigInteger.TEN,
                "StringValue");
    }
}
