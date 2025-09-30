package io.naryo.infrastructure.configuration.persistence.entity.node.interaction;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;
import io.naryo.infrastructure.configuration.persistence.entity.node.interaction.block.EthereumRpcBlockInteractionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.interaction.block.HederaMirrorNodeBlockInteractionEntity;
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

    public static InteractionEntity fromDomain(InteractionConfiguration source) {
        return switch (source) {
            case EthereumRpcBlockInteractionConfiguration ignore ->
                    new EthereumRpcBlockInteractionEntity();
            case HederaMirrorNodeBlockInteractionConfiguration hedera ->
                    new HederaMirrorNodeBlockInteractionEntity(
                            hedera.getLimitPerRequest().value(),
                            hedera.getRetriesPerRequest().value());
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported interaction type: " + source.getClass().getSimpleName());
        };
    }
}
