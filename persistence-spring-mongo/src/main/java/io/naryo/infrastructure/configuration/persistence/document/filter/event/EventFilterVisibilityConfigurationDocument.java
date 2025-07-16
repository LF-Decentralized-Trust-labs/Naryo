package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EventFilterVisibilityConfigurationDocument {

    @NotNull
    private boolean visible;

    @NotNull
    private String privacyGroupId;

    public EventFilterVisibilityConfigurationDocument(boolean visible, String privacyGroupId) {
        this.visible = visible;
        this.privacyGroupId = privacyGroupId;
    }
}
