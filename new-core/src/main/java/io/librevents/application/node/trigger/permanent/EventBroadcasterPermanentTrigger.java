package io.librevents.application.node.trigger.permanent;

import java.util.List;
import java.util.Objects;

import io.librevents.application.broadcaster.BroadcasterWrapper;
import io.librevents.application.node.routing.EventRoutingService;
import io.librevents.domain.event.Event;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EventBroadcasterPermanentTrigger implements PermanentTrigger<Event> {

    private final EventRoutingService eventRoutingService;
    private final List<BroadcasterWrapper> broadcasters;
    private Consumer<Event> consumer;

    public EventBroadcasterPermanentTrigger(
            List<BroadcasterWrapper> broadcasters, EventRoutingService eventRoutingService) {
        Objects.requireNonNull(broadcasters, "broadcasters must not be null");
        Objects.requireNonNull(eventRoutingService, "Event routing service must not be null");
        this.eventRoutingService = eventRoutingService;
        this.broadcasters = broadcasters;
    }

    @Override
    public void onExecute(Consumer<Event> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void trigger(Event event) {
        final List<BroadcasterWrapper> wrappers =
                eventRoutingService.matchingWrappers(event, broadcasters);

        for (BroadcasterWrapper wrapper : wrappers) {
            try {
                wrapper.producer().produce(wrapper.broadcaster(), event);
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
