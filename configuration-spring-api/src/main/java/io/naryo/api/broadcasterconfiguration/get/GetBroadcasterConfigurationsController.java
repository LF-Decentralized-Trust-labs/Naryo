package io.naryo.api.broadcasterconfiguration.get;

import java.util.List;

import io.naryo.api.broadcasterconfiguration.common.BroadcasterConfigurationController;
import io.naryo.api.broadcasterconfiguration.get.model.BroadcasterConfigurationResponse;
import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.application.broadcaster.configuration.revision.BroadcasterConfigurationConfigurationRevisionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class GetBroadcasterConfigurationsController extends BroadcasterConfigurationController {

    private final BroadcasterConfigurationConfigurationRevisionManager revisionManager;
    private final BroadcasterConfigurationResponseMapper responseMapper;

    @GetMapping
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.OK)
    public List<BroadcasterConfigurationResponse> getBroadcasterConfigurations() {
        var liveView = revisionManager.liveView();
        var fingerprints = liveView.itemFingerprintById();
        return responseMapper.map(liveView.revision().domainItems(), fingerprints);
    }
}
