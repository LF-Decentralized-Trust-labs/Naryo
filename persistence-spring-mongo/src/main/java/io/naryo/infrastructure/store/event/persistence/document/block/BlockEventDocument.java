package io.naryo.infrastructure.store.event.persistence.document.block;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.event.block.BlockEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "block_event")
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

    public static BlockEventDocument fromBlockEvent(BlockEvent blockEvent) {
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

    public BlockEvent toBlockEvent() {
        return new BlockEvent(
                UUID.fromString(nodeId),
                new NonNegativeBlockNumber(number),
                hash,
                logsBloom,
                size,
                gasUsed,
                timestamp,
                transactions);
    }
}
