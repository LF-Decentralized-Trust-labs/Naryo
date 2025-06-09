package io.naryo.tests;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import io.naryo.dto.event.ContractEventStatus;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.message.BlockEvent;
import io.naryo.dto.message.ContractEvent;
import io.naryo.dto.message.TransactionEvent;
import io.naryo.dto.transaction.TransactionStatus;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.shaded.org.awaitility.Durations;

import static io.naryo.utils.KafkaConsumerFactory.createKafkaConsumer;

@ContextConfiguration(initializers = BaseKafkaBroadcasterTest.Initializer.class)
public abstract class BaseKafkaBroadcasterTest extends BaseBroadcasterTest {

    protected static final String CONTRACT_TOPIC = "contract-events-" + UUID.randomUUID();
    protected static final String BLOCK_TOPIC = "block-events-" + UUID.randomUUID();
    protected static final String TRANSACTION_TOPIC = "transaction-events-" + UUID.randomUUID();
    protected static KafkaConsumer<String, String> contractConsumer;
    protected static KafkaConsumer<String, String> blockConsumer;
    protected static KafkaConsumer<String, String> transactionConsumer;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        contractConsumer =
                createKafkaConsumer(kafkaContainer.getBootstrapServers(), CONTRACT_TOPIC);
        blockConsumer = createKafkaConsumer(kafkaContainer.getBootstrapServers(), BLOCK_TOPIC);
        transactionConsumer =
                createKafkaConsumer(kafkaContainer.getBootstrapServers(), TRANSACTION_TOPIC);
    }

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

    @Override
    protected Boolean isEventEmitted(ContractEventFilter eventFilter) {
        final List<ContractEvent> events = new ArrayList<>();
        Supplier<Boolean> checkEvents =
                () ->
                        events.stream()
                                .noneMatch(
                                        v ->
                                                v.getDetails()
                                                        .getFilterId()
                                                        .equals(eventFilter.getId()));
        AtomicLong startTime = new AtomicLong(System.currentTimeMillis());
        Awaitility.await()
                .atMost(Durations.TEN_SECONDS)
                .pollInterval(Durations.ONE_SECOND)
                .until(
                        () -> {
                            events.addAll(
                                    kafkaConsumerFactory.pollConsumerRecord(
                                            contractConsumer, ContractEvent.class, CONTRACT_TOPIC));

                            long elapsedTime = System.currentTimeMillis() - startTime.get();

                            boolean noEventFound = checkEvents.get();
                            return noEventFound && elapsedTime > Durations.FIVE_SECONDS.toMillis();
                        });
        return !checkEvents.get();
    }

    @Override
    protected Optional<TransactionEvent> getTransactionEvent(
            String transactionHash, TransactionStatus status) {
        return kafkaConsumerFactory.getTransactionMessage(
                transactionConsumer,
                TransactionEvent.class,
                TRANSACTION_TOPIC,
                Durations.ONE_MINUTE,
                transactionHash,
                status);
    }

    @Override
    protected Optional<BlockEvent> getBlockEvent(BigInteger expectedBlockNumber) {
        return kafkaConsumerFactory.getMessage(
                blockConsumer,
                BlockEvent.class,
                BLOCK_TOPIC,
                Durations.ONE_MINUTE,
                (BlockEvent b) -> b.getDetails().getNumber().equals(expectedBlockNumber));
    }

    @Override
    protected Optional<ContractEvent> getContractEvent(
            String transactionHash, ContractEventStatus status) {
        return kafkaConsumerFactory.getContractEventMessage(
                contractConsumer,
                ContractEvent.class,
                CONTRACT_TOPIC,
                Durations.ONE_MINUTE,
                transactionHash,
                status);
    }
}
