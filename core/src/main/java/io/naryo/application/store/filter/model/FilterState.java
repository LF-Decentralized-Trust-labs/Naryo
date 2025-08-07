package io.naryo.application.store.filter.model;

import java.math.BigInteger;
import java.util.UUID;

public record FilterState(
        UUID filterId, BigInteger latestBlockNumber, BigInteger endBlock, boolean sync) {

    public FilterState {
        if (filterId == null) {
            throw new IllegalArgumentException("Filter id cannot be null");
        }
        if (latestBlockNumber == null) {
            throw new IllegalArgumentException("Last block number cannot be null");
        }
        if (endBlock == null) {
            throw new IllegalArgumentException("End block number cannot be null");
        }
    }
}
