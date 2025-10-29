package io.naryo.api.broadcaster.update.model;

import io.naryo.api.broadcaster.common.request.BroadcasterRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Update broadcaster request")
public record UpdateBroadcasterRequest(
        @NotNull BroadcasterRequest broadcaster, @NotBlank String prevItemHash) {}
