package io.naryo.application.configuration.source.model.node.subscription;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface BlockSubscriptionDescriptor extends SubscriptionDescriptor {

    BlockSubscriptionMethod getMethod();

    Optional<BigInteger> getInitialBlock();

    Optional<BigInteger> getConfirmationBlocks();

    Optional<BigInteger> getMissingTxRetryBlocks();

    Optional<BigInteger> getEventInvalidationBlockThreshold();

    Optional<BigInteger> getReplayBlockOffset();

    Optional<BigInteger> getSyncBlockLimit();

    void setInitialBlock(BigInteger initialBlock);

    void setConfirmationBlocks(BigInteger confirmationBlocks);

    void setMissingTxRetryBlocks(BigInteger missingTxRetryBlocks);

    void setEventInvalidationBlockThreshold(BigInteger eventInvalidationBlockThreshold);

    void setReplayBlockOffset(BigInteger replayBlockOffset);

    void setSyncBlockLimit(BigInteger syncBlockLimit);

    @Override
    default SubscriptionStrategy getStrategy() {
        return SubscriptionStrategy.BLOCK_BASED;
    }

    @Override
    default SubscriptionDescriptor merge(SubscriptionDescriptor other) {
        if (!(other instanceof BlockSubscriptionDescriptor otherBlockSubscription)) {
            return this;
        }

        if (!this.getMethod().equals(otherBlockSubscription.getMethod())) {
            return this;
        }

        mergeOptionals(
                this::setInitialBlock,
                this.getInitialBlock(),
                otherBlockSubscription.getInitialBlock());
        mergeOptionals(
                this::setConfirmationBlocks,
                this.getConfirmationBlocks(),
                otherBlockSubscription.getConfirmationBlocks());
        mergeOptionals(
                this::setMissingTxRetryBlocks,
                this.getMissingTxRetryBlocks(),
                otherBlockSubscription.getMissingTxRetryBlocks());
        mergeOptionals(
                this::setEventInvalidationBlockThreshold,
                this.getEventInvalidationBlockThreshold(),
                otherBlockSubscription.getEventInvalidationBlockThreshold());
        mergeOptionals(
                this::setReplayBlockOffset,
                this.getReplayBlockOffset(),
                otherBlockSubscription.getReplayBlockOffset());
        mergeOptionals(
                this::setSyncBlockLimit,
                this.getSyncBlockLimit(),
                otherBlockSubscription.getSyncBlockLimit());

        return SubscriptionDescriptor.super.merge(other);
    }
}
