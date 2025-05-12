package io.librevents.application.broadcaster.configuration;

import java.util.Set;

import io.librevents.application.broadcaster.BroadcasterWrapper;
import io.librevents.application.configuration.ConfigurationManager;
import io.librevents.domain.broadcaster.BroadcasterTarget;
import io.librevents.domain.broadcaster.BroadcasterType;

public interface BroadcasterConfigurationManager extends ConfigurationManager<BroadcasterType> {

    void registerBroadcaster(String configurationName, BroadcasterTarget target);

    Set<BroadcasterWrapper> wrappers();
}
