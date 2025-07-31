package io.naryo.infrastructure.configuration.persistence.entity.broadcaster;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.broadcaster.BroadcasterDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.AllBroadcasterTargetDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.BlockBroadcasterTargetDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.ContractEventBroadcasterTargetDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.FilterBroadcasterTargetDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.TransactionBroadcasterTargetDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.target.*;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

@Entity
@Table(name = "broadcasters")
@AllArgsConstructor
@NoArgsConstructor
public final class BroadcasterEntity implements BroadcasterDescriptor {

    private @Id UUID id;

    private @Column(name = "configuration_id") UUID configurationId;

    private @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) @JoinColumn(
            name = "target_id") @Valid BroadcasterTargetEntity target;

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public Optional<UUID> getConfigurationId() {
        return Optional.ofNullable(this.configurationId);
    }

    @Override
    public Optional<BroadcasterTargetDescriptor> getTarget() {
        return Optional.ofNullable(this.target);
    }

    @Override
    public void setConfigurationId(UUID configurationId) {
        this.configurationId = configurationId;
    }

    @Override
    public void setTarget(BroadcasterTargetDescriptor target) {
        if (target == null) {
            this.target = null;
            return;
        }

        switch (target) {
            case BlockBroadcasterTargetDescriptor blockTarget ->
                    this.target =
                            new BlockBroadcasterTargetEntity(
                                    valueOrNull(blockTarget.getDestination()));

            case TransactionBroadcasterTargetDescriptor transactionTarget ->
                    this.target =
                            new TransactionBroadcasterTargetEntity(
                                    valueOrNull(transactionTarget.getDestination()));

            case ContractEventBroadcasterTargetDescriptor contractEventTarget ->
                    this.target =
                            new ContractEventBroadcasterTargetEntity(
                                    valueOrNull(contractEventTarget.getDestination()));

            case FilterBroadcasterTargetDescriptor filterTarget ->
                    this.target =
                            new FilterBroadcasterTargetEntity(
                                    valueOrNull(filterTarget.getDestination()),
                                    filterTarget.getFilterId());
            case AllBroadcasterTargetDescriptor allTarget ->
                    this.target =
                            new AllBroadcasterTargetEntity(valueOrNull(allTarget.getDestination()));
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported target type: " + target.getClass().getSimpleName());
        }
    }
}
