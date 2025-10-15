package io.naryo.api.filter.create.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.filter.Filter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreateTransactionFilterRequest.class, name = "TRANSACTION"),
    @JsonSubTypes.Type(value = CreateContractEventFilterRequest.class, name = "CONTRACT-EVENT"),
    @JsonSubTypes.Type(value = CreateGlobalEventFilterRequest.class, name = "GLOBAL-EVENT")
})
public abstract class CreateFilterRequest {

    @NotBlank
    protected String name;

    @NotNull
    protected UUID nodeId;

    protected CreateFilterRequest(String name, UUID nodeId) {
        this.name = name;
        this.nodeId = nodeId;
    }

    public abstract Filter toDomain();
}
