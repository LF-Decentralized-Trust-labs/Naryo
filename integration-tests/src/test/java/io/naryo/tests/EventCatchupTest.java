package io.naryo.tests;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import io.naryo.BaseIntegrationTest;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.message.ContractEvent;
import io.naryo.utils.EventFilterCreator;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.awaitility.Durations;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static io.naryo.utils.KafkaConsumerFactory.createKafkaConsumer;
import static io.naryo.utils.StringManipulation.stringToBytes;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles(value = {"mongo", "kafka"})
@ContextConfiguration(initializers = EventCatchupTest.Initializer.class)
class EventCatchupTest extends BaseIntegrationTest {

    private static final int EVENT_NUMBER_BEFORE_START = 30;
    private static final String CONTRACT_TOPIC = "contract-events-" + UUID.randomUUID();
    private static KafkaConsumer<String, String> contractConsumer;
    private static BigInteger actualBlockNumber;

    @BeforeAll
    static void beforeAll() {
        contractConsumer =
                createKafkaConsumer(kafkaContainer.getBootstrapServers(), CONTRACT_TOPIC);
    }

    @BeforeEach
    void beforeEach() throws Exception {
        actualBlockNumber = web3j.ethBlockNumber().send().getBlockNumber();
        for (int i = 0; i < EVENT_NUMBER_BEFORE_START; i++) {
            defaultContract
                    .emitEvent(stringToBytes("BytesValue"), BigInteger.TEN, "StringValue")
                    .send();
        }
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            TestPropertyValues.of("kafka.topic.contractEvents=" + CONTRACT_TOPIC).applyTo(context);
        }
    }

    @Test
    void testEventsCatchupOnStart() {
        ContractEventFilter eventFilter =
                EventFilterCreator.buildDummyEventFilter(defaultContract.getContractAddress());
        eventFilterCreator.createFilter(eventFilter);

        List<ContractEvent> events =
                kafkaConsumerFactory.getMessages(
                        contractConsumer,
                        ContractEvent.class,
                        CONTRACT_TOPIC,
                        Durations.TWO_MINUTES,
                        e -> e.size() == EVENT_NUMBER_BEFORE_START);

        assertEquals(EVENT_NUMBER_BEFORE_START, events.size());
        assertEquals(
                events.stream()
                        .map(ContractEvent::getDetails)
                        .sorted(Comparator.comparing(ContractEventDetails::getBlockNumber))
                        .toList()
                        .getLast()
                        .getBlockNumber(),
                actualBlockNumber.add(BigInteger.valueOf(EVENT_NUMBER_BEFORE_START)));
    }
}
