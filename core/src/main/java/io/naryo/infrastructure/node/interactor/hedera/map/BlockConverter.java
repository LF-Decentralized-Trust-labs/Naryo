package io.naryo.infrastructure.node.interactor.hedera.map;

import java.math.BigInteger;
import java.util.List;

import io.naryo.application.node.interactor.block.dto.Block;
import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.infrastructure.node.interactor.hedera.response.BlockResponseModel;

public final class BlockConverter {

    private static final String EMPTY_LOGS_BLOOM = "0x";
    private static final String ZERO_LOGS_BLOOM = "0x" + "0".repeat(512);

    public static Block map(BlockResponseModel response, List<Transaction> transactions) {
        String ethTimestamp =
                response.timestamp().from().substring(0, response.timestamp().from().indexOf("."));
        return new Block(
                response.number(),
                response.hash(),
                sanitizeLogsBloom(response.logsBloom()),
                response.size(),
                response.gasUsed(),
                new BigInteger(ethTimestamp),
                transactions);
    }

    private static String sanitizeLogsBloom(String logsBloom) {
        if (logsBloom == null || logsBloom.equalsIgnoreCase(EMPTY_LOGS_BLOOM)) {
            return ZERO_LOGS_BLOOM;
        }
        return logsBloom;
    }
}
