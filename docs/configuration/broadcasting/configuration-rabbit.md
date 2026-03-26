# Rabbit Configuration Overview

This document describes how to configure **Naryo** using **Rabbit** as a broadcaster.

## Table of Contents

- [Broadcaster Spring Rabbit Configuration](#broadcaster-spring-rabbit-configuration)
- [Broadcasting Configuration](#broadcasting-configuration)

## Broadcaster Spring Rabbit Configuration

To use a Rabbit broker as a broadcaster in a Naryo server, first add the required dependencies to your build,
then configure the Rabbit connection.

### 1) Add the module

Add the dependency to your `build.gradle` file with the desired version:

```groovy
dependencies {
    implementation 'io.naryo:broadcaster-spring-rabbit:<version>'
}
```

### 2) Configure Spring to connect to Rabbit

Add the following to your application.yaml:

```yaml
spring:
  rabbitmq:
    host: {host} # e.g. localhost
    port: {port} # e.g. 5672
    username: {username} # e.g. guest
    password: {password} # e.g. guest
```
---

## Broadcasting Configuration

To use RabbitMQ as a broadcaster, a `Broadcaster Configuration` must be configured with `type=rabbitmq`.

Here is an example of a `naryo` configuration for a RabbitMQ broadcaster:

```yaml
naryo:
  broadcasting:
    configuration:
      - id: "550e8400-e29b-41d4-a716-446655440000"
        type: rabbitmq
        exchange: "naryo-exchange"
        cache:
          expirationTime: 5m
    broadcasters:
      - id: "cad022c2-3e41-426f-bb82-c0b86d58d675"
        configurationId: "550e8400-e29b-41d4-a716-446655440000"
        target:
          type: FILTER
          destinations:
            - "events"
```

### Properties

| Parameter                            | Description                                                                                                                              | Value Type | Default Value |
|--------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------|------------|---------------|
| `configurations[].exchange`          | The name of the RabbitMQ exchange to which messages will be sent.                                                                        | String     |               |
| `broadcasters[].target.destinations` | A list of prefixes for the routing key. The final routing key will be in the format `destination.key`. For example, `events.event_name`. | List       |               |

For more information about the generic broadcasting configuration, please refer to the [Core Configuration Overview](../configuration-core.md).

## Conclusion & Next Steps

This guide has covered the basics of configuring a RabbitMQ broadcaster in a Naryo server.
You can now proceed to the [Broadcasting Guide](./index.md) to learn more about broadcasting.
