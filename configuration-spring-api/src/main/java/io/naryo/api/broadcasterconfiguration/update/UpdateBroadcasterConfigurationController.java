package io.naryo.api.broadcasterconfiguration.update;

import io.naryo.api.broadcasterconfiguration.common.BroadcasterConfigurationController;
import io.naryo.api.broadcasterconfiguration.update.model.UpdateBroadcasterConfigurationRequest;
import io.naryo.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.operation.UpdateOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static io.naryo.infrastructure.util.serialization.ConfigurationSchemaConverter.rawObjectsToSchema;

@Validated
@RestController
@RequiredArgsConstructor
public class UpdateBroadcasterConfigurationController extends BroadcasterConfigurationController {

    private final @Qualifier("broadcasterConfigurationRevisionQueue") RevisionOperationQueue<
                    BroadcasterConfiguration>
            broadcasterConfigRevisionQueue;
    private final BroadcasterConfigurationMapperRegistry mapperRegistry;
    private final ConfigurationSchemaRegistry schemaRegistry;

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OperationId update(@Valid @RequestBody UpdateBroadcasterConfigurationRequest request) {
        String broadcasterType = request.getType().getName();
        ConfigurationSchema schema =
                schemaRegistry.getSchema(ConfigurationSchemaType.BROADCASTER, broadcasterType);
        request.setAdditionalProperties(
                rawObjectsToSchema(request.getAdditionalProperties(), schema));

        BroadcasterConfiguration broadcasterConfiguration =
                mapperRegistry.map(broadcasterType, request);
        RevisionOperation<BroadcasterConfiguration> op =
                new UpdateOperation<>(
                        request.getId(), request.getPrevItemHash(), broadcasterConfiguration);
        return broadcasterConfigRevisionQueue.enqueue(op);
    }
}
