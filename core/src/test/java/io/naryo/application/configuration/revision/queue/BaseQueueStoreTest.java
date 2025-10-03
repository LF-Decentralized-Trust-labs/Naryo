package io.naryo.application.configuration.revision.queue;

import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.store.RevisionOperationStore;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

abstract class BaseQueueStoreTest<Q extends RevisionOperationQueue<T>, T> {
    protected RevisionOperationStore store;
    protected Q queue;

    protected abstract Q newQueue(RevisionOperationStore store);

    @BeforeEach
    void setUp() {
        store = mock(RevisionOperationStore.class);
        queue = newQueue(store);
    }

    protected OperationId enqueueAndVerifyAccepted(RevisionOperation<T> op) {
        OperationId id = queue.enqueue(op);
        verify(store).accepted(id.getValue());
        return id;
    }
}
