package io.naryo.api.filter.common.response;

import io.naryo.domain.filter.event.EventFilterVisibilityConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "Event filter visibility configuration")
@Getter
public class EventFilterVisibilityConfigurationResponse {

    private final @NotNull Boolean visible;
    private final String privacyGroupId;

    private EventFilterVisibilityConfigurationResponse(Boolean visible, String privacyGroupId) {
        this.visible = visible;
        this.privacyGroupId = privacyGroupId;
    }

    public static EventFilterVisibilityConfigurationResponse fromDomain(
            EventFilterVisibilityConfiguration eventFilterVisibilityConfiguration) {
        return new EventFilterVisibilityConfigurationResponse(
                eventFilterVisibilityConfiguration.isVisible(),
                eventFilterVisibilityConfiguration.getPrivacyGroupId());
    }
}
