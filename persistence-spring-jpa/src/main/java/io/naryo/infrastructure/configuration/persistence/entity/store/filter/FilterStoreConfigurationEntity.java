package io.naryo.infrastructure.configuration.persistence.entity.store.filter;

import java.util.Optional;

import io.naryo.application.configuration.source.model.store.filter.FilterStoreConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.infrastructure.configuration.persistence.entity.store.StoreFeatureConfigurationEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("filter")
@Getter
@NoArgsConstructor
public final class FilterStoreConfigurationEntity extends StoreFeatureConfigurationEntity
        implements FilterStoreConfigurationDescriptor {

    private @Column(name = "filter_store_destination", nullable = false) String destination;

    public FilterStoreConfigurationEntity(String destination) {
        super(StoreFeatureType.FILTER_SYNC);
        this.destination = destination;
    }

    @Override
    public Optional<String> getDestination() {
        return Optional.ofNullable(this.destination);
    }

    @Override
    public void setDestination(@Nullable String destination) {
        this.destination = destination;
    }
}
