package io.naryo.application.configuration.source.model.filter.transaction;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import io.naryo.application.configuration.source.model.DescriptorTestStringArgumentsProvider;
import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.application.configuration.source.model.filter.FilterDescriptorTest;
import io.naryo.application.configuration.source.model.filter.event.global.GlobalEventFilterDescriptorTest;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.transaction.IdentifierType;
import lombok.Getter;
import lombok.Setter;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TransactionFilterDescriptorTest extends FilterDescriptorTest {

    @Override
    protected FilterDescriptor getFilterDescriptor() {
        return new DummyTransactionFilterDescriptor();
    }

    @Test
    void testMerge_otherIsNotATransactionFilter() {
        DummyTransactionFilterDescriptor original = new DummyTransactionFilterDescriptor();
        FilterDescriptor other =
                new GlobalEventFilterDescriptorTest.DummyGlobalEventFilterDescriptor();

        FilterDescriptor result = original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original TransactionFilterDescriptor when merging with a different descriptor");
    }

    @ParameterizedTest
    @MethodSource("identifierTypeParameters")
    void testMerge_identifierType(
            IdentifierType originalIdentifierType,
            IdentifierType otherIdentifierType,
            IdentifierType expectedIdentifierType) {
        TransactionFilterDescriptor original = new DummyTransactionFilterDescriptor();
        original.setIdentifierType(originalIdentifierType);
        TransactionFilterDescriptor other = new DummyTransactionFilterDescriptor();
        other.setIdentifierType(otherIdentifierType);

        TransactionFilterDescriptor result = (TransactionFilterDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedIdentifierType),
                result.getIdentifierType(),
                "Should merge the identifier type");
    }

    private static Stream<Arguments> identifierTypeParameters() {
        IdentifierType original = Instancio.create(IdentifierType.class);
        IdentifierType other = Instancio.create(IdentifierType.class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestStringArgumentsProvider.class)
    void testMerge_value(String originalValue, String otherValue, String expectedValue) {
        TransactionFilterDescriptor original = new DummyTransactionFilterDescriptor();
        original.setValue(originalValue);
        TransactionFilterDescriptor other = new DummyTransactionFilterDescriptor();
        other.setValue(otherValue);

        TransactionFilterDescriptor result = (TransactionFilterDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedValue), result.getValue(), "Should merge the value");
    }

    @ParameterizedTest
    @MethodSource("statusesParameters")
    void testMerge_statuses(
            Set<TransactionStatus> originalStatuses,
            Set<TransactionStatus> otherStatuses,
            Set<TransactionStatus> expectedStatuses) {
        TransactionFilterDescriptor original = new DummyTransactionFilterDescriptor();
        original.setStatuses(originalStatuses);
        TransactionFilterDescriptor other = new DummyTransactionFilterDescriptor();
        other.setStatuses(otherStatuses);

        TransactionFilterDescriptor result = (TransactionFilterDescriptor) original.merge(other);

        assertEquals(expectedStatuses, result.getStatuses(), "Should merge the statuses");
    }

    private static Stream<Arguments> statusesParameters() {
        Set<TransactionStatus> original = Instancio.createSet(TransactionStatus.class);
        Set<TransactionStatus> other = Instancio.createSet(TransactionStatus.class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(new HashSet<>(), other, other));
    }

    @Setter
    public static class DummyTransactionFilterDescriptor extends DummyFilterDescriptor
            implements TransactionFilterDescriptor {
        private IdentifierType identifierType;
        private String value;
        private @Getter Set<TransactionStatus> statuses = new HashSet<>();

        public DummyTransactionFilterDescriptor() {
            super();
        }

        @Override
        public Optional<IdentifierType> getIdentifierType() {
            return Optional.ofNullable(identifierType);
        }

        @Override
        public Optional<String> getValue() {
            return Optional.ofNullable(value);
        }
    }
}
