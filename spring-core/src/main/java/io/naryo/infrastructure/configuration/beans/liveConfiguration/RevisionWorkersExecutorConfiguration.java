package io.naryo.infrastructure.configuration.beans.liveConfiguration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RevisionWorkersExecutorConfiguration {

    @Bean(name = "revisionWorkersExecutor")
    @ConditionalOnMissingBean(name = "revisionWorkersExecutor")
    public ExecutorService revisionWorkersExecutor() {
        return Executors.newCachedThreadPool(
                r -> {
                    Thread t = new Thread(r, "revision-worker");
                    t.setDaemon(true);
                    return t;
                });
    }
}
