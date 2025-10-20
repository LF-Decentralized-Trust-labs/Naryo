package io.naryo.api.filter.update;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.filter.update.model.UpdateContractEventFilterRequest;
import io.naryo.api.filter.update.model.UpdateGlobalEventFilterRequest;
import io.naryo.api.filter.update.model.UpdateTransactionFilterRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdateFilterController.class)
class UpdateFilterControllerTest {

    private final String URI = "/api/v1/filters";

    @Autowired MockMvc mvc;

    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    @SuppressWarnings("unchecked")
    RevisionOperationQueue<Filter> operationQueue;

    @Test
    void update_transaction_filter_ok() throws Exception {
        var opId = new OperationId(UUID.randomUUID());
        var filterId = UUID.randomUUID();
        var input = new UpdateTransactionFilterRequestBuilder().build();

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        put(URI + "/" + filterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writerFor(UpdateTransactionFilterRequest.class)
                                                .writeValueAsBytes(input)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void update_global_event_filter_ok() throws Exception {
        var opId = new OperationId(UUID.randomUUID());
        var filterId = UUID.randomUUID();
        var input = new UpdateGlobalEventFilterRequestBuilder().build();

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        put(URI + "/" + filterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writerFor(UpdateGlobalEventFilterRequest.class)
                                                .writeValueAsBytes(input)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void update_contract_event_filter_ok() throws Exception {
        var opId = new OperationId(UUID.randomUUID());
        var filterId = UUID.randomUUID();
        var input = new UpdateContractEventFilterRequestBuilder().build();

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        put(URI + "/" + filterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writerFor(UpdateContractEventFilterRequest.class)
                                                .writeValueAsBytes(input)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void update_but_malformedJson() throws Exception {
        var filterId = UUID.randomUUID();
        String malformedJson = "{ \"name\": \"x\", ";

        mvc.perform(
                        put(URI + "/" + filterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_but_queueOverflow() throws Exception {
        var filterId = UUID.randomUUID();
        var input = new UpdateTransactionFilterRequestBuilder().build();

        doThrow(new QueueOverflowException("Low lane full", "LOW", "UPDATE"))
                .when(operationQueue)
                .enqueue(any(RevisionOperation.class));

        mvc.perform(
                        put(URI + "/" + filterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void update_but_queueClosed() throws Exception {
        var filterId = UUID.randomUUID();
        var input = new UpdateTransactionFilterRequestBuilder().build();

        doThrow(new QueueClosedException())
                .when(operationQueue)
                .enqueue(any(RevisionOperation.class));

        mvc.perform(
                        put(URI + "/" + filterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isServiceUnavailable());
    }
}
