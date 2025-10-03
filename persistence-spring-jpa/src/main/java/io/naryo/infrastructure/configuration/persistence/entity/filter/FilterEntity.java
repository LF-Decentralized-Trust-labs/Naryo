package io.naryo.infrastructure.configuration.persistence.entity.filter;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.event.ContractEventFilter;
import io.naryo.domain.filter.event.GlobalEventFilter;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.ContractEventFilterEntity;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.EventSpecification;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.FilterVisibility;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.GlobalEventFilterEntity;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.sync.FilterSyncEntity;
import io.naryo.infrastructure.configuration.persistence.entity.filter.transaction.TransactionFilterEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "filter")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
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

    public static FilterEntity fromDomain(Filter source) {
        return switch (source) {
            case ContractEventFilter contract ->
                    new ContractEventFilterEntity(
                            contract.getId(),
                            contract.getName().value(),
                            contract.getNodeId(),
                            EventSpecification.fromDomain(contract.getSpecification()),
                            contract.getStatuses(),
                            FilterSyncEntity.fromDomain(contract.getFilterSyncState()),
                            FilterVisibility.fromDomain(contract.getVisibilityConfiguration()),
                            contract.getContractAddress());
            case GlobalEventFilter global ->
                    new GlobalEventFilterEntity(
                            global.getId(),
                            global.getName().value(),
                            global.getNodeId(),
                            EventSpecification.fromDomain(global.getSpecification()),
                            global.getStatuses(),
                            FilterSyncEntity.fromDomain(global.getFilterSyncState()),
                            FilterVisibility.fromDomain(global.getVisibilityConfiguration()));
            case TransactionFilter transaction ->
                    new TransactionFilterEntity(
                            transaction.getId(),
                            transaction.getName().value(),
                            transaction.getNodeId(),
                            transaction.getIdentifierType(),
                            transaction.getValue(),
                            transaction.getStatuses());
            default ->
                    throw new IllegalStateException(
                            "Unsupported filter type: " + source.getClass().getSimpleName());
        };
    }
}
