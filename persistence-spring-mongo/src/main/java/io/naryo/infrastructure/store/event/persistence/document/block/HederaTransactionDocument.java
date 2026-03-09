package io.naryo.infrastructure.store.event.persistence.document.block;

import java.math.BigInteger;
import java.util.List;

import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.application.node.interactor.block.dto.hedera.BatchKey;
import io.naryo.application.node.interactor.block.dto.hedera.HederaTransaction;
import io.naryo.application.node.interactor.block.dto.hedera.MaxCustomFee;
import io.naryo.application.node.interactor.block.dto.hedera.NftTransfer;
import io.naryo.application.node.interactor.block.dto.hedera.StakingRewardTransfer;
import io.naryo.application.node.interactor.block.dto.hedera.TokenTransfer;
import io.naryo.application.node.interactor.block.dto.hedera.TransactionName;
import io.naryo.application.node.interactor.block.dto.hedera.Transfer;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@Getter
@TypeAlias("hedera_transaction")
public final class HederaTransactionDocument extends TransactionDocument {

    private final BatchKey batchKey;
    private final String bytes;
    private final Integer chargedTxFee;
    private final String entityId;
    private final List<MaxCustomFee> maxCustomFees;
    private final String memoBase64;
    private final TransactionName name;
    private final List<NftTransfer> nftTransfers;
    private final String node;
    private final Integer nonce;
    private final String parentConsensusTimestamp;
    private final Boolean scheduled;
    private final List<StakingRewardTransfer> stakingRewardTransfers;
    private final List<TokenTransfer> tokenTransfers;
    private final String transactionId;
    private final List<Transfer> transfers;
    private final String validDurationSeconds;
    private final String validStartTimestamp;
    private final String consensusTimestamp;

    public HederaTransactionDocument(
            String hash,
            BigInteger blockNumber,
            String from,
            String to,
            String value,
            String fee,
            String timestamp,
            String result,
            BatchKey batchKey,
            String bytes,
            Integer chargedTxFee,
            String entityId,
            List<MaxCustomFee> maxCustomFees,
            String memoBase64,
            TransactionName name,
            List<NftTransfer> nftTransfers,
            String node,
            Integer nonce,
            String parentConsensusTimestamp,
            Boolean scheduled,
            List<StakingRewardTransfer> stakingRewardTransfers,
            List<TokenTransfer> tokenTransfers,
            String transactionId,
            List<Transfer> transfers,
            String validDurationSeconds,
            String validStartTimestamp,
            String consensusTimestamp) {
        super(hash, blockNumber, from, to, value, fee, timestamp, result);
        this.batchKey = batchKey;
        this.bytes = bytes;
        this.chargedTxFee = chargedTxFee;
        this.entityId = entityId;
        this.maxCustomFees = maxCustomFees;
        this.memoBase64 = memoBase64;
        this.name = name;
        this.nftTransfers = nftTransfers;
        this.node = node;
        this.nonce = nonce;
        this.parentConsensusTimestamp = parentConsensusTimestamp;
        this.scheduled = scheduled;
        this.stakingRewardTransfers = stakingRewardTransfers;
        this.tokenTransfers = tokenTransfers;
        this.transactionId = transactionId;
        this.transfers = transfers;
        this.validDurationSeconds = validDurationSeconds;
        this.validStartTimestamp = validStartTimestamp;
        this.consensusTimestamp = consensusTimestamp;
    }

    public static HederaTransactionDocument fromHederaTransaction(
            HederaTransaction hederaTransaction) {
        return new HederaTransactionDocument(
                hederaTransaction.getHash(),
                hederaTransaction.getBlockNumber(),
                hederaTransaction.getFrom(),
                hederaTransaction.getTo(),
                hederaTransaction.getValue(),
                hederaTransaction.getFee(),
                hederaTransaction.getTimestamp(),
                hederaTransaction.getStatus(),
                hederaTransaction.getBatchKey(),
                hederaTransaction.getBytes(),
                hederaTransaction.getChargedTxFee(),
                hederaTransaction.getEntityId(),
                hederaTransaction.getMaxCustomFees(),
                hederaTransaction.getMemoBase64(),
                hederaTransaction.getName(),
                hederaTransaction.getNftTransfers(),
                hederaTransaction.getNode(),
                hederaTransaction.getNonce(),
                hederaTransaction.getParentConsensusTimestamp(),
                hederaTransaction.getScheduled(),
                hederaTransaction.getStakingRewardTransfers(),
                hederaTransaction.getTokenTransfers(),
                hederaTransaction.getTransactionId(),
                hederaTransaction.getTransfers(),
                hederaTransaction.getValidDurationSeconds(),
                hederaTransaction.getValidStartTimestamp(),
                hederaTransaction.getConsensusTimestamp());
    }

    @Override
    public Transaction toTransaction() {
        return new HederaTransaction(
                getHash(),
                getBlockNumber(),
                getFrom(),
                getTo(),
                getValue(),
                getFee(),
                getTimestamp(),
                getStatus(),
                getBatchKey(),
                getBytes(),
                getChargedTxFee(),
                getEntityId(),
                getMaxCustomFees(),
                getMemoBase64(),
                getName(),
                getNftTransfers(),
                getNode(),
                getNonce(),
                getParentConsensusTimestamp(),
                getScheduled(),
                getStakingRewardTransfers(),
                getTokenTransfers(),
                getTransactionId(),
                getTransfers(),
                getValidDurationSeconds(),
                getValidStartTimestamp(),
                getConsensusTimestamp());
    }
}
