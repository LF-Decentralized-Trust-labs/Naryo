package io.librevents.domain.common;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class NonNegativeBlockNumberTest {

    @Test
    void testNonNegativeBlockNumberCreation() {
        NonNegativeBlockNumber blockNumber = new NonNegativeBlockNumber(BigInteger.TEN);
        assertEquals(BigInteger.TEN, blockNumber.value());
    }

    @Test
    void testNonNegativeBlockNumberCreationWithNegativeValue() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new NonNegativeBlockNumber(BigInteger.valueOf(-1)));
        assertEquals("Block number must be >= 0", exception.getMessage());
    }

    @Test
    void testNonNegativeBlockNumberCreationWithNullValue() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class, () -> new NonNegativeBlockNumber(null));
        assertEquals("Block number must be >= 0", exception.getMessage());
    }

    @Test
    void testIsZero() {
        NonNegativeBlockNumber blockNumber = new NonNegativeBlockNumber(BigInteger.ZERO);
        assertTrue(blockNumber.isZero());

        NonNegativeBlockNumber nonZeroBlockNumber = new NonNegativeBlockNumber(BigInteger.ONE);
        assertFalse(nonZeroBlockNumber.isZero());
    }

    @Test
    void testIsGreaterThan() {
        NonNegativeBlockNumber blockNumber = new NonNegativeBlockNumber(BigInteger.TEN);
        NonNegativeBlockNumber otherBlockNumber = new NonNegativeBlockNumber(BigInteger.valueOf(5));
        assertTrue(blockNumber.isGreaterThan(otherBlockNumber));

        NonNegativeBlockNumber equalBlockNumber = new NonNegativeBlockNumber(BigInteger.TEN);
        assertFalse(blockNumber.isGreaterThan(equalBlockNumber));

        NonNegativeBlockNumber smallerBlockNumber =
                new NonNegativeBlockNumber(BigInteger.valueOf(15));
        assertFalse(blockNumber.isGreaterThan(smallerBlockNumber));
    }

    @Test
    void testAdd() {
        NonNegativeBlockNumber blockNumber1 = new NonNegativeBlockNumber(BigInteger.TEN);
        NonNegativeBlockNumber blockNumber2 = new NonNegativeBlockNumber(BigInteger.valueOf(5));
        NonNegativeBlockNumber result = blockNumber1.add(blockNumber2);
        assertEquals(new NonNegativeBlockNumber(BigInteger.valueOf(15)), result);
    }
}
