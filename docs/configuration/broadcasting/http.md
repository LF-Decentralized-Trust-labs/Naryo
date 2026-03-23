# HTTP Broadcaster Configuration

This document describes how to configure **Naryo** using **HTTP** as a broadcaster.

## Broadcasting Configuration

To use HTTP as a broadcaster, a `Broadcaster Configuration` must be configured with `type=http`.

Here is an example of a `naryo` configuration for an HTTP broadcaster:

```yaml
naryo:
  broadcasting:
    configuration:
      - id: "550e8400-e29b-41d4-a716-446655440000"
        type: http
        endpoint:
          url: "https://example.com/broadcast"
        cache:
          expirationTime: 5m
    broadcasters:
      - id: "cad022c2-3e41-426f-bb82-c0b86d58d675"
        configurationId: "550e8400-e29b-41d4-a716-446655440000"
        target:
          type: FILTER
          destinations:
            - "/block"
```

### Properties

| Parameter                            | Description                                                  | Value Type | Default Value |
|--------------------------------------|--------------------------------------------------------------|------------|---------------|
| `configurations[].endpoint.url`      | The URL of the HTTP endpoint to which messages will be sent. | String     |               |
| `broadcasters[].target.destinations` | A list of paths that will be appended to the endpoint URL.   | List       |               |

For more information about the generic broadcasting configuration, please refer to the [Core Configuration Overview](../configuration-core.md).

## Conclusion & Next Steps

This section has covered the basics of configuring **Naryo** using **HTTP** as a broadcaster.
For more information about the broadcaster configuration, please refer to the [Core Configuration Overview](../configuration-core.md).

1. [Kafka Configuration](./configuration-kafka.md)
2. [RabbitMQ Configuration](./configuration-rabbit.md)
3. [Tutorials](../../tutorials/index.md)
