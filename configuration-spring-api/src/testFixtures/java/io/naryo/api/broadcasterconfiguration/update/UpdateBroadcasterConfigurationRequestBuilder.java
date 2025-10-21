package io.naryo.api.broadcasterconfiguration.update;

import java.time.Duration;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import io.naryo.api.RequestBuilder;
import io.naryo.api.broadcasterconfiguration.common.model.BroadcasterCacheConfigurationRequest;
import io.naryo.api.broadcasterconfiguration.update.model.UpdateBroadcasterConfigurationRequest;
import org.instancio.Instancio;

public class UpdateBroadcasterConfigurationRequestBuilder
        implements RequestBuilder<
                UpdateBroadcasterConfigurationRequestBuilder,
                UpdateBroadcasterConfigurationRequest> {

    private UUID id;
    private String type;
    private BroadcasterCacheConfigurationRequest cache;
    private Map<String, Object> additionalProperties;
    private String prevItemHash;

    @Override
    public UpdateBroadcasterConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public UpdateBroadcasterConfigurationRequest build() {
        return new UpdateBroadcasterConfigurationRequest(
                getId(), getType(), getCache(), getAdditionalProperties(), getPrevItemHash());
    }

    public UpdateBroadcasterConfigurationRequestBuilder withId(UUID id) {
        this.id = id;
        return self();
    }

    public UUID getId() {
        return this.id == null ? UUID.randomUUID() : this.id;
    }

    public UpdateBroadcasterConfigurationRequestBuilder withRandomType() {
        var random = new Random().nextInt(3);
        return switch (random) {
            case 0 -> withHttpType();
            case 1 -> withKafkaType();
            case 2 -> withRabbitmqType();
            default -> throw new IllegalStateException("Unexpected value: " + random);
        };
    }

    private UpdateBroadcasterConfigurationRequestBuilder withType(String type) {
        this.type = type;
        return self();
    }

    public UpdateBroadcasterConfigurationRequestBuilder withHttpType() {
        return withType("http");
    }

    public UpdateBroadcasterConfigurationRequestBuilder withKafkaType() {
        return withType("kafka");
    }

    public UpdateBroadcasterConfigurationRequestBuilder withRabbitmqType() {
        return withType("rabbitmq");
    }

    public String getType() {
        return this.type == null ? "http" : this.type;
    }

    public UpdateBroadcasterConfigurationRequestBuilder withCache(
            BroadcasterCacheConfigurationRequest cache) {
        this.cache = cache;
        return self();
    }

    public BroadcasterCacheConfigurationRequest getCache() {
        return this.cache == null
                ? new BroadcasterCacheConfigurationRequest(Duration.ofMinutes(5))
                : this.cache;
    }

    public UpdateBroadcasterConfigurationRequestBuilder withAdditionalProperties(
            Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
        return self();
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties == null ? Map.of() : this.additionalProperties;
    }

    public UpdateBroadcasterConfigurationRequestBuilder withPrevItemHash(String prevItemHash) {
        this.prevItemHash = prevItemHash;
        return self();
    }

    public String getPrevItemHash() {
        return this.prevItemHash == null ? Instancio.create(String.class) : this.prevItemHash;
    }
}
