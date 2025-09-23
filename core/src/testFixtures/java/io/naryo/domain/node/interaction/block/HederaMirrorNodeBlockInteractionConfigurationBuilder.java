package io.naryo.domain.node.interaction.block;

import io.naryo.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.LimitPerRequest;
import io.naryo.domain.node.interaction.block.hedera.RetriesPerRequest;
import org.instancio.Instancio;

public final class HederaMirrorNodeBlockInteractionConfigurationBuilder
        extends BlockInteractionConfigurationBuilder<
                HederaMirrorNodeBlockInteractionConfigurationBuilder,
                HederaMirrorNodeBlockInteractionConfiguration> {

    private LimitPerRequest limitPerRequest;
    private RetriesPerRequest retriesPerRequest;

    @Override
    public HederaMirrorNodeBlockInteractionConfigurationBuilder self() {
        return this;
    }

    @Override
    public HederaMirrorNodeBlockInteractionConfiguration build() {
        return null;
    }

    public HederaMirrorNodeBlockInteractionConfigurationBuilder withLimitPerRequest(
            LimitPerRequest limitPerRequest) {
        this.limitPerRequest = limitPerRequest;
        return self();
    }

    public HederaMirrorNodeBlockInteractionConfigurationBuilder withRetriesPerRequest(
            RetriesPerRequest retriesPerRequest) {
        this.retriesPerRequest = retriesPerRequest;
        return self();
    }

    public LimitPerRequest getLimitPerRequest() {
        return this.limitPerRequest == null
                ? Instancio.create(LimitPerRequest.class)
                : this.limitPerRequest;
    }

    public RetriesPerRequest getRetriesPerRequest() {
        return this.retriesPerRequest == null
                ? Instancio.create(RetriesPerRequest.class)
                : this.retriesPerRequest;
    }
}
