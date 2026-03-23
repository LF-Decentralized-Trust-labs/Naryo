# Kafka Configuration Overview

This document describes how to configure **Naryo** using **Kafka** as a broadcaster.

## Table of Contents

- [Broadcaster Spring Kafka Configuration](#broadcaster-spring-kafka-configuration)
- [Broadcasting Configuration](#broadcasting-configuration)

## Broadcaster Spring Kafka Configuration

To use a Kafka broker as a broadcaster in a Naryo server, first add the required dependencies to your build,
then configure the Kafka connection.

### 1) Add the module

Add the dependency to your `build.gradle` file with the desired version:

```groovy
dependencies {
    implementation 'io.naryo:broadcaster-spring-kafka:<version>'
}
```

### 2) Configure Spring to connect to Kafka

Add the following to your application.yaml:

```yaml
spring:
  kafka:
    producer:
      bootstrap-servers: {host}:{port}  # e.g. localhost:9092
    # Additional Kafka-specific configurations can be added here
```

Please refer to the [Spring Kafka documentation](https://spring.io/projects/spring-kafka) for more information.

## Broadcasting Configuration

To use Kafka as a broadcaster, a `Broadcaster Configuration` must be configured with `type=KAFKA`.

Here is an example of a `naryo` configuration for a Kafka broadcaster:

```yaml
naryo:
  broadcasting:
    configuration:
      - id: "550e8400-e29b-41d4-a716-446655440000"
        type: KAFKA
        cache:
          expirationTime: 5m
    broadcasters:
      - id: "cad022c2-3e41-426f-bb82-c0b86d58d675"
        configurationId: "550e8400-e29b-41d4-a716-446655440000"
        target:
          type: FILTER
          destinations:
            - "naryo-events"
```

### Properties

| Parameter             | Description                                            | Value Type | Default Value |
|-----------------------|--------------------------------------------------------|------------|---------------|
| `target.destinations` | A list of Kafka topics to which messages will be sent. | List       |               |

For more information about the generic broadcasting configuration, please refer to the [Core Configuration Overview](../configuration-core.md).

## Conclusion & Next Steps

This guide has covered the basics of configuring a Kafka broadcaster in a Naryo server.
You can now proceed to the [Broadcasting Guide](./index.md) to learn more about broadcasting.
