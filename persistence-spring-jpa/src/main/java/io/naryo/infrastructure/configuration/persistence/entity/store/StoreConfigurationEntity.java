package io.naryo.infrastructure.configuration.persistence.entity.store;

import java.util.UUID;

import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import io.naryo.domain.configuration.store.StoreState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "store_configurations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "store_type")
@NoArgsConstructor
@Setter
public abstract class StoreConfigurationEntity implements StoreConfigurationDescriptor {

    private @Column(name = "node_id") @Id UUID nodeId;
    private @Column(name = "state") @NotNull StoreState state;

    public StoreConfigurationEntity(UUID nodeId, StoreState state) {
        this.nodeId = nodeId;
        this.state = state;
    }

    @Override
    public UUID getNodeId() {
        return this.nodeId;
    }

    @Override
    public StoreState getState() {
        return this.state;
    }
}
