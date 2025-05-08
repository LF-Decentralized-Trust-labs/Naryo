package io.librevents.infrastructure.node.interactor.hedera.exception;

public final class EmptyResponseException extends RuntimeException {

    public EmptyResponseException(String message) {
        super(message);
    }
}
