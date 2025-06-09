package io.naryo.infrastructure.configuration.source.env.model.filter.transaction;

import java.util.List;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.infrastructure.configuration.source.env.model.filter.FilterConfigurationProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TransactionFilterConfigurationProperties(
        @NotNull IdentifierType identifierType,
        @NotBlank String value,
        @NotEmpty List<TransactionStatus> statuses)
        implements FilterConfigurationProperties {

    public TransactionFilterConfigurationProperties {
        if (statuses == null || statuses.isEmpty()) {
            statuses = List.of(TransactionStatus.values());
        }
    }
}
