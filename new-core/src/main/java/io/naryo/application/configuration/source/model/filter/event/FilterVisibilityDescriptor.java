package io.naryo.application.configuration.source.model.filter.event;

import io.naryo.application.configuration.source.model.MergeableDescriptor;

public interface FilterVisibilityDescriptor
        extends MergeableDescriptor<FilterVisibilityDescriptor> {

    boolean getVisible();

    String getPrivacyGroupId();

    void setVisible(boolean visible);

    void setPrivacyGroupId(String privacyGroupId);

    @Override
    default FilterVisibilityDescriptor merge(FilterVisibilityDescriptor other) {
        if (other == null) {
            return this;
        }

        if (this.getVisible() != other.getVisible()) {
            this.setVisible(other.getVisible());
        }

        if (!this.getPrivacyGroupId().equals(other.getPrivacyGroupId())) {
            this.setPrivacyGroupId(other.getPrivacyGroupId());
        }

        return this;
    }
}
