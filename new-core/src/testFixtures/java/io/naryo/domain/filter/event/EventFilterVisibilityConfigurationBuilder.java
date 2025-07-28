package io.naryo.domain.filter.event;

import io.naryo.domain.DomainBuilder;
import org.instancio.Instancio;

public class EventFilterVisibilityConfigurationBuilder
        implements DomainBuilder<
                EventFilterVisibilityConfigurationBuilder, EventFilterVisibilityConfiguration> {

    private Boolean visible;
    private String privacyGroupId;

    @Override
    public EventFilterVisibilityConfigurationBuilder self() {
        return this;
    }

    @Override
    public EventFilterVisibilityConfiguration build() {
        return this.isVisible()
                ? EventFilterVisibilityConfiguration.visible()
                : EventFilterVisibilityConfiguration.invisible(this.getPrivacyGroupId());
    }

    public EventFilterVisibilityConfigurationBuilder withVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public EventFilterVisibilityConfigurationBuilder withPrivacyGroupId(String privacyGroupId) {
        this.privacyGroupId = privacyGroupId;
        return this;
    }

    private boolean isVisible() {
        return this.visible == null ? Instancio.create(Boolean.class) : this.visible;
    }

    private String getPrivacyGroupId() {
        return this.privacyGroupId == null ? Instancio.create(String.class) : this.privacyGroupId;
    }
}
