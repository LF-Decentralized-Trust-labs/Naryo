package io.naryo.application.filter.revision;

import java.util.Random;

import io.naryo.application.configuration.revision.manager.BaseConfigurationRevisionManagerTest;
import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManager;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.filter.configuration.manager.FilterConfigurationManager;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterBuilder;
import io.naryo.domain.filter.event.ContractEventFilterBuilder;
import io.naryo.domain.filter.event.GlobalEventFilterBuilder;
import io.naryo.domain.filter.transaction.TransactionFilterBuilder;

class FilterConfigurationRevisionManagerTest
        extends BaseConfigurationRevisionManagerTest<
                Filter, FilterConfigurationManager, FilterRevisionFingerprinter> {

    protected FilterConfigurationRevisionManagerTest() {
        super(Filter::getId, FilterConfigurationManager.class);
    }

    @Override
    protected ConfigurationRevisionManager<Filter> createManager(
            FilterConfigurationManager configurationManager,
            FilterRevisionFingerprinter fingerprinter,
            LiveRegistry<Filter> liveRegistry) {
        return new FilterConfigurationRevisionManager(
                configurationManager, fingerprinter, liveRegistry);
    }

    @Override
    protected FilterRevisionFingerprinter createFingerprinter() {
        return new FilterRevisionFingerprinter();
    }

    @Override
    protected Filter newItem() {
        return newBuilder().build();
    }

    @Override
    protected Filter updatedVariantOf(Filter base) {
        return newBuilder().withId(base.getId()).build();
    }

    private FilterBuilder<?, ?> newBuilder() {
        var random = new Random().nextInt(3);
        return switch (random) {
            case 0 -> new ContractEventFilterBuilder();
            case 1 -> new GlobalEventFilterBuilder();
            case 2 -> new TransactionFilterBuilder();
            default -> throw new IllegalStateException("Unexpected value: " + random);
        };
    }
}
