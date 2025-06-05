package io.librevents.infrastructure.configuration.beans.env;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.librevents.infrastructure.configuration.source.env.model.EnvironmentProperties;
import io.librevents.infrastructure.util.serialization.CamelCaseNormalizer;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
public class EnvironmentAutoConfiguration {

    static final String ROOT_PROP_KEY = "librevents";

    @Autowired private Validator validator;

    @Bean
    public EnvironmentProperties mainProperties(
            ConfigurableEnvironment env,
            ObjectMapper objectMapper,
            List<EnvironmentInitializer> initializers) {

        initializers.forEach(EnvironmentInitializer::initialize);

        Map<String, Object> raw =
                CamelCaseNormalizer.normalize(
                        Binder.get(env)
                                .bind(ROOT_PROP_KEY, Bindable.mapOf(String.class, Object.class))
                                .orElseThrow(
                                        () ->
                                                new IllegalStateException(
                                                        "No configurationId found")));

        try {
            EnvironmentProperties props =
                    objectMapper.convertValue(raw, EnvironmentProperties.class);

            var violations = validator.validate(props);
            if (!violations.isEmpty()) {
                String errorMessages =
                        violations.stream()
                                .sorted(Comparator.comparing(v -> v.getPropertyPath().toString()))
                                .map(
                                        v ->
                                                String.format(
                                                        "    Property: %s.%s\n    Reason: %s\n    Value: %s\n",
                                                        ROOT_PROP_KEY,
                                                        v.getPropertyPath(),
                                                        v.getMessage(),
                                                        v.getInvalidValue()))
                                .collect(Collectors.joining("\n"));

                throw new IllegalStateException(
                        String.format(
                                """
                Failed to bind properties under '%s' to %s:

                %s
                """,
                                ROOT_PROP_KEY,
                                EnvironmentProperties.class.getName(),
                                errorMessages));
            }

            return props;

        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(buildDeserializationErrorMessage(e), e);
        }
    }

    private String buildDeserializationErrorMessage(Exception e) {
        if (e instanceof JsonMappingException mappingException) {
            String propertyPath =
                    mappingException.getPath().stream()
                            .map(
                                    ref ->
                                            ref.getFieldName() != null
                                                    ? ref.getFieldName()
                                                    : "[" + ref.getIndex() + "]")
                            .collect(Collectors.joining("."));

            return String.format(
                    """
            Failed to bind properties under '%s' to %s:

                Property: %s.%s
                Reason: %s
            """,
                    ROOT_PROP_KEY,
                    EnvironmentProperties.class.getName(),
                    ROOT_PROP_KEY,
                    propertyPath,
                    mappingException.getOriginalMessage());
        }

        return "Failed to bind configuration: " + e.getMessage();
    }
}
