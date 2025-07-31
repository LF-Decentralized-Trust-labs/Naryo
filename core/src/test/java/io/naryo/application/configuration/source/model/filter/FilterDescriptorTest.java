package io.naryo.application.configuration.source.model.filter;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.DescriptorTestStringArgumentsProvider;
import io.naryo.application.configuration.source.model.DescriptorTestUuidArgumentsProvider;
import io.naryo.domain.filter.FilterType;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public abstract class FilterDescriptorTest {

    protected abstract FilterDescriptor getFilterDescriptor();

    @Test
    void testMerge_differentFilterType() {
        FilterDescriptor original =
                new DummyFilterDescriptor() {
                    @Override
                    public FilterType getType() {
                        return FilterType.EVENT;
                    }
                };
        FilterDescriptor other =
                new DummyFilterDescriptor() {
                    @Override
                    public FilterType getType() {
                        return FilterType.TRANSACTION;
                    }
                };

        FilterDescriptor result = original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original FilterDescriptor when merging with a different FilterType");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestStringArgumentsProvider.class)
    void testMerge_name(String originalName, String otherName, String expectedName) {
        FilterDescriptor original = this.getFilterDescriptor();
        original.setName(originalName);
        FilterDescriptor other = this.getFilterDescriptor();
        other.setName(otherName);

        FilterDescriptor result = original.merge(other);

        assertEquals(Optional.ofNullable(expectedName), result.getName(), "Should merge the name");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestUuidArgumentsProvider.class)
    void testMerge_nodeId(UUID originalNodeId, UUID otherNodeId, UUID expectedNodeId) {
        FilterDescriptor original = this.getFilterDescriptor();
        original.setNodeId(originalNodeId);
        FilterDescriptor other = this.getFilterDescriptor();
        other.setNodeId(otherNodeId);

        FilterDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedNodeId),
                result.getNodeId(),
                "Should merge the node id");
    }

    public abstract static class DummyFilterDescriptor implements FilterDescriptor {
        private final @Getter UUID id;
        private @Setter String name;
        private @Setter UUID nodeId;

        protected DummyFilterDescriptor() {
            this.id = UUID.randomUUID();
        }

        @Override
        public Optional<String> getName() {
            return Optional.ofNullable(name);
        }

        @Override
        public Optional<UUID> getNodeId() {
            return Optional.ofNullable(nodeId);
        }
    }
}
