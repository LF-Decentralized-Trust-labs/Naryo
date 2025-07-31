package io.naryo.domain.filter.transaction;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.AbstractFilterTest;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionFilterTest extends AbstractFilterTest {

    @Override
    protected Filter createFilter(UUID id, FilterName name, UUID nodeId) {
        return new TransactionFilter(
                id, name, nodeId, IdentifierType.HASH, "0x0", Set.of(TransactionStatus.FAILED));
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
                                Set.of(TransactionStatus.FAILED)));
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
                                IdentifierType.HASH,
                                null,
                                Set.of(TransactionStatus.FAILED)));
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
                                IdentifierType.HASH,
                                "0x0",
                                null));
    }

    @Test
    void testEmptyStatuses() {
        TransactionFilter filter =
                new TransactionFilter(
                        DEFAULT_ID,
                        new FilterName(DEFAULT_NAME),
                        DEFAULT_ID,
                        IdentifierType.HASH,
                        "0x0",
                        new HashSet<>());

        assertEquals("0x0", filter.getValue());
        assertEquals(IdentifierType.HASH, filter.getIdentifierType());
        assertEquals(Set.of(TransactionStatus.values()), filter.getStatuses());
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
                                IdentifierType.HASH,
                                "",
                                Set.of(TransactionStatus.FAILED)));
    }

    @Test
    void testValidValue() {
        TransactionFilter filter =
                new TransactionFilter(
                        DEFAULT_ID,
                        new FilterName(DEFAULT_NAME),
                        DEFAULT_ID,
                        IdentifierType.HASH,
                        "0x0",
                        Set.of(TransactionStatus.FAILED));

        assertEquals("0x0", filter.getValue());
        assertEquals(IdentifierType.HASH, filter.getIdentifierType());
        assertEquals(Set.of(TransactionStatus.FAILED), filter.getStatuses());
    }
}
