package io.naryo.api.node.delete;

import java.util.UUID;

import io.naryo.api.node.NodeController;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RemoveOperation;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.node.Node;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public final class DeleteNodeController extends NodeController {

    private final @Qualifier("nodeRevisionQueue") RevisionOperationQueue<Node> operationQueue;

    public DeleteNodeController(RevisionOperationQueue<Node> operationQueue) {
        this.operationQueue = operationQueue;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OperationId delete(@PathVariable("id") UUID id, @RequestBody DeleteNodeRequest request) {
        RevisionOperation<Node> op = new RemoveOperation<>(id, request.prevItemHash());
        return operationQueue.enqueue(op);
    }
}
