package io.naryo.api.operation.error;

import io.naryo.api.error.NotFoundException;

import java.util.UUID;

public class OperationNotFoundException extends NotFoundException {

    private static final String OPERATION_NOT_FOUND_MSG = "Operation with id %s not found";

    public OperationNotFoundException(UUID id) {
        super(String.format(OPERATION_NOT_FOUND_MSG, id.toString()));
    }
}
