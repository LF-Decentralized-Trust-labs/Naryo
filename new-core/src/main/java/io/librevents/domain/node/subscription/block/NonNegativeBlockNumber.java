package io.librevents.domain.node.subscription.block;

import java.math.BigInteger;

public record NonNegativeBlockNumber(BigInteger value) {

    public NonNegativeBlockNumber {
        if (value == null || value.signum() < 0) {
            throw new IllegalArgumentException("Block number must be >= 0");
        }
    }

    public boolean isZero() {
        return value.signum() == 0;
    }

    public NonNegativeBlockNumber add(NonNegativeBlockNumber other) {
        return new NonNegativeBlockNumber(this.value.add(other.value));
    }

    public boolean isGreaterThan(NonNegativeBlockNumber other) {
        return this.value.compareTo(other.value) > 0;
    }
}
