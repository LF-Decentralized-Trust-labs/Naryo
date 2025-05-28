package io.librevents.application.configuration.mapper;

import io.librevents.domain.configuration.Configuration;

public interface ConfigurationMapperRegistry<T extends Configuration> {

    <S> void register(String type, Class<S> sourceClass, ConfigurationMapper<T, S> mapper);

    <S> T map(String type, S source);
}
