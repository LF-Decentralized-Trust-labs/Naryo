package io.naryo.application.configuration.source.model.filter.event;

import java.util.Optional;

import io.naryo.application.configuration.source.model.MergeableDescriptor;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface FilterVisibilityDescriptor
        extends MergeableDescriptor<FilterVisibilityDescriptor> {

    Optional<Boolean> getVisible();

    Optional<String> getPrivacyGroupId();

    void setVisible(Boolean visible);

    void setPrivacyGroupId(String privacyGroupId);

    @Override
    default FilterVisibilityDescriptor merge(FilterVisibilityDescriptor other) {
        if (other == null) {
            return this;
        }

        mergeOptionals(this::setVisible, this.getVisible(), other.getVisible());
        mergeOptionals(
                this::setPrivacyGroupId, this.getPrivacyGroupId(), other.getPrivacyGroupId());

        return this;
    }
}
