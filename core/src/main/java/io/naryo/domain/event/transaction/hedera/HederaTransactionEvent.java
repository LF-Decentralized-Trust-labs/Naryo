package io.naryo.domain.event.transaction.hedera;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import io.naryo.application.node.interactor.block.dto.hedera.*;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.transaction.TransactionEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HederaTransactionEvent extends TransactionEvent {

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
    @Setter private String message;

    public HederaTransactionEvent(
            UUID nodeId,
            String hash,
            TransactionStatus status,
            NonNegativeBlockNumber blockNumber,
            BigInteger blockTimestamp,
            String sender,
            String receiver,
            String value,
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
            String consensusTimestamp,
            String message) {
        super(nodeId, hash, status, blockNumber, blockTimestamp, sender, receiver, value);
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
        this.message = message;
    }
}
