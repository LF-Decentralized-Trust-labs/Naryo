package io.naryo.infrastructure.configuration.persistence.entity.filter;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "filters")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "filter_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
@AllArgsConstructor
public abstract class FilterEntity implements FilterDescriptor {

    private @Column(name = "id") @Id UUID id;

    private @Column(name = "name") @Nullable String name;

    private @Column(name = "node_id") @Nullable UUID nodeId;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setNodeId(UUID nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public Optional<UUID> getNodeId() {
        return nodeId == null ? Optional.empty() : Optional.of(nodeId);
    }
}
