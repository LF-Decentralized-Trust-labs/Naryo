package io.librevents.infrastructure.broadcaster.http.configuration;

import java.util.Objects;
import java.util.UUID;

import io.librevents.domain.broadcaster.BroadcasterType;
import io.librevents.domain.common.connection.endpoint.ConnectionEndpoint;
import io.librevents.domain.configuration.broadcaster.BroadcasterCache;
import io.librevents.domain.configuration.broadcaster.BroadcasterConfiguration;
import lombok.Getter;

@Getter
public final class HttpBroadcasterConfiguration extends BroadcasterConfiguration {

    private ConnectionEndpoint endpoint;

    public HttpBroadcasterConfiguration(
            UUID id, BroadcasterCache cache, ConnectionEndpoint endpoint) {
        super(id, () -> "http", cache);
        Objects.requireNonNull(endpoint, "endpoint must not be null");
        this.endpoint = endpoint;
    }

    @Override
    public BroadcasterType getType() {
        return super.getType();
    }

    @Override
    public BroadcasterConfiguration merge(BroadcasterConfiguration other) {
        if (!this.getId().equals(other.getId())) {
            throw new IllegalArgumentException("Cannot merge configurations with different IDs");
        }

        if (other.getCache() != null
                && !other.getCache().expirationTime().equals(this.cache.expirationTime())) {
            this.cache = other.getCache();
        }

        if (other instanceof HttpBroadcasterConfiguration httpConfig) {
            if (httpConfig.getEndpoint() != null
                    && !this.endpoint.equals(httpConfig.getEndpoint())) {
                this.endpoint = httpConfig.getEndpoint();
            }
        }

        return this;
    }
}
