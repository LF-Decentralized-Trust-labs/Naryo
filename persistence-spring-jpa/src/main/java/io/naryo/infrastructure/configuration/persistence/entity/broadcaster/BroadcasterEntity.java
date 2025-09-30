package io.naryo.infrastructure.configuration.persistence.entity.broadcaster;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.broadcaster.BroadcasterDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.target.*;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

@Entity
@Table(name = "broadcaster")
@AllArgsConstructor
@NoArgsConstructor
public final class BroadcasterEntity implements BroadcasterDescriptor {

    private @Column(name = "id") @Id UUID id;

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

        this.target = BroadcasterTargetEntity.fromDescriptor(target);
    }

    public static BroadcasterEntity fromDomain(Broadcaster source) {
        return new BroadcasterEntity(
                source.getId(),
                source.getConfigurationId(),
                BroadcasterTargetEntity.fromDomain(source.getTarget()));
    }

    public static BroadcasterEntity fromDescriptor(BroadcasterDescriptor descriptor) {
        return new BroadcasterEntity(
                descriptor.getId(),
                valueOrNull(descriptor.getConfigurationId()),
                BroadcasterTargetEntity.fromDescriptor(valueOrNull(descriptor.getTarget())));
    }
}
