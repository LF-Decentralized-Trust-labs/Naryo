package io.librevents.infrastructure.node.interactor.hedera.exception;

public final class UnexpectedResponseException extends RuntimeException {

    public UnexpectedResponseException(String message) {
        super(message);
    }

    public UnexpectedResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
