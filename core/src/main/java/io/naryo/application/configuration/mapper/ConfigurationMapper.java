package io.naryo.application.configuration.mapper;

import io.naryo.domain.configuration.Configuration;

@FunctionalInterface
public interface ConfigurationMapper<R extends Configuration, S> {
    R map(S source);
}
