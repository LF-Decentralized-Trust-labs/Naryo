package io.naryo.application.configuration.revision.worker;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.RevisionConflictException;
import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManager;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.queue.InMemoryWeightedRevisionOperationQueue.Task;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.revision.store.RevisionOperationStore;

public final class RevisionOperationWorker<T> implements AutoCloseable {

    private final RevisionOperationQueue<T> queue;
    private final ConfigurationRevisionManager<T> manager;
    private final RevisionOperationStore store;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private volatile Future<?> task;

    public RevisionOperationWorker(
            RevisionOperationQueue<T> queue,
            ConfigurationRevisionManager<T> manager,
            RevisionOperationStore store) {
        this.queue = Objects.requireNonNull(queue, "queue");
        this.manager = Objects.requireNonNull(manager, "manager");
        this.store = Objects.requireNonNull(store, "store");
    }

    public void start(ExecutorService executor) {
        Objects.requireNonNull(executor, "executor");
        if (!running.compareAndSet(false, true)) return;
        this.task = executor.submit(this::runLoop);
    }

    private void runLoop() {
        while (running.get()) {
            try {
                Task<T> task = this.queue.dequeue();
                if (task == null) continue;

                OperationId opId = task.id();
                RevisionOperation<T> op = task.op();

                store.running(opId.getValue());

                try {
                    this.manager.apply(op);
                    var liveView = manager.liveView();
                    if (liveView != null && liveView.revision() != null) {
                        store.succeeded(
                                opId.getValue(),
                                liveView.revision().version(),
                                liveView.revision().hash());
                    } else {
                        store.failed(
                                opId.getValue(),
                                "PUBLICATION_ERROR",
                                "Live view not updated after apply");
                    }
                } catch (RevisionConflictException e) {
                    store.failed(opId.getValue(), "REVISION_CONFLICT", safeMsg(e));
                } catch (Exception e) {
                    store.failed(
                            opId.getValue(),
                            "UNEXPECTED_ERROR",
                            e.getClass().getSimpleName() + ": " + safeMsg(e));
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            } catch (Throwable t) {
                t.printStackTrace(System.err);
            }
        }
    }

    public void stop() {
        if (!running.compareAndSet(true, false)) return;
        Future<?> t = this.task;
        if (t != null) {
            t.cancel(true);
        }
    }

    @Override
    public void close() throws Exception {
        this.stop();
        if (queue instanceof AutoCloseable ac) {
            ac.close();
        }
    }

    private static String safeMsg(Throwable t) {
        String msg = t.getMessage();
        return (msg == null || msg.isBlank()) ? t.getClass().getSimpleName() : msg;
    }
}
