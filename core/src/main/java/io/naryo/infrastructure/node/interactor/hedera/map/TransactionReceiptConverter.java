package io.naryo.infrastructure.node.interactor.hedera.map;

import java.math.BigInteger;

import io.naryo.application.node.interactor.block.dto.Log;
import io.naryo.application.node.interactor.block.dto.TransactionReceipt;
import io.naryo.infrastructure.node.interactor.hedera.response.ContractResultResponseModel;

public final class TransactionReceiptConverter {

    public static TransactionReceipt map(ContractResultResponseModel model) {
        return new TransactionReceipt(
                model.hash(),
                model.transactionIndex(),
                model.blockHash(),
                new BigInteger(model.blockNumber()),
                model.gasConsumed(),
                model.gasUsed(),
                model.contractId(),
                null,
                model.from(),
                model.to() != null ? model.to() : model.contractId(),
                model.logs().stream()
                        .map(
                                log ->
                                        new Log(
                                                new BigInteger(log.index()),
                                                model.transactionIndex(),
                                                model.hash(),
                                                model.blockHash(),
                                                new BigInteger(model.blockNumber()),
                                                log.address(),
                                                log.data(),
                                                null,
                                                log.topics()))
                        .toList(),
                model.bloom(),
                model.status(),
                model.errorMessage());
    }
}
