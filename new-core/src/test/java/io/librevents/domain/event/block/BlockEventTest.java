package io.librevents.domain.event.block;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.event.EventType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockEventTest {

    @Test
    void testBlockEventCreation() {
        BlockEvent blockEvent =
                new BlockEvent(
                        UUID.randomUUID(),
                        new NonNegativeBlockNumber(BigInteger.ONE),
                        "0x1234567890abcdef",
                        "0xabcdef1234567890",
                        BigInteger.valueOf(1000),
                        BigInteger.valueOf(50000),
                        BigInteger.valueOf(System.currentTimeMillis()),
                        List.of());
        assertNotNull(blockEvent);
        assertEquals(EventType.BLOCK, blockEvent.getEventType());
    }

    @Test
    void testBlockEventCreationWithNullNodeId() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockEvent(
                                null,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "0x1234567890abcdef",
                                "0xabcdef1234567890",
                                BigInteger.valueOf(1000),
                                BigInteger.valueOf(50000),
                                BigInteger.valueOf(System.currentTimeMillis()),
                                List.of()));
    }

    @Test
    void testBlockEventCreationWithNullNumber() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockEvent(
                                UUID.randomUUID(),
                                null,
                                "0x1234567890abcdef",
                                "0xabcdef1234567890",
                                BigInteger.valueOf(1000),
                                BigInteger.valueOf(50000),
                                BigInteger.valueOf(System.currentTimeMillis()),
                                List.of()));
    }

    @Test
    void testBlockEventCreationWithNullHash() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockEvent(
                                UUID.randomUUID(),
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                null,
                                "0xabcdef1234567890",
                                BigInteger.valueOf(1000),
                                BigInteger.valueOf(50000),
                                BigInteger.valueOf(System.currentTimeMillis()),
                                List.of()));
    }

    @Test
    void testBlockEventCreationWithNullLogsBloom() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockEvent(
                                UUID.randomUUID(),
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "0x1234567890abcdef",
                                null,
                                BigInteger.valueOf(1000),
                                BigInteger.valueOf(50000),
                                BigInteger.valueOf(System.currentTimeMillis()),
                                List.of()));
    }

    @Test
    void testBlockEventCreationWithNullSize() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockEvent(
                                UUID.randomUUID(),
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "0x1234567890abcdef",
                                "0xabcdef1234567890",
                                null,
                                BigInteger.valueOf(50000),
                                BigInteger.valueOf(System.currentTimeMillis()),
                                List.of()));
    }

    @Test
    void testBlockEventCreationWithNullGasUsed() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockEvent(
                                UUID.randomUUID(),
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "0x1234567890abcdef",
                                "0xabcdef1234567890",
                                BigInteger.valueOf(1000),
                                null,
                                BigInteger.valueOf(System.currentTimeMillis()),
                                List.of()));
    }

    @Test
    void testBlockEventCreationWithNullTimestamp() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockEvent(
                                UUID.randomUUID(),
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "0x1234567890abcdef",
                                "0xabcdef1234567890",
                                BigInteger.valueOf(1000),
                                BigInteger.valueOf(50000),
                                null,
                                List.of()));
    }

    @Test
    void testBlockEventCreationWithEmptyHash() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        new BlockEvent(
                                UUID.randomUUID(),
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "",
                                "0xabcdef1234567890",
                                BigInteger.valueOf(1000),
                                BigInteger.valueOf(50000),
                                BigInteger.valueOf(System.currentTimeMillis()),
                                List.of()));
    }

    @Test
    void testBlockEventCreationWithEmptyLogsBloom() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        new BlockEvent(
                                UUID.randomUUID(),
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "0x1234567890abcdef",
                                "",
                                BigInteger.valueOf(1000),
                                BigInteger.valueOf(50000),
                                BigInteger.valueOf(System.currentTimeMillis()),
                                List.of()));
    }

    @Test
    void testBlockEventCreationWithNegativeSize() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        new BlockEvent(
                                UUID.randomUUID(),
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "0x1234567890abcdef",
                                "0xabcdef1234567890",
                                BigInteger.valueOf(-1000),
                                BigInteger.valueOf(50000),
                                BigInteger.valueOf(System.currentTimeMillis()),
                                List.of()));
    }

    @Test
    void testBlockEventCreationWithNegativeGasUsed() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        new BlockEvent(
                                UUID.randomUUID(),
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "0x1234567890abcdef",
                                "0xabcdef1234567890",
                                BigInteger.valueOf(1000),
                                BigInteger.valueOf(-50000),
                                BigInteger.valueOf(System.currentTimeMillis()),
                                List.of()));
    }

    @Test
    void testBlockEventCreationWithNegativeTimestamp() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        new BlockEvent(
                                UUID.randomUUID(),
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "0x1234567890abcdef",
                                "0xabcdef1234567890",
                                BigInteger.valueOf(1000),
                                BigInteger.valueOf(50000),
                                BigInteger.valueOf(-System.currentTimeMillis()),
                                List.of()));
    }
}
