package io.naryo.application.configuration.source.model.broadcaster.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.domain.broadcaster.BroadcasterType;
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
public class BroadcasterConfigurationDescriptorTest {

    @Test
    void testMerge_differentType() {
        BroadcasterConfigurationDescriptor original =
                new DummyBroadcasterConfigurationDescriptor("original");
        BroadcasterConfigurationDescriptor other =
                new DummyBroadcasterConfigurationDescriptor("other");

        BroadcasterConfigurationDescriptor result = original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original BroadcasterConfigurationDescriptor when merging with a different Type");
    }

    @ParameterizedTest
    @MethodSource("cacheParameters")
    void testMerge_cache(
            BroadcasterCacheConfigurationDescriptor originalCache,
            BroadcasterCacheConfigurationDescriptor otherCache,
            BroadcasterCacheConfigurationDescriptor expectedCache) {
        BroadcasterConfigurationDescriptor original = new DummyBroadcasterConfigurationDescriptor();
        original.setCache(originalCache);
        BroadcasterConfigurationDescriptor other = new DummyBroadcasterConfigurationDescriptor();
        other.setCache(otherCache);

        BroadcasterConfigurationDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedCache), result.getCache(), "Should merge the cache");
    }

    private static Stream<Arguments> cacheParameters() {
        BroadcasterCacheConfigurationDescriptor original =
                Instancio.create(
                        BroadcasterCacheConfigurationDescriptorTest
                                .DummyBroadcasterCacheConfigurationDescriptor.class);
        BroadcasterCacheConfigurationDescriptor other =
                Instancio.create(
                        BroadcasterCacheConfigurationDescriptorTest
                                .DummyBroadcasterCacheConfigurationDescriptor.class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(original, null, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }

    @ParameterizedTest
    @MethodSource("additionalPropertiesParameters")
    void testMerge_additionalProperties(
            Map<String, Object> originalAdditionalProperties,
            Map<String, Object> otherAdditionalProperties,
            Map<String, Object> expectedAdditionalProperties) {
        BroadcasterConfigurationDescriptor original = new DummyBroadcasterConfigurationDescriptor();
        original.setAdditionalProperties(originalAdditionalProperties);
        BroadcasterConfigurationDescriptor other = new DummyBroadcasterConfigurationDescriptor();
        other.setAdditionalProperties(otherAdditionalProperties);

        BroadcasterConfigurationDescriptor result = original.merge(other);

        assertEquals(
                expectedAdditionalProperties,
                result.getAdditionalProperties(),
                "Should merge the additional properties");
    }

    private static Stream<Arguments> additionalPropertiesParameters() {
        Map<String, Object> empty = new HashMap<>();
        Map<String, Object> filled = Instancio.createMap(String.class, Object.class);

        return Stream.of(
                Arguments.of(filled, filled, filled),
                Arguments.of(filled, empty, filled),
                Arguments.of(empty, filled, filled),
                Arguments.of(empty, empty, empty));
    }

    @ParameterizedTest
    @MethodSource("propertiesSchemaParameters")
    void testMerge_propertiesSchema(
            ConfigurationSchema originalPropertiesSchema,
            ConfigurationSchema otherPropertiesSchema,
            ConfigurationSchema expectedPropertiesSchema) {
        BroadcasterConfigurationDescriptor original = new DummyBroadcasterConfigurationDescriptor();
        original.setPropertiesSchema(originalPropertiesSchema);
        BroadcasterConfigurationDescriptor other = new DummyBroadcasterConfigurationDescriptor();
        other.setPropertiesSchema(otherPropertiesSchema);

        BroadcasterConfigurationDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedPropertiesSchema),
                result.getPropertiesSchema(),
                "Should merge the properties schema");
    }

    private static Stream<Arguments> propertiesSchemaParameters() {
        ConfigurationSchema original = Instancio.create(ConfigurationSchema.class);
        ConfigurationSchema other = Instancio.create(ConfigurationSchema.class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(original, null, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }

    public static class DummyBroadcasterConfigurationDescriptor
            implements BroadcasterConfigurationDescriptor {
        private final @Getter UUID id;
        private final @Getter BroadcasterType type;
        private @Setter BroadcasterCacheConfigurationDescriptor cache;
        private @Setter Map<String, Object> additionalProperties;
        private @Setter ConfigurationSchema propertiesSchema;

        protected DummyBroadcasterConfigurationDescriptor() {
            this("type");
        }

        protected DummyBroadcasterConfigurationDescriptor(String type) {
            this.id = UUID.randomUUID();
            this.type = () -> type;
        }

        @Override
        public Optional<BroadcasterCacheConfigurationDescriptor> getCache() {
            return Optional.ofNullable(cache);
        }

        @Override
        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties == null ? Map.of() : additionalProperties;
        }

        @Override
        public Optional<ConfigurationSchema> getPropertiesSchema() {
            return Optional.ofNullable(propertiesSchema);
        }
    }
}
