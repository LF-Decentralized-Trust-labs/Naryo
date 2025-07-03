package io.naryo.infrastructure.configuration.source.env.model.filter.event.visibility;

public record EventFilterVisibilityConfigurationProperties(boolean visible, String privacyGroupId) {

    public EventFilterVisibilityConfigurationProperties() {
        this(true, null);
    }
}
