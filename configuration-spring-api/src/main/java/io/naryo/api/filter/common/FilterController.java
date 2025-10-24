package io.naryo.api.filter.common;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Filter", description = "Filters related endpoints")
@RequestMapping("/api/v1/filters")
public abstract class FilterController {}
