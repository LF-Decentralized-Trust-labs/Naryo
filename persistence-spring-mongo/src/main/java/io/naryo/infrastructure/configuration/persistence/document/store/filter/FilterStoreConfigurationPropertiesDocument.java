package io.naryo.infrastructure.configuration.persistence.document.store.filter;

import java.util.Optional;

import io.naryo.application.configuration.source.model.store.filter.FilterStoreConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.infrastructure.configuration.persistence.document.store.StoreFeatureConfigurationPropertiesDocument;
import jakarta.annotation.Nullable;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("filter_store")
public final class FilterStoreConfigurationPropertiesDocument
        extends StoreFeatureConfigurationPropertiesDocument
        implements FilterStoreConfigurationDescriptor {

    private @Nullable String destination;

    public FilterStoreConfigurationPropertiesDocument(@Nullable String destination) {
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
