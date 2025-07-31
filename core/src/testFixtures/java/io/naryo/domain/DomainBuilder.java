package io.naryo.domain;

public interface DomainBuilder<T extends DomainBuilder<T, Y>, Y> {
    T self();

    Y build();
}
