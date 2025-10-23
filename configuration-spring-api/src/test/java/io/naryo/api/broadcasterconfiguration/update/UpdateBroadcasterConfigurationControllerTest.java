package io.naryo.api.broadcasterconfiguration.update;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.broadcasterconfiguration.update.model.UpdateBroadcasterConfigurationRequest;
import io.naryo.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.UpdateOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.broadcaster.http.HttpBroadcasterConfigurationBuilder;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdateBroadcasterConfigurationController.class)
class UpdateBroadcasterConfigurationControllerTest {

    private final String PATH = "/api/v1/broadcaster-configurations";

    @Autowired MockMvc mvc;

    @Autowired ObjectMapper objectMapper;

    @MockitoBean BroadcasterConfigurationMapperRegistry mapperRegistry;

    @MockitoBean(name = "broadcasterConfigurationRevisionQueue")
    RevisionOperationQueue<BroadcasterConfiguration> broadcasterConfigRevisionQueue;

    @MockitoBean ConfigurationSchemaRegistry schemaRegistry;

    @Test
    public void updateBroadcasterConfiguration_accepted() throws Exception {
        String additionalPropertyKey = Instancio.create(String.class);

        UpdateBroadcasterConfigurationRequest request =
                new UpdateBroadcasterConfigurationRequestBuilder().build();

        String broadcasterType = request.broadcasterConfig().getType().getName();
        FieldDefinition fieldDefinition =
                new FieldDefinition(additionalPropertyKey, String.class, true, null);
        ConfigurationSchema dummyConfigurationSchema =
                new ConfigurationSchema(broadcasterType, List.of(fieldDefinition));
        when(schemaRegistry.getSchema(ConfigurationSchemaType.BROADCASTER, broadcasterType))
                .thenReturn(dummyConfigurationSchema);

        BroadcasterConfiguration dummyBroadcasterConfiguration =
                new HttpBroadcasterConfigurationBuilder().build();
        when(mapperRegistry.map(
                        eq(broadcasterType), any(UpdateBroadcasterConfigurationRequest.class)))
                .thenReturn(dummyBroadcasterConfiguration);

        OperationId operationId = OperationId.random();
        UpdateOperation<BroadcasterConfiguration> expectedOperation =
                new UpdateOperation<>(
                        request.broadcasterConfig().getId(),
                        request.prevItemHash(),
                        dummyBroadcasterConfiguration);
        when(broadcasterConfigRevisionQueue.enqueue(expectedOperation)).thenReturn(operationId);

        String expectedResponse = objectMapper.writeValueAsString(operationId);

        mvc.perform(
                        put(PATH)
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }
}
