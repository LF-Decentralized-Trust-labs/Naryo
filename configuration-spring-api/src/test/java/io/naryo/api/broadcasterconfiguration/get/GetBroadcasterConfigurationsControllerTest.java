package io.naryo.api.broadcasterconfiguration.get;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.broadcaster.configuration.mapper.BroadcasterConfigurationAdditionalPropertiesMapperRegistry;
import io.naryo.application.broadcaster.configuration.normalization.BroadcasterConfigurationNormalizerRegistry;
import io.naryo.application.broadcaster.configuration.revision.BroadcasterConfigurationConfigurationRevisionManager;
import io.naryo.application.broadcaster.configuration.revision.BroadcasterConfigurationRevisionFingerprinter;
import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.broadcaster.BroadcasterCache;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfigurationNormalizer;
import io.naryo.domain.configuration.broadcaster.http.HttpBroadcasterConfiguration;
import io.naryo.infrastructure.configuration.beans.broadcaster.http.HttpBroadcasterInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @TestConfiguration
    static class TestConfig {
        @Bean
        public BroadcasterConfigurationAdditionalPropertiesMapperRegistry
                broadcasterConfigurationAdditionalPropertiesMapperRegistry() {
            BroadcasterConfigurationAdditionalPropertiesMapperRegistry registry =
                    new BroadcasterConfigurationAdditionalPropertiesMapperRegistry();

            registry.register(
                    "http",
                    HttpBroadcasterConfiguration.class,
                    httpBroadcasterConfiguration ->
                            Map.of(
                                    "endpoint",
                                    new HttpBroadcasterInitializer.HttpBroadcasterEndpoint(
                                            httpBroadcasterConfiguration
                                                    .getEndpoint()
                                                    .getUrl())));

            return registry;
        }

        @Bean
        public BroadcasterConfigurationResponseMapper broadcasterConfigurationResponseMapper(
                BroadcasterConfigurationAdditionalPropertiesMapperRegistry registry) {
            return new BroadcasterConfigurationResponseMapper(registry);
        }
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

    @Test
    public void getBroadcasterConfigurations_withHttpBroadcaster_ok()
            throws Exception {
        UUID id = UUID.randomUUID();
        String url = "http://example.com";
        HttpBroadcasterConfiguration httpConfig =
                new HttpBroadcasterConfiguration(
                        id,
                        new BroadcasterCache(Duration.ofMinutes(5)),
                        new ConnectionEndpoint(url));

        List<BroadcasterConfiguration> broadcasterConfigurations = List.of(httpConfig);
        LiveView<BroadcasterConfiguration> liveView =
                new LiveView<>(
                        new Revision<>(
                                1,
                                fingerprinter.revisionHash(
                                        broadcasterConfigurations, BroadcasterConfiguration::getId),
                                broadcasterConfigurations),
                        Map.of(httpConfig.getId(), httpConfig),
                        Map.of(httpConfig.getId(), fingerprinter.itemHash(httpConfig)));

        when(revisionManager.liveView()).thenReturn(liveView);

        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].type").value("http"))
                .andExpect(jsonPath("$[0].cache.expirationTime").value("PT5M"))
                .andExpect(jsonPath("$[0].additionalProperties.endpoint.url").value(url));
    }

    @Test
    public void getBroadcasterConfigurations_withMultipleHttpBroadcasters_ok()
                    throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        String url1 = "http://example1.com";
        String url2 = "http://example2.com";

        HttpBroadcasterConfiguration httpConfig1 =
                new HttpBroadcasterConfiguration(
                        id1,
                        new BroadcasterCache(Duration.ofMinutes(5)),
                        new ConnectionEndpoint(url1));

        HttpBroadcasterConfiguration httpConfig2 =
                new HttpBroadcasterConfiguration(
                        id2,
                        new BroadcasterCache(Duration.ofMinutes(10)),
                        new ConnectionEndpoint(url2));

        List<BroadcasterConfiguration> broadcasterConfigurations =
                List.of(httpConfig1, httpConfig2);
        LiveView<BroadcasterConfiguration> liveView =
                new LiveView<>(
                        new Revision<>(
                                1,
                                fingerprinter.revisionHash(
                                        broadcasterConfigurations, BroadcasterConfiguration::getId),
                                broadcasterConfigurations),
                        Map.of(
                                httpConfig1.getId(),
                                httpConfig1,
                                httpConfig2.getId(),
                                httpConfig2),
                        Map.of(
                                httpConfig1.getId(),
                                fingerprinter.itemHash(httpConfig1),
                                httpConfig2.getId(),
                                fingerprinter.itemHash(httpConfig2)));

        when(revisionManager.liveView()).thenReturn(liveView);

        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(id1.toString()))
                .andExpect(jsonPath("$[0].type").value("http"))
                .andExpect(jsonPath("$[0].cache.expirationTime").value("PT5M"))
                .andExpect(jsonPath("$[0].additionalProperties.endpoint.url").value(url1))
                .andExpect(jsonPath("$[1].id").value(id2.toString()))
                .andExpect(jsonPath("$[1].type").value("http"))
                .andExpect(jsonPath("$[1].cache.expirationTime").value("PT10M"))
                .andExpect(jsonPath("$[1].additionalProperties.endpoint.url").value(url2));
    }
}
