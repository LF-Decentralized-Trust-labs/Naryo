package io.naryo.api.broadcaster.common.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.domain.broadcaster.target.*;
import io.naryo.domain.common.Destination;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AllBroadcasterTargetDTO.class, name = "ALL"),
    @JsonSubTypes.Type(value = BlockBroadcasterTargetDTO.class, name = "BLOCK"),
    @JsonSubTypes.Type(value = FilterBroadcasterTargetDTO.class, name = "FILTER"),
    @JsonSubTypes.Type(value = TransactionBroadcasterTargetDTO.class, name = "TRANSACTION"),
    @JsonSubTypes.Type(value = ContractEventBroadcasterTargetDTO.class, name = "CONTRACT_EVENT")
})
public abstract class BroadcasterTargetDTO {

    private final @NotNull BroadcasterTargetType type;
    private final @NotEmpty List<String> destinations;

    protected BroadcasterTargetDTO(
            @NotNull BroadcasterTargetType type, @NotEmpty List<String> destinations) {
        this.type = type;
        this.destinations = destinations;
    }

    public BroadcasterTarget toDomain() {
        List<Destination> destinations = this.destinations.stream().map(Destination::new).toList();

        return switch (this.type) {
            case ALL -> new AllBroadcasterTarget(destinations);
            case BLOCK -> new BlockBroadcasterTarget(destinations);
            case FILTER ->
                    new FilterEventBroadcasterTarget(
                            destinations, ((FilterBroadcasterTargetDTO) this).getFilterId());
            case TRANSACTION -> new TransactionBroadcasterTarget(destinations);
            case CONTRACT_EVENT -> new ContractEventBroadcasterTarget(destinations);
        };
    }

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
}
