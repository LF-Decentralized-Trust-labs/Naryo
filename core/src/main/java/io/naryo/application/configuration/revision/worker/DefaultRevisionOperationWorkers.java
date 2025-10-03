package io.naryo.application.configuration.revision.worker;

import java.util.concurrent.ExecutorService;

import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;

public class DefaultRevisionOperationWorkers implements RevisionOperationWorkers {

    private final RevisionOperationWorker<Node> nodeWorker;
    private final RevisionOperationWorker<Filter> filterWorker;
    private final RevisionOperationWorker<BroadcasterConfiguration> broadcasterConfigurationWorker;
    private final RevisionOperationWorker<Broadcaster> broadcasterWorker;
    private final RevisionOperationWorker<StoreConfiguration> storeWorker;

    private final ExecutorService executor;

    public DefaultRevisionOperationWorkers(
            RevisionOperationWorker<Node> nodeWorker,
            RevisionOperationWorker<Filter> filterWorker,
            RevisionOperationWorker<BroadcasterConfiguration> broadcasterConfigurationWorker,
            RevisionOperationWorker<Broadcaster> broadcasterWorker,
            RevisionOperationWorker<StoreConfiguration> storeWorker,
            ExecutorService executor) {
        this.nodeWorker = nodeWorker;
        this.filterWorker = filterWorker;
        this.broadcasterConfigurationWorker = broadcasterConfigurationWorker;
        this.broadcasterWorker = broadcasterWorker;
        this.storeWorker = storeWorker;
        this.executor = executor;
    }

    @Override
    public void initialize() {
        this.nodeWorker.start(this.executor);
        this.filterWorker.start(this.executor);
        this.broadcasterConfigurationWorker.start(this.executor);
        this.broadcasterWorker.start(this.executor);
        this.storeWorker.start(this.executor);
    }

    @Override
    public void close() throws Exception {
        this.nodeWorker.close();
        this.filterWorker.close();
        this.broadcasterConfigurationWorker.close();
        this.broadcasterWorker.close();
        this.storeWorker.close();
    }
}
