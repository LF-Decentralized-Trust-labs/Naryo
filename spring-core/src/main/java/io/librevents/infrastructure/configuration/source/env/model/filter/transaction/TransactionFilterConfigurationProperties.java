package io.librevents.infrastructure.configuration.source.env.model.filter.transaction;

import java.util.List;

import io.librevents.domain.common.TransactionStatus;
import io.librevents.domain.filter.transaction.IdentifierType;
import io.librevents.infrastructure.configuration.source.env.model.filter.FilterConfigurationProperties;

public record TransactionFilterConfigurationProperties(
        IdentifierType identifierType, String value, List<TransactionStatus> statuses)
        implements FilterConfigurationProperties {}
