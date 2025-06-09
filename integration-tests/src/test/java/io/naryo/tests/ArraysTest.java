package io.naryo.tests;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import io.naryo.BaseIntegrationTest;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.ContractEventStatus;
import io.naryo.dto.event.parameter.NumberParameter;
import io.naryo.dto.event.parameter.StringParameter;
import io.naryo.dto.message.ContractEvent;
import io.naryo.utils.EventFilterCreator;
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
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static io.naryo.utils.KafkaConsumerFactory.createKafkaConsumer;
import static io.naryo.utils.StringManipulation.stringToBytes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles(value = {"mongo", "kafka"})
@ContextConfiguration(initializers = ArraysTest.Initializer.class)
class ArraysTest extends BaseIntegrationTest {

    // "BytesValue" in hex
    private static final String BYTES_VALUE_HEX =
            "0x427974657356616c756500000000000000000000000000000000000000000000";

    // "BytesValue2" in hex
    private static final String BYTES_VALUE2_HEX =
            "0x427974657356616c756532000000000000000000000000000000000000000000";

    private static final String FILTER_ID = String.valueOf(UUID.randomUUID());
    private static final String TOPIC = "contract-events-" + FILTER_ID;

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            TestPropertyValues.of("kafka.topic.contractEvents=" + TOPIC).applyTo(context);
        }
    }

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        eventFilterCreator.createFilter(
                EventFilterCreator.buildDummyEventArrayFilter(
                        FILTER_ID, defaultContract.getContractAddress()));
    }

    @Test
    void testEventWithArrays() throws Exception {
        final KafkaConsumer<String, String> consumer =
                createKafkaConsumer(kafkaContainer.getBootstrapServers(), TOPIC);

        TransactionReceipt txReceipt =
                defaultContract
                        .emitEventArray(
                                BigInteger.ONE,
                                BigInteger.TEN,
                                stringToBytes("BytesValue"),
                                stringToBytes("BytesValue2"))
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
        assertEquals(ContractEventStatus.UNCONFIRMED, eventDetails.getStatus());

        final ArrayList<NumberParameter> uintArray =
                (ArrayList<NumberParameter>)
                        eventDetails.getNonIndexedParameters().getFirst().getValue();

        assertEquals(BigInteger.ONE, uintArray.getFirst().getValue());
        assertEquals(BigInteger.TEN, uintArray.get(1).getValue());

        final ArrayList<StringParameter> bytesArray =
                (ArrayList<StringParameter>)
                        eventDetails.getNonIndexedParameters().get(1).getValue();

        assertEquals(BYTES_VALUE_HEX, bytesArray.getFirst().getValue());
        assertEquals(BYTES_VALUE2_HEX, bytesArray.get(1).getValue());
    }
}
