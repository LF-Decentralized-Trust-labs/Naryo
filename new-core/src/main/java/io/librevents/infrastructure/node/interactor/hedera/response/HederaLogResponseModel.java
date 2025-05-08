package io.librevents.infrastructure.node.interactor.hedera.response;

import java.util.List;

public record HederaLogResponseModel(
        String address,
        String bloom,
        String contractId,
        String data,
        String index,
        List<String> topics) {}
