package io.naryo.infrastructure.configuration.beans.filter;

import java.util.List;

import io.naryo.application.filter.configuration.manager.DefaultFilterConfigurationManager;
import io.naryo.application.filter.configuration.manager.FilterConfigurationManager;
import io.naryo.application.filter.configuration.provider.FilterSourceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(FilterConfigurationManager.class)
    public FilterConfigurationManager filterConfigurationManager(
            List<FilterSourceProvider> providers) {
        return new DefaultFilterConfigurationManager(providers);
    }
}
