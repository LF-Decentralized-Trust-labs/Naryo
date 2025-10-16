package io.naryo.api.operation.error;

import java.util.UUID;

import io.naryo.api.error.NotFoundException;

public class OperationNotFoundException extends NotFoundException {

    private static final String OPERATION_NOT_FOUND_MSG = "Operation with id %s not found";

    public OperationNotFoundException(UUID id) {
        super(String.format(OPERATION_NOT_FOUND_MSG, id.toString()));
    }
}
