package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.target;

import java.util.List;

import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
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

    private @Id @Column(name = "id") @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    private @ElementCollection(fetch = FetchType.EAGER) @CollectionTable(
            name = "broadcaster_target_destination") @Column(name = "destination") List<String>
            destinations;

    public BroadcasterTargetEntity(List<String> destinations) {
        this.destinations = destinations;
    }

    @Override
    public List<String> getDestinations() {
        return this.destinations != null ? destinations : List.of();
    }

    @Override
    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }
}
