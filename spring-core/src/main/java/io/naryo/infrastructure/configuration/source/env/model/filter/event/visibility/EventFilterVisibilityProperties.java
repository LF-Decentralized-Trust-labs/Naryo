package io.naryo.infrastructure.configuration.source.env.model.filter.event.visibility;

import io.naryo.application.configuration.source.model.filter.event.FilterVisibilityDescriptor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public final class EventFilterVisibilityProperties implements FilterVisibilityDescriptor {

    private Optional<Boolean> visible;
    private Optional<String> privacyGroupId;

    public EventFilterVisibilityProperties() {
        this.visible = Optional.empty();
        this.privacyGroupId = Optional.empty();
    }

    public EventFilterVisibilityProperties(Optional<Boolean> visible,
                                           Optional<String> privacyGroupId) {
        this.visible = visible;
        this.privacyGroupId = privacyGroupId;
    }

}
