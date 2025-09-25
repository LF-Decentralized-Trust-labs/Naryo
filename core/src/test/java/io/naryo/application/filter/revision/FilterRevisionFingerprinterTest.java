package io.naryo.application.filter.revision;

import java.util.Random;

import io.naryo.application.common.revision.BaseRevisionFingerprinterTest;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterBuilder;
import io.naryo.domain.filter.event.ContractEventFilterBuilder;
import io.naryo.domain.filter.event.GlobalEventFilterBuilder;
import io.naryo.domain.filter.transaction.TransactionFilterBuilder;
import org.junit.jupiter.api.BeforeEach;

class FilterRevisionFingerprinterTest extends BaseRevisionFingerprinterTest<Filter> {

    protected FilterRevisionFingerprinterTest() {
        super(Filter::getId);
    }

    @BeforeEach
    void setUp() {
        fingerprinter = new FilterRevisionFingerprinter();
    }

    @Override
    protected Filter createInput() {
        var random = new Random().nextInt(3);
        FilterBuilder<?, ?> builder =
                switch (random) {
                    case 0 -> new ContractEventFilterBuilder();
                    case 1 -> new GlobalEventFilterBuilder();
                    case 2 -> new TransactionFilterBuilder();
                    default -> throw new IllegalStateException("Unexpected value: " + random);
                };

        return builder.build();
    }
}
