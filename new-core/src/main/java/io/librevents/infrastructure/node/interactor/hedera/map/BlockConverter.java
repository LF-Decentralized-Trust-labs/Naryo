package io.librevents.infrastructure.node.interactor.hedera.map;

import java.math.BigInteger;
import java.util.List;

import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.interactor.block.dto.Transaction;
import io.librevents.infrastructure.node.interactor.hedera.response.BlockResponseModel;

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
