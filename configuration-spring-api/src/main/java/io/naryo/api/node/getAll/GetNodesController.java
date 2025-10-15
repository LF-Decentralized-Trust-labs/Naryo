package io.naryo.api.node.getAll;

import java.util.List;

import io.naryo.api.node.common.NodeController;
import io.naryo.api.node.common.response.NodeResponse;
import io.naryo.application.node.revision.NodeConfigurationRevisionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class GetNodesController extends NodeController {

    private final NodeConfigurationRevisionManager nodeConfigurationRevisionManager;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NodeResponse> getAll() {
        return nodeConfigurationRevisionManager.liveRegistry().active().domainItems().stream()
                .map(NodeResponse::fromDomain)
                .toList();
    }
}
