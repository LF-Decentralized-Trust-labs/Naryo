package io.naryo.api.broadcasterconfiguration.delete;

import java.util.UUID;

import io.naryo.api.broadcasterconfiguration.common.BroadcasterConfigurationController;
import io.naryo.api.broadcasterconfiguration.delete.model.DeleteBroadcasterConfigurationRequest;
import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RemoveOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
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
public class DeleteBroadcasterConfigurationController extends BroadcasterConfigurationController {

    private final @Qualifier("broadcasterConfigurationRevisionQueue")
            RevisionOperationQueue<BroadcasterConfiguration> queue;

    @DeleteMapping("/{id}")
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OperationId delete(
            @PathVariable("id") UUID id,
            @Valid @RequestBody @NotNull DeleteBroadcasterConfigurationRequest request) {
        return queue.enqueue(new RemoveOperation<>(id, request.prevItemHash()));
    }
}
