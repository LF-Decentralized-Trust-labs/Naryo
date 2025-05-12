package io.librevents.infrastructure.broadcaster.http.configuration;

import io.librevents.domain.broadcaster.BroadcasterType;
import io.librevents.domain.broadcaster.configuration.BroadcasterCache;
import io.librevents.domain.broadcaster.configuration.BroadcasterConfiguration;
import io.librevents.domain.common.connection.endpoint.ConnectionEndpoint;
import lombok.Getter;

import java.util.Objects;

@Getter
public final class HttpBroadcasterConfiguration extends BroadcasterConfiguration {

    private final ConnectionEndpoint endpoint;

    HttpBroadcasterConfiguration(BroadcasterCache cache, ConnectionEndpoint endpoint) {
        super(() -> "http", cache);
        Objects.requireNonNull(endpoint, "endpoint must not be null");
        this.endpoint = endpoint;
    }

    @Override
    public BroadcasterType getType() {
        return super.getType();
    }
}
