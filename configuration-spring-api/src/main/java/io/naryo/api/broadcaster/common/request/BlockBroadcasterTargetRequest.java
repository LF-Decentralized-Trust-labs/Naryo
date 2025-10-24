package io.naryo.api.broadcaster.common.request;

import java.util.List;

import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.target.BlockBroadcasterTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Schema(description = "Block broadcaster target request")
@Getter
public final class BlockBroadcasterTargetRequest extends BroadcasterTargetRequest {

    public BlockBroadcasterTargetRequest(@NotEmpty List<String> destinations) {
        super(destinations);
    }

    @Override
    public BroadcasterTarget toDomain() {
        return new BlockBroadcasterTarget(getDomainDestinations());
    }
}
