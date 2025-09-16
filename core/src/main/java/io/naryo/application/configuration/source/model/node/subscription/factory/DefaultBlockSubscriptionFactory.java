package io.naryo.application.configuration.source.model.node.subscription.factory;

import java.math.BigInteger;
import java.time.Duration;

import io.naryo.application.configuration.source.model.node.subscription.BlockSubscriptionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.PollBlockSubscriptionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.PubsubBlockSubscriptionDescriptor;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.poll.Interval;
import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;

import static io.naryo.application.common.util.OptionalUtil.valueOrDefault;

public final class DefaultBlockSubscriptionFactory implements BlockSubscriptionFactory {

    public static final BigInteger DEFAULT_INITIAL_BLOCK = BigInteger.valueOf(-1);
    public static final BigInteger DEFAULT_CONFIRMATION_BLOCKS = BigInteger.valueOf(12);
    public static final BigInteger DEFAULT_MISSING_TX_RETRY_BLOCKS = BigInteger.valueOf(200);
    public static final BigInteger DEFAULT_EVENT_INVALIDATION_BLOCK_THRESHOLD = BigInteger.TWO;
    public static final BigInteger DEFAULT_REPLAY_BLOCK_OFFSET = BigInteger.valueOf(12);
    public static final BigInteger DEFAULT_SYNC_BLOCK_LIMIT = BigInteger.valueOf(20_000);

    private static final Duration DEFAULT_POLL_INTERVAL = Duration.ofSeconds(5);

    @Override
    public BlockSubscriptionConfiguration create(BlockSubscriptionDescriptor descriptor) {

        return switch (descriptor) {
            case PollBlockSubscriptionDescriptor poll -> buildPollBlockSubscription(poll);
            case PubsubBlockSubscriptionDescriptor pubsub -> buildPubsubSubscription(pubsub);
            default -> throw new IllegalStateException("Unexpected value: " + descriptor);
        };
    }

    private static BlockSubscriptionConfiguration buildPollBlockSubscription(
            PollBlockSubscriptionDescriptor descriptor) {
        Duration interval = valueOrDefault(descriptor.getInterval(), DEFAULT_POLL_INTERVAL);
        BlockSubscriptionMethodConfiguration method =
                new PollBlockSubscriptionMethodConfiguration(new Interval(interval));
        return buildConfig(descriptor, method);
    }

    private static BlockSubscriptionConfiguration buildPubsubSubscription(
            PubsubBlockSubscriptionDescriptor descriptor) {
        BlockSubscriptionMethodConfiguration method =
                new PubSubBlockSubscriptionMethodConfiguration();
        return buildConfig(descriptor, method);
    }

    private static BlockSubscriptionConfiguration buildConfig(
            BlockSubscriptionDescriptor descriptor, BlockSubscriptionMethodConfiguration method) {

        BigInteger initialBlock =
                valueOrDefault(descriptor.getInitialBlock(), DEFAULT_INITIAL_BLOCK);
        NonNegativeBlockNumber confirmationBlocks =
                new NonNegativeBlockNumber(
                        valueOrDefault(
                                descriptor.getConfirmationBlocks(), DEFAULT_CONFIRMATION_BLOCKS));
        NonNegativeBlockNumber missingTxRetryBlocks =
                new NonNegativeBlockNumber(
                        valueOrDefault(
                                descriptor.getMissingTxRetryBlocks(),
                                DEFAULT_MISSING_TX_RETRY_BLOCKS));
        NonNegativeBlockNumber eventInvalidationBlockThreshold =
                new NonNegativeBlockNumber(
                        valueOrDefault(
                                descriptor.getEventInvalidationBlockThreshold(),
                                DEFAULT_EVENT_INVALIDATION_BLOCK_THRESHOLD));
        NonNegativeBlockNumber replayBlockOffset =
                new NonNegativeBlockNumber(
                        valueOrDefault(
                                descriptor.getReplayBlockOffset(), DEFAULT_REPLAY_BLOCK_OFFSET));
        NonNegativeBlockNumber syncBlockLimit =
                new NonNegativeBlockNumber(
                        valueOrDefault(descriptor.getSyncBlockLimit(), DEFAULT_SYNC_BLOCK_LIMIT));

        return new BlockSubscriptionConfiguration(
                method,
                initialBlock,
                confirmationBlocks,
                missingTxRetryBlocks,
                eventInvalidationBlockThreshold,
                replayBlockOffset,
                syncBlockLimit);
    }
}
