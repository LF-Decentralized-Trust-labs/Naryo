package io.naryo.domain.configuration.store.active;

import java.util.Comparator;
import java.util.Map;

import io.naryo.domain.common.Destination;
import io.naryo.domain.common.normalization.NormalizationUtil;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.active.feature.event.block.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.event.block.EventStoreTarget;
import io.naryo.domain.configuration.store.active.feature.filter.FilterStoreConfiguration;
import io.naryo.domain.normalization.Normalizer;

public abstract class BaseActiveStoreNormalizer<T extends ActiveStoreConfiguration>
        implements Normalizer<T> {

    protected Map<StoreFeatureType, StoreFeatureConfiguration> normalize(
            Map<StoreFeatureType, StoreFeatureConfiguration> in) {
        return in.entrySet().stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                Map.Entry::getKey, e -> normalizeFeature(e.getValue())));
    }

    protected StoreFeatureConfiguration normalizeFeature(StoreFeatureConfiguration in) {
        return switch (in) {
            case BlockEventStoreConfiguration blockIn ->
                    blockIn.toBuilder()
                            .targets(
                                    NormalizationUtil.normalize(
                                            blockIn.getTargets(),
                                            Comparator.comparing(EventStoreTarget::type)))
                            .build();
            case FilterStoreConfiguration filterIn ->
                    filterIn.toBuilder()
                            .destination(
                                    new Destination(
                                            NormalizationUtil.normalize(
                                                    filterIn.getDestination().value())))
                            .build();
            default -> throw new IllegalStateException("Unexpected value: " + in);
        };
    }
}
