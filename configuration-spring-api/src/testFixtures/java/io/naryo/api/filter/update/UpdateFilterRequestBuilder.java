package io.naryo.api.filter.update;

import io.naryo.api.RequestBuilder;
import io.naryo.api.filter.common.GlobalEventFilterRequestBuilder;
import io.naryo.api.filter.common.request.FilterRequest;
import io.naryo.api.filter.update.model.UpdateFilterRequest;
import org.instancio.Instancio;

public class UpdateFilterRequestBuilder
        implements RequestBuilder<UpdateFilterRequestBuilder, UpdateFilterRequest> {

    private FilterRequest target;
    private String prevItemHash;

    public UpdateFilterRequestBuilder withTarget(FilterRequest target) {
        this.target = target;
        return self();
    }

    public FilterRequest getTarget() {
        return this.target == null ? new GlobalEventFilterRequestBuilder().build() : this.target;
    }

    public UpdateFilterRequestBuilder withPrevItemHash(String prevItemHash) {
        this.prevItemHash = prevItemHash;
        return self();
    }

    public String getPrevItemHash() {
        return this.prevItemHash == null ? Instancio.create(String.class) : this.prevItemHash;
    }

    @Override
    public UpdateFilterRequestBuilder self() {
        return this;
    }

    @Override
    public UpdateFilterRequest build() {
        return new UpdateFilterRequest(getTarget(), getPrevItemHash());
    }
}
