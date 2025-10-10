package io.naryo.api.filter.response;

public sealed interface FilterResponse
        permits GlobalEventFilterResponse, ContractEventFilterResponse, TransactionFilterResponse {}
