package io.naryo.api.node.create;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.node.common.eth.priv.PrivateEthereumNodeRequestBuilder;
import io.naryo.api.node.common.eth.pub.PublicEthereumNodeRequestBuilder;
import io.naryo.api.node.common.hedera.HederaNodeRequestBuilder;
import io.naryo.api.node.common.request.NodeRequest;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.node.Node;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreateNodeController.class)
class CreateNodeControllerTest {

    private final String PATH = "/api/v1/nodes";

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean RevisionOperationQueue<Node> operationQueue;

    @Test
    void createHederaNode_ok() throws Exception {
        var opId = new OperationId(UUID.randomUUID());
        var input = new HederaNodeRequestBuilder().build();

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writerFor(NodeRequest.class)
                                                .writeValueAsBytes(input)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void createPublicEthNode_ok() throws Exception {
        var opId = new OperationId(UUID.randomUUID());
        var input = new PublicEthereumNodeRequestBuilder().build();

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writerFor(NodeRequest.class)
                                                .writeValueAsBytes(input)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void createPrivateEthNode_ok() throws Exception {
        var opId = new OperationId(UUID.randomUUID());
        var input = new PrivateEthereumNodeRequestBuilder().build();

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writerFor(NodeRequest.class)
                                                .writeValueAsBytes(input)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }
}
