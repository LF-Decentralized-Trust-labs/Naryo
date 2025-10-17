package io.naryo.api.storeconfiguration.update;

import io.naryo.api.storeconfiguration.StoreConfigurationController;
import io.naryo.api.storeconfiguration.common.request.ActiveStoreConfigurationRequest;
import io.naryo.api.storeconfiguration.update.model.UpdateStoreConfigurationRequest;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.operation.UpdateOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.store.configuration.mapper.StoreConfigurationDescriptorMapper;
import io.naryo.domain.configuration.store.StoreConfiguration;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static io.naryo.infrastructure.util.serialization.ConfigurationSchemaConverter.rawObjectsToSchema;

@RestController
public class UpdateStoreConfigurationController extends StoreConfigurationController {

    private final RevisionOperationQueue<StoreConfiguration> storeConfigRevisionQueue;
    private final StoreConfigurationDescriptorMapper descriptorToDomainMapper;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public UpdateStoreConfigurationController(
            @Qualifier("storeConfigRevisionQueue")
                    RevisionOperationQueue<StoreConfiguration> storeConfigRevisionQueue,
            StoreConfigurationDescriptorMapper descriptorToDomainMapper,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.storeConfigRevisionQueue = storeConfigRevisionQueue;
        this.descriptorToDomainMapper = descriptorToDomainMapper;
        this.schemaRegistry = schemaRegistry;
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OperationId update(@Valid @RequestBody UpdateStoreConfigurationRequest request) {
        if (request.target()
                instanceof ActiveStoreConfigurationRequest activeStoreConfigurationRequest) {
            String storeType = activeStoreConfigurationRequest.getType().getName();
            ConfigurationSchema schema =
                    schemaRegistry.getSchema(ConfigurationSchemaType.STORE, storeType);
            activeStoreConfigurationRequest.setAdditionalProperties(
                    rawObjectsToSchema(
                            activeStoreConfigurationRequest.getAdditionalProperties(), schema));
        }

        StoreConfiguration storeConfiguration = descriptorToDomainMapper.map(request.target());

        RevisionOperation<StoreConfiguration> operation =
                new UpdateOperation<>(
                        storeConfiguration.getNodeId(), request.prevItemHash(), storeConfiguration);

        return storeConfigRevisionQueue.enqueue(operation);
    }
}
