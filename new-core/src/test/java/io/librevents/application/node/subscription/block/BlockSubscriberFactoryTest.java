package io.librevents.application.node.subscription.block;

import io.librevents.application.common.Mapper;
import io.librevents.application.node.calculator.StartBlockCalculator;
import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.subscription.block.poll.PollBlockSubscriber;
import io.librevents.application.node.subscription.block.pubsub.PubSubBlockSubscriber;
import io.librevents.domain.event.block.BlockEvent;
import io.librevents.domain.node.Node;
import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BlockSubscriberFactoryTest {

    @Mock private BlockInteractor interactor;

    @Mock private Dispatcher dispatcher;

    @Mock private Mapper<Block, BlockEvent> blockMapper;

    @Mock private Node node;

    @Mock private StartBlockCalculator calculator;

    @Test
    void constructor_nullInteractor_throwsNPE() {
        NullPointerException ex =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                new BlockSubscriberFactory(
                                        null, dispatcher, blockMapper, calculator));
        assertEquals("interactor must not be null", ex.getMessage());
    }

    @Test
    void constructor_nullDispatcher_throwsNPE() {
        NullPointerException ex =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                new BlockSubscriberFactory(
                                        interactor, null, blockMapper, calculator));
        assertEquals("dispatcher must not be null", ex.getMessage());
    }

    @Test
    void constructor_nullBlockMapper_throwsNPE() {
        NullPointerException ex =
                assertThrows(
                        NullPointerException.class,
                        () -> new BlockSubscriberFactory(interactor, dispatcher, null, calculator));
        assertEquals("blockMapper must not be null", ex.getMessage());
    }

    @Test
    void create_nullMethod_throwsNPE() {
        BlockSubscriberFactory factory =
                new BlockSubscriberFactory(interactor, dispatcher, blockMapper, calculator);
        NullPointerException ex =
                assertThrows(NullPointerException.class, () -> factory.create(null, node));
        assertEquals("method must not be null", ex.getMessage());
    }

    @Test
    void create_nullNode_throwsNPE() {
        BlockSubscriberFactory factory =
                new BlockSubscriberFactory(interactor, dispatcher, blockMapper, calculator);
        NullPointerException ex =
                assertThrows(
                        NullPointerException.class,
                        () -> factory.create(BlockSubscriptionMethod.POLL, null));
        assertEquals("node must not be null", ex.getMessage());
    }

    @Test
    void create_poll_returnsPollBlockSubscriber() {
        BlockSubscriberFactory factory =
                new BlockSubscriberFactory(interactor, dispatcher, blockMapper, calculator);
        BlockSubscriber subscriber = factory.create(BlockSubscriptionMethod.POLL, node);
        assertNotNull(subscriber);
        assertTrue(
                subscriber instanceof PollBlockSubscriber,
                "Expected instance of PollBlockSubscriber");
    }

    @Test
    void create_pubsub_returnsPubSubBlockSubscriber() {
        BlockSubscriberFactory factory =
                new BlockSubscriberFactory(interactor, dispatcher, blockMapper, calculator);
        BlockSubscriber subscriber = factory.create(BlockSubscriptionMethod.PUBSUB, node);
        assertNotNull(subscriber);
        assertTrue(
                subscriber instanceof PubSubBlockSubscriber,
                "Expected instance of PubSubBlockSubscriber");
    }

    @Test
    void create_differentCallsProduceDistinctInstances() {
        BlockSubscriberFactory factory =
                new BlockSubscriberFactory(interactor, dispatcher, blockMapper, calculator);
        BlockSubscriber first = factory.create(BlockSubscriptionMethod.POLL, node);
        BlockSubscriber second = factory.create(BlockSubscriptionMethod.POLL, node);
        assertNotSame(first, second, "Factory should create new instances on each call");
    }
}
