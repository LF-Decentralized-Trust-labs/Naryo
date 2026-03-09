package io.naryo.application.node.interactor.block.dto.hedera;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.infrastructure.node.interactor.hedera.response.*;
import lombok.Getter;

@Getter
public class HederaTransaction extends Transaction {

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

    public HederaTransaction(
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
        super(
                hash,
                blockNumber,
                from,
                to,
                value,
                fee,
                timestamp,
                Objects.equals(result, "SUCCESS") ? "0x1" : "0x0");
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
}
