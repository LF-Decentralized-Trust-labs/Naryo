package io.naryo.api.node.common.interaction.block;

import io.naryo.api.node.common.interaction.InteractionConfigurationRequestBuilder;
import io.naryo.api.node.common.request.interaction.HederaMirrorNodeBlockInteractionConfigurationRequest;
import org.instancio.Instancio;

public final class HederaMirrorNodeBlockInteractionConfigurationRequestBuilder
        extends InteractionConfigurationRequestBuilder<
                HederaMirrorNodeBlockInteractionConfigurationRequestBuilder,
                HederaMirrorNodeBlockInteractionConfigurationRequest> {

    private Integer limitPerRequest;
    private Integer retriesPerRequest;

    @Override
    public HederaMirrorNodeBlockInteractionConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public HederaMirrorNodeBlockInteractionConfigurationRequest build() {
        return null;
    }

    public HederaMirrorNodeBlockInteractionConfigurationRequestBuilder withLimitPerRequest(
            Integer limitPerRequest) {
        this.limitPerRequest = limitPerRequest;
        return self();
    }

    public HederaMirrorNodeBlockInteractionConfigurationRequestBuilder withRetriesPerRequest(
            Integer retriesPerRequest) {
        this.retriesPerRequest = retriesPerRequest;
        return self();
    }

    public Integer getLimitPerRequest() {
        return this.limitPerRequest == null
                ? Instancio.create(Integer.class)
                : this.limitPerRequest;
    }

    public Integer getRetriesPerRequest() {
        return this.retriesPerRequest == null
                ? Instancio.create(Integer.class)
                : this.retriesPerRequest;
    }
}
