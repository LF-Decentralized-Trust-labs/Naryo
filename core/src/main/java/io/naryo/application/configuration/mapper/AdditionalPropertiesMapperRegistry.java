package io.naryo.application.configuration.mapper;

import java.util.Map;

import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;

public interface AdditionalPropertiesMapperRegistry {

    <S extends ActiveStoreConfiguration> void register(
            String type, Class<S> sourceClass, AdditionalPropertiesMapper<S> mapper);

    <S extends ActiveStoreConfiguration> Map<String, Object> map(String type, S source);
}
