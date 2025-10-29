package io.naryo.api.broadcaster.update;

import java.util.UUID;

import io.naryo.api.broadcaster.common.BroadcasterController;
import io.naryo.api.broadcaster.update.model.UpdateBroadcasterRequest;
import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.UpdateOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.broadcaster.Broadcaster;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
public class UpdateBroadcasterController extends BroadcasterController {

    private final @Qualifier("broadcasterRevisionQueue") RevisionOperationQueue<Broadcaster> queue;

    @PutMapping("/{id}")
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OperationId update(
            @PathVariable("id") UUID id, @Valid @RequestBody UpdateBroadcasterRequest request) {
        return queue.enqueue(
                new UpdateOperation<>(
                        id, request.prevItemHash(), request.broadcaster().toDomain(id)));
    }
}
