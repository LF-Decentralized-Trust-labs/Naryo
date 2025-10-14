package io.naryo.api.filter.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.filter.Filter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreateTransactionFilterRequest.class, name = "transaction"),
    @JsonSubTypes.Type(value = CreateContractEventFilterRequest.class, name = "contract-event"),
    @JsonSubTypes.Type(value = CreateGlobalEventFilterRequest.class, name = "global-event")
})
public sealed interface CreateFilterRequest
        permits CreateTransactionFilterRequest,
                CreateGlobalEventFilterRequest,
                CreateContractEventFilterRequest {

    static Filter toDomain(CreateFilterRequest req) {
        return switch (req) {
            case CreateTransactionFilterRequest t -> CreateTransactionFilterRequest.toDomain(t);
            case CreateGlobalEventFilterRequest g -> CreateGlobalEventFilterRequest.toDomain(g);
            case CreateContractEventFilterRequest c -> CreateContractEventFilterRequest.toDomain(c);
        };
    }
}
