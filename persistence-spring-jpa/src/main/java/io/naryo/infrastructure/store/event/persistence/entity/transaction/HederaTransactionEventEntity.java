package io.naryo.infrastructure.store.event.persistence.entity.transaction;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import io.naryo.application.node.interactor.block.dto.hedera.*;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.transaction.hedera.HederaTransactionEvent;
import io.naryo.infrastructure.store.event.persistence.converter.*;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("hedera_transaction_event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HederaTransactionEventEntity extends TransactionEventEntity {
    @Column(name = "batch_key")
    @Convert(converter = BatchKeyConverter.class)
    private BatchKey batchKey;

    @Column(name = "bytes")
    private String bytes;

    @Column(name = "charged_tx_fee")
    private Integer chargedTxFee;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "max_custom_fees", length = 4096)
    @Convert(converter = MaxCustomFeeListConverter.class)
    private List<MaxCustomFee> maxCustomFees;

    @Column(name = "memo_base64")
    private String memoBase64;

    @Column(name = "name")
    @Convert(converter = TransactionNameConverter.class)
    private TransactionName name;

    @Column(name = "nft_transfers", length = 4096)
    @Convert(converter = NftTransferListConverter.class)
    private List<NftTransfer> nftTransfers;

    @Column(name = "node")
    private String node;

    @Column(name = "hedera_nonce")
    private Integer nonce;

    @Column(name = "parent_consensus_timestamp")
    private String parentConsensusTimestamp;

    @Column(name = "scheduled")
    private Boolean scheduled;

    @Column(name = "staking_reward_transfers", length = 4096)
    @Convert(converter = StakingRewardTransferListConverter.class)
    private List<StakingRewardTransfer> stakingRewardTransfers;

    @Column(name = "token_transfers", length = 4096)
    @Convert(converter = TokenTransferListConverter.class)
    private List<TokenTransfer> tokenTransfers;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "transfers", length = 4096)
    @Convert(converter = TransferListConverter.class)
    private List<Transfer> transfers;

    @Column(name = "valid_duration_seconds")
    private String validDurationSeconds;

    @Column(name = "valid_start_timestamp")
    private String validStartTimestamp;

    @Column(name = "consensus_timestamp")
    private String consensusTimestamp;

    @Column(name = "message")
    private String message;

    public HederaTransactionEventEntity(
            UUID nodeId,
            String hash,
            BigInteger blockNumber,
            BigInteger blockTimestamp,
            String sender,
            String receiver,
            String value,
            String status,
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
            String validStartTimestamp) {
        super(nodeId, hash, blockNumber, blockTimestamp, sender, receiver, value, status);
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
    }

    public static TransactionEventEntity fromHederaTransactionEvent(HederaTransactionEvent event) {
        return new HederaTransactionEventEntity(
                event.getNodeId(),
                event.getHash(),
                event.getBlockNumber().value(),
                event.getBlockTimestamp(),
                event.getSender(),
                event.getReceiver(),
                event.getValue(),
                event.getStatus().name(),
                event.getBatchKey(),
                event.getBytes(),
                event.getChargedTxFee(),
                event.getEntityId(),
                event.getMaxCustomFees(),
                event.getMemoBase64(),
                event.getName(),
                event.getNftTransfers(),
                event.getNode(),
                event.getNonce(),
                event.getParentConsensusTimestamp(),
                event.getScheduled(),
                event.getStakingRewardTransfers(),
                event.getTokenTransfers(),
                event.getTransactionId(),
                event.getTransfers(),
                event.getValidDurationSeconds(),
                event.getValidStartTimestamp());
    }

    @Override
    public HederaTransactionEvent toTransactionEvent() {
        return new HederaTransactionEvent(
                getNodeId(),
                getHash(),
                TransactionStatus.valueOf(getStatus()),
                new NonNegativeBlockNumber(getBlockNumber()),
                getBlockTimestamp(),
                getSender(),
                getReceiver(),
                getValue(),
                batchKey,
                bytes,
                chargedTxFee,
                entityId,
                maxCustomFees,
                memoBase64,
                name,
                nftTransfers,
                node,
                nonce,
                parentConsensusTimestamp,
                scheduled,
                stakingRewardTransfers,
                tokenTransfers,
                transactionId,
                transfers,
                validDurationSeconds,
                validStartTimestamp,
                message,
                consensusTimestamp);
    }
}
