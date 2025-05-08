package io.librevents.infrastructure.node.interactor.hedera.response;

import java.math.BigInteger;
import java.util.List;

public record ContractResultResponseModel(
        String address,
        BigInteger amount,
        String bloom,
        String callResult,
        String contractId,
        List<String> createdContractIds,
        String errorMessage,
        String from,
        String functionParameters,
        BigInteger gasLimit,
        BigInteger gasUsed,
        String hash,
        String result,
        String status,
        String timestamp,
        String to,
        String accessList,
        BigInteger blockGasUsed,
        String blockHash,
        String blockNumber,
        String chainId,
        String failedInitcode,
        String gasPrice,
        List<HederaLogResponseModel> logs,
        String maxFeePerGas,
        String maxPriorityFeePerGas,
        BigInteger nonce,
        String r,
        String s,
        BigInteger transactionIndex,
        List<StateChangeResponseModel> stateChanges,
        String type,
        long v) {}
