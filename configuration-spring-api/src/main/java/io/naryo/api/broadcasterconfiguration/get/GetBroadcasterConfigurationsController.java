package io.naryo.api.broadcasterconfiguration.get;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.naryo.api.broadcasterconfiguration.common.BroadcasterConfigurationController;
import io.naryo.api.broadcasterconfiguration.get.model.BroadcasterConfigurationResponse;
import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.application.broadcaster.configuration.revision.BroadcasterConfigurationConfigurationRevisionManager;
import io.naryo.application.configuration.revision.LiveView;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
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
        LiveView<BroadcasterConfiguration> liveView = revisionManager.liveView();
        Collection<BroadcasterConfiguration> configurations = liveView.revision().domainItems();
        Map<UUID, String> fingerprints = liveView.itemFingerprintById();
        return responseMapper.map(configurations, fingerprints);
    }
}
