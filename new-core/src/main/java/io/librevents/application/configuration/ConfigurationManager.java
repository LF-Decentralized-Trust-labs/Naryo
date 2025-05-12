package io.librevents.application.configuration;

import java.util.Map;

import io.librevents.domain.common.configuration.Configuration;

public interface ConfigurationManager<T> {

    void registerConfiguration(T type, String name, Map<String, Object> data);

    Configuration getConfiguration(String name);
}
