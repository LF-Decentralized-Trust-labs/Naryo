package io.librevents.application.node.interactor.block.dto;

import java.math.BigInteger;
import java.util.List;

public record Block(
        BigInteger number,
        String hash,
        String logsBloom,
        BigInteger size,
        BigInteger gasUsed,
        BigInteger timestamp,
        List<Transaction> transactions) {}
