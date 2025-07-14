package io.naryo.application.configuration.source.model.node.subscription;

import java.math.BigInteger;

import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;

public interface BlockSubscriptionDescriptor extends SubscriptionDescriptor {

    BlockSubscriptionMethod method();

    BigInteger getInitialBlock();

    BigInteger getConfirmationBlocks();

    BigInteger getMissingTxRetryBlocks();

    BigInteger getEventInvalidationBlockThreshold();

    BigInteger getReplayBlockOffset();

    BigInteger getSyncBlockLimit();

    void setInitialBlock(BigInteger initialBlock);

    void setConfirmationBlocks(BigInteger confirmationBlocks);

    void setMissingTxRetryBlocks(BigInteger missingTxRetryBlocks);

    @Override
    default SubscriptionStrategy strategy() {
        return SubscriptionStrategy.BLOCK_BASED;
    }

    @Override
    default SubscriptionDescriptor merge(SubscriptionDescriptor descriptor) {
        if (descriptor == null) {
            return this;
        }

        if (descriptor instanceof BlockSubscriptionDescriptor other) {
            if (!this.method().equals(other.method())) {
                return descriptor;
            }
        }

        return this;
    }
}
