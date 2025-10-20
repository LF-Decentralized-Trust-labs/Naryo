package io.naryo.api.storeconfiguration.common.request;

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
