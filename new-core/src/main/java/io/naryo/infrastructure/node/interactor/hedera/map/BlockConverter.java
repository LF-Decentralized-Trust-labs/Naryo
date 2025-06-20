package io.naryo.infrastructure.node.interactor.hedera.map;

import java.math.BigInteger;
import java.util.List;

import io.naryo.application.node.interactor.block.dto.Block;
import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.infrastructure.node.interactor.hedera.response.BlockResponseModel;

public final class BlockConverter {

    public static Block map(BlockResponseModel response, List<Transaction> transactions) {
        String ethTimestamp =
                response.timestamp().from().substring(0, response.timestamp().from().indexOf("."));
        return new Block(
                response.number(),
                response.hash(),
                response.logsBloom(),
                response.size(),
                response.gasUsed(),
                new BigInteger(ethTimestamp),
                transactions);
    }
}
