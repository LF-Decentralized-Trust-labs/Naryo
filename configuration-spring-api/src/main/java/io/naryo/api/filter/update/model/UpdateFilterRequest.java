package io.naryo.api.filter.update.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.filter.Filter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Valid
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = UpdateTransactionFilterRequest.class, name = "TRANSACTION"),
    @JsonSubTypes.Type(value = UpdateContractEventFilterRequest.class, name = "CONTRACT-EVENT"),
    @JsonSubTypes.Type(value = UpdateGlobalEventFilterRequest.class, name = "GLOBAL-EVENT")
})
public abstract class UpdateFilterRequest {

    protected @NotBlank String name;

    protected @NotNull UUID nodeId;

    protected @NotBlank String prevItemHash;

    protected UpdateFilterRequest(String name, UUID nodeId, String prevItemHash) {
        this.name = name;
        this.nodeId = nodeId;
        this.prevItemHash = prevItemHash;
    }

    public abstract Filter toDomain(UUID idFromPath);
}
