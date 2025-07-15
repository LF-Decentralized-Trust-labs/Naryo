package io.naryo.infrastructure.configuration.provider.filter.mapper.event;

import io.naryo.domain.filter.event.EventFilterVisibilityConfiguration;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterVisibilityConfigurationDocument;

public abstract class EventFilterVisibilityConfigurationDocumentMapper {

    public static EventFilterVisibilityConfiguration fromDocument(EventFilterVisibilityConfigurationDocument props) {
        if (!props.isVisible()) {
            return EventFilterVisibilityConfiguration.invisible(props.getPrivacyGroupId());
        }

        return EventFilterVisibilityConfiguration.visible();
    }
}
