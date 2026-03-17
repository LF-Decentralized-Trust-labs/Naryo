package io.naryo.infrastructure.store.event.persistence.document.transaction;

import java.math.BigInteger;
import java.util.UUID;

import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.transaction.eth.EthTransactionEvent;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@Getter
@TypeAlias("eth_transaction_event")
public class EthTransactionEventDocument extends TransactionEventDocument {
    private final String blockHash;
    private final BigInteger nonce;
    private final BigInteger transactionIndex;
    private final String input;
    private final String revertReason;

    public EthTransactionEventDocument(
            String nodeId,
            String hash,
            BigInteger blockNumber,
            BigInteger blockTimestamp,
            String sender,
            String receiver,
            String value,
            String status,
            String blockHash,
            BigInteger nonce,
            BigInteger transactionIndex,
            String input,
            String revertReason) {
        super(nodeId, hash, blockNumber, blockTimestamp, sender, receiver, value, status);
        this.blockHash = blockHash;
        this.nonce = nonce;
        this.transactionIndex = transactionIndex;
        this.input = input;
        this.revertReason = revertReason;
    }

    public static TransactionEventDocument fromEthTransactionEvent(EthTransactionEvent event) {
        return new EthTransactionEventDocument(
                event.getNodeId().toString(),
                event.getHash(),
                event.getBlockNumber().value(),
                event.getBlockTimestamp(),
                event.getSender(),
                event.getReceiver(),
                event.getValue(),
                event.getStatus().name(),
                event.getBlockHash(),
                event.getNonce().value(),
                event.getTransactionIndex(),
                event.getInput(),
                event.getRevertReason());
    }

    @Override
    public EthTransactionEvent toTransactionEvent() {
        return new EthTransactionEvent(
                UUID.fromString(getNodeId()),
                getHash(),
                TransactionStatus.valueOf(getStatus()),
                new NonNegativeBlockNumber(getBlockNumber()),
                getBlockTimestamp(),
                getSender(),
                getReceiver(),
                getValue(),
                blockHash,
                new NonNegativeBlockNumber(nonce),
                transactionIndex,
                input,
                revertReason);
    }
}
