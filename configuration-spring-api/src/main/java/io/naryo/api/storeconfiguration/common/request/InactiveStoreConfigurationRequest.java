package io.naryo.api.storeconfiguration.common.request;

import java.util.UUID;

import io.naryo.application.configuration.source.model.store.InactiveStoreConfigurationDescriptor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;

@Schema(description = "Inactive store configuration request")
@EqualsAndHashCode(callSuper = true)
public final class InactiveStoreConfigurationRequest extends StoreConfigurationRequest
        implements InactiveStoreConfigurationDescriptor {

    public InactiveStoreConfigurationRequest(UUID nodeId) {
        super(nodeId);
    }
}
