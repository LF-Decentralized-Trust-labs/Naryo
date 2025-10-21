package io.naryo.api.broadcasterconfiguration.create;

import java.time.Duration;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import io.naryo.api.RequestBuilder;
import io.naryo.api.broadcasterconfiguration.common.model.BroadcasterCacheConfigurationRequest;
import io.naryo.api.broadcasterconfiguration.create.model.CreateBroadcasterConfigurationRequest;
import io.naryo.api.node.common.NodeRequestBuilder;
import io.naryo.api.node.common.eth.priv.PrivateEthereumNodeRequestBuilder;
import io.naryo.api.node.common.eth.pub.PublicEthereumNodeRequestBuilder;
import io.naryo.api.node.common.hedera.HederaNodeRequestBuilder;

public class CreateBroadcasterConfigurationRequestBuilder
        implements RequestBuilder<
                CreateBroadcasterConfigurationRequestBuilder,
                CreateBroadcasterConfigurationRequest> {

    private UUID id;
    private String type;
    private BroadcasterCacheConfigurationRequest cache;
    private Map<String, Object> additionalProperties;

    @Override
    public CreateBroadcasterConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public CreateBroadcasterConfigurationRequest build() {
        return new CreateBroadcasterConfigurationRequest(
                getId(), getType(), getCache(), getAdditionalProperties());
    }

    public CreateBroadcasterConfigurationRequestBuilder withId(UUID id) {
        this.id = id;
        return self();
    }

    public UUID getId() {
        return this.id == null ? UUID.randomUUID() : this.id;
    }

    public CreateBroadcasterConfigurationRequestBuilder withRandomType() {
        var random = new Random().nextInt(3);
           return switch (random) {
                case 0 -> withHttpType();
                case 1 -> withKafkaType();
                case 2 -> withRabbitmqType();
                default -> throw new IllegalStateException("Unexpected value: " + random);
            };
    }


    private CreateBroadcasterConfigurationRequestBuilder withType(String type) {
        this.type = type;
        return self();
    }

    public CreateBroadcasterConfigurationRequestBuilder withHttpType() {
        return withType("http");
    }

    public CreateBroadcasterConfigurationRequestBuilder withKafkaType() {
        return withType("kafka");
    }

    public CreateBroadcasterConfigurationRequestBuilder withRabbitmqType() {
        return withType("rabbitmq");
    }

    public String getType() {
        return this.type == null ? "http" : this.type;
    }

    public CreateBroadcasterConfigurationRequestBuilder withCache(
            BroadcasterCacheConfigurationRequest cache) {
        this.cache = cache;
        return self();
    }

    public BroadcasterCacheConfigurationRequest getCache() {
        return this.cache == null
                ? new BroadcasterCacheConfigurationRequest(Duration.ofMinutes(5))
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
