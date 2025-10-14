package io.naryo.api.node.getAll;

import java.util.List;

import io.naryo.api.node.NodeController;
import io.naryo.api.node.common.response.NodeResponse;
import io.naryo.application.node.revision.NodeConfigurationRevisionManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class GetNodesController extends NodeController {

    private final NodeConfigurationRevisionManager nodeConfigurationRevisionManager;

    public GetNodesController(NodeConfigurationRevisionManager nodeConfigurationRevisionManager) {
        this.nodeConfigurationRevisionManager = nodeConfigurationRevisionManager;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NodeResponse> getAll() {
        return nodeConfigurationRevisionManager.liveRegistry().active().domainItems().stream()
                .map(NodeResponse::fromDomain)
                .toList();
    }
}
