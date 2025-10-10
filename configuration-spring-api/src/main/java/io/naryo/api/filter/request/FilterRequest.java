package io.naryo.api.filter.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TransactionFilterRequest.class, name = "transaction"),
    @JsonSubTypes.Type(value = ContractEventFilterRequest.class, name = "contract-event"),
    @JsonSubTypes.Type(value = GlobalEventFilterRequest.class, name = "global-event")
})
public sealed interface FilterRequest
        permits TransactionFilterRequest, GlobalEventFilterRequest, ContractEventFilterRequest {}
