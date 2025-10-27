package io.naryo.api.storeconfiguration.getAll.model;

import java.util.*;

import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Schema(description = "Active store configuration")
@EqualsAndHashCode(callSuper = true)
@Getter
public final class ActiveStoreConfigurationResponse extends StoreConfigurationResponse {

    private final String type;

    private final Map<String, Object> additionalProperties;

    private final Map<StoreFeatureType, StoreFeatureConfigurationResponse> features;

    public ActiveStoreConfigurationResponse(
            UUID nodeId,
            String currentItemHash,
            String type,
            Map<String, Object> additionalProperties,
            Map<StoreFeatureType, StoreFeatureConfigurationResponse> features) {
        super(nodeId, currentItemHash);
        this.type = type;
        this.additionalProperties = additionalProperties;
        this.features = features;
    }
}
