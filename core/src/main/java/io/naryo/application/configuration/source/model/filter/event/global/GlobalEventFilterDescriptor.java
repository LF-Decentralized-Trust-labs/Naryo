package io.naryo.application.configuration.source.model.filter.event.global;

import io.naryo.application.configuration.source.model.filter.event.EventFilterDescriptor;
import io.naryo.domain.filter.event.EventFilterScope;

public interface GlobalEventFilterDescriptor extends EventFilterDescriptor {

    @Override
    default EventFilterScope getScope() {
        return EventFilterScope.GLOBAL;
    }
}
