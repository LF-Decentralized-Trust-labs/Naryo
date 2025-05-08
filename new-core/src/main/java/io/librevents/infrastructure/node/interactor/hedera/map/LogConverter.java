package io.librevents.infrastructure.node.interactor.hedera.map;

import java.math.BigInteger;
import java.util.List;

import io.librevents.application.node.interactor.block.dto.Log;
import io.librevents.infrastructure.node.interactor.hedera.response.LogResponseModel;

public final class LogConverter {

    public static Log map(LogResponseModel model) {
        return new Log(
                BigInteger.valueOf(model.index()),
                BigInteger.valueOf(model.transactionIndex()),
                model.transactionHash(),
                model.blockHash(),
                BigInteger.valueOf(model.blockNumber()),
                model.address(),
                model.data(),
                null,
                model.topics() != null ? model.topics() : List.of());
    }
}
