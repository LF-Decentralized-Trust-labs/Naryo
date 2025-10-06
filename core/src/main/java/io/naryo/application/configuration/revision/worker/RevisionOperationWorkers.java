package io.naryo.application.configuration.revision.worker;

public interface RevisionOperationWorkers extends AutoCloseable {
    void initialize();

    @Override
    void close() throws Exception;
}
