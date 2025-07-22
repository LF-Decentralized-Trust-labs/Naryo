package io.naryo.application.configuration.source.model.node.subscription;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;

public interface BlockSubscriptionDescriptor extends SubscriptionDescriptor {

    BlockSubscriptionMethod method();

    Optional<BigInteger> getInitialBlock();

    Optional<BigInteger> getConfirmationBlocks();

    Optional<BigInteger> getMissingTxRetryBlocks();

    Optional<BigInteger> getEventInvalidationBlockThreshold();

    Optional<BigInteger> getReplayBlockOffset();

    Optional<BigInteger> getSyncBlockLimit();

    void setInitialBlock(BigInteger initialBlock);

    void setConfirmationBlocks(BigInteger confirmationBlocks);

    void setMissingTxRetryBlocks(BigInteger missingTxRetryBlocks);

    @Override
    default SubscriptionStrategy getStrategy() {
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
