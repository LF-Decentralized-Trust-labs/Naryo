package io.naryo.infrastructure.configuration.persistence.document.node.subscription;

import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.block.PollBlockSubscriptionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.block.PubSubBlockSubscriptionPropertiesDocument;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public abstract class SubscriptionPropertiesDocument implements SubscriptionDescriptor {
    public static SubscriptionPropertiesDocument fromDomain(SubscriptionConfiguration source) {
        BlockSubscriptionConfiguration configuration = (BlockSubscriptionConfiguration) source;
        return switch (configuration.getMethodConfiguration()) {
            case PollBlockSubscriptionMethodConfiguration poll ->
                    new PollBlockSubscriptionPropertiesDocument(
                            configuration.getInitialBlock(),
                            configuration.getConfirmationBlocks().value(),
                            configuration.getMissingTxRetryBlocks().value(),
                            configuration.getEventInvalidationBlockThreshold().value(),
                            configuration.getReplayBlockOffset().value(),
                            configuration.getSyncBlockLimit().value(),
                            poll.getInterval().value());
            case PubSubBlockSubscriptionMethodConfiguration pubsub ->
                    new PubSubBlockSubscriptionPropertiesDocument(
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
