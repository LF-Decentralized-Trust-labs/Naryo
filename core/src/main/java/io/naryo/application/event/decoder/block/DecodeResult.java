package io.naryo.application.event.decoder.block;

import io.naryo.domain.event.contract.ContractEventParameter;

public record DecodeResult(ContractEventParameter<?> parameter, int newOffset) {}
