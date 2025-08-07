package io.naryo.infrastructure.configuration.source.env.model.store.filter;

import java.util.Optional;

import io.naryo.application.configuration.source.model.store.filter.FilterStoreConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.infrastructure.configuration.source.env.model.store.StoreFeatureConfigurationProperties;
import jakarta.annotation.Nullable;

public final class FilterStoreConfigurationProperties extends StoreFeatureConfigurationProperties
        implements FilterStoreConfigurationDescriptor {

    private @Nullable String destination;

    public FilterStoreConfigurationProperties(@Nullable String destination) {
        super(StoreFeatureType.FILTER_SYNC);
        this.destination = destination;
    }

    @Override
    public Optional<String> getDestination() {
        return Optional.ofNullable(destination);
    }

    @Override
    public void setDestination(@Nullable String destination) {
        this.destination = destination;
    }
}
