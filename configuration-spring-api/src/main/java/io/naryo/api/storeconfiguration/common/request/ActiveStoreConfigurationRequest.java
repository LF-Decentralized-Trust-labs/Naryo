package io.naryo.api.storeconfiguration.common.request;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;

@Schema(description = "Active store configuration request")
@EqualsAndHashCode(callSuper = true)
public final class ActiveStoreConfigurationRequest extends StoreConfigurationRequest
        implements ActiveStoreConfigurationDescriptor {

    @NotNull @EqualsAndHashCode.Exclude private String type;

    private Map<String, Object> additionalProperties;

    @NotNull private Map<StoreFeatureType, StoreFeatureConfigurationRequest> features;

    public ActiveStoreConfigurationRequest(
            UUID nodeId,
            String type,
            Map<String, Object> additionalProperties,
            Map<StoreFeatureType, StoreFeatureConfigurationRequest> features) {
        super(nodeId);
        this.type = type;
        this.additionalProperties = additionalProperties;
        this.features = features;
    }

    @Override
    public StoreType getType() {
        return () -> type;
    }

    @JsonGetter("type")
    @EqualsAndHashCode.Include
    public String getTypeJson() {
        return type;
    }

    @Override
    public Map<StoreFeatureType, StoreFeatureConfigurationRequest> getFeatures() {
        return features;
    }

    @Override
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties == null ? Map.of() : additionalProperties;
    }

    @Override
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @Override
    public void setFeatures(
            Map<StoreFeatureType, ? extends StoreFeatureConfigurationDescriptor> features) {}
}
