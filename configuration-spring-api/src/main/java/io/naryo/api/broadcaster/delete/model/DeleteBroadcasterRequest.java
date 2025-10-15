package io.naryo.api.broadcaster.delete.model;

import jakarta.validation.constraints.NotBlank;

public record DeleteBroadcasterRequest(@NotBlank String prevItemHash) {}
