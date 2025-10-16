package io.naryo.api.node.create;

import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.api.node.common.NodeController;
import io.naryo.api.node.create.model.CreateNodeRequest;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.AddOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.node.Node;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class CreateNodeController extends NodeController {

    private final @Qualifier("nodeRevisionQueue") RevisionOperationQueue<Node> operationQueue;

    @PostMapping
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OperationId create(@RequestBody @NotNull CreateNodeRequest request) {
        return operationQueue.enqueue(new AddOperation<>(request.toDomain()));
    }
}
