package io.naryo.tests;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.naryo.BaseIntegrationTest;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.message.ContractEvent;
import io.naryo.integration.eventstore.EventStore;
import io.naryo.utils.EventFilterCreator;
import io.naryo.utils.KafkaConsumerFactory;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.awaitility.Awaitility;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.awaitility.Durations;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static io.naryo.utils.StringManipulation.stringToBytes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles(value = {"postgres", "kafka"})
@ContextConfiguration(initializers = SqlEventStoreTest.Initializer.class)
class SqlEventStoreTest extends BaseIntegrationTest {

    private static final String CONTRACT_TOPIC = "contract-events-" + UUID.randomUUID();
    private static KafkaConsumer<String, String> contractConsumer;
    @Autowired private EventStore eventStore;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        contractConsumer =
                KafkaConsumerFactory.createKafkaConsumer(
                        kafkaContainer.getBootstrapServers(), CONTRACT_TOPIC);
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            TestPropertyValues.of("kafka.topic.contractEvents=" + CONTRACT_TOPIC).applyTo(context);
        }
    }

    @Test
    void testBroadcastEventAddedToEventStore() throws Exception {
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
        final ContractEventDetails eventDetails = contractEvent.get().getDetails();

        Awaitility.await()
                .atMost(Durations.ONE_MINUTE)
                .untilAsserted(
                        () -> {
                            final List<ContractEventDetails> savedEvents =
                                    eventStore
                                            .getContractEventsForSignature(
                                                    eventDetails.getEventSpecificationSignature(),
                                                    Keys.toChecksumAddress(
                                                            defaultContract.getContractAddress()),
                                                    PageRequest.of(0, 100000))
                                            .getContent();
                            assertEquals(1, savedEvents.size());
                            eventDetails.setId(savedEvents.getFirst().getId());
                            assertEquals(eventDetails, savedEvents.getFirst());
                        });
    }
}
