package io.librevents.application.broadcaster;

import io.librevents.domain.broadcaster.Broadcaster;
import io.librevents.domain.broadcaster.BroadcasterType;
import io.librevents.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.librevents.domain.event.Event;

public interface BroadcasterProducer {

    void produce(Broadcaster broadcaster, BroadcasterConfiguration configuration, Event event);

    boolean supports(BroadcasterType type);
}
