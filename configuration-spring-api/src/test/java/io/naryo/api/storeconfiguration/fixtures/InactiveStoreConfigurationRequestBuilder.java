package io.naryo.api.storeconfiguration.fixtures;

import io.naryo.api.storeconfiguration.common.request.InactiveStoreConfigurationRequest;

public class InactiveStoreConfigurationRequestBuilder
        extends StoreConfigurationRequestBuilder<
                InactiveStoreConfigurationRequestBuilder, InactiveStoreConfigurationRequest> {

    @Override
    public InactiveStoreConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public InactiveStoreConfigurationRequest build() {
        return new InactiveStoreConfigurationRequest(getNodeId());
    }
}
