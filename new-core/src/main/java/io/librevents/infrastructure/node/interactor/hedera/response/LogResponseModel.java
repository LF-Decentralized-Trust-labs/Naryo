package io.librevents.infrastructure.node.interactor.hedera.response;

import java.util.List;

public record LogResponseModel(
        String address,
        String bloom,
        String contractId,
        String data,
        Integer index,
        List<String> topics,
        String blockHash,
        Integer blockNumber,
        String rootContractId,
        String timestamp,
        String transactionHash,
        Integer transactionIndex) {}
