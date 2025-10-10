package io.naryo.application.broadcaster.configuration.revision;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.RevisionConflictException;
import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManager;
import io.naryo.application.configuration.revision.operation.AddOperation;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.queue.InMemoryWeightedRevisionOperationQueue.Task;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.revision.worker.BaseDefaultRevisionOperationWorkerTest;
import io.naryo.application.configuration.revision.worker.DefaultRevisionOperationWorker;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.broadcaster.http.HttpBroadcasterConfiguration;
import io.naryo.domain.configuration.broadcaster.http.HttpBroadcasterConfigurationBuilder;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class BroadcasterConfigurationDefaultRevisionOperationWorkerTest
        extends BaseDefaultRevisionOperationWorkerTest<BroadcasterConfiguration> {

    private HttpBroadcasterConfiguration newHttpCfg() {
        return new HttpBroadcasterConfigurationBuilder().withId(UUID.randomUUID()).build();
    }

    @Test
    void marks_succeeded_when_liveView_reflects_publication() throws Exception {
        @SuppressWarnings("unchecked")
        RevisionOperationQueue<BroadcasterConfiguration> q =
                (RevisionOperationQueue<BroadcasterConfiguration>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<BroadcasterConfiguration> m =
                (ConfigurationRevisionManager<BroadcasterConfiguration>) this.manager;

        BroadcasterConfiguration cfg = newHttpCfg();
        RevisionOperation<BroadcasterConfiguration> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        Task<BroadcasterConfiguration> task = new Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        Revision<BroadcasterConfiguration> applied = new Revision<>(1L, "hash1", List.of(cfg));
        when(m.apply(op)).thenReturn(applied);

        LiveView<BroadcasterConfiguration> lv =
                new LiveView<>(applied, Map.of(cfg.getId(), cfg), Map.of());
        when(m.liveView()).thenReturn(lv);

        DefaultRevisionOperationWorker<BroadcasterConfiguration> worker =
                new DefaultRevisionOperationWorker<>(q, m, this.store);
        worker.start(executor);

        TimeUnit.MILLISECONDS.sleep(100);
        worker.close();

        verify(this.store).running(opId.value());
        verify(this.store).succeeded(opId.value(), 1L, "hash1");
    }

    @Test
    void marks_failed_on_revision_conflict() throws Exception {
        @SuppressWarnings("unchecked")
        RevisionOperationQueue<BroadcasterConfiguration> q =
                (RevisionOperationQueue<BroadcasterConfiguration>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<BroadcasterConfiguration> m =
                (ConfigurationRevisionManager<BroadcasterConfiguration>) this.manager;

        BroadcasterConfiguration cfg = newHttpCfg();
        RevisionOperation<BroadcasterConfiguration> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        Task<BroadcasterConfiguration> task = new Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        when(m.apply(op))
                .thenThrow(new RevisionConflictException(cfg.getId(), op.kind(), "conflict"));

        DefaultRevisionOperationWorker<BroadcasterConfiguration> worker =
                new DefaultRevisionOperationWorker<>(q, m, this.store);
        worker.start(executor);

        TimeUnit.MILLISECONDS.sleep(100);
        worker.close();

        verify(this.store).running(opId.value());
        verify(this.store)
                .failed(eq(opId.value()), eq("REVISION_CONFLICT"), contains("conflict"));
    }

    @Test
    void fails_when_liveView_not_updated_after_apply() throws Exception {
        @SuppressWarnings("unchecked")
        RevisionOperationQueue<BroadcasterConfiguration> q =
                (RevisionOperationQueue<BroadcasterConfiguration>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<BroadcasterConfiguration> m =
                (ConfigurationRevisionManager<BroadcasterConfiguration>) this.manager;

        BroadcasterConfiguration cfg = newHttpCfg();
        RevisionOperation<BroadcasterConfiguration> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        Task<BroadcasterConfiguration> task = new Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        Revision<BroadcasterConfiguration> applied = new Revision<>(2L, "hash2", List.of(cfg));
        when(m.apply(op)).thenReturn(applied);
        when(m.liveView()).thenReturn(null);

        DefaultRevisionOperationWorker<BroadcasterConfiguration> worker =
                new DefaultRevisionOperationWorker<>(q, m, this.store);
        worker.start(executor);

        TimeUnit.MILLISECONDS.sleep(100);
        worker.close();

        verify(this.store).running(opId.value());
        verify(this.store)
                .failed(
                        eq(opId.value()),
                        eq("PUBLICATION_ERROR"),
                        contains("Live view not updated"));
    }

    @Test
    void close_stops_loop_cleanly() throws Exception {
        when(queue.dequeue())
                .thenAnswer(
                        invocation -> {
                            Thread.sleep(50);
                            return null;
                        });

        DefaultRevisionOperationWorker<BroadcasterConfiguration> worker =
                new DefaultRevisionOperationWorker<>(queue, manager, store);
        worker.start(executor);

        TimeUnit.MILLISECONDS.sleep(20);
        worker.close();
    }
}
