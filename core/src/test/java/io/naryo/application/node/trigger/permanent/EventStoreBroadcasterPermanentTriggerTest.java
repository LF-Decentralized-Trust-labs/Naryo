package io.naryo.application.node.trigger.permanent;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.store.event.EventStore;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreState;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.EventType;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.eth.priv.PrivateEthereumNodeBuilder;
import io.reactivex.functions.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventStoreBroadcasterPermanentTriggerTest {

    private Node node;
    @Mock private EventStore<ActiveStoreConfiguration, Object, Event<?>> store1;
    @Mock private EventStore<ActiveStoreConfiguration, Object, Event<?>> store2;
    @Mock private Consumer<Event<?>> consumer;
    @Mock private ActiveStoreConfiguration configuration;
    @Mock private LiveRegistry<StoreConfiguration> storeConfigurations;

    private EventStoreBroadcasterPermanentTrigger trigger;

    @BeforeEach
    void setUp() {
        node = new PrivateEthereumNodeBuilder().build();
    }

    @ParameterizedTest
    @MethodSource("supportedEventsParameters")
    void trigger_savesOnlyToStoresThatSupportTheEvent(
            EventType targetEventType, Class<? extends Event<?>> eventClass) {
        trigger =
                new EventStoreBroadcasterPermanentTrigger(
                        targetEventType, node, List.of(store1, store2), storeConfigurations);

        when(storeConfigurations.active())
                .thenReturn(new Revision<>(1, "test-hash", Set.of(configuration)));
        Event<?> evt = mock(eventClass);
        when(store1.supports(configuration.getType(), evt.getClass())).thenReturn(true);
        when(evt.getNodeId()).thenReturn(node.getId());
        when(configuration.getNodeId()).thenReturn(node.getId());
        when(configuration.getState()).thenReturn(StoreState.ACTIVE);

        trigger.trigger(evt);

        verify(store1, times(1)).save(configuration, evt.getKey(), evt);
        verify(store2, never()).save(configuration, evt.getNodeId().toString(), evt);
    }

    @ParameterizedTest
    @MethodSource("supportedEventsParameters")
    void trigger_afterOnExecute_invokesConsumer(
            EventType targetEventType, Class<? extends Event<?>> eventClass) throws Exception {
        trigger =
                new EventStoreBroadcasterPermanentTrigger(
                        targetEventType, node, List.of(store1, store2), storeConfigurations);

        when(storeConfigurations.active())
                .thenReturn(new Revision<>(1, "test-hash", Set.of(configuration)));
        Event<?> evt = mock(eventClass);
        when(store1.supports(configuration.getType(), evt.getClass())).thenReturn(true);
        when(configuration.getNodeId()).thenReturn(node.getId());
        when(configuration.getState()).thenReturn(StoreState.ACTIVE);

        trigger.onExecute(consumer);
        trigger.trigger(evt);

        verify(store1).save(configuration, evt.getKey(), evt);
        verify(consumer).accept(evt);
    }

    @ParameterizedTest
    @MethodSource("supportedEventsParameters")
    void trigger_catchesExceptionsFromSaveAndContinues(
            EventType targetEventType, Class<? extends Event<?>> eventClass) {
        trigger =
                new EventStoreBroadcasterPermanentTrigger(
                        targetEventType, node, List.of(store1, store2), storeConfigurations);

        when(storeConfigurations.active())
                .thenReturn(new Revision<>(1, "test-hash", Set.of(configuration)));
        Event<?> evt = mock(eventClass);
        when(store1.supports(configuration.getType(), evt.getClass())).thenReturn(true);
        when(configuration.getState()).thenReturn(StoreState.ACTIVE);
        when(configuration.getNodeId()).thenReturn(node.getId());

        assertDoesNotThrow(() -> trigger.trigger(evt));

        verify(store1).save(configuration, evt.getKey(), evt);
    }

    @ParameterizedTest
    @MethodSource("supportedEventsParameters")
    void trigger_catchesExceptionsFromConsumerAndContinues(
            EventType targetEventType, Class<? extends Event<?>> eventClass) throws Exception {
        trigger =
                new EventStoreBroadcasterPermanentTrigger(
                        targetEventType, node, List.of(store1, store2), storeConfigurations);

        when(storeConfigurations.active())
                .thenReturn(new Revision<>(1, "test-hash", Set.of(configuration)));
        Event<?> evt = mock(eventClass);
        when(store1.supports(configuration.getType(), evt.getClass())).thenReturn(false);
        when(store2.supports(configuration.getType(), evt.getClass())).thenReturn(false);
        when(configuration.getNodeId()).thenReturn(node.getId());
        when(configuration.getState()).thenReturn(StoreState.ACTIVE);

        trigger.onExecute(consumer);
        doThrow(new Exception("boom")).when(consumer).accept(evt);

        assertDoesNotThrow(() -> trigger.trigger(evt));

        verify(consumer).accept(evt);
    }

    @Test
    void supports_returnsTrueIfEventNodeIsSupported() {
        trigger =
                new EventStoreBroadcasterPermanentTrigger(
                        EventType.BLOCK, node, List.of(store1, store2), storeConfigurations);

        Event<?> evt = mock(Event.class);
        when(evt.getNodeId()).thenReturn(node.getId());
        assertTrue(trigger.supports(evt));
    }

    @Test
    void supports_returnsFalseIfEventNodeIsNotSupported() {
        trigger =
                new EventStoreBroadcasterPermanentTrigger(
                        EventType.BLOCK, node, List.of(store1, store2), storeConfigurations);

        Event<?> evt = mock(Event.class);
        when(evt.getNodeId()).thenReturn(UUID.randomUUID());
        assertFalse(trigger.supports(evt));
    }

    private static Stream<Arguments> supportedEventsParameters() {
        return Stream.of(
                Arguments.of(EventType.BLOCK, BlockEvent.class),
                Arguments.of(EventType.CONTRACT, ContractEvent.class),
                Arguments.of(EventType.TRANSACTION, TransactionEvent.class));
    }
}
