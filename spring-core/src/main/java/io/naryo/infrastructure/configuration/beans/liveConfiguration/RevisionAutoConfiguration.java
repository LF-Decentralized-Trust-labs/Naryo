package io.naryo.infrastructure.configuration.beans.liveConfiguration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManager;
import io.naryo.application.configuration.revision.queue.InMemoryWeightedRevisionOperationQueue;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.revision.registry.DefaultLiveRegistries;
import io.naryo.application.configuration.revision.registry.DefaultLiveRegistry;
import io.naryo.application.configuration.revision.registry.LiveRegistries;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.configuration.revision.store.InMemoryRevisionOperationStore;
import io.naryo.application.configuration.revision.store.RevisionOperationStore;
import io.naryo.application.configuration.revision.worker.RevisionOperationWorker;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.common.http.HttpClient;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RevisionAutoConfiguration {

    private static final int REVISION_OPERATION_QUEUE_HIGH_CAPACITY = 1024;
    private static final int REVISION_OPERATION_QUEUE_LOW_CAPACITY = 1024;
    private static final int REVISION_OPERATION_QUEUE_HIGH_PER_LOW_POLICY = 5;
    private static final long REVISION_OPERATION_QUEUE_POLL_TIMEOUT_MS = 200L;

    @Bean
    @ConditionalOnMissingBean(name = "broadcasterConfigurationsLiveRegistry")
    public LiveRegistry<BroadcasterConfiguration> broadcasterConfigurationsLiveRegistry() {
        return new DefaultLiveRegistry<>();
    }

    @Bean
    @ConditionalOnMissingBean(name = "broadcastersLiveRegistry")
    public LiveRegistry<Broadcaster> broadcastersLiveRegistry() {
        return new DefaultLiveRegistry<>();
    }

    @Bean
    @ConditionalOnMissingBean(name = "filtersLiveRegistry")
    public LiveRegistry<Filter> filtersLiveRegistry() {
        return new DefaultLiveRegistry<>();
    }

    @Bean
    @ConditionalOnMissingBean(name = "nodesLiveRegistry")
    public LiveRegistry<Node> nodesLiveRegistry() {
        return new DefaultLiveRegistry<>();
    }

    @Bean
    @ConditionalOnMissingBean(name = "storeConfigurationsLiveRegistry")
    public LiveRegistry<StoreConfiguration> storeConfigurationsLiveRegistry() {
        return new DefaultLiveRegistry<>();
    }

    @Bean
    @ConditionalOnMissingBean(name = "httpClientLiveRegistry")
    public LiveRegistry<HttpClient> httpClientLiveRegistry() {
        return new DefaultLiveRegistry<>();
    }

    @Bean
    @ConditionalOnMissingBean
    public LiveRegistries liveRegistries(
            LiveRegistry<BroadcasterConfiguration> broadcasterConfigurationsLiveRegistry,
            LiveRegistry<Broadcaster> broadcastersLiveRegistry,
            LiveRegistry<Filter> filtersLiveRegistry,
            LiveRegistry<Node> nodesLiveRegistry,
            LiveRegistry<StoreConfiguration> storeConfigurationsLiveRegistry,
            LiveRegistry<HttpClient> httpClientLiveRegistry) {
        return new DefaultLiveRegistries(
                broadcasterConfigurationsLiveRegistry,
                broadcastersLiveRegistry,
                filtersLiveRegistry,
                nodesLiveRegistry,
                storeConfigurationsLiveRegistry,
                httpClientLiveRegistry);
    }

    @Bean
    @ConditionalOnMissingBean(RevisionOperationStore.class)
    public RevisionOperationStore revisionOperationStore() {
        return new InMemoryRevisionOperationStore();
    }

    @Bean
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
    @ConditionalOnMissingBean(name = "nodeRevisionQueue")
    public RevisionOperationQueue<Node> nodeRevisionQueue(RevisionOperationStore store) {
        return new InMemoryWeightedRevisionOperationQueue<>(
                REVISION_OPERATION_QUEUE_HIGH_CAPACITY,
                REVISION_OPERATION_QUEUE_LOW_CAPACITY,
                REVISION_OPERATION_QUEUE_HIGH_PER_LOW_POLICY,
                REVISION_OPERATION_QUEUE_POLL_TIMEOUT_MS,
                store);
    }

    @Bean
    @ConditionalOnMissingBean(name = "filterRevisionQueue")
    public RevisionOperationQueue<Filter> filterRevisionQueue(RevisionOperationStore store) {
        return new InMemoryWeightedRevisionOperationQueue<>(
                REVISION_OPERATION_QUEUE_HIGH_CAPACITY,
                REVISION_OPERATION_QUEUE_LOW_CAPACITY,
                REVISION_OPERATION_QUEUE_HIGH_PER_LOW_POLICY,
                REVISION_OPERATION_QUEUE_POLL_TIMEOUT_MS,
                store);
    }

    @Bean
    @ConditionalOnMissingBean(name = "broadcasterRevisionQueue")
    public RevisionOperationQueue<Broadcaster> broadcasterRevisionQueue(
            RevisionOperationStore store) {
        return new InMemoryWeightedRevisionOperationQueue<>(
                REVISION_OPERATION_QUEUE_HIGH_CAPACITY,
                REVISION_OPERATION_QUEUE_LOW_CAPACITY,
                REVISION_OPERATION_QUEUE_HIGH_PER_LOW_POLICY,
                REVISION_OPERATION_QUEUE_POLL_TIMEOUT_MS,
                store);
    }

    @Bean
    @ConditionalOnMissingBean(name = "broadcasterConfigurationRevisionQueue")
    public RevisionOperationQueue<BroadcasterConfiguration> broadcasterConfigurationRevisionQueue(
            RevisionOperationStore store) {
        return new InMemoryWeightedRevisionOperationQueue<>(
                REVISION_OPERATION_QUEUE_HIGH_CAPACITY,
                REVISION_OPERATION_QUEUE_LOW_CAPACITY,
                REVISION_OPERATION_QUEUE_HIGH_PER_LOW_POLICY,
                REVISION_OPERATION_QUEUE_POLL_TIMEOUT_MS,
                store);
    }

    @Bean
    @ConditionalOnMissingBean(name = "httpClientRevisionQueue")
    public RevisionOperationQueue<HttpClient> HttpClientRevisionQueue(
            RevisionOperationStore store) {
        return new InMemoryWeightedRevisionOperationQueue<>(
                REVISION_OPERATION_QUEUE_HIGH_CAPACITY,
                REVISION_OPERATION_QUEUE_LOW_CAPACITY,
                REVISION_OPERATION_QUEUE_HIGH_PER_LOW_POLICY,
                REVISION_OPERATION_QUEUE_POLL_TIMEOUT_MS,
                store);
    }

    @Bean
    @ConditionalOnMissingBean(name = "storeConfigRevisionQueue")
    public RevisionOperationQueue<StoreConfiguration> storeConfigRevisionQueue(
            RevisionOperationStore store) {
        return new InMemoryWeightedRevisionOperationQueue<>(
                REVISION_OPERATION_QUEUE_HIGH_CAPACITY,
                REVISION_OPERATION_QUEUE_LOW_CAPACITY,
                REVISION_OPERATION_QUEUE_HIGH_PER_LOW_POLICY,
                REVISION_OPERATION_QUEUE_POLL_TIMEOUT_MS,
                store);
    }

    @Bean
    @ConditionalOnMissingBean(name = "nodeRevisionWorker")
    public RevisionOperationWorker<Node> nodeRevisionWorker(
            RevisionOperationQueue<Node> nodeRevisionQueue,
            ConfigurationRevisionManager<Node> nodeRevisionManager,
            RevisionOperationStore store) {
        return new RevisionOperationWorker<>(nodeRevisionQueue, nodeRevisionManager, store);
    }

    @Bean
    @ConditionalOnMissingBean(name = "filterRevisionWorker")
    public RevisionOperationWorker<Filter> filterRevisionWorker(
            RevisionOperationQueue<Filter> filterRevisionQueue,
            ConfigurationRevisionManager<Filter> filterRevisionManager,
            RevisionOperationStore store) {
        return new RevisionOperationWorker<>(filterRevisionQueue, filterRevisionManager, store);
    }

    @Bean
    @ConditionalOnMissingBean(name = "broadcasterRevisionWorker")
    public RevisionOperationWorker<Broadcaster> broadcasterRevisionWorker(
            RevisionOperationQueue<Broadcaster> broadcasterRevisionQueue,
            ConfigurationRevisionManager<Broadcaster> broadcasterRevisionManager,
            RevisionOperationStore store) {
        return new RevisionOperationWorker<>(
                broadcasterRevisionQueue, broadcasterRevisionManager, store);
    }

    @Bean
    @ConditionalOnMissingBean(name = "broadcasterConfigurationRevisionWorker")
    public RevisionOperationWorker<BroadcasterConfiguration> broadcasterConfigurationRevisionWorker(
            RevisionOperationQueue<BroadcasterConfiguration> broadcasterConfigurationRevisionQueue,
            ConfigurationRevisionManager<BroadcasterConfiguration>
                    broadcasterConfigurationRevisionManager,
            RevisionOperationStore store) {
        return new RevisionOperationWorker<>(
                broadcasterConfigurationRevisionQueue,
                broadcasterConfigurationRevisionManager,
                store);
    }

    @Bean
    @ConditionalOnMissingBean(name = "storeConfigRevisionWorker")
    public RevisionOperationWorker<StoreConfiguration> storeConfigRevisionWorker(
            RevisionOperationQueue<StoreConfiguration> storeConfigurationRevisionQueue,
            ConfigurationRevisionManager<StoreConfiguration> storeConfigurationRevisionManager,
            RevisionOperationStore store) {
        return new RevisionOperationWorker<>(
                storeConfigurationRevisionQueue, storeConfigurationRevisionManager, store);
    }
}
