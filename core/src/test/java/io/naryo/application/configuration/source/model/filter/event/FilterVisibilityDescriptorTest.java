package io.naryo.application.configuration.source.model.filter.event;

import java.util.Optional;

import io.naryo.application.configuration.source.model.DescriptorTestBooleanArgumentsProvider;
import io.naryo.application.configuration.source.model.DescriptorTestStringArgumentsProvider;
import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FilterVisibilityDescriptorTest {

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestBooleanArgumentsProvider.class)
    void testMerge_visible(Boolean originalVisible, Boolean otherVisible, Boolean expectedVisible) {
        FilterVisibilityDescriptor original = new DummyFilterVisibilityDescriptor();
        original.setVisible(originalVisible);
        FilterVisibilityDescriptor other = new DummyFilterVisibilityDescriptor();
        other.setVisible(otherVisible);

        FilterVisibilityDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedVisible),
                result.getVisible(),
                "Should merge the visible");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestStringArgumentsProvider.class)
    void testMerge_privacyGroupId(
            String originalPrivacyGroupId,
            String otherPrivacyGroupId,
            String expectedPrivacyGroupId) {
        FilterVisibilityDescriptor original = new DummyFilterVisibilityDescriptor();
        original.setPrivacyGroupId(originalPrivacyGroupId);
        FilterVisibilityDescriptor other = new DummyFilterVisibilityDescriptor();
        other.setPrivacyGroupId(otherPrivacyGroupId);

        FilterVisibilityDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedPrivacyGroupId),
                result.getPrivacyGroupId(),
                "Should merge the privacy group filterId");
    }

    @Setter
    public static class DummyFilterVisibilityDescriptor implements FilterVisibilityDescriptor {
        private Boolean visible;
        private String privacyGroupId;

        @Override
        public Optional<Boolean> getVisible() {
            return Optional.ofNullable(visible);
        }

        @Override
        public Optional<String> getPrivacyGroupId() {
            return Optional.ofNullable(privacyGroupId);
        }
    }
}
