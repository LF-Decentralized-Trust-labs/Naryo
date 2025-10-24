package io.naryo.api.broadcaster.common.request;

import java.util.List;

import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.target.AllBroadcasterTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Schema(description = "ALL Broadcaster")
@Getter
public final class AllBroadcasterTargetDTO extends BroadcasterTargetDTO {

    public AllBroadcasterTargetDTO(@NotEmpty List<String> destinations) {
        super(destinations);
    }

    @Override
    public BroadcasterTarget toDomain() {
        return new AllBroadcasterTarget(getDomainDestinations());
    }
}
