package io.naryo.api.broadcaster.create;

import io.naryo.api.broadcaster.common.BroadcasterController;
import io.naryo.api.broadcaster.common.request.BroadcasterRequest;
import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.AddOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.broadcaster.Broadcaster;
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
public class CreateBroadcasterController extends BroadcasterController {

    private final @Qualifier("broadcasterRevisionQueue") RevisionOperationQueue<Broadcaster> queue;

    @PostMapping
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OperationId create(@Valid @NotNull @RequestBody BroadcasterRequest request) {
        return queue.enqueue(new AddOperation<>(request.toDomain()));
    }
}
