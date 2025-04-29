package io.librevents.domain.broadcaster;

import java.util.Objects;

import io.librevents.domain.broadcaster.configuration.BroadcasterConfiguration;

public record Broadcaster(BroadcasterTarget target, BroadcasterConfiguration configuration) {

    public Broadcaster {
        Objects.requireNonNull(target, "target cannot be null");
        Objects.requireNonNull(configuration, "configuration cannot be null");
    }
}
