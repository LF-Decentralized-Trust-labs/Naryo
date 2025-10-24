package io.naryo.api.broadcaster.get;

import java.util.List;

import io.naryo.api.broadcaster.common.BroadcasterController;
import io.naryo.api.broadcaster.get.model.BroadcasterResponse;
import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.application.broadcaster.revision.BroadcasterConfigurationRevisionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class GetBroadcastersController extends BroadcasterController {

    private final BroadcasterConfigurationRevisionManager revisionManager;

    @GetMapping
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.OK)
    public List<BroadcasterResponse> getBroadcasters() {
        var liveView = revisionManager.liveView();
        var fingerprints = liveView.itemFingerprintById();
        return liveView.revision().domainItems().stream()
                .map(
                        bc -> {
                            String currentItemHash = fingerprints.get(bc.getId());
                            return BroadcasterResponse.fromDomain(bc, currentItemHash);
                        })
                .toList();
    }
}
