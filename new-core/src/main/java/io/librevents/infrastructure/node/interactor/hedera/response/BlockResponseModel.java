package io.librevents.infrastructure.node.interactor.hedera.response;

import java.math.BigInteger;

public record BlockResponseModel(
        Integer count,
        BigInteger gasUsed,
        String hapiVersion,
        String hash,
        String logsBloom,
        String name,
        BigInteger number,
        String previousHash,
        BigInteger size,
        TimestampResponseModel timestamp) {}
