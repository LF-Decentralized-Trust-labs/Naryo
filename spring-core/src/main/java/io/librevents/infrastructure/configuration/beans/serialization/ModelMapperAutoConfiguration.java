package io.librevents.infrastructure.configuration.beans.serialization;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentSerializer;
import io.librevents.infrastructure.util.reflection.TypeResolver;
import io.librevents.infrastructure.util.serialization.DurationDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper(List<EnvironmentSerializer<?>> serializers) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Duration.class, new DurationDeserializer());
        serializers.forEach(
                serializer ->
                        module.addDeserializer(
                                TypeResolver.getGenericTypeClass(serializer, 0), serializer));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        mapper.findAndRegisterModules();
        mapper.registerModule(module);
        return mapper;
    }
}
