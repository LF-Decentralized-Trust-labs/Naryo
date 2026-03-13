package io.naryo.infrastructure.node.interactor.hedera.response;

import java.util.List;

import io.naryo.application.node.interactor.block.dto.hedera.TransactionName;

public record TransactionResponseModel(
        BatchKeyResponseModel batchKey,
        String bytes,
        Integer chargedTxFee,
        String consensusTimestamp,
        String entityId,
        List<MaxCustomFeeResponseModel> maxCustomFees,
        String maxFee,
        String memoBase64,
        TransactionName name,
        List<NftTransferResponseModel> nftTransfers,
        String node,
        Integer nonce,
        String parentConsensusTimestamp,
        String result,
        Boolean scheduled,
        List<StakingRewardTransferResponseModel> stakingRewardTransfers,
        List<TokenTransferResponseModel> tokenTransfers,
        String transactionHash,
        String transactionId,
        List<TransferResponseModel> transfers,
        String validDurationSeconds,
        String validStartTimestamp) {}
