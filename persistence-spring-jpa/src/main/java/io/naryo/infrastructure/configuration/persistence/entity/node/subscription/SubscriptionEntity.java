package io.naryo.infrastructure.configuration.persistence.entity.node.subscription;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;
import io.naryo.infrastructure.configuration.persistence.entity.node.subscription.block.PollBlockSubscriptionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.subscription.block.PubSubBlockSubscriptionEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "node_subscription")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "subscription_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
public abstract class SubscriptionEntity implements SubscriptionDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    @Getter
    private UUID id;

    public static SubscriptionEntity fromDomain(SubscriptionConfiguration source) {
        BlockSubscriptionConfiguration configuration = (BlockSubscriptionConfiguration) source;
        return switch (configuration.getMethodConfiguration()) {
            case PollBlockSubscriptionMethodConfiguration poll ->
                    new PollBlockSubscriptionEntity(
                            configuration.getInitialBlock(),
                            configuration.getConfirmationBlocks().value(),
                            configuration.getMissingTxRetryBlocks().value(),
                            configuration.getEventInvalidationBlockThreshold().value(),
                            configuration.getReplayBlockOffset().value(),
                            configuration.getSyncBlockLimit().value(),
                            poll.getInterval().value());
            case PubSubBlockSubscriptionMethodConfiguration pubsub ->
                    new PubSubBlockSubscriptionEntity(
                            configuration.getInitialBlock(),
                            configuration.getConfirmationBlocks().value(),
                            configuration.getMissingTxRetryBlocks().value(),
                            configuration.getEventInvalidationBlockThreshold().value(),
                            configuration.getReplayBlockOffset().value(),
                            configuration.getSyncBlockLimit().value());
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported subscription type: "
                                    + configuration
                                            .getMethodConfiguration()
                                            .getClass()
                                            .getSimpleName());
        };
    }
}
