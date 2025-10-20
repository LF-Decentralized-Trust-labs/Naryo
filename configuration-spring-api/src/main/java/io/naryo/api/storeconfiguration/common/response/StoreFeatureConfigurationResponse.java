package io.naryo.api.storeconfiguration.common.response;

import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.event.block.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.filter.FilterStoreConfiguration;
import lombok.Getter;

@Getter
public abstract class StoreFeatureConfigurationResponse {

    public static StoreFeatureConfigurationResponse map(
            StoreFeatureConfiguration storeFeatureConfiguration) {
        return switch (storeFeatureConfiguration) {
            case BlockEventStoreConfiguration blockEventStoreConfiguration ->
                    BlockEventStoreConfigurationResponse.map(blockEventStoreConfiguration);
            case FilterStoreConfiguration filterStoreConfiguration ->
                    FilterStoreConfigurationResponse.map(filterStoreConfiguration);
            default ->
                    throw new IllegalStateException(
                            "Unexpected value: " + storeFeatureConfiguration);
        };
    }
}
