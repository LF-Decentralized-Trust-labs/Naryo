package io.naryo.application.configuration.source.model.filter.event;

import io.naryo.application.configuration.source.model.MergeableDescriptor;

import java.util.Optional;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface FilterVisibilityDescriptor
        extends MergeableDescriptor<FilterVisibilityDescriptor> {

    Optional<Boolean> getVisible();

    Optional<String> getPrivacyGroupId();

    void setVisible(Optional<Boolean> visible);

    void setPrivacyGroupId(Optional<String> privacyGroupId);

    @Override
    default FilterVisibilityDescriptor merge(FilterVisibilityDescriptor other) {
        if (other == null) {
            return this;
        }

        mergeOptionals(this::setVisible, this.getVisible(), other.getVisible());
        mergeOptionals(this::setPrivacyGroupId, this.getPrivacyGroupId(), other.getPrivacyGroupId());

        return this;
    }
}
