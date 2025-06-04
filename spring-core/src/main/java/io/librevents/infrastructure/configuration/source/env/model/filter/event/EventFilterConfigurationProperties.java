package io.librevents.infrastructure.configuration.source.env.model.filter.event;

import java.util.List;

import io.librevents.domain.common.event.ContractEventStatus;
import io.librevents.domain.filter.event.EventFilterScope;
import io.librevents.infrastructure.configuration.source.env.model.filter.FilterConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.filter.event.sync.SyncConfigurationProperties;

public record EventFilterConfigurationProperties(
        EventFilterScope scope,
        EventSpecification specification,
        List<ContractEventStatus> statuses,
        SyncConfigurationProperties sync,
        EventFilterConfigurationAdditionalProperties configuration)
        implements FilterConfigurationProperties {}
