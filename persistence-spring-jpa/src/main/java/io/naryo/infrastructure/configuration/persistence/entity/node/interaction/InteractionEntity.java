package io.naryo.infrastructure.configuration.persistence.entity.node.interaction;

import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interactions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "interaction_type", discriminatorType = DiscriminatorType.STRING, length = 50)
@NoArgsConstructor
public abstract class InteractionEntity implements InteractionDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private java.util.UUID id;

    // Getter para el ID
    public java.util.UUID getId() {
        return id;
    }
}
