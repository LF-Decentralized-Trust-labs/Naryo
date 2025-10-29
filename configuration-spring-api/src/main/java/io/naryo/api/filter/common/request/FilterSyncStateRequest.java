package io.naryo.api.filter.common.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.filter.event.FilterSyncState;
import io.naryo.domain.filter.event.sync.SyncStrategy;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "strategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BlockActiveFilterSyncStateRequest.class, name = "BLOCK_BASED")
})
@Schema(
        description = "Base filter sync state request",
        discriminatorProperty = "strategy",
        discriminatorMapping = {
            @DiscriminatorMapping(
                    value = "BLOCK_BASED",
                    schema = BlockActiveFilterSyncStateRequest.class)
        })
@Getter
public abstract class FilterSyncStateRequest {

    protected final @NotNull SyncStrategy strategy;

    protected FilterSyncStateRequest(SyncStrategy strategy) {
        this.strategy = strategy;
    }

    public abstract FilterSyncState toDomain();
}
