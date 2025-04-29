package io.librevents.application.node.trigger.permanent.block;

import java.util.List;

import io.librevents.application.broadcaster.BroadcasterProducer;
import io.librevents.application.broadcaster.BroadcasterWrapper;
import io.librevents.application.node.routing.EventRoutingService;
import io.librevents.application.node.trigger.permanent.EventBroadcasterPermanentTrigger;
import io.librevents.domain.event.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventBroadcasterPermanentTriggerTest {

    @Mock private EventRoutingService routingService;

    @Mock private BroadcasterWrapper wrapper1;
    @Mock private BroadcasterWrapper wrapper2;

    @Mock private BroadcasterProducer producer1;
    @Mock private BroadcasterProducer producer2;

    @Mock private Event event;

    private List<BroadcasterWrapper> wrappers;
    private EventBroadcasterPermanentTrigger trigger;

    @BeforeEach
    void setUp() {
        wrappers = List.of(wrapper1, wrapper2);
        trigger = new EventBroadcasterPermanentTrigger(wrappers, routingService);
    }

    @Test
    void constructor_nullGuards() {
        assertThrows(
                NullPointerException.class,
                () -> new EventBroadcasterPermanentTrigger(null, routingService));
        assertThrows(
                NullPointerException.class,
                () -> new EventBroadcasterPermanentTrigger(wrappers, null));
    }

    @Test
    void onExecute_setsConsumerAnd_invokesAfterProduce() throws Exception {
        // no wrappers match → only consumer runs
        when(routingService.matchingWrappers(event, wrappers)).thenReturn(List.of());

        @SuppressWarnings("unchecked")
        io.reactivex.functions.Consumer<Event> consumer =
                mock(io.reactivex.functions.Consumer.class);
        trigger.onExecute(consumer);

        trigger.trigger(event);

        verify(consumer).accept(event);
    }

    @Test
    void trigger_delegatesToRoutingService_and_usesProducer() throws Exception {
        // only wrapper2 should be invoked
        when(routingService.matchingWrappers(event, wrappers)).thenReturn(List.of(wrapper2));
        when(wrapper2.producer()).thenReturn(producer2);

        trigger.trigger(event);

        verify(producer2).produce(event);
        verifyNoInteractions(producer1);
    }

    @Test
    void trigger_continuesWhenProducerThrowsAndInvokesAll() throws Exception {
        // both wrappers match
        when(routingService.matchingWrappers(event, wrappers))
                .thenReturn(List.of(wrapper1, wrapper2));
        when(wrapper1.producer()).thenReturn(producer1);
        when(wrapper2.producer()).thenReturn(producer2);

        doThrow(new RuntimeException("fail1")).when(producer1).produce(event);
        // producer2 will succeed

        trigger.trigger(event);

        verify(producer1).produce(event);
        verify(producer2).produce(event);
    }

    @Test
    void trigger_consumesEventEvenIfProducerFails() throws Exception {
        // both wrappers match
        when(routingService.matchingWrappers(event, wrappers)).thenReturn(List.of(wrapper1));
        when(wrapper1.producer()).thenReturn(producer1);
        doThrow(new RuntimeException("boom")).when(producer1).produce(event);

        @SuppressWarnings("unchecked")
        io.reactivex.functions.Consumer<Event> consumer =
                mock(io.reactivex.functions.Consumer.class);
        trigger.onExecute(consumer);

        // should not propagate despite producer exception
        assertDoesNotThrow(() -> trigger.trigger(event));

        // consumer still runs
        verify(consumer).accept(event);
    }

    @Test
    void supports_alwaysTrue() {
        assertTrue(trigger.supports(event));
    }
}
