package io.naryo.api.broadcaster.common.request;

import java.util.List;

import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.target.AllBroadcasterTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Schema(description = "All broadcaster target request")
@Getter
public final class AllBroadcasterTargetRequest extends BroadcasterTargetRequest {

    public AllBroadcasterTargetRequest(@NotEmpty List<String> destinations) {
        super(destinations);
    }

    @Override
    public BroadcasterTarget toDomain() {
        return new AllBroadcasterTarget(getDomainDestinations());
    }
}
