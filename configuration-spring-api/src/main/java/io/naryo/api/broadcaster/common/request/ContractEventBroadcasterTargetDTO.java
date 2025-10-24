package io.naryo.api.broadcaster.common.request;

import java.util.List;

import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.target.ContractEventBroadcasterTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Schema(description = "CONTRACT_EVENT Broadcaster")
@Getter
public final class ContractEventBroadcasterTargetDTO extends BroadcasterTargetDTO {

    public ContractEventBroadcasterTargetDTO(@NotEmpty List<String> destinations) {
        super(destinations);
    }

    @Override
    public BroadcasterTarget toDomain() {
        return new ContractEventBroadcasterTarget(getDomainDestinations());
    }
}
