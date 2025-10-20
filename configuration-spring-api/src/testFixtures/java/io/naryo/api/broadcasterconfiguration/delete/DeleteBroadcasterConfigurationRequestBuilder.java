package io.naryo.api.broadcasterconfiguration.delete;

import io.naryo.api.RequestBuilder;
import io.naryo.api.broadcasterconfiguration.delete.model.DeleteBroadcasterConfigurationRequest;
import org.instancio.Instancio;

public class DeleteBroadcasterConfigurationRequestBuilder
        implements RequestBuilder<
                DeleteBroadcasterConfigurationRequestBuilder,
                DeleteBroadcasterConfigurationRequest> {

    private String prevItemHash;

    @Override
    public DeleteBroadcasterConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public DeleteBroadcasterConfigurationRequest build() {
        return new DeleteBroadcasterConfigurationRequest(getPrevItemHash());
    }

    public DeleteBroadcasterConfigurationRequestBuilder withPrevItemHash(String prevItemHash) {
        this.prevItemHash = prevItemHash;
        return self();
    }

    public String getPrevItemHash() {
        return this.prevItemHash == null ? Instancio.create(String.class) : this.prevItemHash;
    }
}
