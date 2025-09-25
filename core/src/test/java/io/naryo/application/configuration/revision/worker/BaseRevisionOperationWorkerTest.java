package io.naryo.application.configuration.revision.worker;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManager;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.revision.store.RevisionOperationStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

public abstract class BaseRevisionOperationWorkerTest<T> {

    protected static final class DummyItem {
        final UUID id;
        final String value;

        DummyItem(UUID id, String value) {
            this.id = id;
            this.value = value;
        }

        @Override
        public String toString() {
            return "DummyItem{" + id + "," + value + "}";
        }
    }

    protected Function<String, DummyItem> dummyFactory = v -> new DummyItem(UUID.randomUUID(), v);

    protected ExecutorService executor;
    protected RevisionOperationQueue<T> queue;
    protected ConfigurationRevisionManager<T> manager;
    protected RevisionOperationStore store;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void baseSetUp() {
        executor =
                Executors.newSingleThreadExecutor(
                        r -> {
                            Thread t = new Thread(r, "worker-test");
                            t.setDaemon(true);
                            return t;
                        });
        queue = mock(RevisionOperationQueue.class);
        manager = mock(ConfigurationRevisionManager.class);
        store = mock(RevisionOperationStore.class);
    }

    @AfterEach
    void baseTearDown() {
        executor.shutdownNow();
    }
}
