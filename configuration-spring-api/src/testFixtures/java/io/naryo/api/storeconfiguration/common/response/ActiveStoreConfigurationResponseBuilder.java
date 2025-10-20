package io.naryo.api.storeconfiguration.common.response;

import java.util.Map;

import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import org.instancio.Instancio;

public class ActiveStoreConfigurationResponseBuilder
        extends StoreConfigurationResponseBuilder<
                ActiveStoreConfigurationResponseBuilder, ActiveStoreConfigurationResponse> {

    private String type;
    private Map<String, Object> additionalProperties;
    private Map<StoreFeatureType, StoreFeatureConfigurationResponse> features;

    @Override
    public ActiveStoreConfigurationResponseBuilder self() {
        return this;
    }

    @Override
    public ActiveStoreConfigurationResponse build() {
        return new ActiveStoreConfigurationResponse(
                getNodeId(),
                getCurrentItemHash(),
                getType(),
                getAdditionalProperties(),
                getFeatures());
    }

    public ActiveStoreConfigurationResponseBuilder withType(String type) {
        this.type = type;
        return self();
    }

    public String getType() {
        return this.type == null ? Instancio.create(String.class) : this.type;
    }

    public ActiveStoreConfigurationResponseBuilder withAdditionalProperties(
            Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
        return self();
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties == null ? Map.of() : this.additionalProperties;
    }

    public ActiveStoreConfigurationResponseBuilder withFeatures(
            Map<StoreFeatureType, StoreFeatureConfigurationResponse> features) {
        this.features = features;
        return self();
    }

    public Map<StoreFeatureType, StoreFeatureConfigurationResponse> getFeatures() {
        return this.features == null
                ? Map.of(
                        StoreFeatureType.FILTER_SYNC,
                        new FilterStoreConfigurationResponseBuilder().build())
                : this.features;
    }
}
