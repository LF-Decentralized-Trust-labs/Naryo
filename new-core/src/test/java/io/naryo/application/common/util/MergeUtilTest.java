package io.naryo.application.common.util;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class MergeUtilTest {

    @Test
    void testMergeOptionals_bothEmpty() {
        Optional<String> original = Optional.empty();
        Optional<String> other = Optional.empty();

        AtomicReference<String> result = new AtomicReference<>();
        Consumer<String> testSetter = result::set;

        MergeUtil.mergeOptionals(testSetter, original, other);

        assertNull(result.get(), "Setter should not be called when both optionals are empty");
    }

    @Test
    void testMergeOptionals_originalEmptyOtherPresent() {
        Optional<String> original = Optional.empty();
        Optional<String> other = Optional.of("test");

        AtomicReference<String> result = new AtomicReference<>();
        Consumer<String> testSetter = result::set;

        MergeUtil.mergeOptionals(testSetter, original, other);

        assertEquals(
                other.get(), result.get(), "Setter should be called with the value from 'other'");
    }

    @Test
    void testMergeOptionals_originalPresentOtherEmpty() {
        Optional<String> original = Optional.of("original");
        Optional<String> other = Optional.empty();

        AtomicReference<String> result = new AtomicReference<>();
        Consumer<String> testSetter = result::set;

        MergeUtil.mergeOptionals(testSetter, original, other);

        assertNull(
                result.get(),
                "Setter should not be called when original is present and other is empty");
    }

    @Test
    void testMergeCollections_bothEmpty() {
        List<String> original = new ArrayList<>();
        List<String> other = new ArrayList<>();

        AtomicReference<List<String>> result = new AtomicReference<>();
        Consumer<List<String>> testSetter = result::set;

        MergeUtil.mergeCollections(testSetter, original, other);

        assertNull(result.get(), "Setter should not be called when both collections are empty");
    }

    @Test
    void testMergeCollections_originalEmptyOtherNotEmpty() {
        List<String> original = new ArrayList<>();
        List<String> other = Arrays.asList("test1", "test2");

        AtomicReference<List<String>> result = new AtomicReference<>();
        Consumer<List<String>> testSetter = result::set;

        MergeUtil.mergeCollections(testSetter, original, other);

        assertEquals(other, result.get(), "Setter should be called with the value from 'other'");
    }

    @Test
    void testMergeCollections_originalNotEmptyOtherNotEmpty() {
        List<String> original = Arrays.asList("original1", "original2");
        List<String> other = Arrays.asList("test1", "test2");

        AtomicReference<List<String>> result = new AtomicReference<>();
        Consumer<List<String>> testSetter = result::set;

        MergeUtil.mergeCollections(testSetter, original, other);

        assertNull(result.get(), "Setter should not be called when original is not empty");
    }

    @Test
    void testMergeMaps_bothEmpty() {
        Map<String, String> original = new HashMap<>();
        Map<String, String> other = new HashMap<>();

        AtomicReference<Map<String, String>> result = new AtomicReference<>();
        Consumer<Map<String, String>> testSetter = result::set;

        MergeUtil.mergeMaps(testSetter, original, other);

        assertNull(result.get(), "Setter should not be called when both maps are empty");
    }

    @Test
    void testMergeMaps_originalEmptyOtherNotEmpty() {
        Map<String, String> original = new HashMap<>();
        Map<String, String> other = Instancio.createMap(String.class, String.class);

        AtomicReference<Map<String, String>> result = new AtomicReference<>();
        Consumer<Map<String, String>> testSetter = result::set;

        MergeUtil.mergeMaps(testSetter, original, other);

        assertEquals(other, result.get(), "Setter should be called with the value from 'other'");
    }

    @Test
    void testMergeMaps_originalNotEmptyOtherEmpty() {
        Map<String, String> original = Instancio.createMap(String.class, String.class);
        Map<String, String> other = new HashMap<>();

        AtomicReference<Map<String, String>> result = new AtomicReference<>();
        Consumer<Map<String, String>> testSetter = result::set;

        MergeUtil.mergeMaps(testSetter, original, other);

        assertNull(
                result.get(),
                "Setter should not be called when original is not empty and other is empty");
    }

    @Test
    void testMergeDescriptors_bothEmpty() {
        Optional<MockMergeableDescriptor> original = Optional.empty();
        Optional<MockMergeableDescriptor> other = Optional.empty();

        AtomicReference<MockMergeableDescriptor> result = new AtomicReference<>();
        Consumer<MockMergeableDescriptor> testSetter = result::set;

        MergeUtil.mergeDescriptors(testSetter, original, other);

        assertNull(result.get(), "Setter should not be called when both descriptors are empty");
    }

    @Test
    void testMergeDescriptors_originalEmptyOtherPresent() {
        Optional<MockMergeableDescriptor> original = Optional.empty();
        Optional<MockMergeableDescriptor> other = Optional.of(new MockMergeableDescriptor("test"));

        AtomicReference<MockMergeableDescriptor> result = new AtomicReference<>();
        Consumer<MockMergeableDescriptor> testSetter = result::set;

        MergeUtil.mergeDescriptors(testSetter, original, other);

        assertEquals(
                other.get(), result.get(), "Setter should be called with the value from 'other'");
    }

    @Test
    void testMergeDescriptors_bothPresentNotEqual() {
        MockMergeableDescriptor merged = new MockMergeableDescriptor("merged");
        MockMergeableDescriptor original = new MockMergeableDescriptor("original", merged);
        MockMergeableDescriptor other = new MockMergeableDescriptor("other");

        AtomicReference<MockMergeableDescriptor> result = new AtomicReference<>();
        Consumer<MockMergeableDescriptor> testSetter = result::set;

        MergeUtil.mergeDescriptors(testSetter, Optional.of(original), Optional.of(other));

        assertEquals(merged, result.get(), "Setter should be called with the merged value");
    }

    @Test
    void testMergeDescriptors_bothPresentAndEqual() {
        Optional<MockMergeableDescriptor> original =
                Optional.of(new MockMergeableDescriptor("test"));
        Optional<MockMergeableDescriptor> other = Optional.of(new MockMergeableDescriptor("test"));

        AtomicReference<MockMergeableDescriptor> result = new AtomicReference<>();
        Consumer<MockMergeableDescriptor> testSetter = result::set;

        MergeUtil.mergeDescriptors(testSetter, original, other);

        assertNull(
                result.get(),
                "Setter should not be called when both descriptors are present and equal");
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class MockMergeableDescriptor
            implements MergeableDescriptor<MockMergeableDescriptor> {
        private final String value;
        private @Nullable @EqualsAndHashCode.Exclude MockMergeableDescriptor mergeResult;

        @Override
        public MockMergeableDescriptor merge(MockMergeableDescriptor other) {
            return mergeResult != null ? mergeResult : this;
        }
    }
}
