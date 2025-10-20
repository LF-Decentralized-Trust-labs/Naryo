package io.naryo.api.filter.common;

import io.naryo.api.filter.common.request.EventFilterVisibilityConfigurationRequest;
import org.instancio.Instancio;

public class EventFilterVisibilityConfigurationRequestBuilder {

    private Boolean visible;
    private String privacyGroupId;

    public EventFilterVisibilityConfigurationRequestBuilder withVisible(Boolean visible) {
        this.visible = visible;
        return this;
    }

    public EventFilterVisibilityConfigurationRequestBuilder withPrivacyGroupId(
            String privacyGroupId) {
        this.privacyGroupId = privacyGroupId;
        return this;
    }

    public Boolean getVisible() {
        return this.visible != null ? this.visible : Instancio.create(Boolean.class);
    }

    public String getPrivacyGroupId() {
        return this.privacyGroupId != null ? this.privacyGroupId : Instancio.create(String.class);
    }

    public EventFilterVisibilityConfigurationRequest build() {
        return new EventFilterVisibilityConfigurationRequest(getVisible(), getPrivacyGroupId());
    }
}
