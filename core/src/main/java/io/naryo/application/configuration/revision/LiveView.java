package io.naryo.application.configuration.revision;

import java.util.Map;
import java.util.UUID;

public record LiveView<T>(
        Revision<T> revision, Map<UUID, T> byId, Map<UUID, String> itemFingerprintById) {}
