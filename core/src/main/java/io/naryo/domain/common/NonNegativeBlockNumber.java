package io.naryo.domain.common;

import java.math.BigInteger;

public record NonNegativeBlockNumber(BigInteger value) {

    public NonNegativeBlockNumber {
        if (value == null || value.signum() < 0) {
            throw new IllegalArgumentException("Block number must be >= 0");
        }
    }

    public static NonNegativeBlockNumber valueOf(BigInteger value) {
        return new NonNegativeBlockNumber(value);
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

    public int compareTo(NonNegativeBlockNumber other) {
        return this.value.compareTo(other.value);
    }
}
