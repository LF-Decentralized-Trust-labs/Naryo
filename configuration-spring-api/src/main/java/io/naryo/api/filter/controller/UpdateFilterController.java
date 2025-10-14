package io.naryo.api.filter.controller;

import java.util.UUID;

import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.api.filter.request.UpdateFilterRequest;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.RevisionOperationStatus;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.operation.UpdateOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.revision.store.RevisionOperationStore;
import io.naryo.domain.filter.Filter;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
public class UpdateFilterController extends FilterController {

    private final @Qualifier("filterRevisionQueue") RevisionOperationQueue<Filter> operationQueue;
    private final RevisionOperationStore operationStore;

    @PutMapping("/{id}/{prevItemHash}")
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RevisionOperationStatus update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateFilterRequest filterToUpdate) {
        Filter proposed = UpdateFilterRequest.toDomain(filterToUpdate, id);
        String prevItemHash = filterToUpdate.prevItemHash();
        RevisionOperation<Filter> op = new UpdateOperation<>(id, prevItemHash, proposed);
        OperationId opId = operationQueue.enqueue(op);
        return operationStore
            .get(opId.value())
            .orElseThrow(() -> new ValidationException("Operation status not found"));
    }
}
