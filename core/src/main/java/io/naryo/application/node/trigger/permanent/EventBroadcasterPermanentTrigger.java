package io.naryo.application.node.trigger.permanent;

import java.util.List;
import java.util.Objects;

import io.naryo.application.broadcaster.BroadcasterProducer;
import io.naryo.application.node.routing.EventRoutingService;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.event.Event;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EventBroadcasterPermanentTrigger implements PermanentTrigger<Event> {

    private final EventRoutingService eventRoutingService;
    private final List<BroadcasterProducer> producers;
    private final List<Broadcaster> broadcasters;
    private final List<BroadcasterConfiguration> configurations;
    private Consumer<Event> consumer;

    public EventBroadcasterPermanentTrigger(
            List<Broadcaster> broadcasters,
            EventRoutingService eventRoutingService,
            List<BroadcasterProducer> producers,
            List<BroadcasterConfiguration> configurations) {
        Objects.requireNonNull(broadcasters, "broadcasters must not be null");
        Objects.requireNonNull(producers, "producers must not be null");
        Objects.requireNonNull(eventRoutingService, "Event routing service must not be null");
        Objects.requireNonNull(configurations, "configurations must not be null");
        this.eventRoutingService = eventRoutingService;
        this.producers = producers;
        this.broadcasters = broadcasters;
        this.configurations = configurations;
    }

    @Override
    public void onExecute(Consumer<Event> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void trigger(Event event) {
        final List<Broadcaster> matchedBroadcasters =
                eventRoutingService.matchingWrappers(event, broadcasters);

        for (Broadcaster broadcaster : matchedBroadcasters) {
            try {
                BroadcasterConfiguration configuration =
                        configurations.stream()
                                .filter(
                                        config ->
                                                config.getId()
                                                        .equals(broadcaster.getConfigurationId()))
                                .findFirst()
                                .orElseThrow(
                                        () ->
                                                new IllegalStateException(
                                                        "Configuration not found for broadcaster: "
                                                                + broadcaster.getId()));
                producers.stream()
                        .filter(producer -> producer.supports(configuration.getType()))
                        .forEach(producer -> producer.produce(broadcaster, configuration, event));
            } catch (Exception e) {
                log.error("Error produce block event", e);
            }
        }
        if (this.consumer != null) {
            try {
                this.consumer.accept(event);
            } catch (Exception e) {
                log.error("Error consume block event", e);
            }
        }
    }

    @Override
    public boolean supports(Event event) {
        return true;
    }
}
