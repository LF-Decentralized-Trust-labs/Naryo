package io.naryo.domain.configuration.store.active;

import java.util.Map;
import java.util.UUID;

import io.naryo.domain.configuration.Configuration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreState;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class ActiveStoreConfiguration extends StoreConfiguration implements Configuration {

    private final StoreType type;
    private final Map<StoreFeatureType, StoreFeatureConfiguration> features;

    protected ActiveStoreConfiguration(
            UUID nodeId,
            StoreType type,
            Map<StoreFeatureType, StoreFeatureConfiguration> features) {
        super(nodeId, StoreState.ACTIVE);
        this.type = type;
        this.features = features;
    }

    @SuppressWarnings("unchecked")
    public <C extends StoreFeatureConfiguration> C getFeature(StoreFeatureType type) {
        return (C) features.get(type);
    }
}
