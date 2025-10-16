package io.naryo.api.filter.delete;

import java.util.UUID;

import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.api.filter.common.FilterController;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RemoveOperation;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.filter.Filter;
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

    @DeleteMapping("/{id}/{prevItemHash}")
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OperationId delete(
            @PathVariable("id") UUID id, @PathVariable("prevItemHash") String prevItemHash) {
        RevisionOperation<Filter> op = new RemoveOperation<>(id, prevItemHash);
        return operationQueue.enqueue(op);
    }
}
