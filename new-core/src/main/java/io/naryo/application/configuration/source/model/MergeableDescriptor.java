package io.naryo.application.configuration.source.model;

public interface MergeableDescriptor<T> extends Descriptor {

    T merge(T other);
}
