package io.naryo.api.node.create;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.node.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CreateNodeController.class)
class CreateNodeControllerTest {

    @Autowired MockMvc mvc;

    @Autowired ObjectMapper objectMapper;

    @MockitoBean RevisionOperationQueue<Node> operationQueue;

    /*@Test
    void addNode_ok() throws Exception {
        var opId = new OperationId(UUID.randomUUID());

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                post("/api/v1/nodes/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(
                        objectMapper.writeValueAsBytes()))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedResponse));
    }*/
}
