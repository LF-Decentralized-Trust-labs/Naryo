package io.librevents.infrastructure.configuration.source.env.model.filter.event.contract;

import io.librevents.infrastructure.configuration.source.env.model.filter.event.EventFilterConfigurationAdditionalProperties;

public record ContractEventFilterConfigurationAdditionalProperties(String address)
        implements EventFilterConfigurationAdditionalProperties {}
