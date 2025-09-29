package io.naryo.application.filter.revision;

import io.naryo.application.configuration.revision.manager.DefaultConfigurationRevisionManager;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.filter.configuration.manager.FilterConfigurationManager;
import io.naryo.domain.filter.Filter;

public final class FilterConfigurationRevisionManager
        extends DefaultConfigurationRevisionManager<Filter> {

    public FilterConfigurationRevisionManager(
            FilterConfigurationManager configurationManager,
            FilterRevisionFingerprinter fingerprinter,
            LiveRegistry<Filter> live) {
        super(configurationManager, fingerprinter, Filter::getId, live);
    }
}
