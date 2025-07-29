package io.naryo.application.configuration.source.model.filter.event;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.application.configuration.source.model.filter.FilterDescriptorTest;
import io.naryo.application.configuration.source.model.filter.event.sync.BlockFilterSyncDescriptorTest;
import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import io.naryo.application.configuration.source.model.filter.transaction.TransactionFilterDescriptorTest;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterScope;
import lombok.Getter;
import lombok.Setter;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public abstract class EventFilterDescriptorTest extends FilterDescriptorTest {

    protected abstract EventFilterDescriptor getFilterDescriptor();

    @Test
    void testMerge_otherIsNotAnEventFilter() {
        EventFilterDescriptor original = getFilterDescriptor();
        DummyFilterDescriptor other =
                new TransactionFilterDescriptorTest.DummyTransactionFilterDescriptor();

        FilterDescriptor result = original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original EventFilterDescriptor when merging with a different descriptor");
    }

    @Test
    void testMerge_differentScope() {
        EventFilterDescriptor original =
                new DummyEventFilterDescriptor() {
                    @Override
                    public EventFilterScope getScope() {
                        return EventFilterScope.CONTRACT;
                    }
                };
        EventFilterDescriptor other =
                new DummyEventFilterDescriptor() {
                    @Override
                    public EventFilterScope getScope() {
                        return EventFilterScope.GLOBAL;
                    }
                };

        FilterDescriptor result = original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original EventFilterDescriptor when merging with a different scope");
    }

    @ParameterizedTest
    @MethodSource("statusesParameters")
    void testMerge_statuses(
            Set<ContractEventStatus> originalStatuses,
            Set<ContractEventStatus> otherStatuses,
            Set<ContractEventStatus> expectedStatuses) {
        EventFilterDescriptor original = getFilterDescriptor();
        original.setStatuses(originalStatuses);
        EventFilterDescriptor other = getFilterDescriptor();
        other.setStatuses(otherStatuses);

        EventFilterDescriptor result = (EventFilterDescriptor) original.merge(other);

        assertEquals(expectedStatuses, result.getStatuses(), "Should merge the statuses");
    }

    private static Stream<Arguments> statusesParameters() {
        Set<ContractEventStatus> original = Instancio.createSet(ContractEventStatus.class);
        Set<ContractEventStatus> other = Instancio.createSet(ContractEventStatus.class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(new HashSet<>(), other, other));
    }

    @ParameterizedTest
    @MethodSource("specificationParameters")
    void testMerge_specification(
            EventSpecificationDescriptor originalSpecification,
            EventSpecificationDescriptor otherSpecification,
            EventSpecificationDescriptor expectedSpecification) {
        EventFilterDescriptor original = getFilterDescriptor();
        original.setSpecification(originalSpecification);
        EventFilterDescriptor other = getFilterDescriptor();
        other.setSpecification(otherSpecification);

        EventFilterDescriptor result = (EventFilterDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedSpecification),
                result.getSpecification(),
                "Should merge the specification");
    }

    private static Stream<Arguments> specificationParameters() {
        EventSpecificationDescriptor original =
                Instancio.create(
                        EventSpecificationDescriptorTest.DummyEventSpecificationDescriptor.class);
        EventSpecificationDescriptor other =
                Instancio.create(
                        EventSpecificationDescriptorTest.DummyEventSpecificationDescriptor.class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }

    @ParameterizedTest
    @MethodSource("syncParameters")
    void testMerge_sync(
            FilterSyncDescriptor originalSync,
            FilterSyncDescriptor otherSync,
            FilterSyncDescriptor expectedSync) {
        EventFilterDescriptor original = getFilterDescriptor();
        original.setSync(originalSync);
        EventFilterDescriptor other = getFilterDescriptor();
        other.setSync(otherSync);

        EventFilterDescriptor result = (EventFilterDescriptor) original.merge(other);

        assertEquals(Optional.ofNullable(expectedSync), result.getSync(), "Should merge the sync");
    }

    private static Stream<Arguments> syncParameters() {
        FilterSyncDescriptor original =
                Instancio.create(
                        BlockFilterSyncDescriptorTest.DummyBlockFilterSyncDescriptor.class);
        FilterSyncDescriptor other =
                Instancio.create(
                        BlockFilterSyncDescriptorTest.DummyBlockFilterSyncDescriptor.class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }

    @ParameterizedTest
    @MethodSource("visibilityParameters")
    void testMerge_visibility(
            FilterVisibilityDescriptor originalVisibility,
            FilterVisibilityDescriptor otherVisibility,
            FilterVisibilityDescriptor expectedVisibility) {
        EventFilterDescriptor original = getFilterDescriptor();
        original.setVisibility(originalVisibility);
        EventFilterDescriptor other = getFilterDescriptor();
        other.setVisibility(otherVisibility);

        EventFilterDescriptor result = (EventFilterDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedVisibility),
                result.getVisibility(),
                "Should merge the visibility");
    }

    private static Stream<Arguments> visibilityParameters() {
        FilterVisibilityDescriptor original =
                Instancio.create(
                        FilterVisibilityDescriptorTest.DummyFilterVisibilityDescriptor.class);
        FilterVisibilityDescriptor other =
                Instancio.create(
                        FilterVisibilityDescriptorTest.DummyFilterVisibilityDescriptor.class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }

    @Setter
    public abstract static class DummyEventFilterDescriptor extends DummyFilterDescriptor
            implements EventFilterDescriptor {
        private @Getter Set<ContractEventStatus> statuses = new HashSet<>();
        private EventSpecificationDescriptor specification;
        private FilterSyncDescriptor sync;
        private FilterVisibilityDescriptor visibility;

        @Override
        public Optional<EventSpecificationDescriptor> getSpecification() {
            return Optional.ofNullable(specification);
        }

        @Override
        public Optional<FilterSyncDescriptor> getSync() {
            return Optional.ofNullable(sync);
        }

        @Override
        public Optional<FilterVisibilityDescriptor> getVisibility() {
            return Optional.ofNullable(visibility);
        }
    }
}
