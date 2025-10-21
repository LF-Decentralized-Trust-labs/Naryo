package io.naryo.application.configuration.mapper;

import java.util.Map;

public interface AdditionalPropertiesMapperRegistry<T> {

    <S extends T> void register(
            String type, Class<S> sourceClass, AdditionalPropertiesMapper<S> mapper);

    <S extends T> Map<String, Object> map(String type, S source);
}
