package io.naryo.api.broadcaster.update.model;

import io.naryo.api.broadcaster.common.request.BroadcasterRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBroadcasterRequest(
        @NotNull BroadcasterRequest broadcaster, @NotBlank String prevItemHash) {}
