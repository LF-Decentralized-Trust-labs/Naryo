package io.naryo.application.node.trigger.permanent;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.naryo.application.event.store.EventStore;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;
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

    @Mock private EventStore<Event, EventStoreConfiguration> store1;
    @Mock private EventStore<Event, EventStoreConfiguration> store2;
    @Mock private Consumer<Event> consumer;
    @Mock private EventStoreConfiguration configuration;

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
        when(store1.supports(evt)).thenReturn(true);
        when(store2.supports(evt)).thenReturn(false);
        UUID nodeId = UUID.randomUUID();
        when(evt.getNodeId()).thenReturn(nodeId);
        when(configuration.getNodeId()).thenReturn(nodeId);

        trigger.trigger(evt);

        verify(store1, times(1)).save(evt, configuration);
        verify(store2, never()).save(any(), any());
    }

    @Test
    void trigger_afterOnExecute_invokesConsumer() throws Exception {
        Event evt = mock(Event.class);
        when(store1.supports(evt)).thenReturn(true);
        when(store2.supports(evt)).thenReturn(false);
        UUID nodeId = UUID.randomUUID();
        when(evt.getNodeId()).thenReturn(nodeId);
        when(configuration.getNodeId()).thenReturn(nodeId);

        trigger.onExecute(consumer);
        trigger.trigger(evt);

        verify(store1).save(evt, configuration);
        verify(consumer).accept(evt);
    }

    @Test
    void trigger_catchesExceptionsFromSaveAndContinues() throws Exception {
        Event evt = mock(Event.class);
        when(store1.supports(evt)).thenReturn(true);
        UUID nodeId = UUID.randomUUID();
        when(evt.getNodeId()).thenReturn(nodeId);
        when(configuration.getNodeId()).thenReturn(nodeId);
        doThrow(new RuntimeException("db down")).when(store1).save(evt, configuration);

        assertDoesNotThrow(() -> trigger.trigger(evt));

        verify(store1).save(evt, configuration);
    }

    @Test
    void trigger_catchesExceptionsFromConsumerAndContinues() throws Exception {
        Event evt = mock(Event.class);
        when(store1.supports(evt)).thenReturn(false);
        when(store2.supports(evt)).thenReturn(false);

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
