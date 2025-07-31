package io.naryo.domain.filter.event;

public class GlobalEventFilterBuilder
        extends EventFilterBuilder<GlobalEventFilterBuilder, GlobalEventFilter> {

    @Override
    public GlobalEventFilterBuilder self() {
        return this;
    }

    @Override
    public GlobalEventFilter build() {
        return new GlobalEventFilter(
                this.getId(),
                this.getName(),
                this.getNodeId(),
                this.getSpecification(),
                this.getStatuses(),
                this.getSyncState(),
                this.getVisibilityConfiguration());
    }
}
