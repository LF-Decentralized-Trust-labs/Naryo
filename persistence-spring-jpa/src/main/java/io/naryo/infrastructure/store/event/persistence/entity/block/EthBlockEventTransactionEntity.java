package io.naryo.infrastructure.store.event.persistence.entity.block;

import java.math.BigInteger;

import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.application.node.interactor.block.dto.eth.EthTransaction;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("ETH")
public class EthBlockEventTransactionEntity extends BlockEventTransactionEntity {

    @Column(name = "eth_index")
    private BigInteger index;

    @Column(name = "eth_nonce")
    private BigInteger nonce;

    @Column(name = "eth_block_hash")
    private String blockHash;

    @Column(name = "eth_input")
    private String input;

    @Column(name = "eth_log_bloom")
    private String logBloom;

    @Column(name = "eth_revert_reason")
    private String revertReason;

    public EthBlockEventTransactionEntity(EthTransaction transaction) {
        super(
                transaction.getHash(),
                transaction.getBlockNumber(),
                transaction.getFrom(),
                transaction.getTo(),
                transaction.getValue(),
                transaction.getFee(),
                transaction.getTimestamp(),
                transaction.getStatus());
        this.index = transaction.getIndex();
        this.nonce = transaction.getNonce();
        this.blockHash = transaction.getBlockHash();
        this.input = transaction.getInput();
        this.logBloom = transaction.getLogBloom();
        this.revertReason = transaction.getRevertReason();
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
                index,
                nonce,
                blockHash,
                input,
                logBloom,
                revertReason);
    }
}
