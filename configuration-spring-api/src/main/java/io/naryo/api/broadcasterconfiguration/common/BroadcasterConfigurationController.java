package io.naryo.api.broadcasterconfiguration.common;

import io.naryo.api.error.ConfigurationApiErrors;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(
        name = "Broadcaster Configuration",
        description = "Broadcaster configurations related endpoints")
@RequestMapping("api/v1/broadcaster-configurations")
@ConfigurationApiErrors
public abstract class BroadcasterConfigurationController {}
