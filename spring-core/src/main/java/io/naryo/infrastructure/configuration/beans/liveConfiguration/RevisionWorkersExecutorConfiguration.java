package io.naryo.infrastructure.configuration.beans.liveConfiguration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.naryo.application.configuration.revision.worker.DefaultRevisionOperationWorkers;
import io.naryo.application.configuration.revision.worker.RevisionOperationWorker;
import io.naryo.application.configuration.revision.worker.RevisionOperationWorkers;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Bean
    @ConditionalOnMissingBean(RevisionOperationWorkers.class)
    public RevisionOperationWorkers revisionOperationWorkers(
            RevisionOperationWorker<Node> nodeWorker,
            RevisionOperationWorker<Filter> filterWorker,
            RevisionOperationWorker<BroadcasterConfiguration> broadcasterConfigurationWorker,
            RevisionOperationWorker<Broadcaster> broadcasterWorker,
            RevisionOperationWorker<StoreConfiguration> storeWorker,
            @Qualifier("revisionWorkersExecutor") ExecutorService executor) {
        return new DefaultRevisionOperationWorkers(
                nodeWorker,
                filterWorker,
                broadcasterConfigurationWorker,
                broadcasterWorker,
                storeWorker,
                executor);
    }
}
