package io.naryo.domain.broadcaster;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import io.naryo.domain.broadcaster.target.*;
import io.naryo.domain.common.Destination;
import io.naryo.domain.common.normalization.NormalizationUtil;
import io.naryo.domain.normalization.Normalizer;

public final class BroadcasterNormalizer implements Normalizer<Broadcaster> {

    public static final BroadcasterNormalizer INSTANCE = new BroadcasterNormalizer();

    public BroadcasterNormalizer() {}

    @Override
    public Broadcaster normalize(Broadcaster in) {
        if (in == null) {
            return null;
        }

        var builder = in.toBuilder();

        BroadcasterTarget target =
                switch (in.getTarget()) {
                    case AllBroadcasterTarget allIn ->
                            allIn.toBuilder()
                                    .destinations(normalize(allIn.getDestinations()))
                                    .build();
                    case BlockBroadcasterTarget blockIn ->
                            blockIn.toBuilder()
                                    .destinations(normalize(blockIn.getDestinations()))
                                    .build();
                    case ContractEventBroadcasterTarget ceIn ->
                            ceIn.toBuilder()
                                    .destinations(normalize(ceIn.getDestinations()))
                                    .build();
                    case TransactionBroadcasterTarget taIn ->
                            taIn.toBuilder()
                                    .destinations(normalize(taIn.getDestinations()))
                                    .build();
                    case FilterEventBroadcasterTarget feIn ->
                            feIn.toBuilder()
                                    .destinations(normalize(feIn.getDestinations()))
                                    .build();
                    default ->
                            throw new IllegalStateException("Unexpected value: " + in.getTarget());
                };

        return builder.target(target).build();
    }

    List<Destination> normalize(List<Destination> in) {
        if (in == null) return List.of();
        return in.stream()
                .map(this::normalize)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Destination::value, Comparator.naturalOrder()))
                .toList();
    }

    Destination normalize(Destination in) {
        if (in == null || in.value() == null) {
            return null;
        }
        return new Destination(NormalizationUtil.normalize(in.value()));
    }
}
