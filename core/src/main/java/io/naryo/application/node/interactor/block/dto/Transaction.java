package io.naryo.application.node.interactor.block.dto;

import java.math.BigInteger;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class Transaction {

    private final String hash;
    private final BigInteger blockNumber;
    private final String from;
    private final String to;
    private final String value;
    private final String fee;
    private final String timestamp;
    private final String status;

    protected Transaction(
            String hash,
            BigInteger blockNumber,
            String from,
            String to,
            String value,
            String fee,
            String timestamp,
            String status) {
        this.hash = hash;
        this.blockNumber = blockNumber;
        this.from = from;
        this.to = to;
        this.value = value;
        this.fee = fee;
        this.timestamp = timestamp;
        this.status = status;
    }
}
