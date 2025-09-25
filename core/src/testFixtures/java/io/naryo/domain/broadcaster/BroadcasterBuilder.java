package io.naryo.domain.broadcaster;

import java.util.UUID;

import io.naryo.domain.DomainBuilder;
import io.naryo.domain.broadcaster.target.AllBroadcasterTargetBuilder;

public class BroadcasterBuilder implements DomainBuilder<BroadcasterBuilder, Broadcaster> {

    private UUID id;
    private BroadcasterTarget target;
    private UUID configurationId;

    @Override
    public BroadcasterBuilder self() {
        return this;
    }

    public Broadcaster build() {
        return new Broadcaster(this.getId(), this.getTarget(), this.getConfigurationId());
    }

    public BroadcasterBuilder withId(UUID id) {
        this.id = id;
        return self();
    }

    public BroadcasterBuilder withTarget(BroadcasterTarget target) {
        this.target = target;
        return self();
    }

    public BroadcasterBuilder withConfigurationId(UUID configurationId) {
        this.configurationId = configurationId;
        return self();
    }

    protected UUID getId() {
        return this.id == null ? UUID.randomUUID() : this.id;
    }

    protected BroadcasterTarget getTarget() {
        return this.target == null ? new AllBroadcasterTargetBuilder().build() : this.target;
    }

    protected UUID getConfigurationId() {
        return this.configurationId == null ? UUID.randomUUID() : this.configurationId;
    }
}
