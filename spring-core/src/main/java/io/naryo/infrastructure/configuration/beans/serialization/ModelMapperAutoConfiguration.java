package io.naryo.infrastructure.configuration.beans.serialization;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import io.naryo.infrastructure.util.reflection.TypeResolver;
import io.naryo.infrastructure.util.serialization.DurationDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper(List<EnvironmentDeserializer<?>> serializers) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Duration.class, new DurationDeserializer());
        serializers.forEach(
                serializer ->
                        module.addDeserializer(
                                TypeResolver.getGenericTypeClass(serializer, 0), serializer));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.findAndRegisterModules();
        mapper.registerModule(module);
        return mapper;
    }
}
