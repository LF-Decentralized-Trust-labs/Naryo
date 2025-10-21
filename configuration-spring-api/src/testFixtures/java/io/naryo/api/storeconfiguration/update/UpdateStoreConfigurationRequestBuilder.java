package io.naryo.api.storeconfiguration.update;

import io.naryo.api.RequestBuilder;
import io.naryo.api.storeconfiguration.common.request.InactiveStoreConfigurationRequestBuilder;
import io.naryo.api.storeconfiguration.common.request.StoreConfigurationRequest;
import io.naryo.api.storeconfiguration.update.model.UpdateStoreConfigurationRequest;
import org.instancio.Instancio;

public class UpdateStoreConfigurationRequestBuilder
        implements RequestBuilder<
                UpdateStoreConfigurationRequestBuilder, UpdateStoreConfigurationRequest> {

    private StoreConfigurationRequest target;
    private String prevItemHash;

    public UpdateStoreConfigurationRequestBuilder withTarget(StoreConfigurationRequest target) {
        this.target = target;
        return self();
    }

    public StoreConfigurationRequest getTarget() {
        return this.target == null
                ? new InactiveStoreConfigurationRequestBuilder().build()
                : this.target;
    }

    public UpdateStoreConfigurationRequestBuilder withPrevItemHash(String prevItemHash) {
        this.prevItemHash = prevItemHash;
        return self();
    }

    public String getPrevItemHash() {
        return this.prevItemHash == null ? Instancio.create(String.class) : this.prevItemHash;
    }

    @Override
    public UpdateStoreConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public UpdateStoreConfigurationRequest build() {
        return new UpdateStoreConfigurationRequest(this.getTarget(), this.getPrevItemHash());
    }
}
