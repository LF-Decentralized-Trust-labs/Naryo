package io.naryo.api.storeconfiguration.delete;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.storeconfiguration.delete.model.DeleteStoreConfigurationRequest;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RemoveOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.domain.configuration.store.StoreConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeleteStoreConfigurationController.class)
class DeleteStoreConfigurationControllerTest {

    private final String PATH = "/api/v1/store-configuration";

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean RevisionOperationQueue<StoreConfiguration> operationQueue;

    @Test
    void delete_ok() throws Exception {
        UUID nodeId = UUID.randomUUID();
        OperationId operationId = OperationId.random();

        DeleteStoreConfigurationRequest request =
                new DeleteStoreConfigurationRequestBuilder().build();

        RemoveOperation<StoreConfiguration> expectedOperation =
                new RemoveOperation<>(nodeId, request.prevItemHash());
        when(operationQueue.enqueue(expectedOperation)).thenReturn(operationId);

        String expectedResponse = objectMapper.writeValueAsString(operationId);

        mvc.perform(
                        delete(PATH + "/" + nodeId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }
}
