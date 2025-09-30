package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.target;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.model.broadcaster.target.*;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.target.*;
import io.naryo.domain.common.Destination;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "broadcaster_target")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "target_type")
@NoArgsConstructor
@Getter
public abstract class BroadcasterTargetEntity implements BroadcasterTargetDescriptor {

    private @Id @Column(name = "id") @GeneratedValue(strategy = GenerationType.UUID) UUID id;

    private @ElementCollection(fetch = FetchType.EAGER) @CollectionTable(
            name = "broadcaster_target_destination",
            joinColumns = @JoinColumn(name = "broadcaster_target_id")) @Column(
            name = "destination",
            nullable = false) Set<String> destinations;

    public BroadcasterTargetEntity(Set<String> destinations) {
        this.destinations = destinations;
    }

    @Override
    public Set<String> getDestinations() {
        return this.destinations != null ? destinations : Set.of();
    }

    @Override
    public void setDestinations(Set<String> destinations) {
        this.destinations = new HashSet<>(destinations);
    }

    public static BroadcasterTargetEntity fromDomain(BroadcasterTarget domain) {
        return switch (domain) {
            case BlockBroadcasterTarget blockTarget ->
                    new BlockBroadcasterTargetEntity(
                            blockTarget.getDestinations().stream()
                                    .map(Destination::value)
                                    .collect(Collectors.toSet()));

            case TransactionBroadcasterTarget transactionTarget ->
                    new TransactionBroadcasterTargetEntity(
                            transactionTarget.getDestinations().stream()
                                    .map(Destination::value)
                                    .collect(Collectors.toSet()));

            case ContractEventBroadcasterTarget contractEventTarget ->
                    new ContractEventBroadcasterTargetEntity(
                            contractEventTarget.getDestinations().stream()
                                    .map(Destination::value)
                                    .collect(Collectors.toSet()));

            case FilterEventBroadcasterTarget filterTarget ->
                    new FilterBroadcasterTargetEntity(
                            filterTarget.getDestinations().stream()
                                    .map(Destination::value)
                                    .collect(Collectors.toSet()),
                            filterTarget.getFilterId());

            case AllBroadcasterTarget allTarget ->
                    new AllBroadcasterTargetEntity(
                            allTarget.getDestinations().stream()
                                    .map(Destination::value)
                                    .collect(Collectors.toSet()));

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported target type: " + domain.getClass().getSimpleName());
        };
    }
}
