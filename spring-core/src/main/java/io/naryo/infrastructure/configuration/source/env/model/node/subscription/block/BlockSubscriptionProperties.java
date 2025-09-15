package io.naryo.infrastructure.configuration.source.env.model.node.subscription.block;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.subscription.BlockSubscriptionDescriptor;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.SubscriptionProperties;
import jakarta.annotation.Nullable;
import lombok.Setter;

public abstract class BlockSubscriptionProperties extends SubscriptionProperties
        implements BlockSubscriptionDescriptor {

    private @Setter @Nullable BigInteger initialBlock;
    private @Setter @Nullable BigInteger confirmationBlocks;
    private @Setter @Nullable BigInteger missingTxRetryBlocks;
    private @Setter @Nullable BigInteger eventInvalidationBlockThreshold;
    private @Setter @Nullable BigInteger replayBlockOffset;
    private @Setter @Nullable BigInteger syncBlockLimit;

    public BlockSubscriptionProperties(
            BigInteger initialBlock,
            BigInteger confirmationBlocks,
            BigInteger missingTxRetryBlocks,
            BigInteger eventInvalidationBlockThreshold,
            BigInteger replayBlockOffset,
            BigInteger syncBlockLimit
    ) {
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
