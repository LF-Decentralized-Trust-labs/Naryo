package io.librevents.application.node.interactor.block.dto;

import java.math.BigInteger;
import java.util.List;

public record Log(
    BigInteger index,
    BigInteger transactionIndex,
    String transactionHash,
    String blockHash,
    BigInteger blockNumber,
    String address,
    String data,
    String type,
    List<String> topics
) {
}
