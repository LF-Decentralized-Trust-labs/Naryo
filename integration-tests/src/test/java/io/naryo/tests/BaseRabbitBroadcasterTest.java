package io.naryo.tests;

import java.math.BigInteger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.function.Supplier;

import io.naryo.dto.event.ContractEventStatus;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.message.BlockEvent;
import io.naryo.dto.message.ContractEvent;
import io.naryo.dto.message.TransactionEvent;
import io.naryo.dto.transaction.TransactionStatus;
import io.naryo.utils.RabbitConsumerFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.shaded.org.awaitility.Durations;

@ContextConfiguration(initializers = BaseRabbitBroadcasterTest.Initializer.class)
public abstract class BaseRabbitBroadcasterTest extends BaseBroadcasterTest {

    protected static final String DEFAULT_EXCHANGE = "default_exchange_" + UUID.randomUUID();
    protected static final String CONTRACT_EVENT_QUEUE_NAME =
            "contract_events_" + UUID.randomUUID();
    protected static final String BLOCK_EVENT_QUEUE_NAME = "block_events_" + UUID.randomUUID();
    protected static final String TRANSACTION_EVENT_QUEUE_NAME =
            "transaction_events_" + UUID.randomUUID();
    protected static final String CONTRACT_EVENT_ROUTING_KEY = CONTRACT_EVENT_QUEUE_NAME + ".#";
    protected static final String BLOCK_EVENT_ROUTING_KEY = BLOCK_EVENT_QUEUE_NAME + ".#";
    protected static final String TRANSACTION_EVENT_ROUTING_KEY =
            TRANSACTION_EVENT_QUEUE_NAME + ".#";

    protected final List<ContractEvent> contractEvents = new ArrayList<>();
    protected final List<BlockEvent> blockEvents = new ArrayList<>();
    protected final List<TransactionEvent> transactionEvents = new ArrayList<>();
    protected SimpleMessageListenerContainer contractEventListener;
    protected SimpleMessageListenerContainer blockEventListener;
    protected SimpleMessageListenerContainer transactionEventListener;
    private final RabbitConsumerFactory consumerFactory;
    private final RabbitAdmin admin;

    public BaseRabbitBroadcasterTest(RabbitConsumerFactory consumerFactory, RabbitAdmin admin) {
        this.consumerFactory = consumerFactory;
        this.admin = admin;
    }

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        TopicExchange exchange = new TopicExchange(DEFAULT_EXCHANGE);
        admin.declareExchange(exchange);
        Queue contractQueue = new Queue(CONTRACT_EVENT_QUEUE_NAME, true, false, false);
        Queue blockQueue = new Queue(BLOCK_EVENT_QUEUE_NAME, true, false, false);
        Queue transactionQueue = new Queue(TRANSACTION_EVENT_QUEUE_NAME, true, false, false);
        admin.declareQueue(contractQueue);
        admin.declareQueue(blockQueue);
        admin.declareQueue(transactionQueue);
        admin.declareBinding(
                BindingBuilder.bind(contractQueue).to(exchange).with(CONTRACT_EVENT_ROUTING_KEY));
        admin.declareBinding(
                BindingBuilder.bind(blockQueue).to(exchange).with(BLOCK_EVENT_ROUTING_KEY));
        admin.declareBinding(
                BindingBuilder.bind(transactionQueue)
                        .to(exchange)
                        .with(TRANSACTION_EVENT_ROUTING_KEY));
        contractEventListener =
                consumerFactory.createMessageListener(
                        ContractEvent.class, contractEvents::add, CONTRACT_EVENT_QUEUE_NAME);
        blockEventListener =
                consumerFactory.createMessageListener(
                        BlockEvent.class, blockEvents::add, BLOCK_EVENT_QUEUE_NAME);
        transactionEventListener =
                consumerFactory.createMessageListener(
                        TransactionEvent.class,
                        transactionEvents::add,
                        TRANSACTION_EVENT_QUEUE_NAME);
    }

    @AfterEach
    protected void cleanUp() {
        contractEventListener.destroy();
        blockEventListener.destroy();
        transactionEventListener.destroy();
        contractEvents.clear();
        blockEvents.clear();
        transactionEvents.clear();
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                            "broadcaster.enableBlockNotifications=true",
                            "rabbitmq.exchange=" + DEFAULT_EXCHANGE,
                            "rabbitmq.routingKey.contractEvents=" + CONTRACT_EVENT_ROUTING_KEY,
                            "rabbitmq.routingKey.blockEvents=" + BLOCK_EVENT_ROUTING_KEY,
                            "rabbitmq.routingKey.transactionEvents="
                                    + TRANSACTION_EVENT_ROUTING_KEY)
                    .applyTo(context);
        }
    }

    @Override
    protected Optional<ContractEvent> getContractEvent(
            String transactionHash, ContractEventStatus status) {
        Predicate<ContractEvent> predicate =
                v ->
                        v.getDetails().getTransactionHash().equals(transactionHash)
                                && v.getDetails().getStatus().equals(status);
        return getContractEvent(Durations.ONE_MINUTE, predicate);
    }

    @Override
    protected Optional<BlockEvent> getBlockEvent(BigInteger expectedBlockNumber) {
        Predicate<BlockEvent> predicate =
                (BlockEvent b) -> b.getDetails().getNumber().equals(expectedBlockNumber);
        return getBlockEvent(Durations.ONE_MINUTE, predicate);
    }

    @Override
    protected Optional<TransactionEvent> getTransactionEvent(
            String transactionHash, TransactionStatus status) {
        Predicate<TransactionEvent> predicate =
                v ->
                        v.getDetails().getTransactionHash().equals(transactionHash)
                                && v.getDetails().getStatus().equals(status);
        return getTransactionEvent(Durations.ONE_MINUTE, predicate);
    }

    @Override
    protected Boolean isEventEmitted(ContractEventFilter eventFilter) {
        Supplier<Boolean> checkEvents =
                () ->
                        contractEvents.stream()
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
                            long elapsedTime = System.currentTimeMillis() - startTime.get();
                            boolean noEventFound = checkEvents.get();
                            return noEventFound && elapsedTime > Durations.FIVE_SECONDS.toMillis();
                        });
        return !checkEvents.get();
    }

    private Optional<ContractEvent> getContractEvent(
            Duration timeout, Predicate<ContractEvent> predicate) {
        Awaitility.await().atMost(timeout).until(() -> contractEvents.stream().anyMatch(predicate));
        return contractEvents.stream().filter(predicate).findFirst();
    }

    private Optional<TransactionEvent> getTransactionEvent(
            Duration timeout, Predicate<TransactionEvent> predicate) {
        Awaitility.await()
                .atMost(timeout)
                .until(() -> transactionEvents.stream().anyMatch(predicate));
        return transactionEvents.stream().filter(predicate).findFirst();
    }

    private Optional<BlockEvent> getBlockEvent(Duration timeout, Predicate<BlockEvent> predicate) {
        Awaitility.await().atMost(timeout).until(() -> blockEvents.stream().anyMatch(predicate));
        return blockEvents.stream().filter(predicate).findFirst();
    }
}
