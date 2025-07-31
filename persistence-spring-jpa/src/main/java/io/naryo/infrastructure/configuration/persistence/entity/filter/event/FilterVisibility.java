package io.naryo.infrastructure.configuration.persistence.entity.filter.event;

import java.util.Optional;

import io.naryo.application.configuration.source.model.filter.event.FilterVisibilityDescriptor;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Setter
@NoArgsConstructor
public class FilterVisibility implements FilterVisibilityDescriptor {

    private @Column(name = "visibility_visible") @Nullable Boolean visible;

    private @Column(name = "visibility_privacy_group_id") @Nullable String privacyGroupId;

    public FilterVisibility(Boolean visible, String privacyGroupId) {
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
