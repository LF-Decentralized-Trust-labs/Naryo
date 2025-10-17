package io.naryo.api.storeconfiguration.delete;

import java.util.UUID;

import io.naryo.api.storeconfiguration.StoreConfigurationController;
import io.naryo.api.storeconfiguration.delete.model.DeleteStoreConfigurationRequest;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RemoveOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.configuration.store.StoreConfiguration;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class DeleteStoreConfigurationController extends StoreConfigurationController {

    private final RevisionOperationQueue<StoreConfiguration> storeConfigRevisionQueue;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OperationId delete(
            @PathVariable("id") UUID id,
            @Valid @RequestBody DeleteStoreConfigurationRequest request) {
        return storeConfigRevisionQueue.enqueue(new RemoveOperation<>(id, request.prevItemHash()));
    }
}
