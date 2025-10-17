package io.naryo.api;

public interface RequestBuilder<T extends RequestBuilder<T, Y>, Y> {
    T self();

    Y build();
}
