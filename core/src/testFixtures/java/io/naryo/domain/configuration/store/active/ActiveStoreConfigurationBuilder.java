package io.naryo.domain.configuration.store.active;

import java.util.Map;

import io.naryo.domain.configuration.store.StoreConfigurationBuilder;
import io.naryo.domain.configuration.store.active.feature.BlockEventStoreConfigurationBuilder;
import io.naryo.domain.configuration.store.active.feature.FilterStoreConfigurationBuilder;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;

public abstract class ActiveStoreConfigurationBuilder<
                T extends ActiveStoreConfigurationBuilder<T, Y>, Y extends ActiveStoreConfiguration>
        extends StoreConfigurationBuilder<T, Y> {

    private Map<StoreFeatureType, StoreFeatureConfiguration> features;

    public T withFeatures(Map<StoreFeatureType, StoreFeatureConfiguration> features) {
        this.features = features;
        return self();
    }

    protected Map<StoreFeatureType, StoreFeatureConfiguration> getFeatures() {
        return this.features == null
                ? Map.of(
                        StoreFeatureType.EVENT,
                        new BlockEventStoreConfigurationBuilder().build(),
                        StoreFeatureType.FILTER_SYNC,
                        new FilterStoreConfigurationBuilder().build())
                : this.features;
    }
}
