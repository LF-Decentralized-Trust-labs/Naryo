package io.naryo.api.broadcaster.common.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.target.*;
import io.naryo.domain.common.Destination;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AllBroadcasterTargetDTO.class, name = "ALL"),
    @JsonSubTypes.Type(value = BlockBroadcasterTargetDTO.class, name = "BLOCK"),
    @JsonSubTypes.Type(value = FilterBroadcasterTargetDTO.class, name = "FILTER"),
    @JsonSubTypes.Type(value = TransactionBroadcasterTargetDTO.class, name = "TRANSACTION"),
    @JsonSubTypes.Type(value = ContractEventBroadcasterTargetDTO.class, name = "CONTRACT_EVENT")
})
@Schema(
        description = "Base class for broadcaster",
        discriminatorProperty = "type",
        discriminatorMapping = {
            @DiscriminatorMapping(value = "ALL", schema = AllBroadcasterTargetDTO.class),
            @DiscriminatorMapping(value = "BLOCK", schema = BlockBroadcasterTargetDTO.class),
            @DiscriminatorMapping(value = "FILTER", schema = FilterBroadcasterTargetDTO.class),
            @DiscriminatorMapping(
                    value = "TRANSACTION",
                    schema = TransactionBroadcasterTargetDTO.class),
            @DiscriminatorMapping(
                    value = "CONTRACT_EVENT",
                    schema = ContractEventBroadcasterTargetDTO.class)
        })
@Getter
public abstract class BroadcasterTargetDTO {

    private final @NotEmpty List<String> destinations;

    protected BroadcasterTargetDTO(@NotEmpty List<String> destinations) {
        this.destinations = destinations;
    }

    public abstract BroadcasterTarget toDomain();

    public static BroadcasterTargetDTO fromDomain(BroadcasterTarget target) {
        List<String> destinations =
                target.getDestinations().stream().map(Destination::value).toList();
        return switch (target.getType()) {
            case ALL -> new AllBroadcasterTargetDTO(destinations);
            case BLOCK -> new BlockBroadcasterTargetDTO(destinations);
            case FILTER ->
                    new FilterBroadcasterTargetDTO(
                            destinations, ((FilterEventBroadcasterTarget) target).getFilterId());
            case TRANSACTION -> new TransactionBroadcasterTargetDTO(destinations);
            case CONTRACT_EVENT -> new ContractEventBroadcasterTargetDTO(destinations);
        };
    }

    protected List<Destination> getDomainDestinations() {
        return this.destinations.stream().map(Destination::new).toList();
    }
}
