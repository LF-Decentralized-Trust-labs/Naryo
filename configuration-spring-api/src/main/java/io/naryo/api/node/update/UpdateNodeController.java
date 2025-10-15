package io.naryo.api.node.update;

import java.util.UUID;

import io.naryo.api.node.common.NodeController;
import io.naryo.api.node.update.model.UpdateNodeRequest;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.UpdateOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.node.Node;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public final class UpdateNodeController extends NodeController {

    private final @Qualifier("nodeRevisionQueue") RevisionOperationQueue<Node> operationQueue;

    @PutMapping("/{id")
    public OperationId update(
            @PathVariable("id") UUID id, @RequestBody @NotNull UpdateNodeRequest request) {
        return operationQueue.enqueue(
                new UpdateOperation<>(id, request.getPrevItemHash(), request.toDomain()));
    }
}
