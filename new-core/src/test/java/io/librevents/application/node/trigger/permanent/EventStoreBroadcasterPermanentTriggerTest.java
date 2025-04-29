package io.librevents.application.node.trigger.permanent;

import java.util.List;

import io.librevents.application.event.store.EventStore;
import io.librevents.domain.event.Event;
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

    @Mock private EventStore<Event> store1;
    @Mock private EventStore<Event> store2;
    @Mock private Consumer<Event> consumer;

    private EventStoreBroadcasterPermanentTrigger trigger;

    @BeforeEach
    void setUp() {
        trigger = new EventStoreBroadcasterPermanentTrigger(List.of(store1, store2));
    }

    @Test
    void trigger_savesOnlyToStoresThatSupportTheEvent() throws Exception {
        Event evt = mock(Event.class);
        when(store1.supports(evt)).thenReturn(true);
        when(store2.supports(evt)).thenReturn(false);

        trigger.trigger(evt);

        verify(store1, times(1)).save(evt);
        verify(store2, never()).save(any());
    }

    @Test
    void trigger_afterOnExecute_invokesConsumer() throws Exception {
        Event evt = mock(Event.class);
        when(store1.supports(evt)).thenReturn(true);
        when(store2.supports(evt)).thenReturn(false);

        trigger.onExecute(consumer);
        trigger.trigger(evt);

        verify(store1).save(evt);
        verify(consumer).accept(evt);
    }

    @Test
    void trigger_catchesExceptionsFromSaveAndContinues() throws Exception {
        Event evt = mock(Event.class);
        when(store1.supports(evt)).thenReturn(true);
        doThrow(new RuntimeException("db down")).when(store1).save(evt);

        assertDoesNotThrow(() -> trigger.trigger(evt));

        verify(store1).save(evt);
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
