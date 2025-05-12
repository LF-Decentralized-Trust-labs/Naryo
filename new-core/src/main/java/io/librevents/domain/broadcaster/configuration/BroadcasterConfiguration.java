package io.librevents.domain.broadcaster.configuration;

import java.util.Objects;

import io.librevents.domain.broadcaster.BroadcasterType;
import io.librevents.domain.common.configuration.Configuration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class BroadcasterConfiguration implements Configuration {

    private final BroadcasterType type;
    private final BroadcasterCache cache;

    protected BroadcasterConfiguration(BroadcasterType type, BroadcasterCache cache) {
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(cache, "cache must not be null");
        this.type = type;
        this.cache = cache;
    }
}
