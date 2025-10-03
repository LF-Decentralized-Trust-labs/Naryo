package io.naryo.application.configuration.revision.queue;

public class QueueClosedException extends IllegalStateException {
    public QueueClosedException() {
        super("Queue is closed");
    }
}
