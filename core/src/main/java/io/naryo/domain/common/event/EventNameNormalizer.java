package io.naryo.domain.common.event;

import io.naryo.domain.normalization.Normalizer;

public final class EventNameNormalizer implements Normalizer<EventName> {

    public static final EventNameNormalizer INSTANCE = new EventNameNormalizer();

    @Override
    public EventName normalize(EventName in) {
        if (in == null || in.value() == null) {
            return null;
        }

        return new EventName(in.value().trim().toLowerCase().replaceAll("[^a-z0-9_-]", ""));
    }
}
