package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.Set;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.target.*;
import io.naryo.domain.common.Destination;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public abstract class BroadcasterTargetDocument implements BroadcasterTargetDescriptor {

    private @Nullable @NotEmpty Set<String> destinations;

    public BroadcasterTargetDocument(Set<String> destinations) {
        this.destinations = destinations;
    }

    @Override
    public Set<String> getDestinations() {
        return destinations;
    }

    @Override
    public void setDestinations(Set<String> destinations) {
        this.destinations = destinations;
    }

    public static BroadcasterTargetDocument fromDomain(BroadcasterTarget source) {
        Set<String> destinations =
                source.getDestinations().stream()
                        .map(Destination::value)
                        .collect(Collectors.toSet());
        return switch (source) {
            case BlockBroadcasterTarget blockTarget ->
                    new BlockBroadcasterTargetDocument(destinations);

            case TransactionBroadcasterTarget transactionTarget ->
                    new TransactionBroadcasterTargetDocument(destinations);

            case ContractEventBroadcasterTarget contractEventTarget ->
                    new ContractEventBroadcasterTargetDocument(destinations);

            case FilterEventBroadcasterTarget filterTarget ->
                    new FilterBroadcasterTargetDocument(destinations, filterTarget.getFilterId());

            case AllBroadcasterTarget allTarget -> new AllBroadcasterTargetDocument(destinations);

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported target type: " + source.getClass().getSimpleName());
        };
    }
}
