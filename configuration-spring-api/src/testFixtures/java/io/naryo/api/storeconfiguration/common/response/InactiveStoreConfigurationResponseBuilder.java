package io.naryo.api.storeconfiguration.common.response;

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
