package io.librevents.infrastructure.configuration.beans.broadcaster.configuration;

import io.librevents.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BroadcasterAutoConfiguration {

    @Bean
    public BroadcasterConfigurationMapperRegistry broadcasterConfigurationMapperRegistry() {
        return new BroadcasterConfigurationMapperRegistry();
    }
}
