package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.target;

import java.util.Set;
import java.util.UUID;

import io.naryo.application.configuration.source.model.broadcaster.target.FilterBroadcasterTargetDescriptor;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("filter")
@NoArgsConstructor
@Getter
public class FilterBroadcasterTargetEntity extends BroadcasterTargetEntity
        implements FilterBroadcasterTargetDescriptor {

    private @Column(name = "filter_id") @NotNull UUID filterId;

    public FilterBroadcasterTargetEntity(Set<String> destinations, UUID filterId) {
        super(destinations);
        this.filterId = filterId;
    }
}
