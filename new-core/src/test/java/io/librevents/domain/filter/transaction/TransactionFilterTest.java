package io.librevents.domain.filter.transaction;

import java.util.List;
import java.util.UUID;

import io.librevents.domain.common.TransactionStatus;
import io.librevents.domain.filter.AbstractFilterTest;
import io.librevents.domain.filter.Filter;
import io.librevents.domain.filter.FilterName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionFilterTest extends AbstractFilterTest {

    @Override
    protected Filter createFilter(UUID id, FilterName name, UUID nodeId) {
        return new TransactionFilter(
                id,
                name,
                nodeId,
                IdentifierType.CONTRACT_ADDRESS,
                "0x0",
                List.of(TransactionStatus.FAILED));
    }

    @Test
    void testNullIdentifierType() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new TransactionFilter(
                                UUID.randomUUID(),
                                new FilterName("Test"),
                                UUID.randomUUID(),
                                null,
                                "0x0",
                                List.of(TransactionStatus.FAILED)));
    }

    @Test
    void testNullValue() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new TransactionFilter(
                                UUID.randomUUID(),
                                new FilterName("Test"),
                                UUID.randomUUID(),
                                IdentifierType.CONTRACT_ADDRESS,
                                null,
                                List.of(TransactionStatus.FAILED)));
    }

    @Test
    void testNullStatuses() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new TransactionFilter(
                                UUID.randomUUID(),
                                new FilterName("Test"),
                                UUID.randomUUID(),
                                IdentifierType.CONTRACT_ADDRESS,
                                "0x0",
                                null));
    }

    @Test
    void testEmptyStatuses() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        new TransactionFilter(
                                UUID.randomUUID(),
                                new FilterName("Test"),
                                UUID.randomUUID(),
                                IdentifierType.CONTRACT_ADDRESS,
                                "0x0",
                                List.of()));
    }

    @Test
    void testEmptyValue() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        new TransactionFilter(
                                UUID.randomUUID(),
                                new FilterName("Test"),
                                UUID.randomUUID(),
                                IdentifierType.CONTRACT_ADDRESS,
                                "",
                                List.of(TransactionStatus.FAILED)));
    }

    @Test
    void testValidValue() {
        TransactionFilter filter =
                new TransactionFilter(
                        DEFAULT_ID,
                        new FilterName(DEFAULT_NAME),
                        DEFAULT_ID,
                        IdentifierType.CONTRACT_ADDRESS,
                        "0x0",
                        List.of(TransactionStatus.FAILED));

        assertEquals("0x0", filter.getValue());
        assertEquals(IdentifierType.CONTRACT_ADDRESS, filter.getIdentifierType());
        assertEquals(List.of(TransactionStatus.FAILED), filter.getStatuses());
    }
}
