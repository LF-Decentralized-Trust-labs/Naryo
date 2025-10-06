package io.naryo.application.configuration.revision.queue;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.operation.RevisionOperationKind;
import io.naryo.application.configuration.revision.store.RevisionOperationStore;

public class InMemoryWeightedRevisionOperationQueue<T>
        implements RevisionOperationQueue<T>, AutoCloseable {

    public record Task<T>(OperationId id, RevisionOperation<T> op) {}

    private final BlockingQueue<Task<T>> highCapacityQueue;
    private final BlockingQueue<Task<T>> lowCapacityQueue;
    private final int highPerLowPolicy;
    private final long pollTimeoutMs;

    private final RevisionOperationStore store;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public InMemoryWeightedRevisionOperationQueue(
            int highCapacity,
            int lowCapacity,
            int highPerLowPolicy,
            long pollTimeoutMs,
            RevisionOperationStore store) {
        if (highCapacity <= 0 || lowCapacity <= 0)
            throw new IllegalArgumentException("Capacities must be > 0");
        if (highPerLowPolicy <= 0)
            throw new IllegalArgumentException("highPerLowCapacity must be > 0");
        this.highCapacityQueue = new ArrayBlockingQueue<>(highCapacity);
        this.lowCapacityQueue = new ArrayBlockingQueue<>(lowCapacity);
        this.highPerLowPolicy = highPerLowPolicy;
        this.pollTimeoutMs = pollTimeoutMs;
        this.store = Objects.requireNonNull(store, "store");
    }

    @Override
    public OperationId enqueue(RevisionOperation<T> op) {
        Objects.requireNonNull(op);
        if (closed.get()) throw new QueueClosedException();
        OperationId opId = OperationId.random();
        Task<T> task = new Task<>(opId, op);

        boolean offered;
        if (op.kind() == RevisionOperationKind.ADD) {
            offered = this.lowCapacityQueue.offer(task);
            if (!offered) {
                throw new QueueOverflowException("Low lane is full", "LOW", op.kind().name());
            }
        } else {
            offered = this.highCapacityQueue.offer(task);
            if (!offered) {
                throw new QueueOverflowException("High lane is full", "HIGH", op.kind().name());
            }
        }
        store.accepted(opId.getValue());
        return opId;
    }

    @Override
    public Task<T> dequeue() throws InterruptedException {
        for (int i = 0; i < highPerLowPolicy; i++) {
            if (closed.get()) return null;
            Task<T> highPriorityTask =
                    this.highCapacityQueue.poll(pollTimeoutMs, TimeUnit.MILLISECONDS);
            if (highPriorityTask != null) return highPriorityTask;
        }
        return this.lowCapacityQueue.poll(pollTimeoutMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public void close() {
        closed.set(true);
        this.highCapacityQueue.clear();
        this.lowCapacityQueue.clear();
    }
}
