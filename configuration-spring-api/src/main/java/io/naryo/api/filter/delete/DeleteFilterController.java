package io.naryo.api.filter.delete;

import java.util.UUID;

import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.api.filter.common.FilterController;
import io.naryo.api.filter.delete.model.DeleteFilterRequest;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RemoveOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.filter.Filter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
public class DeleteFilterController extends FilterController {

    private final @Qualifier("filterRevisionQueue") RevisionOperationQueue<Filter> operationQueue;

    @DeleteMapping("/{id}")
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OperationId delete(
            @PathVariable("id") UUID id, @Valid @RequestBody @NotNull DeleteFilterRequest request) {
        return operationQueue.enqueue(new RemoveOperation<>(id, request.prevItemHash()));
    }
}
