package io.naryo.infrastructure.configuration.source.env.model.filter.event.visibility;

import io.naryo.application.configuration.source.model.filter.event.FilterVisibilityDescriptor;
import jakarta.annotation.Nullable;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@NoArgsConstructor
@Setter
public final class EventFilterVisibilityProperties implements FilterVisibilityDescriptor {

    private @Nullable Boolean visible;
    private @Nullable String privacyGroupId;

    public EventFilterVisibilityProperties(Boolean visible, String privacyGroupId) {
        this.visible = visible;
        this.privacyGroupId = privacyGroupId;
    }

    @Override
    public Optional<Boolean> getVisible() {
        return Optional.ofNullable(visible);
    }

    @Override
    public Optional<String> getPrivacyGroupId() {
        return Optional.ofNullable(privacyGroupId);
    }
}
