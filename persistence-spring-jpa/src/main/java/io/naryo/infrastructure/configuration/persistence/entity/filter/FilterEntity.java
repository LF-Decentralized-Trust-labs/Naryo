package io.naryo.infrastructure.configuration.persistence.entity.filter;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.contract.ContractEventFilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.global.GlobalEventFilterDescriptor;
import io.naryo.application.configuration.source.model.filter.transaction.TransactionFilterDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.ContractEventFilterEntity;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.GlobalEventFilterEntity;
import io.naryo.infrastructure.configuration.persistence.entity.filter.transaction.TransactionFilterEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

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

    public static FilterEntity fromDescriptor(FilterDescriptor descriptor) {
        return switch (descriptor) {
            case ContractEventFilterDescriptor contract ->
                    new ContractEventFilterEntity(
                            contract.getId(),
                            valueOrNull(contract.getName()),
                            valueOrNull(contract.getNodeId()),
                            valueOrNull(contract.getSpecification()),
                            contract.getStatuses(),
                            valueOrNull(contract.getSync()),
                            valueOrNull(contract.getVisibility()),
                            valueOrNull(contract.getAddress()));
            case GlobalEventFilterDescriptor global ->
                    new GlobalEventFilterEntity(
                            global.getId(),
                            valueOrNull(global.getName()),
                            valueOrNull(global.getNodeId()),
                            valueOrNull(global.getSpecification()),
                            global.getStatuses(),
                            valueOrNull(global.getSync()),
                            valueOrNull(global.getVisibility()));
            case TransactionFilterDescriptor transaction ->
                    new TransactionFilterEntity(
                            transaction.getId(),
                            valueOrNull(transaction.getName()),
                            valueOrNull(transaction.getNodeId()),
                            valueOrNull(transaction.getIdentifierType()),
                            valueOrNull(transaction.getValue()),
                            transaction.getStatuses());
            default ->
                    throw new IllegalStateException(
                            "Unsupported filter type: " + descriptor.getClass().getSimpleName());
        };
    }
}
