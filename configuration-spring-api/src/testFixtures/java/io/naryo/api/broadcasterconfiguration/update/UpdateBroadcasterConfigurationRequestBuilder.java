package io.naryo.api.broadcasterconfiguration.update;

import io.naryo.api.RequestBuilder;
import io.naryo.api.broadcasterconfiguration.common.request.BroadcasterConfigurationRequest;
import io.naryo.api.broadcasterconfiguration.common.request.BroadcasterConfigurationRequestBuilder;
import io.naryo.api.broadcasterconfiguration.update.model.UpdateBroadcasterConfigurationRequest;
import org.instancio.Instancio;

public class UpdateBroadcasterConfigurationRequestBuilder
        implements RequestBuilder<
                UpdateBroadcasterConfigurationRequestBuilder,
                UpdateBroadcasterConfigurationRequest> {

    private BroadcasterConfigurationRequestBuilder broadcasterConfigRequestBuilder;
    private String prevItemHash;

    @Override
    public UpdateBroadcasterConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public UpdateBroadcasterConfigurationRequest build() {
        return new UpdateBroadcasterConfigurationRequest(getTarget(), getPrevItemHash());
    }

    public UpdateBroadcasterConfigurationRequestBuilder withTarget(
            BroadcasterConfigurationRequestBuilder targetBuilder) {
        this.broadcasterConfigRequestBuilder = targetBuilder;
        return self();
    }

    public BroadcasterConfigurationRequest getTarget() {
        return this.broadcasterConfigRequestBuilder == null
                ? new BroadcasterConfigurationRequestBuilder().build()
                : this.broadcasterConfigRequestBuilder.build();
    }

    public UpdateBroadcasterConfigurationRequestBuilder withPrevItemHash(String prevItemHash) {
        this.prevItemHash = prevItemHash;
        return self();
    }

    public String getPrevItemHash() {
        return this.prevItemHash == null ? Instancio.create(String.class) : this.prevItemHash;
    }
}
