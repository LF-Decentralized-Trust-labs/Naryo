package io.librevents.infrastructure.broadcaster.http.configuration;

import io.librevents.application.configuration.ConfigurationProvider;
import io.librevents.domain.broadcaster.BroadcasterType;
import io.librevents.domain.broadcaster.configuration.BroadcasterCache;
import io.librevents.domain.common.connection.endpoint.ConnectionEndpoint;

import java.util.Map;

public final class HttpBroadcasterConfigurationProvider
        implements ConfigurationProvider<HttpBroadcasterConfiguration> {

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
