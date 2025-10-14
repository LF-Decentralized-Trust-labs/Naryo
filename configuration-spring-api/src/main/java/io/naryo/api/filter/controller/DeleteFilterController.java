package io.naryo.api.filter.controller;

import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.application.configuration.revision.RevisionOperationStatus;
import io.naryo.application.configuration.revision.operation.RemoveOperation;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.revision.store.RevisionOperationStore;
import io.naryo.domain.filter.Filter;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
public class DeleteFilterController extends FilterController {

    private final @Qualifier("filterRevisionQueue") RevisionOperationQueue<Filter> operationQueue;
    private final RevisionOperationStore operationStore;

    @DeleteMapping("/{id}/{prevItemHash}")
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RevisionOperationStatus delete(
        @PathVariable("id") UUID id, @PathVariable("prevItemHash") String prevItemHash) {
        RevisionOperation<Filter> op = new RemoveOperation<>(id, prevItemHash);
        var opId = operationQueue.enqueue(op);
        return operationStore
            .get(opId.value())
            .orElseThrow(
                () ->
                    new ValidationException(
                        "Operation status not found"));
    }
}
