package io.naryo.api.broadcasterconfiguration.update;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import io.naryo.api.RequestBuilder;
import io.naryo.api.broadcasterconfiguration.common.model.BroadcasterCacheConfigurationRequest;
import io.naryo.api.broadcasterconfiguration.update.model.UpdateBroadcasterConfigurationRequestTarget;

public class UpdateBroadcasterConfigurationRequestTargetBuilder
        implements RequestBuilder<
                UpdateBroadcasterConfigurationRequestTargetBuilder,
                UpdateBroadcasterConfigurationRequestTarget> {

    private UUID id;
    private String type;
    private BroadcasterCacheConfigurationRequest cache;
    private Map<String, Object> additionalProperties;

    @Override
    public UpdateBroadcasterConfigurationRequestTargetBuilder self() {
        return this;
    }

    @Override
    public UpdateBroadcasterConfigurationRequestTarget build() {
        return new UpdateBroadcasterConfigurationRequestTarget(
                getId(), getType(), getCache(), getAdditionalProperties());
    }

    public UpdateBroadcasterConfigurationRequestTargetBuilder withId(UUID id) {
        this.id = id;
        return self();
    }

    public UUID getId() {
        return this.id == null ? UUID.randomUUID() : this.id;
    }

    public UpdateBroadcasterConfigurationRequestTargetBuilder withType(String type) {
        this.type = type;
        return self();
    }

    public String getType() {
        return this.type == null ? "http" : this.type;
    }

    public UpdateBroadcasterConfigurationRequestTargetBuilder withCache(
            BroadcasterCacheConfigurationRequest cache) {
        this.cache = cache;
        return self();
    }

    public BroadcasterCacheConfigurationRequest getCache() {
        return this.cache == null
                ? new BroadcasterCacheConfigurationRequest(Duration.ofMinutes(5))
                : this.cache;
    }

    public UpdateBroadcasterConfigurationRequestTargetBuilder withAdditionalProperties(
            Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
        return self();
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties == null ? Map.of() : this.additionalProperties;
    }
}
