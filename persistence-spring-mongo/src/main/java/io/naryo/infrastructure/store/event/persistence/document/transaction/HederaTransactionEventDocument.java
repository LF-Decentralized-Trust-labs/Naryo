package io.naryo.infrastructure.store.event.persistence.document.transaction;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import io.naryo.application.node.interactor.block.dto.hedera.BatchKey;
import io.naryo.application.node.interactor.block.dto.hedera.MaxCustomFee;
import io.naryo.application.node.interactor.block.dto.hedera.NftTransfer;
import io.naryo.application.node.interactor.block.dto.hedera.StakingRewardTransfer;
import io.naryo.application.node.interactor.block.dto.hedera.TokenTransfer;
import io.naryo.application.node.interactor.block.dto.hedera.TransactionName;
import io.naryo.application.node.interactor.block.dto.hedera.Transfer;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.transaction.hedera.HederaTransactionEvent;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@Getter
@TypeAlias("hedera_transaction_event")
public class HederaTransactionEventDocument extends TransactionEventDocument {
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
    private String message;

    public HederaTransactionEventDocument(
            String nodeId,
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
            String validStartTimestamp,
            String consensusTimestamp,
            String message) {
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
        this.consensusTimestamp = consensusTimestamp;
        this.message = message;
    }

    public static TransactionEventDocument fromHederaTransactionEvent(
            HederaTransactionEvent event) {
        return new HederaTransactionEventDocument(
                event.getNodeId().toString(),
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
                event.getValidStartTimestamp(),
                event.getConsensusTimestamp(),
                event.getMessage());
    }

    @Override
    public HederaTransactionEvent toTransactionEvent() {
        return new HederaTransactionEvent(
                UUID.fromString(getNodeId()),
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
                consensusTimestamp,
                message);
    }
}
