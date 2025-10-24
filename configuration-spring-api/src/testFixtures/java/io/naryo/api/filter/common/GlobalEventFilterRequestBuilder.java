package io.naryo.api.filter.common;

import io.naryo.api.filter.common.request.GlobalEventFilterRequest;

public class GlobalEventFilterRequestBuilder
        extends EventFilterRequestBuilder<
                GlobalEventFilterRequestBuilder, GlobalEventFilterRequest> {

    @Override
    public GlobalEventFilterRequestBuilder self() {
        return this;
    }

    @Override
    public GlobalEventFilterRequest build() {
        return new GlobalEventFilterRequest(
                getName(),
                getNodeId(),
                getSpecification(),
                getStatuses(),
                getFilterSyncState(),
                getVisibilityConfiguration());
    }
}
