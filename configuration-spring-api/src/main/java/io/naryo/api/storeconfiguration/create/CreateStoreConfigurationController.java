package io.naryo.api.storeconfiguration.create;

import java.util.UUID;

import io.naryo.api.storeconfiguration.StoreConfigurationController;
import io.naryo.api.storeconfiguration.common.request.ActiveStoreConfigurationRequest;
import io.naryo.api.storeconfiguration.common.request.StoreConfigurationRequest;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.AddOperation;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.store.configuration.mapper.StoreConfigurationDescriptorToDomainMapper;
import io.naryo.domain.configuration.store.StoreConfiguration;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static io.naryo.infrastructure.util.serialization.ConfigurationSchemaConverter.rawObjectsToSchema;

@RestController
public class CreateStoreConfigurationController extends StoreConfigurationController {

    private final RevisionOperationQueue<StoreConfiguration> storeConfigRevisionQueue;
    private final StoreConfigurationDescriptorToDomainMapper descriptorToDomainMapper;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public CreateStoreConfigurationController(
            @Qualifier("storeConfigRevisionQueue")
                    RevisionOperationQueue<StoreConfiguration> storeConfigRevisionQueue,
            StoreConfigurationDescriptorToDomainMapper descriptorToDomainMapper,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.storeConfigRevisionQueue = storeConfigRevisionQueue;
        this.descriptorToDomainMapper = descriptorToDomainMapper;
        this.schemaRegistry = schemaRegistry;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID create(@Valid @RequestBody StoreConfigurationRequest request) {
        if (request instanceof ActiveStoreConfigurationRequest activeStoreConfigurationRequest) {
            String storeType = activeStoreConfigurationRequest.getType().getName();
            ConfigurationSchema schema =
                    schemaRegistry.getSchema(ConfigurationSchemaType.STORE, storeType);
            activeStoreConfigurationRequest.setAdditionalProperties(
                    rawObjectsToSchema(
                            activeStoreConfigurationRequest.getAdditionalProperties(), schema));
        }

        StoreConfiguration storeConfiguration = descriptorToDomainMapper.map(request);
        RevisionOperation<StoreConfiguration> op = new AddOperation<>(storeConfiguration);
        OperationId opId = storeConfigRevisionQueue.enqueue(op);

        return opId.value();
    }
}
