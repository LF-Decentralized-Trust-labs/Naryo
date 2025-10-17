package io.naryo.api.storeconfiguration;

import io.naryo.api.error.ConfigurationApiErrors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/store-configuration")
@ConfigurationApiErrors
public abstract class StoreConfigurationController {}
