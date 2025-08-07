package io.naryo.domain.configuration.store.active.feature.filter;

import io.naryo.domain.common.Destination;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public final class FilterStoreConfiguration extends StoreFeatureConfiguration {

    private final Destination destination;

    public FilterStoreConfiguration(Destination destination) {
        super(StoreFeatureType.FILTER_SYNC);
        this.destination = destination;
    }
}
