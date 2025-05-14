package io.librevents.infrastructure.broadcaster.http.configuration;

import java.util.Map;

import io.librevents.application.broadcaster.configuration.BroadcasterConfigurationProvider;
import io.librevents.domain.broadcaster.BroadcasterType;
import io.librevents.domain.broadcaster.configuration.BroadcasterCache;
import io.librevents.domain.common.connection.endpoint.ConnectionEndpoint;

public final class HttpBroadcasterConfigurationProvider
        extends BroadcasterConfigurationProvider<HttpBroadcasterConfiguration> {

    public HttpBroadcasterConfigurationProvider() {
        super(() -> "http");
    }

    @Override
    public HttpBroadcasterConfiguration create(Map<String, Object> config) {
        var cache = (BroadcasterCache) config.get("cache");
        var endpoint = (ConnectionEndpoint) config.get("endpoint");
        return new HttpBroadcasterConfiguration(cache, endpoint);
    }

    @Override
    public boolean supports(BroadcasterType type) {
        return type.getName().equals("http");
    }
}
