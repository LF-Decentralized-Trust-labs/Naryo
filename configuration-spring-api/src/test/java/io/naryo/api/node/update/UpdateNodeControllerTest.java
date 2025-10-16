package io.naryo.api.node.update;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.node.update.eth.priv.UpdatePrivateEthereumNodeRequestBuilder;
import io.naryo.api.node.update.eth.pub.UpdatePublicEthereumNodeRequestBuilder;
import io.naryo.api.node.update.hedera.UpdateHederaNodeRequestBuilder;
import io.naryo.api.node.update.model.UpdateNodeRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdateNodeController.class)
class UpdateNodeControllerTest {

    private final String PATH = "/api/v1/nodes/";

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean RevisionOperationQueue<Node> operationQueue;

    @Test
    void updateHederaNode_ok() throws Exception {
        var nodeId = UUID.randomUUID();
        var opId = new OperationId(UUID.randomUUID());
        var input = new UpdateHederaNodeRequestBuilder().build();

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        put(PATH + nodeId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writerFor(UpdateNodeRequest.class)
                                                .writeValueAsBytes(input)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void updatePublicEthNode_ok() throws Exception {
        var nodeId = UUID.randomUUID();
        var opId = new OperationId(UUID.randomUUID());
        var input = new UpdatePublicEthereumNodeRequestBuilder().build();

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        put(PATH + nodeId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writerFor(UpdateNodeRequest.class)
                                                .writeValueAsBytes(input)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void updatePrivateEthNode_ok() throws Exception {
        var nodeId = UUID.randomUUID();
        var opId = new OperationId(UUID.randomUUID());
        var input = new UpdatePrivateEthereumNodeRequestBuilder().build();

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        put(PATH + nodeId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writerFor(UpdateNodeRequest.class)
                                                .writeValueAsBytes(input)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }
}
