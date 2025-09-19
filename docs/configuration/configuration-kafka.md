# 📚 Kafka Configuration Overview

This document describes how to configure **Naryo** using **Kafka** as a broadcaster.

---

## Table of Contents

- [Broadcaster Spring Kafka Configuration](#broadcaster-spring-kafka-configuration)
- [📡 Broadcasting Configuration](#-broadcasting-configuration)

---

## Broadcaster Spring Kafka Configuration

To use a Kafka broker as a broadcaster in a Naryo server, first add the required dependencies to your build,
then configure the Kafka connection.

### 1) Add the module

Add the dependency to your `build.gradle` file with desired version:

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

---

## 📡 Broadcaster Configuration

To use Kafka as a broadcaster, a Broadcaster Configuration shall be configured with `type=kafka`.

To define the topic to be used, make use of the configured Broadcaster's `target` property.

References to configuring a Broadcaster
[yaml](./configuration-core.md#-2-broadcasting-configuration),
[jpa](./configuration-jpa.md#-broadcasting-configuration) or
[mongo](./configuration-mongo.md#-broadcasting-configuration)) ).

---

## 👈 Previous steps

1. [Getting Started](../getting_started.md)
2. [Naryo Configuration](./index.md)
3. [Core Configuration Overview](./configuration-core.md)

## 👉 Next steps

1. [Tutorials](../tutorials/index.md)
