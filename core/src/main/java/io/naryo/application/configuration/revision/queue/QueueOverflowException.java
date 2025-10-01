package io.naryo.application.configuration.revision.queue;

public final class QueueOverflowException extends IllegalStateException {
    private final String lane;
    private final String kind;

    public QueueOverflowException(String message, String lane, String kind) {
        super(message);
        this.lane = lane;
        this.kind = kind;
    }
}
