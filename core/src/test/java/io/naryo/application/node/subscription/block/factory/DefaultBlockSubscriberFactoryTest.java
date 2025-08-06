package io.naryo.application.node.subscription.block.factory;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.naryo.application.common.Mapper;
import io.naryo.application.configuration.resilence.ResilienceRegistry;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.Block;
import io.naryo.application.node.subscription.block.BlockSubscriber;
import io.naryo.application.node.subscription.block.poll.PollBlockSubscriber;
import io.naryo.application.node.subscription.block.pubsub.PubSubBlockSubscriber;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultBlockSubscriberFactoryTest {

    @Mock private Mapper<Block, BlockEvent> blockMapper;

    @Mock private ResilienceRegistry resilienceRegistry;

    @Mock private BlockInteractor blockInteractor;

    @Mock private Dispatcher dispatcher;

    @Mock private Node node;

    @Mock private BlockSubscriptionConfiguration subscriptionConfig;

    @Mock private BlockSubscriptionMethodConfiguration methodConfig;

    @Mock private CircuitBreaker circuitBreaker;

    @Mock private Retry retry;

    private DefaultBlockSubscriberFactory factory;

    @BeforeEach
    void setUp() {
        when(resilienceRegistry.getOrDefault(anyString(), eq(CircuitBreaker.class)))
                .thenReturn(circuitBreaker);
        when(resilienceRegistry.getOrDefault(anyString(), eq(Retry.class))).thenReturn(retry);
        when(node.getSubscriptionConfiguration()).thenReturn(subscriptionConfig);
        when(subscriptionConfig.getStrategy()).thenReturn(SubscriptionStrategy.BLOCK_BASED);
        when(subscriptionConfig.getMethodConfiguration()).thenReturn(methodConfig);

        factory = new DefaultBlockSubscriberFactory(blockMapper, resilienceRegistry);
    }

    @Test
    void create_shouldReturnPollBlockSubscriber_whenMethodIsPoll() {
        // Given
        when(methodConfig.getMethod()).thenReturn(BlockSubscriptionMethod.POLL);

        // When
        BlockSubscriber subscriber =
                factory.create(
                        blockInteractor,
                        dispatcher,
                        node,
                        new StartBlockCalculator(node, blockInteractor));

        // Then
        assertNotNull(subscriber);
        assertTrue(subscriber instanceof PollBlockSubscriber);
        verify(resilienceRegistry).getOrDefault("subscription-cb", CircuitBreaker.class);
        verify(resilienceRegistry).getOrDefault("subscription-retry", Retry.class);
    }

    @Test
    void create_shouldReturnPubSubBlockSubscriber_whenMethodIsPubSub() {
        // Given
        when(methodConfig.getMethod()).thenReturn(BlockSubscriptionMethod.PUBSUB);

        // When
        BlockSubscriber subscriber =
                factory.create(
                        blockInteractor,
                        dispatcher,
                        node,
                        new StartBlockCalculator(node, blockInteractor));

        // Then
        assertNotNull(subscriber);
        assertTrue(subscriber instanceof PubSubBlockSubscriber);
        verify(resilienceRegistry).getOrDefault("subscription-cb", CircuitBreaker.class);
        verify(resilienceRegistry).getOrDefault("subscription-retry", Retry.class);
    }
}
