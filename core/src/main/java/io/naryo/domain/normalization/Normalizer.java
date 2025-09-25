package io.naryo.domain.normalization;

public interface Normalizer<T> {

    T normalize(T in);
}
