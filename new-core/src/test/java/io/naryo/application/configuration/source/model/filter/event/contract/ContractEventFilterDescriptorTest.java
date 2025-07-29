package io.naryo.application.configuration.source.model.filter.event.contract;

import java.util.Optional;

import io.naryo.application.configuration.source.model.DescriptorTestStringArgumentsProvider;
import io.naryo.application.configuration.source.model.filter.event.EventFilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.EventFilterDescriptorTest;
import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ContractEventFilterDescriptorTest extends EventFilterDescriptorTest {

    @Override
    protected EventFilterDescriptor getFilterDescriptor() {
        return new DummyContractEventFilterDescriptor();
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestStringArgumentsProvider.class)
    void testMerge_address(String originalAddress, String otherAddress, String expectedAddress) {
        DummyContractEventFilterDescriptor original = new DummyContractEventFilterDescriptor();
        original.setAddress(originalAddress);
        DummyContractEventFilterDescriptor other = new DummyContractEventFilterDescriptor();
        other.setAddress(otherAddress);

        DummyContractEventFilterDescriptor result =
                (DummyContractEventFilterDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedAddress),
                result.getAddress(),
                "Should merge the address");
    }

    @Setter
    protected static class DummyContractEventFilterDescriptor extends DummyEventFilterDescriptor
            implements ContractEventFilterDescriptor {
        private String address;

        @Override
        public Optional<String> getAddress() {
            return Optional.ofNullable(address);
        }
    }
}
