package io.naryo.application.node.interactor.block.dto.hedera;

import java.math.BigInteger;

public record TopicMessage(
        ChunkInfo chunkInfo,
        String consensusTimestamp,
        String message,
        String payerAccountId,
        String runningHash,
        BigInteger runningHashVersion,
        BigInteger sequenceNumber,
        String topicId) {}
