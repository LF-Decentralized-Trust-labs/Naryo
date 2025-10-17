package io.naryo.api.broadcaster.common.request;

import java.util.List;

import io.naryo.domain.broadcaster.BroadcasterTargetType;
import jakarta.validation.constraints.NotEmpty;

public final class BlockBroadcasterTargetDTO extends BroadcasterTargetDTO {

    public BlockBroadcasterTargetDTO(@NotEmpty List<String> destinations) {
        super(BroadcasterTargetType.BLOCK, destinations);
    }
}
