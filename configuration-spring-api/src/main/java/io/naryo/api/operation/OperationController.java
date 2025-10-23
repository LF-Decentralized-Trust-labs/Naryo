package io.naryo.api.operation;

import java.util.Optional;
import java.util.UUID;

import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.api.operation.error.OperationNotFoundException;
import io.naryo.api.operation.response.RevisionOperationStatusResponse;
import io.naryo.application.configuration.revision.RevisionOperationStatus;
import io.naryo.application.configuration.revision.store.RevisionOperationStore;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Operations", description = "Operations related endpoints")
@RestController
@RequestMapping("api/v1/operations")
public class OperationController {

    private final RevisionOperationStore revisionOperationStore;

    public OperationController(RevisionOperationStore revisionOperationStore) {
        this.revisionOperationStore = revisionOperationStore;
    }

    @GetMapping("/{id}")
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.OK)
    public RevisionOperationStatusResponse getOperation(@PathVariable UUID id) {
        Optional<RevisionOperationStatus> operationStatus = revisionOperationStore.get(id);

        return operationStatus
                .map(RevisionOperationStatusResponse::fromRevisionOperationStatus)
                .orElseThrow(() -> new OperationNotFoundException(id));
    }
}
