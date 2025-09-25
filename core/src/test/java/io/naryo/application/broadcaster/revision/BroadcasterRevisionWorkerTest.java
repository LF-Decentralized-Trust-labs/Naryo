package io.naryo.application.broadcaster.revision;

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
import io.naryo.application.configuration.revision.queue.InMemoryWeightedRevisionOperationQueue;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.revision.worker.BaseRevisionOperationWorkerTest;
import io.naryo.application.configuration.revision.worker.RevisionOperationWorker;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterBuilder;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BroadcasterRevisionWorkerTest extends BaseRevisionOperationWorkerTest<Broadcaster> {

    private Broadcaster newBroadcaster() {
        return new BroadcasterBuilder().withId(UUID.randomUUID()).build();
    }

    @Test
    void marks_succeeded_when_liveView_reflects_publication() throws Exception {
        @SuppressWarnings("unchecked")
        RevisionOperationQueue<Broadcaster> q = (RevisionOperationQueue<Broadcaster>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<Broadcaster> m =
                (ConfigurationRevisionManager<Broadcaster>) this.manager;

        Broadcaster cfg = newBroadcaster();
        RevisionOperation<Broadcaster> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        InMemoryWeightedRevisionOperationQueue.Task<Broadcaster> task =
                new InMemoryWeightedRevisionOperationQueue.Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        Revision<Broadcaster> applied = new Revision<>(1L, "hash1", List.of(cfg));
        when(m.apply(op)).thenReturn(applied);

        LiveView<Broadcaster> lv = new LiveView<>(applied, Map.of(cfg.getId(), cfg), Map.of());
        when(m.liveView()).thenReturn(lv);

        RevisionOperationWorker<Broadcaster> worker =
                new RevisionOperationWorker<>(q, m, this.store);
        worker.start(executor);

        TimeUnit.MILLISECONDS.sleep(100);
        worker.stop();

        verify(this.store).running(opId.getValue());
        verify(this.store).succeeded(opId.getValue(), 1L, "hash1");
    }

    @Test
    void marks_failed_on_revision_conflict() throws Exception {
        @SuppressWarnings("unchecked")
        RevisionOperationQueue<Broadcaster> q = (RevisionOperationQueue<Broadcaster>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<Broadcaster> m =
                (ConfigurationRevisionManager<Broadcaster>) this.manager;

        Broadcaster cfg = newBroadcaster();
        RevisionOperation<Broadcaster> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        InMemoryWeightedRevisionOperationQueue.Task<Broadcaster> task =
                new InMemoryWeightedRevisionOperationQueue.Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        when(m.apply(op))
                .thenThrow(new RevisionConflictException(cfg.getId(), op.kind(), "conflict"));

        RevisionOperationWorker<Broadcaster> worker =
                new RevisionOperationWorker<>(q, m, this.store);
        worker.start(executor);

        TimeUnit.MILLISECONDS.sleep(100);
        worker.stop();

        verify(this.store).running(opId.getValue());
        verify(this.store)
                .failed(eq(opId.getValue()), eq("REVISION_CONFLICT"), contains("conflict"));
    }

    @Test
    void fails_when_liveView_not_updated_after_apply() throws Exception {
        @SuppressWarnings("unchecked")
        RevisionOperationQueue<Broadcaster> q = (RevisionOperationQueue<Broadcaster>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<Broadcaster> m =
                (ConfigurationRevisionManager<Broadcaster>) this.manager;

        Broadcaster cfg = newBroadcaster();
        RevisionOperation<Broadcaster> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        InMemoryWeightedRevisionOperationQueue.Task<Broadcaster> task =
                new InMemoryWeightedRevisionOperationQueue.Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        Revision<Broadcaster> applied = new Revision<>(2L, "hash2", List.of(cfg));
        when(m.apply(op)).thenReturn(applied);
        when(m.liveView()).thenReturn(null);

        RevisionOperationWorker<Broadcaster> worker =
                new RevisionOperationWorker<>(q, m, this.store);
        worker.start(executor);

        TimeUnit.MILLISECONDS.sleep(100);
        worker.stop();

        verify(this.store).running(opId.getValue());
        verify(this.store)
                .failed(
                        eq(opId.getValue()),
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

        RevisionOperationWorker<Broadcaster> worker =
                new RevisionOperationWorker<>(queue, manager, store);
        worker.start(executor);

        TimeUnit.MILLISECONDS.sleep(20);
        worker.close();
    }
}
