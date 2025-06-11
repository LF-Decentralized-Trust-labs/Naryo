package io.naryo.infrastructure.node.interactor.hedera.map;

import java.math.BigInteger;

import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.infrastructure.node.interactor.hedera.response.ContractResultResponseModel;

public final class TransactionConverter {

    public static Transaction map(ContractResultResponseModel model) {
        return new Transaction(
                model.hash(),
                model.transactionIndex(),
                model.nonce(),
                new BigInteger(model.blockNumber()),
                model.blockHash(),
                model.status(),
                model.from(),
                model.to() != null ? model.to() : model.contractId(),
                model.amount() != null ? model.amount().toString() : null,
                model.functionParameters(),
                model.bloom(),
                model.errorMessage());
    }
}
