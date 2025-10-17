package io.naryo.api.storeconfiguration.common.request;

import java.util.UUID;

import io.naryo.application.configuration.source.model.store.InactiveStoreConfigurationDescriptor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class InactiveStoreConfigurationRequest extends StoreConfigurationRequest
        implements InactiveStoreConfigurationDescriptor {

    public InactiveStoreConfigurationRequest(UUID nodeId) {
        super(nodeId);
    }
}
