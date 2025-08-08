package io.naryo.application.node.trigger.permanent;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.naryo.application.store.event.EventStore;
import io.naryo.domain.configuration.store.StoreState;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.event.Event;
import io.reactivex.functions.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventStoreBroadcasterPermanentTriggerTest {

    @Mock private EventStore<ActiveStoreConfiguration, Object, Event> store1;
    @Mock private EventStore<ActiveStoreConfiguration, Object, Event> store2;
    @Mock private Consumer<Event> consumer;
    @Mock private ActiveStoreConfiguration configuration;

    private EventStoreBroadcasterPermanentTrigger trigger;

    @BeforeEach
    void setUp() {
        trigger =
                new EventStoreBroadcasterPermanentTrigger(
                        Set.of(store1, store2), List.of(configuration));
    }

    @Test
    void trigger_savesOnlyToStoresThatSupportTheEvent() throws Exception {
        Event evt = mock(Event.class);
        when(store1.supports(configuration.getType(), evt.getClass())).thenReturn(true);
        when(store2.supports(configuration.getType(), evt.getClass())).thenReturn(false);
        UUID nodeId = UUID.randomUUID();
        when(evt.getNodeId()).thenReturn(nodeId);
        when(configuration.getNodeId()).thenReturn(nodeId);
        when(configuration.getState()).thenReturn(StoreState.ACTIVE);

        trigger.trigger(evt);

        verify(store1, times(1)).save(configuration, evt.getKey(), evt);
        verify(store2, never()).save(configuration, evt.getNodeId().toString(), evt);
    }

    @Test
    void trigger_afterOnExecute_invokesConsumer() throws Exception {
        Event evt = mock(Event.class);
        when(store1.supports(configuration.getType(), evt.getClass())).thenReturn(true);
        when(store2.supports(configuration.getType(), evt.getClass())).thenReturn(false);
        UUID nodeId = UUID.randomUUID();
        when(evt.getNodeId()).thenReturn(nodeId);
        when(configuration.getNodeId()).thenReturn(nodeId);
        when(configuration.getState()).thenReturn(StoreState.ACTIVE);

        trigger.onExecute(consumer);
        trigger.trigger(evt);

        verify(store1).save(configuration, evt.getKey(), evt);
        verify(consumer).accept(evt);
    }

    @Test
    void trigger_catchesExceptionsFromSaveAndContinues() throws Exception {
        Event evt = mock(Event.class);
        when(store1.supports(configuration.getType(), evt.getClass())).thenReturn(true);
        UUID nodeId = UUID.randomUUID();
        when(evt.getNodeId()).thenReturn(nodeId);
        when(configuration.getState()).thenReturn(StoreState.ACTIVE);
        when(configuration.getNodeId()).thenReturn(nodeId);

        assertDoesNotThrow(() -> trigger.trigger(evt));

        verify(store1).save(configuration, evt.getKey(), evt);
    }

    @Test
    void trigger_catchesExceptionsFromConsumerAndContinues() throws Exception {
        Event evt = mock(Event.class);
        when(store1.supports(configuration.getType(), evt.getClass())).thenReturn(false);
        when(store2.supports(configuration.getType(), evt.getClass())).thenReturn(false);
        UUID nodeId = UUID.randomUUID();
        when(evt.getNodeId()).thenReturn(nodeId);
        when(configuration.getNodeId()).thenReturn(nodeId);
        when(configuration.getState()).thenReturn(StoreState.ACTIVE);

        trigger.onExecute(consumer);
        doThrow(new Exception("boom")).when(consumer).accept(evt);

        assertDoesNotThrow(() -> trigger.trigger(evt));

        verify(consumer).accept(evt);
    }

    @Test
    void supportsAlwaysReturnsTrue() {
        Event evt = mock(Event.class);
        assertTrue(trigger.supports(evt));
    }
}
