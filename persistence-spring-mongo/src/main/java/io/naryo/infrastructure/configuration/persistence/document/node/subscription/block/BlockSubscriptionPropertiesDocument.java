package io.naryo.infrastructure.configuration.persistence.document.node.subscription.block;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.subscription.BlockSubscriptionDescriptor;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.SubscriptionPropertiesDocument;
import jakarta.annotation.Nullable;
import lombok.Setter;

@Setter
public abstract class BlockSubscriptionPropertiesDocument extends SubscriptionPropertiesDocument
        implements BlockSubscriptionDescriptor {

    private @Nullable BigInteger initialBlock;
    private @Nullable BigInteger confirmationBlocks;
    private @Nullable BigInteger missingTxRetryBlocks;
    private @Nullable BigInteger eventInvalidationBlockThreshold;
    private @Nullable BigInteger replayBlockOffset;
    private @Nullable BigInteger syncBlockLimit;

    public BlockSubscriptionPropertiesDocument(
            BigInteger initialBlock,
            BigInteger confirmationBlocks,
            BigInteger missingTxRetryBlocks,
            BigInteger eventInvalidationBlockThreshold,
            BigInteger replayBlockOffset,
            BigInteger syncBlockLimit) {
        this.initialBlock = initialBlock;
        this.confirmationBlocks = confirmationBlocks;
        this.missingTxRetryBlocks = missingTxRetryBlocks;
        this.eventInvalidationBlockThreshold = eventInvalidationBlockThreshold;
        this.replayBlockOffset = replayBlockOffset;
        this.syncBlockLimit = syncBlockLimit;
    }

    @Override
    public Optional<BigInteger> getInitialBlock() {
        return Optional.ofNullable(initialBlock);
    }

    @Override
    public Optional<BigInteger> getConfirmationBlocks() {
        return Optional.ofNullable(confirmationBlocks);
    }

    @Override
    public Optional<BigInteger> getMissingTxRetryBlocks() {
        return Optional.ofNullable(missingTxRetryBlocks);
    }

    @Override
    public Optional<BigInteger> getEventInvalidationBlockThreshold() {
        return Optional.ofNullable(eventInvalidationBlockThreshold);
    }

    @Override
    public Optional<BigInteger> getReplayBlockOffset() {
        return Optional.ofNullable(replayBlockOffset);
    }

    @Override
    public Optional<BigInteger> getSyncBlockLimit() {
        return Optional.ofNullable(syncBlockLimit);
    }
}
