package io.naryo.domain.filter.event.sync.block;

import java.math.BigInteger;

import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.filter.event.sync.SyncStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockActiveSyncStateTest {

    @Test
    void testConstructor() {
        BlockActiveSyncState state =
                new BlockActiveSyncState(new NonNegativeBlockNumber(BigInteger.ONE));
        assertEquals(SyncStrategy.BLOCK_BASED, state.getStrategy());
        assertFalse(state.isSync());
        assertEquals(new NonNegativeBlockNumber(BigInteger.ONE), state.getInitialBlock());
    }

    @Test
    void testConstructorWithNullInitialBlock() {
        assertThrows(NullPointerException.class, () -> new BlockActiveSyncState(null));
    }

    @Test
    void testSetSync() {
        BlockActiveSyncState state =
                new BlockActiveSyncState(new NonNegativeBlockNumber(BigInteger.ONE));
        assertFalse(state.isSync());
        state.setSync(true);
        assertTrue(state.isSync());
    }
}
