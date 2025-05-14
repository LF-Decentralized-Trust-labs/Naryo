package io.librevents.infrastructure.configuration.beans.env;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.librevents.infrastructure.configuration.source.env.model.EnvironmentProperties;
import io.librevents.infrastructure.util.serialization.CamelCaseNormalizer;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
public class EnvironmentAutoConfiguration {

    static final String ROOT_PROP_KEY = "librevents";

    @Bean
    public EnvironmentProperties mainProperties(
            ConfigurableEnvironment env, ObjectMapper objectMapper) {
        Map<String, Object> raw =
                CamelCaseNormalizer.normalize(
                        Binder.get(env)
                                .bind(ROOT_PROP_KEY, Bindable.mapOf(String.class, Object.class))
                                .orElseThrow(
                                        () -> new IllegalStateException("No configuration found")));

        return objectMapper.convertValue(raw, EnvironmentProperties.class);
    }
}
