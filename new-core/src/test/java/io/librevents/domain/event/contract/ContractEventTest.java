package io.librevents.domain.event.contract;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import io.librevents.domain.event.contract.parameter.AddressParameter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContractEventTest {

    @Test
    void testConstructor() {
        ContractEvent event =
                new ContractEvent(
                        UUID.randomUUID(),
                        List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                        "0xabcdef1234567890",
                        BigInteger.valueOf(1),
                        BigInteger.valueOf(1),
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        ContractEventStatus.CONFIRMED,
                        BigInteger.valueOf(1));

        assertNotNull(event);
        assertEquals("0xabcdef1234567890", event.getTransactionHash());
        assertEquals(BigInteger.valueOf(1), event.getLogIndex());
        assertEquals(BigInteger.valueOf(1), event.getBlockNumber());
        assertEquals("0xabcdef1234567890", event.getBlockHash());
        assertEquals("0xabcdef1234567890", event.getContractAddress());
        assertEquals("0xabcdef1234567890", event.getSender());
        assertEquals(ContractEventStatus.CONFIRMED, event.getStatus());
        assertEquals(BigInteger.valueOf(1), event.getTimestamp());
    }

    @Test
    void testConstructorWithNullNodeId() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new ContractEvent(
                            null,
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            "0xabcdef1234567890",
                            BigInteger.valueOf(1),
                            BigInteger.valueOf(1),
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            ContractEventStatus.CONFIRMED,
                            BigInteger.valueOf(1));
                });
    }

    @Test
    void testConstructorWithNullParameters() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            null,
                            "0xabcdef1234567890",
                            BigInteger.valueOf(1),
                            BigInteger.valueOf(1),
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            ContractEventStatus.CONFIRMED,
                            BigInteger.valueOf(1));
                });
    }

    @Test
    void testConstructorWithNullTransactionHash() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            null,
                            BigInteger.valueOf(1),
                            BigInteger.valueOf(1),
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            ContractEventStatus.CONFIRMED,
                            BigInteger.valueOf(1));
                });
    }

    @Test
    void testConstructorWithEmptyTransactionHash() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            "",
                            BigInteger.valueOf(1),
                            BigInteger.valueOf(1),
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            ContractEventStatus.CONFIRMED,
                            BigInteger.valueOf(1));
                });
    }

    @Test
    void testConstructorWithNullLogIndex() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            "0xabcdef1234567890",
                            null,
                            BigInteger.valueOf(1),
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            ContractEventStatus.CONFIRMED,
                            BigInteger.valueOf(1));
                });
    }

    @Test
    void testConstructorWithNegativeLogIndex() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            "0xabcdef1234567890",
                            BigInteger.valueOf(-1),
                            BigInteger.valueOf(1),
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            ContractEventStatus.CONFIRMED,
                            BigInteger.valueOf(1));
                });
    }

    @Test
    void testConstructorWithNullBlockNumber() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            "0xabcdef1234567890",
                            BigInteger.valueOf(1),
                            null,
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            ContractEventStatus.CONFIRMED,
                            BigInteger.valueOf(1));
                });
    }

    @Test
    void testConstructorWithNegativeBlockNumber() {
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                new ContractEvent(
                        UUID.randomUUID(),
                        List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                        "0xabcdef1234567890",
                        BigInteger.valueOf(1),
                        BigInteger.valueOf(-1),
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        ContractEventStatus.CONFIRMED,
                        BigInteger.valueOf(1));
            }
        );
    }

    @Test
    void testConstructorWithNullBlockHash() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            "0xabcdef1234567890",
                            BigInteger.valueOf(1),
                            BigInteger.valueOf(1),
                            null,
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            ContractEventStatus.CONFIRMED,
                            BigInteger.valueOf(1));
                });
    }

    @Test
    void testConstructorWithEmptyBlockHash() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            "0xabcdef1234567890",
                            BigInteger.valueOf(1),
                            BigInteger.valueOf(1),
                            "",
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            ContractEventStatus.CONFIRMED,
                            BigInteger.valueOf(1));
                });
    }

    @Test
    void testConstructorWithNullContractAddress() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            "0xabcdef1234567890",
                            BigInteger.valueOf(1),
                            BigInteger.valueOf(1),
                            "0xabcdef1234567890",
                            null,
                            "0xabcdef1234567890",
                            ContractEventStatus.CONFIRMED,
                            BigInteger.valueOf(1));
                });
    }

    @Test
    void testConstructorWithEmptyContractAddress() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            "0xabcdef1234567890",
                            BigInteger.valueOf(1),
                            BigInteger.valueOf(1),
                            "0xabcdef1234567890",
                            "",
                            "0xabcdef1234567890",
                            ContractEventStatus.CONFIRMED,
                            BigInteger.valueOf(1));
                });
    }

    @Test
    void testConstructorWithNullSender() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            "0xabcdef1234567890",
                            BigInteger.valueOf(1),
                            BigInteger.valueOf(1),
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            null,
                            ContractEventStatus.CONFIRMED,
                            BigInteger.valueOf(1));
                });
    }

    @Test
    void testConstructorWithEmptySender() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            "0xabcdef1234567890",
                            BigInteger.valueOf(1),
                            BigInteger.valueOf(1),
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            "",
                            ContractEventStatus.CONFIRMED,
                            BigInteger.valueOf(1));
                });
    }

    @Test
    void testConstructorWithNullStatus() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            "0xabcdef1234567890",
                            BigInteger.valueOf(1),
                            BigInteger.valueOf(1),
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            null,
                            BigInteger.valueOf(1));
                });
    }

    @Test
    void testConstructorWithNullTimestamp() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            "0xabcdef1234567890",
                            BigInteger.valueOf(1),
                            BigInteger.valueOf(1),
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            ContractEventStatus.CONFIRMED,
                            null);
                });
    }

    @Test
    void testConstructorWithNegativeTimestamp() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new ContractEvent(
                            UUID.randomUUID(),
                            List.of(new AddressParameter(true, 1, "0x1234567890abcdef")),
                            "0xabcdef1234567890",
                            BigInteger.valueOf(1),
                            BigInteger.valueOf(1),
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            "0xabcdef1234567890",
                            ContractEventStatus.CONFIRMED,
                            BigInteger.valueOf(-1));
                });
    }
}
