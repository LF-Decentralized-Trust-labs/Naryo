package io.naryo.api.broadcasterconfiguration.create;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import io.naryo.api.RequestBuilder;
import io.naryo.api.broadcasterconfiguration.common.request.BroadcasterCacheRequest;
import io.naryo.api.broadcasterconfiguration.create.model.CreateBroadcasterConfigurationRequest;

public class CreateBroadcasterConfigurationRequestBuilder
        implements RequestBuilder<
                CreateBroadcasterConfigurationRequestBuilder,
                CreateBroadcasterConfigurationRequest> {

    private UUID id;
    private String type;
    private BroadcasterCacheRequest cache;
    private Map<String, Object> additionalProperties;

    @Override
    public CreateBroadcasterConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public CreateBroadcasterConfigurationRequest build() {
        return new CreateBroadcasterConfigurationRequest(
                getType(), getCache(), getAdditionalProperties());
    }

    public CreateBroadcasterConfigurationRequestBuilder withId(UUID id) {
        this.id = id;
        return self();
    }

    public UUID getId() {
        return this.id;
    }

    private CreateBroadcasterConfigurationRequestBuilder withType(String type) {
        this.type = type;
        return self();
    }

    public CreateBroadcasterConfigurationRequestBuilder withHttpType() {
        return withType("http");
    }

    public String getType() {
        return this.type == null ? "http" : this.type;
    }

    public CreateBroadcasterConfigurationRequestBuilder withCache(
            BroadcasterCacheRequest cache) {
        this.cache = cache;
        return self();
    }

    public BroadcasterCacheRequest getCache() {
        return this.cache == null
                ? new BroadcasterCacheRequest(Duration.ofMinutes(5))
                : this.cache;
    }

    public CreateBroadcasterConfigurationRequestBuilder withAdditionalProperties(
            Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
        return self();
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties == null ? Map.of() : this.additionalProperties;
    }
}
