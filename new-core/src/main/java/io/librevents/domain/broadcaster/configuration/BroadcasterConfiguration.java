package io.librevents.domain.broadcaster.configuration;

import io.librevents.domain.broadcaster.BroadcasterType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public abstract class BroadcasterConfiguration {

    private final BroadcasterType type;
    private final BroadcasterCache cache;

    protected BroadcasterConfiguration(BroadcasterType type, BroadcasterCache cache) {
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(cache, "cache must not be null");
        this.type = type;
        this.cache = cache;
    }
}
