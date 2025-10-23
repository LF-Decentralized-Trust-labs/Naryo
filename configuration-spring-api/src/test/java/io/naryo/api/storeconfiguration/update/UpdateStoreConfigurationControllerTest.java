package io.naryo.api.storeconfiguration.update;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.storeconfiguration.common.request.ActiveStoreConfigurationRequest;
import io.naryo.api.storeconfiguration.common.request.ActiveStoreConfigurationRequestBuilder;
import io.naryo.api.storeconfiguration.common.request.InactiveStoreConfigurationRequest;
import io.naryo.api.storeconfiguration.common.request.InactiveStoreConfigurationRequestBuilder;
import io.naryo.api.storeconfiguration.update.model.UpdateStoreConfigurationRequest;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.UpdateOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.store.configuration.mapper.StoreConfigurationDescriptorMapper;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.active.HttpStoreConfigurationBuilder;
import io.naryo.infrastructure.configuration.beans.store.http.HttpStoreInitializer;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdateStoreConfigurationController.class)
class UpdateStoreConfigurationControllerTest {

    private final String PATH = "/api/v1/store-configurations";

    @Autowired MockMvc mvc;

    @Autowired ObjectMapper objectMapper;

    @MockitoBean StoreConfigurationDescriptorMapper descriptorToDomainMapper;

    @MockitoBean(name = "storeConfigRevisionQueue")
    RevisionOperationQueue<StoreConfiguration> storeConfigRevisionQueue;

    @MockitoBean ConfigurationSchemaRegistry schemaRegistry;

    @Test
    public void updateStoreConfiguration_inactive() throws Exception {
        InactiveStoreConfigurationRequest target =
                new InactiveStoreConfigurationRequestBuilder().build();

        UpdateStoreConfigurationRequest request =
                new UpdateStoreConfigurationRequestBuilder().withTarget(target).build();

        OperationId operationId = OperationId.random();

        StoreConfiguration dummyStoreConfiguration = new HttpStoreConfigurationBuilder().build();
        when(descriptorToDomainMapper.map(target)).thenReturn(dummyStoreConfiguration);

        UpdateOperation<StoreConfiguration> expectedOperation =
                new UpdateOperation<>(
                        dummyStoreConfiguration.getNodeId(),
                        request.prevItemHash(),
                        dummyStoreConfiguration);
        when(storeConfigRevisionQueue.enqueue(expectedOperation)).thenReturn(operationId);

        String expectedResponse = objectMapper.writeValueAsString(operationId);

        mvc.perform(
                        put(PATH)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void updateStoreConfiguration_active() throws Exception {
        String additionalPropertyKey = Instancio.create(String.class);
        Object additionalPropertyValue =
                new HttpStoreInitializer.HttpStoreEndpoint(Instancio.create(String.class));
        ActiveStoreConfigurationRequest target =
                new ActiveStoreConfigurationRequestBuilder()
                        .withAdditionalProperties(
                                Map.of(additionalPropertyKey, additionalPropertyValue))
                        .build();

        UpdateStoreConfigurationRequest request =
                new UpdateStoreConfigurationRequestBuilder().withTarget(target).build();

        String storeType = target.getType().getName();
        FieldDefinition fieldDefinition =
                new FieldDefinition(
                        additionalPropertyKey,
                        HttpStoreInitializer.HttpStoreEndpoint.class,
                        true,
                        null);
        ConfigurationSchema dummyConfigurationSchema =
                new ConfigurationSchema(storeType, List.of(fieldDefinition));
        when(schemaRegistry.getSchema(ConfigurationSchemaType.STORE, storeType))
                .thenReturn(dummyConfigurationSchema);

        StoreConfiguration dummyStoreConfiguration = new HttpStoreConfigurationBuilder().build();
        when(descriptorToDomainMapper.map(target)).thenReturn(dummyStoreConfiguration);

        OperationId operationId = OperationId.random();
        UpdateOperation<StoreConfiguration> expectedOperation =
                new UpdateOperation<>(
                        dummyStoreConfiguration.getNodeId(),
                        request.prevItemHash(),
                        dummyStoreConfiguration);
        when(storeConfigRevisionQueue.enqueue(expectedOperation)).thenReturn(operationId);

        String expectedResponse = objectMapper.writeValueAsString(operationId);

        mvc.perform(
                        put(PATH)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }
}
