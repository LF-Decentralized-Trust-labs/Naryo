package io.naryo.application.configuration.source.model.node.subscription;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.configuration.source.model.DescriptorTestBigIntegerArgumentsProvider;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
abstract class BlockSubscriptionDescriptorTest extends SubscriptionDescriptorTest {

    @Override
    protected abstract BlockSubscriptionDescriptor getSubscriptionDescriptor();

    @Test
    void testMerge_differentSubscriptionMethod() {
        BlockSubscriptionDescriptor original =
                new DummyBlockSubscriptionDescriptor() {
                    @Override
                    public BlockSubscriptionMethod getMethod() {
                        return BlockSubscriptionMethod.POLL;
                    }
                };
        BlockSubscriptionDescriptor other =
                new DummyBlockSubscriptionDescriptor() {
                    @Override
                    public BlockSubscriptionMethod getMethod() {
                        return BlockSubscriptionMethod.PUBSUB;
                    }
                };

        BlockSubscriptionDescriptor result = (BlockSubscriptionDescriptor) original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original BlockSubscriptionDescriptor when merging with a different method");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestBigIntegerArgumentsProvider.class)
    void testMerge_initialBlock(
            BigInteger originalInitialBlock,
            BigInteger otherInitialBlock,
            BigInteger expectedInitialBlock) {
        BlockSubscriptionDescriptor original = getSubscriptionDescriptor();
        original.setInitialBlock(originalInitialBlock);
        BlockSubscriptionDescriptor other = getSubscriptionDescriptor();
        other.setInitialBlock(otherInitialBlock);

        BlockSubscriptionDescriptor result = (BlockSubscriptionDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedInitialBlock),
                result.getInitialBlock(),
                "Should merge the initial block");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestBigIntegerArgumentsProvider.class)
    void testMerge_confirmationBlocks(
            BigInteger originalConfirmationBlocks,
            BigInteger otherConfirmationBlocks,
            BigInteger expectedConfirmationBlocks) {
        BlockSubscriptionDescriptor original = getSubscriptionDescriptor();
        original.setConfirmationBlocks(originalConfirmationBlocks);
        BlockSubscriptionDescriptor other = getSubscriptionDescriptor();
        other.setConfirmationBlocks(otherConfirmationBlocks);

        BlockSubscriptionDescriptor result = (BlockSubscriptionDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedConfirmationBlocks),
                result.getConfirmationBlocks(),
                "Should merge the confirmation blocks");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestBigIntegerArgumentsProvider.class)
    void testMerge_missingTxRetryBlocks(
            BigInteger originalMissingTxRetryBlocks,
            BigInteger otherMissingTxRetryBlocks,
            BigInteger expectedMissingTxRetryBlocks) {
        BlockSubscriptionDescriptor original = getSubscriptionDescriptor();
        original.setMissingTxRetryBlocks(originalMissingTxRetryBlocks);
        BlockSubscriptionDescriptor other = getSubscriptionDescriptor();
        other.setMissingTxRetryBlocks(otherMissingTxRetryBlocks);

        BlockSubscriptionDescriptor result = (BlockSubscriptionDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedMissingTxRetryBlocks),
                result.getMissingTxRetryBlocks(),
                "Should merge the missing tx retry blocks");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestBigIntegerArgumentsProvider.class)
    void testMerge_eventInvalidationBlockThreshold(
            BigInteger originalThreshold, BigInteger otherThreshold, BigInteger expectedThreshold) {
        BlockSubscriptionDescriptor original = getSubscriptionDescriptor();
        original.setEventInvalidationBlockThreshold(originalThreshold);
        BlockSubscriptionDescriptor other = getSubscriptionDescriptor();
        other.setEventInvalidationBlockThreshold(otherThreshold);

        BlockSubscriptionDescriptor result = (BlockSubscriptionDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedThreshold),
                result.getEventInvalidationBlockThreshold(),
                "Should merge the event invalidation block threshold");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestBigIntegerArgumentsProvider.class)
    void testMerge_replayBlockOffset(
            BigInteger originalOffset, BigInteger otherOffset, BigInteger expectedOffset) {
        BlockSubscriptionDescriptor original = getSubscriptionDescriptor();
        original.setReplayBlockOffset(originalOffset);
        BlockSubscriptionDescriptor other = getSubscriptionDescriptor();
        other.setReplayBlockOffset(otherOffset);

        BlockSubscriptionDescriptor result = (BlockSubscriptionDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedOffset),
                result.getReplayBlockOffset(),
                "Should merge the replay block offset");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestBigIntegerArgumentsProvider.class)
    void testMerge_syncBlockLimit(
            BigInteger originalLimit, BigInteger otherLimit, BigInteger expectedLimit) {
        BlockSubscriptionDescriptor original = getSubscriptionDescriptor();
        original.setSyncBlockLimit(originalLimit);
        BlockSubscriptionDescriptor other = getSubscriptionDescriptor();
        other.setSyncBlockLimit(otherLimit);

        BlockSubscriptionDescriptor result = (BlockSubscriptionDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedLimit),
                result.getSyncBlockLimit(),
                "Should merge the sync block limit");
    }

    @Setter
    protected abstract static class DummyBlockSubscriptionDescriptor
            extends DummySubscriptionDescriptor implements BlockSubscriptionDescriptor {
        private BigInteger initialBlock;
        private BigInteger confirmationBlocks;
        private BigInteger missingTxRetryBlocks;
        private BigInteger eventInvalidationBlockThreshold;
        private BigInteger replayBlockOffset;
        private BigInteger syncBlockLimit;

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
}
