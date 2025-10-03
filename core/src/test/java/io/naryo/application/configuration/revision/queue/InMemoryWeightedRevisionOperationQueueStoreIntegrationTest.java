package io.naryo.application.configuration.revision.queue;

import java.util.UUID;

import io.naryo.application.configuration.revision.*;
import io.naryo.application.configuration.revision.operation.AddOperation;
import io.naryo.application.configuration.revision.operation.RemoveOperation;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.operation.UpdateOperation;
import io.naryo.application.configuration.revision.queue.InMemoryWeightedRevisionOperationQueue.Task;
import io.naryo.application.configuration.revision.store.RevisionOperationStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryWeightedRevisionOperationQueueStoreIntegrationTest
        extends BaseQueueStoreTest<InMemoryWeightedRevisionOperationQueue<String>, String> {

    @Override
    protected InMemoryWeightedRevisionOperationQueue<String> newQueue(
            RevisionOperationStore store) {
        return new InMemoryWeightedRevisionOperationQueue<>(3, 3, 2, 100, store);
    }

    @Test
    void constructor_rejects_invalid_params() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new InMemoryWeightedRevisionOperationQueue<>(0, 1, 1, 100, store));
        assertThrows(
                IllegalArgumentException.class,
                () -> new InMemoryWeightedRevisionOperationQueue<>(1, 0, 1, 100, store));
        assertThrows(
                IllegalArgumentException.class,
                () -> new InMemoryWeightedRevisionOperationQueue<>(1, 1, 0, 100, store));
        assertThrows(
                NullPointerException.class,
                () -> new InMemoryWeightedRevisionOperationQueue<>(1, 1, 1, 100, null));
    }

    @Test
    void enqueue_puts_high_ops_in_high_queue_and_calls_store() {
        RevisionOperation<String> op = new AddOperation<>("item");
        enqueueAndVerifyAccepted(op);
    }

    @Test
    void enqueue_puts_low_ops_in_low_queue() throws InterruptedException {
        RevisionOperation<String> op =
                new UpdateOperation<>(UUID.randomUUID(), "itemHash", "proposed");
        OperationId id = queue.enqueue(op);
        Task<String> dequeued = queue.dequeue();

        assertEquals(id, dequeued.id());
        assertEquals(op, dequeued.op());
    }

    @Test
    void enqueue_fails_if_queue_is_full() {
        var queue = new InMemoryWeightedRevisionOperationQueue<String>(1, 1, 1, 100, store);
        RevisionOperation<String> op = new AddOperation<>("item");

        queue.enqueue(op);
        assertThrows(IllegalStateException.class, () -> queue.enqueue(op));
    }

    @Test
    void dequeue_prioritizes_high_before_low() throws InterruptedException {
        RevisionOperation<String> highOp1 = new RemoveOperation<>(UUID.randomUUID(), "itemHash");
        RevisionOperation<String> highOp2 =
                new UpdateOperation<>(UUID.randomUUID(), "itemHash", "proposed");
        RevisionOperation<String> lowOp = new AddOperation<>("item");

        var queue = new InMemoryWeightedRevisionOperationQueue<String>(3, 3, 2, 100, store);

        var idHigh1 = queue.enqueue(highOp1);
        var idHigh2 = queue.enqueue(highOp2);
        var idLow = queue.enqueue(lowOp);

        Task<String> t1 = queue.dequeue();
        assertEquals(idHigh1, t1.id());
        Task<String> t2 = queue.dequeue();
        assertEquals(idHigh2, t2.id());
        Task<String> t3 = queue.dequeue();
        assertEquals(idLow, t3.id());
    }

    @Test
    void dequeue_returns_null_if_empty() throws InterruptedException {
        var queue = new InMemoryWeightedRevisionOperationQueue<>(1, 1, 1, 50, store);
        assertNull(queue.dequeue());
    }
}
