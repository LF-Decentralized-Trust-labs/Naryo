package io.naryo.infrastructure.broadcaster;

import io.naryo.domain.broadcaster.BroadcasterTargetType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class BroadcasterTargetDocument {
    private BroadcasterTargetType type;
    private String destination;

    private UUID filterId;
}
