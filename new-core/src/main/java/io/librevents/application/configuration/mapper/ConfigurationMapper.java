package io.librevents.application.configuration.mapper;

import io.librevents.domain.configuration.Configuration;

@FunctionalInterface
public interface ConfigurationMapper<R extends Configuration, S> {
    R map(S source);
}
