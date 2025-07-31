package io.naryo.application.configuration.source.model.filter.event;

import java.util.Optional;

import io.naryo.application.configuration.source.model.DescriptorTestIntegerArgumentsProvider;
import io.naryo.application.configuration.source.model.DescriptorTestStringArgumentsProvider;
import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EventSpecificationDescriptorTest {

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestStringArgumentsProvider.class)
    void testMerge_signature(
            String originalSignature, String otherSignature, String expectedSignature) {
        EventSpecificationDescriptor original = new DummyEventSpecificationDescriptor();
        original.setSignature(originalSignature);
        EventSpecificationDescriptor other = new DummyEventSpecificationDescriptor();
        other.setSignature(otherSignature);

        EventSpecificationDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedSignature),
                result.getSignature(),
                "Should merge the signature");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestIntegerArgumentsProvider.class)
    void testMerge_correlationId(
            Integer originalCorrelationId,
            Integer otherCorrelationId,
            Integer expectedCorrelationId) {
        EventSpecificationDescriptor original = new DummyEventSpecificationDescriptor();
        original.setCorrelationId(originalCorrelationId);
        EventSpecificationDescriptor other = new DummyEventSpecificationDescriptor();
        other.setCorrelationId(otherCorrelationId);

        EventSpecificationDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedCorrelationId),
                result.getCorrelationId(),
                "Should merge the correlation id");
    }

    @Setter
    public static class DummyEventSpecificationDescriptor implements EventSpecificationDescriptor {
        private String signature;
        private Integer correlationId;

        @Override
        public Optional<String> getSignature() {
            return Optional.ofNullable(signature);
        }

        @Override
        public Optional<Integer> getCorrelationId() {
            return Optional.ofNullable(correlationId);
        }
    }
}
