package io.librevents.application.broadcaster;

import io.librevents.domain.broadcaster.Broadcaster;

public record BroadcasterWrapper(Broadcaster broadcaster, BroadcasterProducer producer) {}
