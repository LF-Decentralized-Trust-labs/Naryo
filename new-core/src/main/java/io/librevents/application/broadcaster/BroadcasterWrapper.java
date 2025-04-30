package io.librevents.application.broadcaster;

import java.util.Objects;

import io.librevents.domain.broadcaster.Broadcaster;

public record BroadcasterWrapper(Broadcaster broadcaster, BroadcasterProducer producer) {
    public BroadcasterWrapper {
        Objects.requireNonNull(broadcaster, "broadcaster must not be null");
        Objects.requireNonNull(producer, "producer must not be null");
    }
}
