package io.naryo.application.node.interactor.block.dto.eth;

import java.math.BigInteger;

import io.naryo.application.node.interactor.block.dto.Transaction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EthTransaction extends Transaction {
    private final BigInteger index;
    private final BigInteger nonce;
    private final String blockHash;
    private final String input;
    private final String logBloom;
    private final String revertReason;

    public EthTransaction(
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
}
