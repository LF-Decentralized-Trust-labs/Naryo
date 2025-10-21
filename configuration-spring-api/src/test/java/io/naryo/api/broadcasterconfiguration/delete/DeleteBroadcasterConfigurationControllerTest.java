package io.naryo.api.broadcasterconfiguration.delete;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.queue.QueueClosedException;
import io.naryo.application.configuration.revision.queue.QueueOverflowException;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeleteBroadcasterConfigurationController.class)
class DeleteBroadcasterConfigurationControllerTest {

    private final String URI = "/api/v1/broadcaster-configuration";

    @Autowired MockMvc mvc;

    @Autowired ObjectMapper objectMapper;

    @MockitoBean(name = "broadcasterConfigurationRevisionQueue")
    @SuppressWarnings("unchecked")
    RevisionOperationQueue<BroadcasterConfiguration> operationQueue;

    @Test
    void deleteBroadcasterConfiguration_ok() throws Exception {
        var opId = new OperationId(UUID.randomUUID());
        var broadcasterConfigId = UUID.randomUUID();
        var request = new DeleteBroadcasterConfigurationRequestBuilder().build();

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);
        String requestBody = objectMapper.writeValueAsString(request);

        mvc.perform(
                        delete(URI + "/" + broadcasterConfigId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void delete_but_queueOverflow() throws Exception {
        var broadcasterConfigId = UUID.randomUUID();
        var request = new DeleteBroadcasterConfigurationRequestBuilder().build();

        doThrow(new QueueOverflowException("Low lane full", "LOW", "REMOVE"))
                .when(operationQueue)
                .enqueue(any(RevisionOperation.class));

        String requestBody = objectMapper.writeValueAsString(request);

        mvc.perform(
                        delete(URI + "/" + broadcasterConfigId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void delete_but_queueClosed() throws Exception {
        var broadcasterConfigId = UUID.randomUUID();
        var request = new DeleteBroadcasterConfigurationRequestBuilder().build();

        doThrow(new QueueClosedException())
                .when(operationQueue)
                .enqueue(any(RevisionOperation.class));

        String requestBody = objectMapper.writeValueAsString(request);

        mvc.perform(
                        delete(URI + "/" + broadcasterConfigId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isServiceUnavailable());
    }
}
