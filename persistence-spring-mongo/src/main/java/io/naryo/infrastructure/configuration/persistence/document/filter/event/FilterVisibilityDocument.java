package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import java.util.Optional;

import io.naryo.application.configuration.source.model.filter.event.FilterVisibilityDescriptor;
import jakarta.annotation.Nullable;
import lombok.Setter;

@Setter
public class FilterVisibilityDocument implements FilterVisibilityDescriptor {

    @Nullable private Boolean visible;

    @Nullable private String privacyGroupId;

    public FilterVisibilityDocument(boolean visible, String privacyGroupId) {
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
