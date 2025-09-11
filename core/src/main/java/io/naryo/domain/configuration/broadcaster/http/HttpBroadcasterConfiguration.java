package io.naryo.domain.configuration.broadcaster.http;

import java.util.Objects;
import java.util.UUID;

import io.naryo.domain.broadcaster.BroadcasterType;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.broadcaster.BroadcasterCache;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import lombok.Getter;

import static io.naryo.domain.HttpConstants.HTTP_TYPE;

@Getter
public final class HttpBroadcasterConfiguration extends BroadcasterConfiguration {

    private final ConnectionEndpoint endpoint;

    public HttpBroadcasterConfiguration(
            UUID id, BroadcasterCache cache, ConnectionEndpoint endpoint) {
        super(id, () -> HTTP_TYPE, cache);
        Objects.requireNonNull(endpoint, "endpoint must not be null");
        this.endpoint = endpoint;
    }

    @Override
    public BroadcasterType getType() {
        return super.getType();
    }
}
