package io.naryo.api.filter.common.request;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.api.broadcaster.common.request.*;
import io.naryo.domain.filter.Filter;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TransactionFilterRequest.class, name = "TRANSACTION"),
    @JsonSubTypes.Type(value = ContractEventFilterRequest.class, name = "EVENT_CONTRACT"),
    @JsonSubTypes.Type(value = GlobalEventFilterRequest.class, name = "EVENT_GLOBAL")
})
@Schema(
        description = "Base filter request",
        discriminatorProperty = "type",
        discriminatorMapping = {
            @DiscriminatorMapping(value = "TRANSACTION", schema = TransactionFilterRequest.class),
            @DiscriminatorMapping(
                    value = "EVENT_CONTRACT",
                    schema = ContractEventFilterRequest.class),
            @DiscriminatorMapping(value = "EVENT_GLOBAL", schema = GlobalEventFilterRequest.class)
        })
@Getter
public abstract class FilterRequest {

    protected @NotBlank String name;

    protected @NotNull UUID nodeId;

    protected FilterRequest(String name, UUID nodeId) {
        this.name = name;
        this.nodeId = nodeId;
    }

    public Filter toDomain() {
        return toDomain(UUID.randomUUID());
    }

    public abstract Filter toDomain(UUID id);
}
