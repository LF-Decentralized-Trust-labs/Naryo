package io.naryo.application.node.trigger.permanent.block;

import java.util.List;
import java.util.UUID;

import io.naryo.application.broadcaster.BroadcasterProducer;
import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.node.routing.EventRoutingService;
import io.naryo.application.node.trigger.permanent.EventBroadcasterPermanentTrigger;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterType;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.event.Event;
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

    @Mock private BroadcasterProducer producer1;
    @Mock private BroadcasterProducer producer2;

    @Mock private Broadcaster broadcaster1;
    @Mock private Broadcaster broadcaster2;

    @Mock private BroadcasterConfiguration configuration1;
    @Mock private BroadcasterConfiguration configuration2;

    @Mock private Event<?> event;

    @Mock private LiveView<Broadcaster> broadcasters;
    @Mock private LiveView<BroadcasterConfiguration> configurations;
    private List<BroadcasterProducer> producers;
    private EventBroadcasterPermanentTrigger trigger;

    @BeforeEach
    void setUp() {
        producers = List.of(producer1, producer2);
        trigger =
                new EventBroadcasterPermanentTrigger(
                        broadcasters, routingService, producers, configurations);
    }

    @Test
    void constructor_nullGuards() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new EventBroadcasterPermanentTrigger(
                                null, routingService, producers, configurations));
        assertThrows(
                NullPointerException.class,
                () ->
                        new EventBroadcasterPermanentTrigger(
                                broadcasters, null, producers, configurations));
        assertThrows(
                NullPointerException.class,
                () ->
                        new EventBroadcasterPermanentTrigger(
                                broadcasters, routingService, null, configurations));
        assertThrows(
                NullPointerException.class,
                () ->
                        new EventBroadcasterPermanentTrigger(
                                broadcasters, routingService, producers, null));
    }

    @Test
    void onExecute_setsConsumerAnd_invokesAfterProduce() throws Exception {
        // no wrappers match → only consumer runs
        when(routingService.matchingWrappers(event, broadcasters)).thenReturn(List.of());

        @SuppressWarnings("unchecked")
        io.reactivex.functions.Consumer<Event<?>> consumer =
                mock(io.reactivex.functions.Consumer.class);
        trigger.onExecute(consumer);

        trigger.trigger(event);

        verify(consumer).accept(event);
    }

    @Test
    void trigger_delegatesToRoutingService_and_usesProducer() {
        // only wrapper2 should be invoked
        when(routingService.matchingWrappers(event, broadcasters))
                .thenReturn(List.of(broadcaster2));
        when(configurations.revision())
                .thenReturn(
                        new Revision<>(1, "test-hash", List.of(configuration1, configuration2)));
        UUID configurationId = UUID.randomUUID();
        when(broadcaster2.getConfigurationId()).thenReturn(configurationId);
        when(configuration1.getId()).thenReturn(UUID.randomUUID());
        when(configuration2.getId()).thenReturn(configurationId);
        when(configuration2.getType()).thenReturn(() -> "type2");
        when(producer2.supports(any())).thenReturn(true);
        when(producer1.supports(any())).thenReturn(false);

        assertDoesNotThrow(() -> trigger.trigger(event));

        verify(producer2).produce(broadcaster2, configuration2, event);
        verify(producer1, never()).produce(any(), any(), any());
    }

    @Test
    void trigger_continuesWhenProducerThrowsAndInvokesAll() {
        when(routingService.matchingWrappers(event, broadcasters))
                .thenReturn(List.of(broadcaster1, broadcaster2));
        when(configurations.revision())
                .thenReturn(
                        new Revision<>(1, "test-hash", List.of(configuration1, configuration2)));

        UUID configurationId = UUID.randomUUID();
        UUID configurationId2 = UUID.randomUUID();
        BroadcasterType broadcasterType = () -> "type1";
        BroadcasterType broadcasterType2 = () -> "type2";
        when(broadcaster1.getConfigurationId()).thenReturn(configurationId);
        when(broadcaster2.getConfigurationId()).thenReturn(configurationId2);
        when(configuration1.getId()).thenReturn(configurationId);
        when(configuration2.getId()).thenReturn(configurationId2);
        when(configuration1.getType()).thenReturn(broadcasterType);
        when(configuration2.getType()).thenReturn(broadcasterType2);
        when(producer1.supports(broadcasterType)).thenReturn(true);
        when(producer1.supports(broadcasterType2)).thenReturn(false);
        when(producer2.supports(broadcasterType2)).thenReturn(true);
        doThrow(new RuntimeException("fail1"))
                .when(producer1)
                .produce(broadcaster1, configuration1, event);

        trigger.trigger(event);

        verify(producer1).produce(broadcaster1, configuration1, event);
        verify(producer2).produce(broadcaster2, configuration2, event);
    }

    @Test
    void trigger_consumesEventEvenIfProducerFails() throws Exception {
        // both wrappers match
        when(routingService.matchingWrappers(event, broadcasters))
                .thenReturn(List.of(broadcaster1));

        @SuppressWarnings("unchecked")
        io.reactivex.functions.Consumer<Event<?>> consumer =
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
