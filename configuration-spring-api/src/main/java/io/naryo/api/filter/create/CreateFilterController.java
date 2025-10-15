package io.naryo.api.filter.create;

import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.api.filter.FilterController;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.AddOperation;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.revision.store.RevisionOperationStore;
import io.naryo.domain.filter.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
public class CreateFilterController extends FilterController {

    protected final @Qualifier("filterRevisionQueue") RevisionOperationQueue<Filter> operationQueue;
    protected final RevisionOperationStore operationStore;

    @PostMapping
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OperationId create(@Valid @RequestBody CreateFilterRequest filterRequest) {
        Filter filter = CreateFilterRequest.toDomain(filterRequest);
        RevisionOperation<Filter> op = new AddOperation<>(filter);
        return operationQueue.enqueue(op);
    }
}
