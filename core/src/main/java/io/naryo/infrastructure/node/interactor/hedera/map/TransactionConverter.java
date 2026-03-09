package io.naryo.infrastructure.node.interactor.hedera.map;

import java.math.BigInteger;
import java.util.Comparator;

import io.naryo.application.node.interactor.block.dto.eth.EthTransaction;
import io.naryo.application.node.interactor.block.dto.hedera.*;
import io.naryo.infrastructure.node.interactor.hedera.response.ContractResultResponseModel;
import io.naryo.infrastructure.node.interactor.hedera.response.TransactionResponseModel;
import io.naryo.infrastructure.node.interactor.hedera.response.TransferResponseModel;

public final class TransactionConverter {

    public static EthTransaction map(ContractResultResponseModel model) {
        return new EthTransaction(
                model.hash(),
                new BigInteger(model.blockNumber()),
                model.from(),
                model.to() != null ? model.to() : model.contractId(),
                model.amount() != null ? model.amount().toString() : null,
                model.maxFeePerGas(),
                model.timestamp(),
                model.status(),
                model.transactionIndex(),
                model.nonce(),
                model.blockHash(),
                model.functionParameters(),
                model.bloom(),
                model.errorMessage());
    }

    public static HederaTransaction map(TransactionResponseModel model, BigInteger blockNumber) {
        TransferResponseModel fromTransfer =
                model.transfers().stream()
                        .min(Comparator.comparing(TransferResponseModel::amount))
                        .orElse(null);

        TransferResponseModel toTransfer =
                model.transfers().stream()
                        .max(Comparator.comparing(TransferResponseModel::amount))
                        .orElse(null);

        return new HederaTransaction(
                model.transactionHash(),
                blockNumber,
                fromTransfer != null ? fromTransfer.account() : null,
                toTransfer != null ? toTransfer.account() : null,
                toTransfer != null ? toTransfer.amount().toString() : null,
                model.maxFee(),
                model.consensusTimestamp(),
                model.result(),
                model.batchKey() != null
                        ? new BatchKey(model.batchKey().type(), model.batchKey().key())
                        : null,
                model.bytes(),
                model.chargedTxFee(),
                model.entityId(),
                model.maxCustomFees().stream()
                        .map(
                                fee ->
                                        new MaxCustomFee(
                                                fee.accountId(),
                                                fee.amount(),
                                                fee.denominatingTokenId()))
                        .toList(),
                model.memoBase64(),
                model.name(),
                model.nftTransfers().stream()
                        .map(
                                transfer ->
                                        new NftTransfer(
                                                transfer.isApproval(),
                                                transfer.receiverAccountId(),
                                                transfer.senderAccountId(),
                                                transfer.serialNumber(),
                                                transfer.tokenId()))
                        .toList(),
                model.node(),
                model.nonce(),
                model.parentConsensusTimestamp(),
                model.scheduled(),
                model.stakingRewardTransfers().stream()
                        .map(
                                transfer ->
                                        new StakingRewardTransfer(
                                                transfer.account(), transfer.amount()))
                        .toList(),
                model.tokenTransfers().stream()
                        .map(
                                transfer ->
                                        new TokenTransfer(
                                                transfer.tokenId(),
                                                transfer.account(),
                                                transfer.amount(),
                                                transfer.isApproval()))
                        .toList(),
                model.transactionId(),
                model.transfers().stream()
                        .map(
                                transfer ->
                                        new Transfer(
                                                transfer.account(),
                                                transfer.amount(),
                                                transfer.isApproval()))
                        .toList(),
                model.validDurationSeconds(),
                model.validStartTimestamp(),
                model.consensusTimestamp());
    }
}
