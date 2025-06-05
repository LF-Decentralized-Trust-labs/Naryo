package io.librevents.infrastructure.configuration.source.env.model.filter.event.sync.block;

import java.math.BigInteger;

import io.librevents.infrastructure.configuration.source.env.model.filter.event.sync.SyncConfigurationAdditionalProperties;
import jakarta.validation.constraints.NotNull;

public record BlockSyncConfigurationAdditionalProperties(@NotNull BigInteger initialBlock)
        implements SyncConfigurationAdditionalProperties {}
