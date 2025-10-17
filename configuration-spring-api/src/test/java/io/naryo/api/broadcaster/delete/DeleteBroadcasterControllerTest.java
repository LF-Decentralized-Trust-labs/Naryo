package io.naryo.api.broadcaster.delete;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.broadcaster.delete.model.DeleteBroadcasterRequest;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.broadcaster.Broadcaster;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeleteBroadcasterController.class)
class DeleteBroadcasterControllerTest {

    private @Autowired MockMvc mvc;
    private @Autowired ObjectMapper objectMapper;
    private @MockitoBean RevisionOperationQueue<Broadcaster> operationQueue;

    @Test
    void deleteBroadcaster_ok() throws Exception {
        var broadcasterId = UUID.randomUUID();
        var opId = new OperationId(UUID.randomUUID());

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        delete("/api/v1/broadcasters/" + broadcasterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsBytes(
                                                new DeleteBroadcasterRequest("prevItemHash"))))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }
}
