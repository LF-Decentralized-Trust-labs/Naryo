package io.naryo.api.storeconfiguration.getAll;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.storeconfiguration.common.response.ActiveStoreConfigurationResponseBuilder;
import io.naryo.api.storeconfiguration.common.response.InactiveStoreConfigurationResponseBuilder;
import io.naryo.api.storeconfiguration.getAll.model.StoreConfigurationResponse;
import io.naryo.api.storeconfiguration.getAll.model.mapper.StoreConfigurationResponseMapper;
import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.store.revision.StoreConfigurationRevisionManager;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.active.HttpStoreConfigurationBuilder;
import io.naryo.domain.configuration.store.active.http.HttpStoreConfiguration;
import io.naryo.domain.configuration.store.inactive.InactiveStoreConfiguration;
import io.naryo.domain.configuration.store.inactive.InactiveStoreConfigurationBuilder;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GetAllStoreConfigurationController.class)
class GetAllStoreConfigurationControllerTest {

    private final String PATH = "/api/v1/store-configurations";

    @Autowired MockMvc mvc;

    @Autowired ObjectMapper objectMapper;

    @MockitoBean StoreConfigurationRevisionManager storeConfigurationRevisionManager;

    @MockitoBean StoreConfigurationResponseMapper storeConfigurationResponseMapper;

    @Test
    public void getAll_ok() throws Exception {
        HttpStoreConfiguration httpStoreConfiguration = new HttpStoreConfigurationBuilder().build();
        InactiveStoreConfiguration inactiveStoreConfiguration =
                new InactiveStoreConfigurationBuilder().build();

        LiveView<StoreConfiguration> liveView =
                new LiveView<>(
                        new Revision<>(
                                1,
                                "test-hash",
                                List.of(httpStoreConfiguration, inactiveStoreConfiguration)),
                        Map.of(),
                        Map.of(
                                httpStoreConfiguration.getNodeId(),
                                Instancio.create(String.class),
                                inactiveStoreConfiguration.getNodeId(),
                                Instancio.create(String.class)));

        when(storeConfigurationRevisionManager.liveView()).thenReturn(liveView);

        List<StoreConfigurationResponse> dummyStoreConfigurations =
                List.of(
                        new ActiveStoreConfigurationResponseBuilder().build(),
                        new InactiveStoreConfigurationResponseBuilder().build());
        when(storeConfigurationResponseMapper.map(
                        liveView.revision().domainItems(), liveView.itemFingerprintById()))
                .thenReturn(dummyStoreConfigurations);

        String expectedResponse = objectMapper.writeValueAsString(dummyStoreConfigurations);

        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }
}
