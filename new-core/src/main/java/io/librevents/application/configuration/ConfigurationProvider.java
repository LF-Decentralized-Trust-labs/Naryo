package io.librevents.application.configuration;

import java.util.Map;

import io.librevents.domain.broadcaster.BroadcasterType;
import io.librevents.domain.common.configuration.Configuration;

public interface ConfigurationProvider<T extends Configuration> {

    boolean supports(BroadcasterType type);

    T create(Map<String, Object> config);
}
