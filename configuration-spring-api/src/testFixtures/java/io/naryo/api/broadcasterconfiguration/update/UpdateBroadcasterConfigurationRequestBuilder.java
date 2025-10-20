package io.naryo.api.broadcasterconfiguration.update;

import io.naryo.api.RequestBuilder;
import io.naryo.api.broadcasterconfiguration.update.model.UpdateBroadcasterConfigurationRequest;
import io.naryo.api.broadcasterconfiguration.update.model.UpdateBroadcasterConfigurationRequestTarget;
import org.instancio.Instancio;

public class UpdateBroadcasterConfigurationRequestBuilder
        implements RequestBuilder<
                UpdateBroadcasterConfigurationRequestBuilder,
                UpdateBroadcasterConfigurationRequest> {

    private UpdateBroadcasterConfigurationRequestTarget target;
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
            UpdateBroadcasterConfigurationRequestTarget target) {
        this.target = target;
        return self();
    }

    public UpdateBroadcasterConfigurationRequestTarget getTarget() {
        return this.target == null
                ? new UpdateBroadcasterConfigurationRequestTargetBuilder().build()
                : this.target;
    }

    public UpdateBroadcasterConfigurationRequestBuilder withPrevItemHash(String prevItemHash) {
        this.prevItemHash = prevItemHash;
        return self();
    }

    public String getPrevItemHash() {
        return this.prevItemHash == null ? Instancio.create(String.class) : this.prevItemHash;
    }
}
