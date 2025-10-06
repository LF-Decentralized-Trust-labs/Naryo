package io.naryo.application.configuration.revision.worker;

import java.util.concurrent.ExecutorService;

public interface RevisionOperationWorker<T> extends AutoCloseable {

    void start(ExecutorService executor);

    @Override
    void close() throws Exception;
}
