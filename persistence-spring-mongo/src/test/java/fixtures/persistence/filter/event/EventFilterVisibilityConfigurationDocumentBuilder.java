package fixtures.persistence.filter.event;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.FilterVisibilityDocument;
import org.instancio.Instancio;

public class EventFilterVisibilityConfigurationDocumentBuilder {

    private Boolean visible;
    private String privacyGroupId;

    public FilterVisibilityDocument build() {
        return new FilterVisibilityDocument(this.isVisible(), this.getPrivacyGroupId());
    }

    public EventFilterVisibilityConfigurationDocumentBuilder withVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public EventFilterVisibilityConfigurationDocumentBuilder withPrivacyGroupId(
            String privacyGroupId) {
        this.privacyGroupId = privacyGroupId;
        return this;
    }

    private boolean isVisible() {
        return this.visible == null ? Instancio.create(Boolean.class) : this.visible;
    }

    private String getPrivacyGroupId() {
        return this.privacyGroupId == null ? Instancio.create(String.class) : this.privacyGroupId;
    }
}
