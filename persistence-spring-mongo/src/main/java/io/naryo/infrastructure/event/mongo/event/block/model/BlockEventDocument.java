package io.naryo.infrastructure.event.mongo.event.block.model;

import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.domain.event.block.BlockEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigInteger;
import java.util.List;

@Document
@AllArgsConstructor
@Getter
public final class BlockEventDocument {
    private final String nodeId;
    private final @MongoId BigInteger number;
    private final String hash;
    private final String logsBloom;
    private final BigInteger size;
    private final BigInteger gasUsed;
    private final BigInteger timestamp;
    private final List<Transaction> transactions;

    public static BlockEventDocument from(BlockEvent blockEvent) {
        return new BlockEventDocument(
                blockEvent.getNodeId().toString(),
                blockEvent.getNumber().value(),
                blockEvent.getHash(),
                blockEvent.getLogsBloom(),
                blockEvent.getSize(),
                blockEvent.getGasUsed(),
                blockEvent.getTimestamp(),
                blockEvent.getTransactions());
    }
}
