package io.naryo.api.broadcaster.get.model;

import java.util.List;

import io.naryo.domain.broadcaster.BroadcasterTargetType;

public record BroadcasterTargetResponse(BroadcasterTargetType type, List<String> destinations) {}
