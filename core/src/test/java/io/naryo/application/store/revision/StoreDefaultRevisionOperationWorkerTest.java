package io.naryo.application.store.revision;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.RevisionConflictException;
import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManager;
import io.naryo.application.configuration.revision.operation.AddOperation;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.queue.InMemoryWeightedRevisionOperationQueue;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.revision.worker.BaseDefaultRevisionOperationWorkerTest;
import io.naryo.application.configuration.revision.worker.DefaultRevisionOperationWorker;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreConfigurationBuilder;
import io.naryo.domain.configuration.store.active.HttpStoreConfigurationBuilder;
import io.naryo.domain.configuration.store.inactive.InactiveStoreConfigurationBuilder;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StoreDefaultRevisionOperationWorkerTest
        extends BaseDefaultRevisionOperationWorkerTest<StoreConfiguration> {

    private StoreConfiguration newStoreConfiguration() {
        return this.newItem();
    }

    @Test
    void marks_succeeded_when_liveView_reflects_publication() throws Exception {
        @SuppressWarnings("unchecked")
        RevisionOperationQueue<StoreConfiguration> q =
                (RevisionOperationQueue<StoreConfiguration>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<StoreConfiguration> m =
                (ConfigurationRevisionManager<StoreConfiguration>) this.manager;

        StoreConfiguration cfg = newStoreConfiguration();
        RevisionOperation<StoreConfiguration> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        InMemoryWeightedRevisionOperationQueue.Task<StoreConfiguration> task =
                new InMemoryWeightedRevisionOperationQueue.Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        Revision<StoreConfiguration> applied = new Revision<>(1L, "hash1", List.of(cfg));
        when(m.apply(op)).thenReturn(applied);

        LiveView<StoreConfiguration> lv =
                new LiveView<>(applied, Map.of(cfg.getNodeId(), cfg), Map.of());
        when(m.liveView()).thenReturn(lv);

        DefaultRevisionOperationWorker<StoreConfiguration> worker =
                new DefaultRevisionOperationWorker<>(q, m, this.store);
        worker.start(executor);

        TimeUnit.MILLISECONDS.sleep(100);
        worker.close();

        verify(this.store).running(opId.getValue());
        verify(this.store).succeeded(opId.getValue(), 1L, "hash1");
    }

    @Test
    void marks_failed_on_revision_conflict() throws Exception {
        @SuppressWarnings("unchecked")
        RevisionOperationQueue<StoreConfiguration> q =
                (RevisionOperationQueue<StoreConfiguration>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<StoreConfiguration> m =
                (ConfigurationRevisionManager<StoreConfiguration>) this.manager;

        StoreConfiguration cfg = newStoreConfiguration();
        RevisionOperation<StoreConfiguration> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        InMemoryWeightedRevisionOperationQueue.Task<StoreConfiguration> task =
                new InMemoryWeightedRevisionOperationQueue.Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        when(m.apply(op))
                .thenThrow(new RevisionConflictException(cfg.getNodeId(), op.kind(), "conflict"));

        DefaultRevisionOperationWorker<StoreConfiguration> worker =
                new DefaultRevisionOperationWorker<>(q, m, this.store);
        worker.start(executor);

        TimeUnit.MILLISECONDS.sleep(100);
        worker.close();

        verify(this.store).running(opId.getValue());
        verify(this.store)
                .failed(eq(opId.getValue()), eq("REVISION_CONFLICT"), contains("conflict"));
    }

    @Test
    void fails_when_liveView_not_updated_after_apply() throws Exception {
        @SuppressWarnings("unchecked")
        RevisionOperationQueue<StoreConfiguration> q =
                (RevisionOperationQueue<StoreConfiguration>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<StoreConfiguration> m =
                (ConfigurationRevisionManager<StoreConfiguration>) this.manager;

        StoreConfiguration cfg = newStoreConfiguration();
        RevisionOperation<StoreConfiguration> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        InMemoryWeightedRevisionOperationQueue.Task<StoreConfiguration> task =
                new InMemoryWeightedRevisionOperationQueue.Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        Revision<StoreConfiguration> applied = new Revision<>(2L, "hash2", List.of(cfg));
        when(m.apply(op)).thenReturn(applied);
        when(m.liveView()).thenReturn(null);

        DefaultRevisionOperationWorker<StoreConfiguration> worker =
                new DefaultRevisionOperationWorker<>(q, m, this.store);
        worker.start(executor);

        TimeUnit.MILLISECONDS.sleep(100);
        worker.close();

        verify(this.store).running(opId.getValue());
        verify(this.store)
                .failed(
                        eq(opId.getValue()),
                        eq("PUBLICATION_ERROR"),
                        contains("Live view not updated"));
    }

    protected StoreConfiguration newItem() {
        return newBuilder().build();
    }

    private StoreConfigurationBuilder<?, ?> newBuilder() {
        var random = new Random().nextInt(2);
        return switch (random) {
            case 0 -> new InactiveStoreConfigurationBuilder();
            case 1 -> new HttpStoreConfigurationBuilder();
            default -> throw new IllegalStateException("Unexpected value: " + random);
        };
    }
}
