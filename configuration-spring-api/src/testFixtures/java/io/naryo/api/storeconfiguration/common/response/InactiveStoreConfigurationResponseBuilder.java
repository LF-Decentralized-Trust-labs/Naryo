package io.naryo.api.storeconfiguration.common.response;

import io.naryo.api.storeconfiguration.getAll.model.InactiveStoreConfigurationResponse;

public class InactiveStoreConfigurationResponseBuilder
        extends StoreConfigurationResponseBuilder<
                InactiveStoreConfigurationResponseBuilder, InactiveStoreConfigurationResponse> {

    @Override
    public InactiveStoreConfigurationResponseBuilder self() {
        return this;
    }

    @Override
    public InactiveStoreConfigurationResponse build() {
        return new InactiveStoreConfigurationResponse(getNodeId(), getCurrentItemHash());
    }
}
