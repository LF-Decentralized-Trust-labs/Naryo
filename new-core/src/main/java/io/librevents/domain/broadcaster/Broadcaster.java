package io.librevents.domain.broadcaster;

import io.librevents.domain.broadcaster.configuration.BroadcasterConfiguration;

import java.util.Objects;

public record Broadcaster(BroadcasterTarget target, BroadcasterConfiguration configuration) {

    public Broadcaster {
        Objects.requireNonNull(target, "target cannot be null");
        Objects.requireNonNull(configuration, "configuration cannot be null");
    }

}
