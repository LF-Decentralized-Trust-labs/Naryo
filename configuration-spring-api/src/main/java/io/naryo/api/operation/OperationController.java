package io.naryo.api.operation;

import java.util.Optional;
import java.util.UUID;

import io.naryo.api.operation.response.RevisionOperationStatusResponse;
import io.naryo.application.configuration.revision.RevisionOperationStatus;
import io.naryo.application.configuration.revision.store.RevisionOperationStore;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/v1/operations")
public class OperationController {

    private final RevisionOperationStore revisionOperationStore;

    public OperationController(RevisionOperationStore revisionOperationStore) {
        this.revisionOperationStore = revisionOperationStore;
    }

    @GetMapping("/{id}")
    public RevisionOperationStatusResponse getOperation(@PathVariable UUID id) {
        Optional<RevisionOperationStatus> operationStatus = revisionOperationStore.get(id);

        return operationStatus
                .map(RevisionOperationStatusResponse::fromRevisionOperationStatus)
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Operation with id " + id + " not found"));
    }
}
