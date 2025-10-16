package io.naryo.api.broadcaster.delete;

import java.util.UUID;

import io.naryo.api.broadcaster.common.BroadcasterController;
import io.naryo.api.broadcaster.delete.model.DeleteBroadcasterRequest;
import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RemoveOperation;
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
public class DeleteBroadcasterController extends BroadcasterController {

    private final @Qualifier("broadcasterRevisionQueue") RevisionOperationQueue<Broadcaster> queue;

    @DeleteMapping("/{id}")
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OperationId delete(
            @PathVariable("id") UUID id,
            @Valid @RequestBody @NotNull DeleteBroadcasterRequest request) {
        return queue.enqueue(new RemoveOperation<>(id, request.prevItemHash()));
    }
}
