package io.naryo.application.broadcaster;

import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterType;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.event.Event;

public interface BroadcasterProducer {

    void produce(Broadcaster broadcaster, BroadcasterConfiguration configuration, Event<?> event);

    boolean supports(BroadcasterType type);
}
