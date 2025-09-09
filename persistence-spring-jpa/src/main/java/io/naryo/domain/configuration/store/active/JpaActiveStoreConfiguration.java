package io.naryo.domain.configuration.store.active;

import java.util.Map;
import java.util.UUID;

import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class JpaActiveStoreConfiguration extends ActiveStoreConfiguration {

    public static String JPA_STORE_TYPE = "jpa";

    public JpaActiveStoreConfiguration(
            UUID nodeId, Map<StoreFeatureType, StoreFeatureConfiguration> features) {
        super(nodeId, () -> JPA_STORE_TYPE, features);
    }
}
