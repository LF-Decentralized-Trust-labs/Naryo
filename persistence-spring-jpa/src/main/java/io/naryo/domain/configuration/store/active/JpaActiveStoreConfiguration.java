package io.naryo.domain.configuration.store.active;

import java.util.Map;
import java.util.UUID;

import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static io.naryo.domain.JpaConstants.JPA_TYPE;

@Getter
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public final class JpaActiveStoreConfiguration extends ActiveStoreConfiguration {

    public JpaActiveStoreConfiguration(
            UUID nodeId, Map<StoreFeatureType, StoreFeatureConfiguration> features) {
        super(nodeId, () -> JPA_TYPE, features);
    }
}
