package io.librevents.domain.node.subscription.block;

import java.math.BigInteger;

import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class BlockSubscriptionConfigurationTest {

    protected static class MockBlockSubscriptionMethodConfiguration
            extends BlockSubscriptionMethodConfiguration {
        protected MockBlockSubscriptionMethodConfiguration() {
            super(BlockSubscriptionMethod.POLL);
        }
    }

    @Test
    void testBlockSubscriptionConfigurationCreation() {
        BlockSubscriptionConfiguration config =
                new BlockSubscriptionConfiguration(
                        new MockBlockSubscriptionMethodConfiguration(),
                        new NonNegativeBlockNumber(BigInteger.ZERO),
                        new NonNegativeBlockNumber(BigInteger.ZERO),
                        new NonNegativeBlockNumber(BigInteger.ZERO),
                        new NonNegativeBlockNumber(BigInteger.ZERO),
                        new NonNegativeBlockNumber(BigInteger.ZERO),
                        new NonNegativeBlockNumber(BigInteger.ZERO),
                        new NonNegativeBlockNumber(BigInteger.ZERO));
        assertNotNull(config);
    }

    @Test
    void testBlockSubscriptionMethodConfigurationCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new BlockSubscriptionConfiguration(
                            null,
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO));
                });
    }

    @Test
    void testInitialBlockCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new BlockSubscriptionConfiguration(
                            new MockBlockSubscriptionMethodConfiguration(),
                            null,
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO));
                });
    }

    @Test
    void testLatestBlockCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new BlockSubscriptionConfiguration(
                            new MockBlockSubscriptionMethodConfiguration(),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            null,
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO));
                });
    }

    @Test
    void testConfirmationBlocksCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new BlockSubscriptionConfiguration(
                            new MockBlockSubscriptionMethodConfiguration(),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            null,
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO));
                });
    }

    @Test
    void testMissingTxRetryBlocksCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new BlockSubscriptionConfiguration(
                            new MockBlockSubscriptionMethodConfiguration(),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            null,
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO));
                });
    }

    @Test
    void testEventInvalidationBlockThresholdCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new BlockSubscriptionConfiguration(
                            new MockBlockSubscriptionMethodConfiguration(),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            null,
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO));
                });
    }

    @Test
    void testReplayBlockOffsetCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new BlockSubscriptionConfiguration(
                            new MockBlockSubscriptionMethodConfiguration(),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            null,
                            new NonNegativeBlockNumber(BigInteger.ZERO));
                });
    }

    @Test
    void testSyncBlockLimitCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new BlockSubscriptionConfiguration(
                            new MockBlockSubscriptionMethodConfiguration(),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            null);
                });
    }
}
