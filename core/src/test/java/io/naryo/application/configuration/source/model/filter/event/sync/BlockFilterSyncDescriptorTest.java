package io.naryo.application.configuration.source.model.filter.event.sync;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.configuration.source.model.DescriptorTestBigIntegerArgumentsProvider;
import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BlockFilterSyncDescriptorTest extends FilterSyncDescriptorTest {

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestBigIntegerArgumentsProvider.class)
    void testMerge_correlationId(
            BigInteger originalInitialBlock,
            BigInteger otherInitialBlock,
            BigInteger expectedInitialBlock) {
        BlockFilterSyncDescriptor original = new DummyBlockFilterSyncDescriptor();
        original.setInitialBlock(originalInitialBlock);
        BlockFilterSyncDescriptor other = new DummyBlockFilterSyncDescriptor();
        other.setInitialBlock(otherInitialBlock);

        BlockFilterSyncDescriptor result = (BlockFilterSyncDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedInitialBlock),
                result.getInitialBlock(),
                "Should merge the correlation id");
    }

    @Setter
    public static class DummyBlockFilterSyncDescriptor extends DummyFilterSyncDescriptor
            implements BlockFilterSyncDescriptor {
        private BigInteger initialBlock;

        @Override
        public Optional<BigInteger> getInitialBlock() {
            return Optional.ofNullable(initialBlock);
        }
    }
}
