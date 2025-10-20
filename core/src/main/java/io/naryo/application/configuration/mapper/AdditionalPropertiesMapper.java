package io.naryo.application.configuration.mapper;

import java.util.Map;

@FunctionalInterface
public interface AdditionalPropertiesMapper<S> {
    Map<String, Object> map(S source);
}
