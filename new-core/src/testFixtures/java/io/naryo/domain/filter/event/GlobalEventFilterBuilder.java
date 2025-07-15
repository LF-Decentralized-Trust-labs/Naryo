package io.naryo.domain.filter.event;

import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class GlobalEventFilterBuilder
    extends EventFilterBuilder<GlobalEventFilterBuilder, GlobalEventFilter> {

    @Override
    public GlobalEventFilterBuilder self() {
        return this;
    }

    @Override
    public GlobalEventFilter build() {
        InstancioApi<GlobalEventFilter> builder = Instancio.of(GlobalEventFilter.class);

        return super.buildBase(builder).create();
    }
}
