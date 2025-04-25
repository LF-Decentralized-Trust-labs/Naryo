package io.librevents.application.event.decoder.block;

import io.librevents.domain.event.contract.ContractEventParameter;

public record DecodeResult(ContractEventParameter<?> parameter, int newOffset) {}
