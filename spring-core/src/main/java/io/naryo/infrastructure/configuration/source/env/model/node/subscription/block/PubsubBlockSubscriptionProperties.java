package io.naryo.infrastructure.configuration.source.env.model.node.subscription.block;

import java.math.BigInteger;

import io.naryo.application.configuration.source.model.node.subscription.PubsubBlockSubscriptionDescriptor;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;

public final class PubsubBlockSubscriptionProperties extends BlockSubscriptionProperties
        implements PubsubBlockSubscriptionDescriptor {

    public PubsubBlockSubscriptionProperties(
            BigInteger initialBlock,
            BigInteger confirmationBlocks,
            BigInteger missingTxRetryBlocks,
            BigInteger eventInvalidationBlockThreshold,
            BigInteger replayBlockOffset,
            BigInteger syncBlockLimit) {
        super(
                BlockSubscriptionMethod.PUBSUB,
                initialBlock,
                confirmationBlocks,
                missingTxRetryBlocks,
                eventInvalidationBlockThreshold,
                replayBlockOffset,
                syncBlockLimit);
    }
}
