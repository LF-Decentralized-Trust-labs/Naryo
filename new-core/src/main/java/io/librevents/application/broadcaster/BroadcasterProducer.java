package io.librevents.application.broadcaster;

import io.librevents.domain.event.Event;

public interface BroadcasterProducer {

    void produce(Event event);
}
