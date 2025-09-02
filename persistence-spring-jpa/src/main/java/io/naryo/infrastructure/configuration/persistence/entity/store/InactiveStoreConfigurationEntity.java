package io.naryo.infrastructure.configuration.persistence.entity.store;

import java.util.UUID;

import io.naryo.application.configuration.source.model.store.InactiveStoreConfigurationDescriptor;
import io.naryo.domain.configuration.store.StoreState;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("inactive")
@NoArgsConstructor
public final class InactiveStoreConfigurationEntity extends StoreConfigurationEntity
        implements InactiveStoreConfigurationDescriptor {

    public InactiveStoreConfigurationEntity(UUID nodeId) {
        super(nodeId);
    }
}
