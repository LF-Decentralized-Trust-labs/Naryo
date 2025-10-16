package io.naryo.api.filter.getAll;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.filter.revision.FilterConfigurationRevisionManager;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.domain.filter.transaction.TransactionFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GetFiltersController.class)
class GetFiltersControllerTest {

    private final String URI = "/api/v1/filters";

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    FilterConfigurationRevisionManager filterConfigurationRevisionManager;

    @Test
    void getAll_filters_ok() throws Exception {
        var filterId = UUID.randomUUID();
        var nodeId = UUID.randomUUID();
        Filter filter = new TransactionFilter(
            filterId,
            new FilterName("test-filter"),
            nodeId,
            IdentifierType.FROM_ADDRESS,
            "0x123",
            Set.of()
        );

        var fingerprints = Map.of(filterId, "hash123");
        var revision = new Revision<Filter>(0L, "rev-hash", List.of(filter));
        var liveView = new LiveView<Filter>(revision, Map.of(filterId, filter), fingerprints);

        when(filterConfigurationRevisionManager.liveView()).thenReturn(liveView);

        mvc.perform(
                get(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(filterId.toString()))
            .andExpect(jsonPath("$[0].name").value("test-filter"))
            .andExpect(jsonPath("$[0].nodeId").value(nodeId.toString()))
            .andExpect(jsonPath("$[0].currentItemHash").value("hash123"));
    }

    @Test
    void getAll_filters_empty() throws Exception {
        var revision = new Revision<Filter>(0L, "rev-hash", List.of());
        var liveView = new LiveView<>(revision, Map.of(), Map.of());

        when(filterConfigurationRevisionManager.liveView()).thenReturn(liveView);

        mvc.perform(
                get(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }
}
