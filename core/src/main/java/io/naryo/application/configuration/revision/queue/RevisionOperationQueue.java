package io.naryo.application.configuration.revision.queue;

import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.queue.InMemoryWeightedRevisionOperationQueue.Task;

public interface RevisionOperationQueue<T> {

    OperationId enqueue(RevisionOperation<T> op);

    Task<T> dequeue() throws InterruptedException;
}
