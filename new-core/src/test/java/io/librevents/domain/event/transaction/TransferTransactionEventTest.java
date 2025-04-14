package io.librevents.domain.event.transaction;

import java.math.BigInteger;
import java.util.UUID;

import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.common.TransactionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransferTransactionEventTest extends AbstractTransactionEventTest {

    @Override
    protected TransactionEvent createTransactionEvent(
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
            String revertReason) {
        return new TransferTransactionEvent(
                UUID.randomUUID(),
                hash,
                status,
                nonce,
                blockHash,
                blockNumber,
                blockTimestamp,
                transactionIndex,
                sender,
                receiver,
                value,
                input,
                revertReason);
    }

    @Test
    void testConstructor() {
        TransferTransactionEvent event =
                new TransferTransactionEvent(
                        UUID.randomUUID(),
                        "0x1234567890abcdef",
                        TransactionStatus.CONFIRMED,
                        new NonNegativeBlockNumber(BigInteger.ONE),
                        "0xabcdef1234567890",
                        new NonNegativeBlockNumber(BigInteger.ONE),
                        BigInteger.valueOf(1234567890),
                        BigInteger.valueOf(1),
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        "1000000000000000000",
                        "0xabcdef1234567890",
                        "0x1234567890abcdef");
        assertNotNull(event);
    }
}
