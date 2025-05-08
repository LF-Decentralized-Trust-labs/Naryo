package io.librevents.infrastructure.node.interactor.hedera.map;

import java.math.BigInteger;

import io.librevents.application.node.interactor.block.dto.Transaction;
import io.librevents.infrastructure.node.interactor.hedera.response.ContractResultResponseModel;

public final class TransactionConverter {

    public static Transaction map(ContractResultResponseModel model) {
        return new Transaction(
                model.hash(),
                model.nonce(),
                new BigInteger(model.blockNumber()),
                model.blockHash(),
                model.from(),
                model.to() != null ? model.to() : model.contractId());
    }
}
