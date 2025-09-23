package io.naryo.domain.configuration.broadcaster;

import java.util.Objects;
import java.util.UUID;

import io.naryo.domain.broadcaster.BroadcasterType;
import io.naryo.domain.configuration.Configuration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public abstract class BroadcasterConfiguration implements Configuration {

    protected final UUID id;
    protected final BroadcasterType type;
    protected BroadcasterCache cache;

    protected BroadcasterConfiguration(UUID id, BroadcasterType type, BroadcasterCache cache) {
        Objects.requireNonNull(id, "filterId must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(cache, "cache must not be null");
        this.id = id;
        this.type = type;
        this.cache = cache;
    }
}
