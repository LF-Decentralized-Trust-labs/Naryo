package io.naryo.infrastructure.store.event.persistence.entity.block;

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
import io.naryo.infrastructure.store.event.persistence.converter.BatchKeyConverter;
import io.naryo.infrastructure.store.event.persistence.converter.MaxCustomFeeListConverter;
import io.naryo.infrastructure.store.event.persistence.converter.NftTransferListConverter;
import io.naryo.infrastructure.store.event.persistence.converter.StakingRewardTransferListConverter;
import io.naryo.infrastructure.store.event.persistence.converter.TokenTransferListConverter;
import io.naryo.infrastructure.store.event.persistence.converter.TransactionNameConverter;
import io.naryo.infrastructure.store.event.persistence.converter.TransferListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("HEDERA")
public class HederaBlockEventTransactionEntity extends BlockEventTransactionEntity {

    @Column(name = "hedera_bytes")
    private String bytes;

    @Column(name = "hedera_charged_tx_fee")
    private Integer chargedTxFee;

    @Column(name = "hedera_entity_id")
    private String entityId;

    @Column(name = "hedera_memo_base64")
    private String memoBase64;

    @Column(name = "hedera_node")
    private String node;

    @Column(name = "hedera_nonce")
    private Integer nonce;

    @Column(name = "hedera_parent_consensus_timestamp")
    private String parentConsensusTimestamp;

    @Column(name = "hedera_scheduled")
    private Boolean scheduled;

    @Column(name = "hedera_transaction_id")
    private String transactionId;

    @Column(name = "hedera_valid_duration_seconds")
    private String validDurationSeconds;

    @Column(name = "hedera_valid_start_timestamp")
    private String validStartTimestamp;

    @Column(name = "hedera_batch_key", length = 4096)
    @Convert(converter = BatchKeyConverter.class)
    private BatchKey batchKey;

    @Column(name = "hedera_max_custom_fees", length = 4096)
    @Convert(converter = MaxCustomFeeListConverter.class)
    private List<MaxCustomFee> maxCustomFees;

    @Column(name = "hedera_name")
    @Convert(converter = TransactionNameConverter.class)
    private TransactionName name;

    @Column(name = "hedera_nft_transfers", length = 4096)
    @Convert(converter = NftTransferListConverter.class)
    private List<NftTransfer> nftTransfers;

    @Column(name = "hedera_staking_reward_transfers", length = 4096)
    @Convert(converter = StakingRewardTransferListConverter.class)
    private List<StakingRewardTransfer> stakingRewardTransfers;

    @Column(name = "hedera_token_transfers", length = 4096)
    @Convert(converter = TokenTransferListConverter.class)
    private List<TokenTransfer> tokenTransfers;

    @Column(name = "hedera_transfers", length = 4096)
    @Convert(converter = TransferListConverter.class)
    private List<Transfer> transfers;

    @Column(name = "hedera_consensus_timestamp")
    private String consensusTimestamp;

    public HederaBlockEventTransactionEntity(HederaTransaction transaction) {
        super(
                transaction.getHash(),
                transaction.getBlockNumber(),
                transaction.getFrom(),
                transaction.getTo(),
                transaction.getValue(),
                transaction.getFee(),
                transaction.getTimestamp(),
                transaction.getStatus());
        this.bytes = transaction.getBytes();
        this.chargedTxFee = transaction.getChargedTxFee();
        this.entityId = transaction.getEntityId();
        this.memoBase64 = transaction.getMemoBase64();
        this.node = transaction.getNode();
        this.nonce = transaction.getNonce();
        this.parentConsensusTimestamp = transaction.getParentConsensusTimestamp();
        this.scheduled = transaction.getScheduled();
        this.transactionId = transaction.getTransactionId();
        this.validDurationSeconds = transaction.getValidDurationSeconds();
        this.validStartTimestamp = transaction.getValidStartTimestamp();
        this.batchKey = transaction.getBatchKey();
        this.maxCustomFees = transaction.getMaxCustomFees();
        this.name = transaction.getName();
        this.nftTransfers = transaction.getNftTransfers();
        this.stakingRewardTransfers = transaction.getStakingRewardTransfers();
        this.tokenTransfers = transaction.getTokenTransfers();
        this.transfers = transaction.getTransfers();
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
                consensusTimestamp);
    }
}
