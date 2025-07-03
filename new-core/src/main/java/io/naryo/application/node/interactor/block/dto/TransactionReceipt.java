package io.naryo.application.node.interactor.block.dto;

import java.math.BigInteger;
import java.util.List;

public record TransactionReceipt(
        String hash,
        BigInteger index,
        String blockHash,
        BigInteger blockNumber,
        BigInteger cumulativeGasUsed,
        BigInteger gasUsed,
        String contractAddress,
        String root,
        String from,
        String to,
        List<Log> logs,
        String logsBloom,
        String status,
        String revertReason) {}
