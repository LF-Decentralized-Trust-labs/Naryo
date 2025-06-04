package io.librevents.infrastructure.configuration.source.env.model.filter.event.sync.block;

import java.math.BigInteger;

import io.librevents.infrastructure.configuration.source.env.model.filter.event.sync.SyncConfigurationAdditionalProperties;

public record BlockSyncConfigurationAdditionalProperties(BigInteger initialBlock)
        implements SyncConfigurationAdditionalProperties {}
