package io.naryo.api.broadcasterconfiguration.common;

import io.naryo.api.error.ConfigurationApiErrors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/broadcaster-configurations")
@ConfigurationApiErrors
public abstract class BroadcasterConfigurationController {}
