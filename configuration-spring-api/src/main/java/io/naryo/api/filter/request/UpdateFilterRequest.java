package io.naryo.api.filter.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.filter.Filter;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = UpdateTransactionFilterRequest.class, name = "transaction"),
    @JsonSubTypes.Type(value = UpdateContractEventFilterRequest.class, name = "contract-event"),
    @JsonSubTypes.Type(value = UpdateGlobalEventFilterRequest.class, name = "global-event")
})
public sealed interface UpdateFilterRequest permits UpdateContractEventFilterRequest, UpdateGlobalEventFilterRequest, UpdateTransactionFilterRequest{

    static Filter toDomain(UpdateFilterRequest req, UUID idFromPath) {
        return switch (req) {
            case UpdateTransactionFilterRequest t -> toDomain(t, idFromPath);
            case UpdateGlobalEventFilterRequest g -> toDomain(g, idFromPath);
            case UpdateContractEventFilterRequest c -> toDomain(c, idFromPath);
        };
    }
}
