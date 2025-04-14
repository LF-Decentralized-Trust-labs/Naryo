package io.librevents.domain.event.transaction;

import java.math.BigInteger;

import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.common.TransactionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractTransactionEventTest {

    protected abstract TransactionEvent createTransactionEvent(
            String hash,
            TransactionStatus status,
            NonNegativeBlockNumber nonce,
            String blockHash,
            NonNegativeBlockNumber blockNumber,
            BigInteger blockTimestamp,
            BigInteger transactionIndex,
            String sender,
            String receiver,
            String value,
            String input,
            String revertReason);

    @Test
    void testConstructorWithNullHash() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createTransactionEvent(
                                null,
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "sender",
                                "receiver",
                                "value",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithNullStatus() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                null,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "sender",
                                "receiver",
                                "value",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithNullNonce() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                null,
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "sender",
                                "receiver",
                                "value",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithNullBlockHash() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                null,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "sender",
                                "receiver",
                                "value",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithNullBlockNumber() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                null,
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "sender",
                                "receiver",
                                "value",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithNullBlockTimestamp() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                null,
                                BigInteger.ONE,
                                "sender",
                                "receiver",
                                "value",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithNullTransactionIndex() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                null,
                                "sender",
                                "receiver",
                                "value",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithNullSender() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.ONE,
                                null,
                                "receiver",
                                "value",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithNullReceiver() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "sender",
                                null,
                                "value",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithNullValue() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "sender",
                                "receiver",
                                null,
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithNullInput() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "sender",
                                "receiver",
                                "value",
                                null,
                                ""));
    }

    @Test
    void testConstructorWithEmptyHash() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        createTransactionEvent(
                                "",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "sender",
                                "receiver",
                                "value",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithEmptyBlockHash() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "sender",
                                "receiver",
                                "value",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithEmptySender() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "",
                                "receiver",
                                "value",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithEmptyReceiver() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "sender",
                                "",
                                "value",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithEmptyValue() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "sender",
                                "receiver",
                                "",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithEmptyInput() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "sender",
                                "receiver",
                                "value",
                                "",
                                ""));
    }

    @Test
    void testConstructorWithNegativeBlockTimestamp() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.valueOf(-1),
                                BigInteger.ONE,
                                "sender",
                                "receiver",
                                "value",
                                "input",
                                ""));
    }

    @Test
    void testConstructorWithNegativeTransactionIndex() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        createTransactionEvent(
                                "hash",
                                TransactionStatus.CONFIRMED,
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                "blockHash",
                                new NonNegativeBlockNumber(BigInteger.ONE),
                                BigInteger.ONE,
                                BigInteger.valueOf(-1),
                                "sender",
                                "receiver",
                                "value",
                                "input",
                                ""));
    }
}
