package io.naryo.application.filter.revision;

import io.naryo.application.common.revision.DefaultRevisionFingerprinter;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterNormalizer;

public final class FilterRevisionFingerprinter extends DefaultRevisionFingerprinter<Filter> {

    public FilterRevisionFingerprinter(FilterNormalizer normalizer) {
        super(normalizer);
    }

    public FilterRevisionFingerprinter() {
        super(FilterNormalizer.INSTANCE);
    }
}
