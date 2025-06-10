package io.naryo.infrastructure.configuration.source.env.model.filter.event;

import java.util.List;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.source.env.model.filter.FilterConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.SyncConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.visibility.EventFilterVisibilityConfigurationProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EventFilterConfigurationProperties(
        @NotNull EventFilterScope scope,
        @Valid @NotNull EventSpecification specification,
        @NotEmpty List<ContractEventStatus> statuses,
        @Valid @NotNull SyncConfigurationProperties sync,
        @Valid @NotNull EventFilterVisibilityConfigurationProperties visibilityConfiguration,
        @Valid @NotNull EventFilterConfigurationAdditionalProperties configuration)
        implements FilterConfigurationProperties {

    public EventFilterConfigurationProperties(
            EventFilterScope scope,
            EventSpecification specification,
            List<ContractEventStatus> statuses,
            SyncConfigurationProperties sync,
            EventFilterVisibilityConfigurationProperties visibilityConfiguration,
            EventFilterConfigurationAdditionalProperties configuration) {
        this.scope = scope;
        this.specification = specification;
        this.statuses =
                statuses == null || statuses.isEmpty()
                        ? List.of(ContractEventStatus.values())
                        : statuses;
        this.sync = sync;
        this.visibilityConfiguration =
                visibilityConfiguration == null
                        ? new EventFilterVisibilityConfigurationProperties()
                        : visibilityConfiguration;
        this.configuration = configuration;
    }
}
