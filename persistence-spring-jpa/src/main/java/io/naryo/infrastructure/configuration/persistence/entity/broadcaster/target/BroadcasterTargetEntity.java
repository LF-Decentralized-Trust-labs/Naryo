package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.target;

import java.util.Optional;

import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "broadcasters_target")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "target_type")
@NoArgsConstructor
@Getter
public abstract class BroadcasterTargetEntity implements BroadcasterTargetDescriptor {

    private @Id @Column(name = "id") @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    private @Column(name = "destination") @Nullable @NotBlank String destination;

    public BroadcasterTargetEntity(String destination) {
        this.destination = destination;
    }

    @Override
    public Optional<String> getDestination() {
        return Optional.ofNullable(this.destination);
    }

    @Override
    public void setDestination(String destination) {
        this.destination = destination;
    }
}
