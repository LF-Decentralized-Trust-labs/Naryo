package io.naryo.api.storeconfiguration.getAll;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.naryo.api.storeconfiguration.common.StoreConfigurationController;
import io.naryo.api.storeconfiguration.getAll.model.StoreConfigurationResponse;
import io.naryo.api.storeconfiguration.getAll.model.mapper.StoreConfigurationResponseMapper;
import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.store.revision.StoreConfigurationRevisionManager;
import io.naryo.domain.configuration.store.StoreConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class GetAllStoreConfigurationController extends StoreConfigurationController {

    private final StoreConfigurationRevisionManager storeConfigurationRevisionManager;
    private final StoreConfigurationResponseMapper storeConfigurationResponseMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StoreConfigurationResponse> getAll() {
        LiveView<StoreConfiguration> liveView = storeConfigurationRevisionManager.liveView();

        Collection<StoreConfiguration> storeConfigurations = liveView.revision().domainItems();
        Map<UUID, String> fingerprints = liveView.itemFingerprintById();

        return storeConfigurationResponseMapper.map(storeConfigurations, fingerprints);
    }
}
