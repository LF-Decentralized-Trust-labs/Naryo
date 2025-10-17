package io.naryo.api.storeconfiguration.delete;

import io.naryo.api.RequestBuilder;
import io.naryo.api.storeconfiguration.delete.model.DeleteStoreConfigurationRequest;
import org.instancio.Instancio;

public class DeleteStoreConfigurationRequestBuilder
        implements RequestBuilder<
                DeleteStoreConfigurationRequestBuilder, DeleteStoreConfigurationRequest> {

    private String prevItemHash;

    public DeleteStoreConfigurationRequestBuilder withPrevItemHash(String prevItemHash) {
        this.prevItemHash = prevItemHash;
        return self();
    }

    public String getPrevItemHash() {
        return this.prevItemHash == null ? Instancio.create(String.class) : this.prevItemHash;
    }

    @Override
    public DeleteStoreConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public DeleteStoreConfigurationRequest build() {
        return new DeleteStoreConfigurationRequest(this.getPrevItemHash());
    }
}
