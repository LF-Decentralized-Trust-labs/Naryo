package io.naryo.application.node.revision;

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
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeBuilder;
import io.naryo.domain.node.eth.priv.PrivateEthereumNodeBuilder;
import io.naryo.domain.node.eth.pub.PublicEthereumNodeBuilder;
import io.naryo.domain.node.hedera.HederaNodeBuilder;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NodeDefaultRevisionOperationWorkerTest
        extends BaseDefaultRevisionOperationWorkerTest<Node> {

    private Node newNode() {
        return this.newItem();
    }

    @Test
    void marks_succeeded_when_liveView_reflects_publication() throws Exception {
        @SuppressWarnings("unchecked")
        RevisionOperationQueue<Node> q = (RevisionOperationQueue<Node>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<Node> m = (ConfigurationRevisionManager<Node>) this.manager;

        Node cfg = newNode();
        RevisionOperation<Node> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        InMemoryWeightedRevisionOperationQueue.Task<Node> task =
                new InMemoryWeightedRevisionOperationQueue.Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        Revision<Node> applied = new Revision<>(1L, "hash1", List.of(cfg));
        when(m.apply(op)).thenReturn(applied);

        LiveView<Node> lv = new LiveView<>(applied, Map.of(cfg.getId(), cfg), Map.of());
        when(m.liveView()).thenReturn(lv);

        DefaultRevisionOperationWorker<Node> worker =
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
        RevisionOperationQueue<Node> q = (RevisionOperationQueue<Node>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<Node> m = (ConfigurationRevisionManager<Node>) this.manager;

        Node cfg = newNode();
        RevisionOperation<Node> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        InMemoryWeightedRevisionOperationQueue.Task<Node> task =
                new InMemoryWeightedRevisionOperationQueue.Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        when(m.apply(op))
                .thenThrow(new RevisionConflictException(cfg.getId(), op.kind(), "conflict"));

        DefaultRevisionOperationWorker<Node> worker =
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
        RevisionOperationQueue<Node> q = (RevisionOperationQueue<Node>) this.queue;
        @SuppressWarnings("unchecked")
        ConfigurationRevisionManager<Node> m = (ConfigurationRevisionManager<Node>) this.manager;

        Node cfg = newNode();
        RevisionOperation<Node> op = new AddOperation<>(cfg);
        OperationId opId = OperationId.random();
        InMemoryWeightedRevisionOperationQueue.Task<Node> task =
                new InMemoryWeightedRevisionOperationQueue.Task<>(opId, op);

        when(q.dequeue()).thenReturn(task).thenReturn(null);

        Revision<Node> applied = new Revision<>(2L, "hash2", List.of(cfg));
        when(m.apply(op)).thenReturn(applied);
        when(m.liveView()).thenReturn(null);

        DefaultRevisionOperationWorker<Node> worker =
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

        DefaultRevisionOperationWorker<Node> worker =
                new DefaultRevisionOperationWorker<>(queue, manager, store);
        worker.start(executor);

        TimeUnit.MILLISECONDS.sleep(20);
        worker.close();
    }

    protected Node newItem() {
        return newBuilder().build();
    }

    private NodeBuilder<?, ?> newBuilder() {
        var random = new Random().nextInt(3);
        return switch (random) {
            case 0 -> new PublicEthereumNodeBuilder();
            case 1 -> new PrivateEthereumNodeBuilder();
            case 2 -> new HederaNodeBuilder();
            default -> throw new IllegalStateException("Unexpected value: " + random);
        };
    }
}
