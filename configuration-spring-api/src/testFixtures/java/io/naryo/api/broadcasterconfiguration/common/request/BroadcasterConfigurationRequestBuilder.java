package io.naryo.api.broadcasterconfiguration.common.request;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import io.naryo.api.RequestBuilder;

public class BroadcasterConfigurationRequestBuilder
        implements RequestBuilder<
                BroadcasterConfigurationRequestBuilder, BroadcasterConfigurationRequest> {

    private UUID id;
    private String type;
    private BroadcasterCacheRequest cache;
    private Map<String, Object> additionalProperties;

    @Override
    public BroadcasterConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public BroadcasterConfigurationRequest build() {
        return new BroadcasterConfigurationRequest(
                getId(), getType(), getCache(), getAdditionalProperties());
    }

    public BroadcasterConfigurationRequestBuilder withId(UUID id) {
        this.id = id;
        return self();
    }

    public UUID getId() {
        return this.id == null ? UUID.randomUUID() : this.id;
    }

    private BroadcasterConfigurationRequestBuilder withType() {
        this.type = "http";
        return self();
    }

    public BroadcasterConfigurationRequestBuilder withHttpType() {
        return withType();
    }

    public String getType() {
        return this.type == null ? "http" : this.type;
    }

    public BroadcasterConfigurationRequestBuilder withCache(
            BroadcasterCacheRequest cache) {
        this.cache = cache;
        return self();
    }

    public BroadcasterCacheRequest getCache() {
        return this.cache == null
                ? new BroadcasterCacheRequest(Duration.ofMinutes(5))
                : this.cache;
    }

    public BroadcasterConfigurationRequestBuilder withAdditionalProperties(
            Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
        return self();
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties == null ? Map.of() : this.additionalProperties;
    }
}
