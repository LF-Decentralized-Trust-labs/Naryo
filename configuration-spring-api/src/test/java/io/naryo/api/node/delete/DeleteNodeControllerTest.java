package io.naryo.api.node.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.node.Node;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeleteNodeController.class)
class DeleteNodeControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    RevisionOperationQueue<Node> operationQueue;

    @Test
    void deleteNode_ok() throws Exception {
        var nodeId = UUID.randomUUID();
        var opId = new OperationId(UUID.randomUUID());

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse =
            objectMapper.writeValueAsString(opId);

        mvc.perform(
                delete("/api/v1/nodes/" + nodeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new DeleteNodeRequest("prevItemHash")))
            )
            .andExpect(status().isOk())
            .andExpect(content().json(expectedResponse));

    }


}
