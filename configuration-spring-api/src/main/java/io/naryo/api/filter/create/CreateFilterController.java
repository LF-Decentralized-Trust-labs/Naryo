package io.naryo.api.filter.create;

import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.api.filter.common.FilterController;
import io.naryo.api.filter.common.request.FilterRequest;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.AddOperation;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
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

    @PostMapping
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OperationId create(@Valid @RequestBody FilterRequest filterRequest) {
        RevisionOperation<Filter> op = new AddOperation<>(filterRequest.toDomain());
        return operationQueue.enqueue(op);
    }
}
