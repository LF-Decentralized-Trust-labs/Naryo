package io.naryo.application.configuration.source.model.node.connection.endpoint;

import java.util.Optional;

import io.naryo.application.configuration.source.model.DescriptorTestStringArgumentsProvider;
import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ConnectionEndpointDescriptorTest {

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestStringArgumentsProvider.class)
    void testMerge_times(String originalUrl, String otherUrl, String expectedUrl) {
        ConnectionEndpointDescriptor original = new DummyConnectionEndpointDescriptor();
        original.setUrl(originalUrl);
        ConnectionEndpointDescriptor other = new DummyConnectionEndpointDescriptor();
        other.setUrl(otherUrl);

        ConnectionEndpointDescriptor result = original.merge(other);

        assertEquals(Optional.ofNullable(expectedUrl), result.getUrl(), "Should merge the url");
    }

    @Setter
    public static class DummyConnectionEndpointDescriptor implements ConnectionEndpointDescriptor {
        private String url;

        @Override
        public Optional<String> getUrl() {
            return Optional.ofNullable(url);
        }
    }
}
