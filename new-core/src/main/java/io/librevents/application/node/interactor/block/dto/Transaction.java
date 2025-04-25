package io.librevents.application.node.interactor.block.dto;

import java.math.BigInteger;

public record Transaction(
    String hash,
    BigInteger nonce,
    BigInteger blockNumber,
    String blockHash,
    String from,
    String to
) {
}
