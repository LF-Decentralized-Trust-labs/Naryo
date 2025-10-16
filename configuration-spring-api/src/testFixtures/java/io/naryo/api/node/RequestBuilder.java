package io.naryo.api.node;

public interface RequestBuilder<T extends RequestBuilder<T, Y>, Y> {
    T self();

    Y build();
}
