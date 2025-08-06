package io.naryo.infrastructure.event.mongo.block.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigInteger;

@Document(collection = "latest_blocks")
@Data
@NoArgsConstructor
public final class LatestBlockDocument {

    public LatestBlockDocument(String nodeId, BigInteger blockNumber) {
        this.nodeId = nodeId;
        this.blockNumber = blockNumber;
    }

    @MongoId
    private String nodeId;

    @Getter
    private BigInteger blockNumber;

    private String hash;

    private BigInteger timestamp;
}
