package io.naryo.domain.filter.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)
public class EventFilterVisibilityConfiguration {

    private final boolean visible;
    private final String privacyGroupId;

    private EventFilterVisibilityConfiguration(boolean visible, String privacyGroupId) {
        this.visible = visible;
        this.privacyGroupId = privacyGroupId;
    }

    public static EventFilterVisibilityConfiguration invisible(String privacyGroupId) {
        return new EventFilterVisibilityConfiguration(false, privacyGroupId);
    }

    public static EventFilterVisibilityConfiguration visible() {
        return new EventFilterVisibilityConfiguration(true, null);
    }
}
