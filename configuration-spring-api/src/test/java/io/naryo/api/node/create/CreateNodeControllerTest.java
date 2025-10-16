package io.naryo.api.node.create;

import java.util.Random;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.node.create.eth.priv.CreatePrivateEthereumNodeRequestBuilder;
import io.naryo.api.node.create.eth.pub.CreatePublicEthereumNodeRequestBuilder;
import io.naryo.api.node.create.hedera.CreateHederaNodeRequestBuilder;
import io.naryo.api.node.create.model.CreateNodeRequest;
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
    void addNode_ok() throws Exception {
        var opId = new OperationId(UUID.randomUUID());
        var input = this.createInput();

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writerFor(CreateNodeRequest.class)
                                                .writeValueAsBytes(input)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    private CreateNodeRequest createInput() {
        var random = new Random().nextInt(3);
        CreateNodeRequestBuilder<?, ?> builder =
                switch (random) {
                    case 0 -> new CreatePublicEthereumNodeRequestBuilder();
                    case 1 -> new CreatePrivateEthereumNodeRequestBuilder();
                    case 2 -> new CreateHederaNodeRequestBuilder();
                    default -> throw new IllegalStateException("Unexpected value: " + random);
                };

        return builder.build();
    }
}
