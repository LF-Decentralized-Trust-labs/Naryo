package io.naryo.api.filter.delete;

import io.naryo.api.RequestBuilder;
import io.naryo.api.filter.delete.model.DeleteFilterRequest;
import org.instancio.Instancio;

public class DeleteFilterRequestBuilder
        implements RequestBuilder<DeleteFilterRequestBuilder, DeleteFilterRequest> {

    private String prevItemHash;

    @Override
    public DeleteFilterRequestBuilder self() {
        return this;
    }

    @Override
    public DeleteFilterRequest build() {
        return new DeleteFilterRequest(getPrevItemHash());
    }

    public DeleteFilterRequestBuilder withPrevItemHash(String prevItemHash) {
        this.prevItemHash = prevItemHash;
        return self();
    }

    public String getPrevItemHash() {
        return this.prevItemHash == null ? Instancio.create(String.class) : this.prevItemHash;
    }
}
