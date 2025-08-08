package io.naryo.domain.configuration.store.active.feature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class StoreFeatureConfiguration {

    private final StoreFeatureType type;

    protected StoreFeatureConfiguration(StoreFeatureType type) {
        this.type = type;
    }
}
