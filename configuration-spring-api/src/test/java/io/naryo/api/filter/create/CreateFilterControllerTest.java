package io.naryo.api.filter.create;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.filter.common.ContractEventFilterRequestBuilder;
import io.naryo.api.filter.common.GlobalEventFilterRequestBuilder;
import io.naryo.api.filter.common.TransactionFilterRequestBuilder;
import io.naryo.api.filter.common.request.ContractEventFilterRequest;
import io.naryo.api.filter.common.request.GlobalEventFilterRequest;
import io.naryo.api.filter.common.request.TransactionFilterRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreateFilterController.class)
class CreateFilterControllerTest {

    private final String URI = "/api/v1/filters";

    @Autowired MockMvc mvc;

    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    @SuppressWarnings("unchecked")
    RevisionOperationQueue<Filter> operationQueue;

    @Test
    void create_new_transaction_filter_ok() throws Exception {
        var opId = new OperationId(UUID.randomUUID());
        var input = new TransactionFilterRequestBuilder().build();

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        post(URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writerFor(TransactionFilterRequest.class)
                                                .writeValueAsBytes(input)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void create_new_global_event_filter_ok() throws Exception {
        var opId = new OperationId(UUID.randomUUID());
        var input = new GlobalEventFilterRequestBuilder().build();

        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        post(URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writerFor(GlobalEventFilterRequest.class)
                                                .writeValueAsBytes(input)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void create_new_contract_event_filter_ok() throws Exception {
        var opId = new OperationId(UUID.randomUUID());
        var input = new ContractEventFilterRequestBuilder().build();
        when(operationQueue.enqueue(any())).thenReturn(opId);

        String expectedResponse = objectMapper.writeValueAsString(opId);

        mvc.perform(
                        post(URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writerFor(ContractEventFilterRequest.class)
                                                .writeValueAsBytes(input)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void create_but_malformedJson() throws Exception {
        String malformedJson = "{ \"name\": \"x\", ";

        mvc.perform(post(URI).contentType(MediaType.APPLICATION_JSON).content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_but_queueOverflow() throws Exception {
        var input = new ContractEventFilterRequestBuilder().build();

        doThrow(new QueueOverflowException("Low lane full", "LOW", "ADD"))
                .when(operationQueue)
                .enqueue(any(RevisionOperation.class));

        mvc.perform(
                        post(URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void create_but_queueClosed() throws Exception {
        var input = new ContractEventFilterRequestBuilder().build();
        doThrow(new QueueClosedException())
                .when(operationQueue)
                .enqueue(any(RevisionOperation.class));

        mvc.perform(
                        post(URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isServiceUnavailable());
    }
}
