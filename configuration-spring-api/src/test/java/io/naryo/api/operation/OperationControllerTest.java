package io.naryo.api.operation;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.operation.response.RevisionOperationStatusResponse;
import io.naryo.application.configuration.revision.RevisionOperationState;
import io.naryo.application.configuration.revision.RevisionOperationStatus;
import io.naryo.application.configuration.revision.store.RevisionOperationStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OperationController.class)
class OperationControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean RevisionOperationStore revisionOperationStore;

    @Test
    public void getOperations_ok() throws Exception {
        UUID operationId = UUID.randomUUID();
        RevisionOperationStatus dummyRevisionOperationStatus =
                new RevisionOperationStatus(
                        operationId,
                        RevisionOperationState.FAILED,
                        1L,
                        "hash",
                        "errorCode",
                        "errorMessage",
                        Instant.now(),
                        Instant.now());
        when(revisionOperationStore.get(operationId))
                .thenReturn(Optional.of(dummyRevisionOperationStatus));

        String expectedResponse =
                objectMapper.writeValueAsString(
                        RevisionOperationStatusResponse.fromRevisionOperationStatus(
                                dummyRevisionOperationStatus));

        mvc.perform(get("/api/v1/operations/" + operationId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void getOperations_notFound() throws Exception {
        UUID operationId = UUID.randomUUID();

        when(revisionOperationStore.get(operationId)).thenReturn(Optional.empty());

        mvc.perform(get("/api/v1/operations/" + operationId)).andExpect(status().isNotFound());
    }
}
