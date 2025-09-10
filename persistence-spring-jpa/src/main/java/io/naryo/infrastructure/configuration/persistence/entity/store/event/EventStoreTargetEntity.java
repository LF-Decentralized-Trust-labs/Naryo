package io.naryo.infrastructure.configuration.persistence.entity.store.event;

import java.util.UUID;

import io.naryo.application.configuration.source.model.store.event.EventStoreTargetDescriptor;
import io.naryo.domain.configuration.store.active.feature.event.block.TargetType;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_store_target")
@NoArgsConstructor
public final class EventStoreTargetEntity implements EventStoreTargetDescriptor {

    private @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(
            name = "id",
            nullable = false,
            updatable = false) UUID id;
    private @Enumerated(EnumType.STRING) @Column(name = "target_type", nullable = false) TargetType
            type;
    private @Column(name = "target_destination", nullable = false) String destination;

    public EventStoreTargetEntity(TargetType type, String destination) {
        this.type = type;
        this.destination = destination;
    }

    @Override
    public TargetType type() {
        return this.type;
    }

    @Override
    public String destination() {
        return this.destination;
    }
}
