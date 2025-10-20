package io.naryo.api.filter.common.request;

import io.naryo.domain.filter.event.EventFilterVisibilityConfiguration;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EventFilterVisibilityConfigurationRequest {

    private final @NotNull Boolean visible;
    private final String privacyGroupId;

    public EventFilterVisibilityConfigurationRequest(Boolean visible, String privacyGroupId) {
        this.visible = visible;
        this.privacyGroupId = privacyGroupId;
    }

    public EventFilterVisibilityConfiguration toDomain() {
        if (visible) {
            return EventFilterVisibilityConfiguration.visible();
        } else {
            return EventFilterVisibilityConfiguration.invisible(privacyGroupId);
        }
    }
}
