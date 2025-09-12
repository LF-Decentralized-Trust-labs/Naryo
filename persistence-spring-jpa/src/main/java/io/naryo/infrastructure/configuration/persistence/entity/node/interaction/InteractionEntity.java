package io.naryo.infrastructure.configuration.persistence.entity.node.interaction;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "node_interaction")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "interaction_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
public abstract class InteractionEntity implements InteractionDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    public java.util.UUID getId() {
        return this.id;
    }
}
