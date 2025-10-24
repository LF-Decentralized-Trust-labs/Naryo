package io.naryo.api.broadcaster.update;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.broadcaster.common.request.AllBroadcasterTargetRequest;
import io.naryo.api.broadcaster.common.request.BroadcasterRequest;
import io.naryo.api.broadcaster.update.model.UpdateBroadcasterRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdateBroadcasterController.class)
class UpdateBroadcasterControllerTest {

    private @Autowired MockMvc mvc;
    private @Autowired ObjectMapper objectMapper;
    private @MockitoBean RevisionOperationQueue<Broadcaster> operationQueue;

    @Test
    void updateBroadcaster_ok() throws Exception {
        var broadcasterId = UUID.randomUUID();
        var opId = new OperationId(UUID.randomUUID());
        var input =
                new UpdateBroadcasterRequest(
                        new BroadcasterRequest(
                                new AllBroadcasterTargetRequest(List.of("t1")), UUID.randomUUID()),
                        "prevItemHash");

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        put("/api/v1/broadcasters/" + broadcasterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writerFor(UpdateBroadcasterRequest.class)
                                                .writeValueAsBytes(input)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }
}
