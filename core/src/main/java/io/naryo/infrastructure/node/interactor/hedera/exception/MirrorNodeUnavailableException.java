package io.naryo.infrastructure.node.interactor.hedera.exception;

import java.io.IOException;

public final class MirrorNodeUnavailableException extends IOException {

    public MirrorNodeUnavailableException(String message) {
        super(message);
    }
}
