package io.naryo.api.node.getAll;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.node.common.response.NodeResponse;
import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.node.revision.NodeConfigurationRevisionManager;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeBuilder;
import io.naryo.domain.node.eth.priv.PrivateEthereumNodeBuilder;
import io.naryo.domain.node.eth.pub.PublicEthereumNodeBuilder;
import io.naryo.domain.node.hedera.HederaNodeBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GetNodesController.class)
class GetNodesControllerTest {

    private final String PATH = "/api/v1/nodes";

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean NodeConfigurationRevisionManager revisionManager;

    @Test
    void getNodes_ok() throws Exception {
        var node = this.createInput();
        var revision = new Revision<>(1L, "hash", List.of(node));
        var liveView =
                new LiveView<>(revision, Map.of(node.getId(), node), Map.of(node.getId(), "hash"));
        when(revisionManager.liveView()).thenReturn(liveView);

        String expectedResponse =
                objectMapper.writeValueAsString(
                        List.of(NodeResponse.map(node, liveView.itemFingerprintById())));

        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void getNodes_empty() throws Exception {
        var revision = new Revision<Node>(0L, "hash", List.of());
        var liveView = new LiveView<Node>(revision, Map.of(), Map.of());
        when(revisionManager.liveView()).thenReturn(liveView);
        mvc.perform(get(PATH)).andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    private Node createInput() {
        var random = new Random().nextInt(3);
        NodeBuilder<?, ?> builder =
                switch (random) {
                    case 0 -> new PublicEthereumNodeBuilder();
                    case 1 -> new PrivateEthereumNodeBuilder();
                    case 2 -> new HederaNodeBuilder();
                    default -> throw new IllegalStateException("Unexpected value: " + random);
                };

        return builder.build();
    }
}
