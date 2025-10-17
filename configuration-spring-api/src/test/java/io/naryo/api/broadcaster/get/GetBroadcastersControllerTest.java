package io.naryo.api.broadcaster.get;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.broadcaster.get.model.BroadcasterResponse;
import io.naryo.application.broadcaster.revision.BroadcasterConfigurationRevisionManager;
import io.naryo.application.broadcaster.revision.BroadcasterRevisionFingerprinter;
import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GetBroadcastersController.class)
class GetBroadcastersControllerTest {

    private final String PATH = "/api/v1/broadcasters";
    private final BroadcasterRevisionFingerprinter fingerprinter =
            new BroadcasterRevisionFingerprinter();

    private @Autowired MockMvc mvc;
    private @Autowired ObjectMapper objectMapper;
    private @MockitoBean BroadcasterConfigurationRevisionManager revisionManager;

    @Test
    public void getBroadcasters_returnsOkAndMapsResponses() throws Exception {
        List<Broadcaster> broadcasters =
                List.of(new BroadcasterBuilder().build(), new BroadcasterBuilder().build());
        LiveView<Broadcaster> liveView =
                new LiveView<>(
                        new Revision<>(
                                1,
                                fingerprinter.revisionHash(broadcasters, Broadcaster::getId),
                                broadcasters),
                        Map.of(
                                broadcasters.get(0).getId(),
                                broadcasters.get(0),
                                broadcasters.get(1).getId(),
                                broadcasters.get(1)),
                        Map.of(
                                broadcasters.get(0).getId(),
                                fingerprinter.itemHash(broadcasters.get(0)),
                                broadcasters.get(1).getId(),
                                fingerprinter.itemHash(broadcasters.get(1))));

        when(revisionManager.liveView()).thenReturn(liveView);

        String expectedResponse =
                objectMapper.writeValueAsString(
                        broadcasters.stream()
                                .map(
                                        b ->
                                                BroadcasterResponse.map(
                                                        b, liveView.itemFingerprintById()))
                                .toList());
        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void getBroadcasters_whenEmpty_returnsEmptyList() throws Exception {
        List<Broadcaster> broadcasters = List.of();
        LiveView<Broadcaster> liveView =
                new LiveView<>(
                        new Revision<>(
                                1,
                                fingerprinter.revisionHash(broadcasters, Broadcaster::getId),
                                broadcasters),
                        Map.of(),
                        Map.of());

        when(revisionManager.liveView()).thenReturn(liveView);

        String expectedResponse = objectMapper.writeValueAsString(List.of());
        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }
}
