package io.librevents.infrastructure.configuration.beans.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import io.librevents.infrastructure.configuration.source.env.model.EnvironmentProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class SchedulerConfiguration implements SchedulingConfigurer {
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(scheduledExecutorService);
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(ScheduledExecutorService.class)
    public ScheduledExecutorService scheduler(EnvironmentProperties envProps) {

        scheduledExecutorService =
                Executors.newScheduledThreadPool(
                        envProps.nodes().size(),
                        new CustomizableThreadFactory("librevents-scheduler"));
        return scheduledExecutorService;
    }
}
