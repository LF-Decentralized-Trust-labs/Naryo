package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import io.naryo.application.configuration.source.model.filter.event.FilterVisibilityDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventFilterVisibilityConfigurationDocument implements FilterVisibilityDescriptor {

    @NotNull
    private boolean visible;

    @NotNull
    private String privacyGroupId;

    public EventFilterVisibilityConfigurationDocument(boolean visible, String privacyGroupId) {
        this.visible = visible;
        this.privacyGroupId = privacyGroupId;
    }

    public boolean getVisible() {
        return visible;
    }

}
