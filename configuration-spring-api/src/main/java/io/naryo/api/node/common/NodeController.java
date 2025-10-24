package io.naryo.api.node.common;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Nodes", description = "Nodes related endpoints")
@RequestMapping("api/v1/nodes")
public abstract class NodeController {}
