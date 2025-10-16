package io.naryo.api.filter.create.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.filter.Filter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreateTransactionFilterRequest.class, name = "TRANSACTION"),
    @JsonSubTypes.Type(value = CreateContractEventFilterRequest.class, name = "CONTRACT-EVENT"),
    @JsonSubTypes.Type(value = CreateGlobalEventFilterRequest.class, name = "GLOBAL-EVENT")
})
public abstract class CreateFilterRequest {

    protected @NotBlank String name;

    protected @NotNull UUID nodeId;

    protected CreateFilterRequest(String name, UUID nodeId) {
        this.name = name;
        this.nodeId = nodeId;
    }

    public abstract Filter toDomain();
}
