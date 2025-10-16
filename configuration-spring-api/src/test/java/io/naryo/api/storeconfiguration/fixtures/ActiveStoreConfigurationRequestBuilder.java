package io.naryo.api.storeconfiguration.fixtures;

import java.util.Map;

import io.naryo.api.storeconfiguration.common.request.ActiveStoreConfigurationRequest;
import io.naryo.api.storeconfiguration.common.request.StoreFeatureConfigurationRequest;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import org.instancio.Instancio;

public class ActiveStoreConfigurationRequestBuilder
        extends StoreConfigurationRequestBuilder<
                ActiveStoreConfigurationRequestBuilder, ActiveStoreConfigurationRequest> {

    private String type;
    private Map<String, Object> additionalProperties;
    private Map<StoreFeatureType, StoreFeatureConfigurationRequest> features;

    @Override
    public ActiveStoreConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public ActiveStoreConfigurationRequest build() {
        return new ActiveStoreConfigurationRequest(
                getNodeId(), getType(), getAdditionalProperties(), getFeatures());
    }

    public ActiveStoreConfigurationRequestBuilder withType(String type) {
        this.type = type;
        return self();
    }

    public String getType() {
        return this.type == null ? Instancio.create(String.class) : this.type;
    }

    public ActiveStoreConfigurationRequestBuilder withAdditionalProperties(
            Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
        return self();
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties == null ? Map.of() : this.additionalProperties;
    }

    public ActiveStoreConfigurationRequestBuilder withFeatures(
            Map<StoreFeatureType, StoreFeatureConfigurationRequest> features) {
        this.features = features;
        return self();
    }

    public Map<StoreFeatureType, StoreFeatureConfigurationRequest> getFeatures() {
        return this.features == null
                ? Map.of(
                        StoreFeatureType.FILTER_SYNC,
                        new FilterStoreFeatureConfigurationRequestBuilder().build())
                : this.features;
    }
}
