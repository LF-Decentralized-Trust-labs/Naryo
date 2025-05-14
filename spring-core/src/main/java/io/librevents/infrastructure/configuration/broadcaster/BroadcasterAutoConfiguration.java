package io.librevents.infrastructure.configuration.broadcaster;

import java.util.Set;

import io.librevents.application.broadcaster.BroadcasterProducer;
import io.librevents.application.broadcaster.configuration.BroadcasterConfigurationManager;
import io.librevents.application.broadcaster.configuration.BroadcasterConfigurationProvider;
import io.librevents.application.broadcaster.configuration.DefaultBroadcasterConfigurationManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BroadcasterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(BroadcasterConfigurationManager.class)
    public BroadcasterConfigurationManager broadcasterConfigurationManager(
            Set<BroadcasterConfigurationProvider<?>> configurationProviders,
            Set<BroadcasterProducer> producers) {
        return new DefaultBroadcasterConfigurationManager(configurationProviders, producers);
    }
}
