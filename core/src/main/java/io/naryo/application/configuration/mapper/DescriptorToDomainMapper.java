package io.naryo.application.configuration.mapper;

import io.naryo.application.configuration.source.model.Descriptor;

public interface DescriptorToDomainMapper<S, T extends Descriptor> {

    S map(T source);
}
