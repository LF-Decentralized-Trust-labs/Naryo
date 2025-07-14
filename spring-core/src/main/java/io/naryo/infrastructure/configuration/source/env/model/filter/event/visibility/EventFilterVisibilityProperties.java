package io.naryo.infrastructure.configuration.source.env.model.filter.event.visibility;

import io.naryo.application.configuration.source.model.filter.event.FilterVisibilityDescriptor;
import lombok.Getter;
import lombok.Setter;

public final class EventFilterVisibilityProperties implements FilterVisibilityDescriptor {

    private @Setter boolean visible;
    private @Getter @Setter String privacyGroupId;

    private EventFilterVisibilityProperties(boolean visible, String privacyGroupId) {
        this.visible = visible;
        this.privacyGroupId = privacyGroupId;
    }

    public EventFilterVisibilityProperties(String privacyGroupId) {
        this(false, privacyGroupId);
    }

    public EventFilterVisibilityProperties() {
        this(true, null);
    }

    @Override
    public boolean getVisible() {
        return visible;
    }
}
