package io.naryo.api.node.delete;

import java.util.UUID;

import io.naryo.api.node.common.NodeController;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RemoveOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.node.Node;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
public final class DeleteNodeController extends NodeController {

    private final @Qualifier("nodeRevisionQueue") RevisionOperationQueue<Node> operationQueue;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OperationId delete(
            @PathVariable("id") UUID id, @RequestBody @NotNull DeleteNodeRequest request) {
        return operationQueue.enqueue(new RemoveOperation<>(id, request.prevItemHash()));
    }
}
