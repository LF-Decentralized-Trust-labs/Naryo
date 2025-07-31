package io.naryo.application.node.interactor.block.dto;

import java.math.BigInteger;

public record Transaction(
        String hash,
        BigInteger index,
        BigInteger nonce,
        BigInteger blockNumber,
        String blockHash,
        String status,
        String from,
        String to,
        String value,
        String input,
        String logBloom,
        String revertReason) {}
