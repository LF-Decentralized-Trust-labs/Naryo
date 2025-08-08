package io.naryo.domain.filter.event.sync.block;

import java.math.BigInteger;

import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.filter.event.sync.SyncStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockActiveFilterSyncStateTest {

    @Test
    void testConstructor() {
        BlockActiveFilterSyncState state =
                new BlockActiveFilterSyncState(new NonNegativeBlockNumber(BigInteger.ONE));
        assertEquals(SyncStrategy.BLOCK_BASED, state.getStrategy());
        assertEquals(new NonNegativeBlockNumber(BigInteger.ONE), state.getInitialBlock());
    }

    @Test
    void testConstructorWithNullInitialBlock() {
        assertThrows(NullPointerException.class, () -> new BlockActiveFilterSyncState(null));
    }
}
