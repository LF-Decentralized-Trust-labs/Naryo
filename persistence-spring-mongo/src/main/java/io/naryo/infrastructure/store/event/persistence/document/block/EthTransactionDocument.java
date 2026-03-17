package io.naryo.infrastructure.store.event.persistence.document.block;

import java.math.BigInteger;

import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.application.node.interactor.block.dto.eth.EthTransaction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TypeAlias("eth_transaction")
public final class EthTransactionDocument extends TransactionDocument {
    private final BigInteger index;
    private final BigInteger nonce;
    private final String blockHash;
    private final String input;
    private final String logBloom;
    private final String revertReason;

    public EthTransactionDocument(
            String hash,
            BigInteger blockNumber,
            String from,
            String to,
            String value,
            String fee,
            String timestamp,
            String status,
            BigInteger index,
            BigInteger nonce,
            String blockHash,
            String input,
            String logBloom,
            String revertReason) {
        super(hash, blockNumber, from, to, value, fee, timestamp, status);
        this.index = index;
        this.nonce = nonce;
        this.blockHash = blockHash;
        this.input = input;
        this.logBloom = logBloom;
        this.revertReason = revertReason;
    }

    public static EthTransactionDocument fromEthTransaction(EthTransaction ethTransaction) {
        return new EthTransactionDocument(
                ethTransaction.getHash(),
                ethTransaction.getBlockNumber(),
                ethTransaction.getFrom(),
                ethTransaction.getTo(),
                ethTransaction.getValue(),
                ethTransaction.getFee(),
                ethTransaction.getTimestamp(),
                ethTransaction.getStatus(),
                ethTransaction.getIndex(),
                ethTransaction.getNonce(),
                ethTransaction.getBlockHash(),
                ethTransaction.getInput(),
                ethTransaction.getLogBloom(),
                ethTransaction.getRevertReason());
    }

    @Override
    public Transaction toTransaction() {
        return new EthTransaction(
                getHash(),
                getBlockNumber(),
                getFrom(),
                getTo(),
                getValue(),
                getFee(),
                getTimestamp(),
                getStatus(),
                getIndex(),
                getNonce(),
                getBlockHash(),
                getInput(),
                getLogBloom(),
                getRevertReason());
    }
}
