package io.naryo.api.broadcaster.common;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Broadcaster", description = "Broadcasters related endpoints")
@RequestMapping("api/v1/broadcasters")
public abstract class BroadcasterController {}
