package io.naryo.api.storeconfiguration.create;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.api.storeconfiguration.common.request.ActiveStoreConfigurationRequest;
import io.naryo.api.storeconfiguration.common.request.InactiveStoreConfigurationRequest;
import io.naryo.api.storeconfiguration.common.ActiveStoreConfigurationRequestBuilder;
import io.naryo.api.storeconfiguration.common.InactiveStoreConfigurationRequestBuilder;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.AddOperation;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreateStoreConfigurationController.class)
class CreateStoreConfigurationControllerTest {

    @Autowired MockMvc mvc;

    @Autowired ObjectMapper objectMapper;

    @MockitoBean StoreConfigurationDescriptorMapper descriptorToDomainMapper;

    @MockitoBean(name = "storeConfigRevisionQueue")
    RevisionOperationQueue<StoreConfiguration> storeConfigRevisionQueue;

    @MockitoBean ConfigurationSchemaRegistry schemaRegistry;

    @Test
    public void createStoreConfiguration_inactive() throws Exception {
        InactiveStoreConfigurationRequest request =
                new InactiveStoreConfigurationRequestBuilder().build();

        OperationId operationId = OperationId.random();

        StoreConfiguration dummyStoreConfiguration = new HttpStoreConfigurationBuilder().build();
        when(descriptorToDomainMapper.map(request)).thenReturn(dummyStoreConfiguration);

        AddOperation<StoreConfiguration> expectedOperation =
                new AddOperation<>(dummyStoreConfiguration);
        when(storeConfigRevisionQueue.enqueue(expectedOperation)).thenReturn(operationId);

        String expectedResponse = objectMapper.writeValueAsString(operationId);

        mvc.perform(
                        post("/api/v1/store-configuration")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void createStoreConfiguration_active() throws Exception {
        String additionalPropertyKey = Instancio.create(String.class);
        Object additionalPropertyValue =
                new HttpStoreInitializer.HttpBroadcasterEndpoint(Instancio.create(String.class));
        ActiveStoreConfigurationRequest request =
                new ActiveStoreConfigurationRequestBuilder()
                        .withAdditionalProperties(
                                Map.of(additionalPropertyKey, additionalPropertyValue))
                        .build();

        String storeType = request.getType().getName();
        FieldDefinition fieldDefinition =
                new FieldDefinition(
                        additionalPropertyKey,
                        HttpStoreInitializer.HttpBroadcasterEndpoint.class,
                        true,
                        null);
        ConfigurationSchema dummyConfigurationSchema =
                new ConfigurationSchema(storeType, List.of(fieldDefinition));
        when(schemaRegistry.getSchema(ConfigurationSchemaType.STORE, storeType))
                .thenReturn(dummyConfigurationSchema);

        StoreConfiguration dummyStoreConfiguration = new HttpStoreConfigurationBuilder().build();
        when(descriptorToDomainMapper.map(request)).thenReturn(dummyStoreConfiguration);

        OperationId operationId = OperationId.random();
        AddOperation<StoreConfiguration> expectedOperation =
                new AddOperation<>(dummyStoreConfiguration);
        when(storeConfigRevisionQueue.enqueue(expectedOperation)).thenReturn(operationId);

        String expectedResponse = objectMapper.writeValueAsString(operationId);

        mvc.perform(
                        post("/api/v1/store-configuration")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedResponse));
    }
}
