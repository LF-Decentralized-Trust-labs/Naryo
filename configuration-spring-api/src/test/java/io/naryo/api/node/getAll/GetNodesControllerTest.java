package io.naryo.api.node.getAll;

import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.node.common.response.NodeResponse;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.node.configuration.manager.NodeConfigurationManager;
import io.naryo.application.node.revision.NodeConfigurationRevisionManager;
import io.naryo.application.node.revision.NodeRevisionFingerprinter;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeBuilder;
import io.naryo.domain.node.eth.priv.PrivateEthereumNodeBuilder;
import io.naryo.domain.node.eth.pub.PublicEthereumNodeBuilder;
import io.naryo.domain.node.hedera.HederaNodeBuilder;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GetNodesController.class)
class GetNodesControllerTest {

    private final String PATH = "/api/v1/nodes";

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean NodeConfigurationManager nodeConfigurationManager;
    @MockitoBean NodeRevisionFingerprinter nodeRevisionFingerprinter;
    @MockitoBean LiveRegistry<Node> liveRegistry;

    @TestConfiguration
    static class RealBeanConfig {
        @Bean
        NodeConfigurationRevisionManager nodeConfigurationRevisionManager(
                NodeConfigurationManager nodeConfigurationManager,
                NodeRevisionFingerprinter nodeRevisionFingerprinter,
                LiveRegistry<Node> liveRegistry) {
            return new NodeConfigurationRevisionManager(
                    nodeConfigurationManager, nodeRevisionFingerprinter, liveRegistry);
        }
    }

    @Test
    void getNodes_ok() throws Exception {
        var node = this.createInput();
        var revision = new Revision<>(1, "hash", List.of(node));
        when(liveRegistry.active()).thenReturn(revision);

        String expectedResponse =
                objectMapper.writeValueAsString(List.of(NodeResponse.fromDomain(node)));

        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void getNodes_empty() throws Exception {
        when(liveRegistry.active()).thenReturn(new Revision<>(1, "hash", List.of()));
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
