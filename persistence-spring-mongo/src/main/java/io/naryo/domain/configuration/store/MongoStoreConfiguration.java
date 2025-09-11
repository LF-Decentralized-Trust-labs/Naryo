package io.naryo.domain.configuration.store;

import java.util.Map;
import java.util.UUID;

import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class MongoStoreConfiguration extends ActiveStoreConfiguration {

    public static String MONGO_STORE_TYPE = "mongo";

    public MongoStoreConfiguration(
            UUID nodeId, Map<StoreFeatureType, StoreFeatureConfiguration> features) {
        super(nodeId, () -> MONGO_STORE_TYPE, features);
    }
}
