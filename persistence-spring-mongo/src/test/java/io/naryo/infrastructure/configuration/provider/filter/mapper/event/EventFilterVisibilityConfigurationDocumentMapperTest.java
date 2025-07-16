package io.naryo.infrastructure.configuration.provider.filter.mapper.event;

import fixtures.persistence.filter.event.EventFilterVisibilityConfigurationDocumentBuilder;
import io.naryo.domain.filter.event.EventFilterVisibilityConfiguration;
import io.naryo.domain.filter.event.EventFilterVisibilityConfigurationBuilder;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterVisibilityConfigurationDocument;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class EventFilterVisibilityConfigurationDocumentMapperTest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testFromDocument(boolean visible) {
        EventFilterVisibilityConfigurationDocument document = new EventFilterVisibilityConfigurationDocumentBuilder()
            .withVisible(visible)
            .build();

        EventFilterVisibilityConfiguration expected = new EventFilterVisibilityConfigurationBuilder()
            .withVisible(document.isVisible())
            .withPrivacyGroupId(document.getPrivacyGroupId())
            .build();

        EventFilterVisibilityConfiguration result = EventFilterVisibilityConfigurationDocumentMapper.fromDocument(document);

        assertEquals(expected, result);
    }
}
