package io.naryo.api.storeconfiguration.common.response;

import java.util.*;

import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
public final class ActiveStoreConfigurationResponse extends StoreConfigurationResponse {

    private final String type;

    @Setter
    private Map<String, Object> additionalProperties;

    private final Map<StoreFeatureType, StoreFeatureConfigurationResponse> features;

    public ActiveStoreConfigurationResponse(
            UUID nodeId,
            String type,
            Map<String, Object> additionalProperties,
            Map<StoreFeatureType, StoreFeatureConfigurationResponse> features,
            String currentItemHash) {
        super(nodeId, currentItemHash);
        this.type = type;
        this.additionalProperties = additionalProperties;
        this.features = features;
    }

}
