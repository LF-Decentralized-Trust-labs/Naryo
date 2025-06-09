package io.naryo.infrastructure.configuration.source.env.model.filter.event.contract;

import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventFilterConfigurationAdditionalProperties;
import jakarta.validation.constraints.NotBlank;

public record ContractEventFilterConfigurationAdditionalProperties(@NotBlank String address)
        implements EventFilterConfigurationAdditionalProperties {}
