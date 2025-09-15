package io.naryo.application.configuration.source.model.node.subscription.factory;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.naryo.application.configuration.source.model.node.subscription.BlockSubscriptionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.PollBlockSubscriptionDescriptor;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.method.poll.Interval;
import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;

public final class DefaultBlockSubscriptionFactory implements BlockSubscriptionFactory {

    public static final BigInteger INITIAL_BLOCK = BigInteger.valueOf(-1);
    public static final BigInteger CONFIRMATION_BLOCKS = BigInteger.valueOf(12);
    public static final BigInteger MISSING_TX_RETRY_BLOCKS = BigInteger.valueOf(200);
    public static final BigInteger EVENT_INVALIDATION_BLOCK_THRESHOLD = BigInteger.TWO;
    public static final BigInteger REPLAY_BLOCK_OFFSET = BigInteger.valueOf(12);
    public static final BigInteger SYNC_BLOCK_LIMIT = BigInteger.valueOf(20_000);

    @Override
    public BlockSubscriptionConfiguration create(BlockSubscriptionDescriptor descriptor) {

        this.applyDefaults(descriptor);

        var method =
                switch (descriptor.getMethod()) {
                    case POLL -> {
                        var poll = (PollBlockSubscriptionDescriptor) descriptor;
                        var interval = poll.getInterval().orElse(null);
                        yield new PollBlockSubscriptionMethodConfiguration(new Interval(interval));
                    }
                    case PUBSUB -> new PubSubBlockSubscriptionMethodConfiguration();
                };

        return new BlockSubscriptionConfiguration(
                method,
                descriptor.getInitialBlock().orElseThrow(),
                new NonNegativeBlockNumber(descriptor.getConfirmationBlocks().orElseThrow()),
                new NonNegativeBlockNumber(descriptor.getMissingTxRetryBlocks().orElseThrow()),
                new NonNegativeBlockNumber(
                        descriptor.getEventInvalidationBlockThreshold().orElseThrow()),
                new NonNegativeBlockNumber(descriptor.getReplayBlockOffset().orElseThrow()),
                new NonNegativeBlockNumber(descriptor.getSyncBlockLimit().orElseThrow()));
    }

    public void applyDefaults(BlockSubscriptionDescriptor d) {
        setDefault(d::getInitialBlock, d::setInitialBlock, INITIAL_BLOCK);
        setDefault(d::getConfirmationBlocks, d::setConfirmationBlocks, CONFIRMATION_BLOCKS);
        setDefault(d::getMissingTxRetryBlocks, d::setMissingTxRetryBlocks, MISSING_TX_RETRY_BLOCKS);
        setDefault(
                d::getEventInvalidationBlockThreshold,
                d::setEventInvalidationBlockThreshold,
                EVENT_INVALIDATION_BLOCK_THRESHOLD);
        setDefault(d::getReplayBlockOffset, d::setReplayBlockOffset, REPLAY_BLOCK_OFFSET);
        setDefault(d::getSyncBlockLimit, d::setSyncBlockLimit, SYNC_BLOCK_LIMIT);
    }

    private static <T> void setDefault(
            Supplier<Optional<T>> getter, Consumer<T> setter, T defaultValue) {
        setter.accept(getter.get().orElse(defaultValue));
    }
}
