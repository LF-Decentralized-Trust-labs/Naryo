package io.naryo.api.filter.delete;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.filter.delete.model.DeleteFilterRequest;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.queue.QueueClosedException;
import io.naryo.application.configuration.revision.queue.QueueOverflowException;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.filter.Filter;
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

@WebMvcTest(DeleteFilterController.class)
class DeleteFilterControllerTest {

    private final String URI = "/api/v1/filters";

    @Autowired MockMvc mvc;

    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    @SuppressWarnings("unchecked")
    RevisionOperationQueue<Filter> operationQueue;

    @Test
    void delete_filter_ok() throws Exception {
        var opId = new OperationId(UUID.randomUUID());
        var filterId = UUID.randomUUID();
        var prevItemHash = "hash123";
        var request = new DeleteFilterRequest(prevItemHash);

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);
        String requestBody = objectMapper.writeValueAsString(request);

        mvc.perform(
                        delete(URI + "/" + filterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void delete_but_queueOverflow() throws Exception {
        var filterId = UUID.randomUUID();
        var prevItemHash = "hash123";
        var request = new DeleteFilterRequest(prevItemHash);

        doThrow(new QueueOverflowException("Low lane full", "LOW", "REMOVE"))
                .when(operationQueue)
                .enqueue(any(RevisionOperation.class));

        String requestBody = objectMapper.writeValueAsString(request);

        mvc.perform(
                        delete(URI + "/" + filterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void delete_but_queueClosed() throws Exception {
        var filterId = UUID.randomUUID();
        var prevItemHash = "hash123";
        var request = new DeleteFilterRequest(prevItemHash);

        doThrow(new QueueClosedException())
                .when(operationQueue)
                .enqueue(any(RevisionOperation.class));

        String requestBody = objectMapper.writeValueAsString(request);

        mvc.perform(
                        delete(URI + "/" + filterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isServiceUnavailable());
    }
}
