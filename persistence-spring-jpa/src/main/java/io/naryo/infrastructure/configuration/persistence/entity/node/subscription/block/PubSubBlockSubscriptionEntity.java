package io.naryo.infrastructure.configuration.persistence.entity.node.subscription.block;

import io.naryo.application.configuration.source.model.node.subscription.PubsubBlockSubscriptionDescriptor;

import java.math.BigInteger;

public class PubSubBlockSubscriptionEntity extends BlockSubscriptionEntity implements PubsubBlockSubscriptionDescriptor {

    public PubSubBlockSubscriptionEntity(
            BigInteger initialBlock,
            BigInteger confirmationBlocks,
            BigInteger missingTxRetryBlocks,
            BigInteger eventInvalidationBlockThreshold,
            BigInteger replayBlockOffset,
            BigInteger syncBlockLimit) {
        super(
                initialBlock,
                confirmationBlocks,
                missingTxRetryBlocks,
                eventInvalidationBlockThreshold,
                replayBlockOffset,
                syncBlockLimit);
    }
}
