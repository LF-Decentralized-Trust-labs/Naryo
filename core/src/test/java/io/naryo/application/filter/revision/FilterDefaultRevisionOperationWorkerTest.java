package io.naryo.application.filter.revision;

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
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterBuilder;
import io.naryo.domain.filter.event.ContractEventFilterBuilder;
import io.naryo.domain.filter.event.GlobalEventFilterBuilder;
import io.naryo.domain.filter.transaction.TransactionFilterBuilder;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FilterDefaultRevisionOperationWorkerTest
        extends BaseDefaultRevisionOperationWorkerTest<Filter> {

    private Filter newFilter() {
        return this.newItem();
    }

    @Test
    void marks_succeeded_when_liveView_reflects_publication() throws Exception {
        @SuppressWarnings("unchecked")
        RevisionOperationQueue<Filter> q = (RevisionOperationQueue<Filter>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<Filter> m =
                (ConfigurationRevisionManager<Filter>) this.manager;

        Filter cfg = newFilter();
        RevisionOperation<Filter> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        InMemoryWeightedRevisionOperationQueue.Task<Filter> task =
                new InMemoryWeightedRevisionOperationQueue.Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        Revision<Filter> applied = new Revision<>(1L, "hash1", List.of(cfg));
        when(m.apply(op)).thenReturn(applied);

        LiveView<Filter> lv = new LiveView<>(applied, Map.of(cfg.getId(), cfg), Map.of());
        when(m.liveView()).thenReturn(lv);

        DefaultRevisionOperationWorker<Filter> worker =
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
        RevisionOperationQueue<Filter> q = (RevisionOperationQueue<Filter>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<Filter> m =
                (ConfigurationRevisionManager<Filter>) this.manager;

        Filter cfg = newFilter();
        RevisionOperation<Filter> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        InMemoryWeightedRevisionOperationQueue.Task<Filter> task =
                new InMemoryWeightedRevisionOperationQueue.Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        when(m.apply(op))
                .thenThrow(new RevisionConflictException(cfg.getId(), op.kind(), "conflict"));

        DefaultRevisionOperationWorker<Filter> worker =
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
        RevisionOperationQueue<Filter> q = (RevisionOperationQueue<Filter>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<Filter> m =
                (ConfigurationRevisionManager<Filter>) this.manager;

        Filter cfg = newFilter();
        RevisionOperation<Filter> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        InMemoryWeightedRevisionOperationQueue.Task<Filter> task =
                new InMemoryWeightedRevisionOperationQueue.Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        Revision<Filter> applied = new Revision<>(2L, "hash2", List.of(cfg));
        when(m.apply(op)).thenReturn(applied);
        when(m.liveView()).thenReturn(null);

        DefaultRevisionOperationWorker<Filter> worker =
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

    @Test
    void close_stops_loop_cleanly() throws Exception {
        when(queue.dequeue())
                .thenAnswer(
                        invocation -> {
                            Thread.sleep(50);
                            return null;
                        });

        DefaultRevisionOperationWorker<Filter> worker =
                new DefaultRevisionOperationWorker<>(queue, manager, store);
        worker.start(executor);

        TimeUnit.MILLISECONDS.sleep(20);
        worker.close();
    }

    protected Filter newItem() {
        return newBuilder().build();
    }

    private FilterBuilder<?, ?> newBuilder() {
        var random = new Random().nextInt(3);
        return switch (random) {
            case 0 -> new ContractEventFilterBuilder();
            case 1 -> new GlobalEventFilterBuilder();
            case 2 -> new TransactionFilterBuilder();
            default -> throw new IllegalStateException("Unexpected value: " + random);
        };
    }
}
