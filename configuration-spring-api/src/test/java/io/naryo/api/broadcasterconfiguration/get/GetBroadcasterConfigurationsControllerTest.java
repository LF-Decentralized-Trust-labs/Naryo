package io.naryo.api.broadcasterconfiguration.get;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.broadcasterconfiguration.get.model.BroadcasterConfigurationResponse;
import io.naryo.application.broadcaster.configuration.normalization.BroadcasterConfigurationNormalizerRegistry;
import io.naryo.application.broadcaster.configuration.revision.BroadcasterConfigurationConfigurationRevisionManager;
import io.naryo.application.broadcaster.configuration.revision.BroadcasterConfigurationRevisionFingerprinter;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfigurationNormalizer;
import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.broadcaster.http.HttpBroadcasterConfigurationBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GetBroadcasterConfigurationsController.class)
class GetBroadcasterConfigurationsControllerTest {

    private final String PATH = "/api/v1/broadcaster-configuration";
    private final BroadcasterConfigurationRevisionFingerprinter fingerprinter =
            new BroadcasterConfigurationRevisionFingerprinter(
                    new BroadcasterConfigurationNormalizer(
                            new BroadcasterConfigurationNormalizerRegistry()));

    private @Autowired MockMvc mvc;
    private @Autowired ObjectMapper objectMapper;
    private @MockitoBean BroadcasterConfigurationConfigurationRevisionManager revisionManager;

    @Test
    public void getBroadcasterConfigurations_Ok() throws Exception {
        List<BroadcasterConfiguration> broadcasterConfigurations =
                List.of(
                        new HttpBroadcasterConfigurationBuilder().build(),
                        new HttpBroadcasterConfigurationBuilder().build());
        LiveView<BroadcasterConfiguration> liveView =
                new LiveView<>(
                        new Revision<>(
                                1,
                                fingerprinter.revisionHash(
                                        broadcasterConfigurations, BroadcasterConfiguration::getId),
                                broadcasterConfigurations),
                        Map.of(
                                broadcasterConfigurations.get(0).getId(),
                                broadcasterConfigurations.get(0),
                                broadcasterConfigurations.get(1).getId(),
                                broadcasterConfigurations.get(1)),
                        Map.of(
                                broadcasterConfigurations.get(0).getId(),
                                fingerprinter.itemHash(broadcasterConfigurations.get(0)),
                                broadcasterConfigurations.get(1).getId(),
                                fingerprinter.itemHash(broadcasterConfigurations.get(1))));

        when(revisionManager.liveView()).thenReturn(liveView);

        String expectedResponse =
                objectMapper.writeValueAsString(
                        broadcasterConfigurations.stream()
                                .map(
                                        bc ->
                                                BroadcasterConfigurationResponse.map(
                                                        bc, liveView.itemFingerprintById()))
                                .toList());
        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void getBroadcasterConfigurations_whenEmpty_returnsEmptyList() throws Exception {
        List<BroadcasterConfiguration> broadcasterConfigurations = List.of();
        LiveView<BroadcasterConfiguration> liveView =
                new LiveView<>(
                        new Revision<>(
                                1,
                                fingerprinter.revisionHash(
                                        broadcasterConfigurations, BroadcasterConfiguration::getId),
                                broadcasterConfigurations),
                        Map.of(),
                        Map.of());

        when(revisionManager.liveView()).thenReturn(liveView);

        String expectedResponse = objectMapper.writeValueAsString(List.of());
        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }
}
