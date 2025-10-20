package io.naryo.api.filter.common;

import io.naryo.api.filter.common.request.BlockActiveFilterSyncStateRequest;
import io.naryo.api.filter.common.request.FilterSyncStateRequest;
import org.instancio.Instancio;

public class FilterSyncStateRequestBuilder {

    private Long initialBlock;

    public FilterSyncStateRequestBuilder withInitialBlock(Long initialBlock) {
        this.initialBlock = initialBlock;
        return this;
    }

    public Long getInitialBlock() {
        return this.initialBlock != null ? this.initialBlock : Instancio.create(Long.class);
    }

    public FilterSyncStateRequest build() {
        return new BlockActiveFilterSyncStateRequest(getInitialBlock());
    }
}
