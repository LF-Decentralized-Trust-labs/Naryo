package io.naryo.api.storeconfiguration;

import io.naryo.api.error.ConfigurationApiErrors;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Store Configuration", description = "Store Configurations related endpoints")
@RequestMapping("api/v1/store-configurations")
@ConfigurationApiErrors
public abstract class StoreConfigurationController {}
