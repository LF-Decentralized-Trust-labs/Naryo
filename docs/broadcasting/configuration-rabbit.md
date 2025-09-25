# 📚 Rabbit Configuration Overview

This document describes how to configure **Naryo** using **Rabbit** as a broadcaster.

---

## Table of Contents

- [Broadcaster Spring Rabbit Configuration](#broadcaster-spring-rabbit-configuration)
- [Broadcasting Configuration](#broadcaster-configuration)

---

## Broadcaster Spring Rabbit Configuration

To use a Rabbit broker as a broadcaster in a Naryo server, first add the required dependencies to your build,
then configure the Rabbit connection.

### 1) Add the module

Add the dependency to your `build.gradle` file with desired version:

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

## Broadcaster Configuration

To use Rabbit as a broadcaster, a Broadcaster Configuration shall be configured with `type=rabbitmq`.

To define the exchange and routing key to be used, make use of the configured Broadcaster's `target` property.

References to configuring a Broadcaster
[yaml](../configuration/configuration-core.md#-2-broadcasting-configuration),
[jpa](../configuration/configuration-jpa.md#-broadcasting-configuration) or
[mongo](../configuration/configuration-mongo.md#-broadcasting-configuration)) ).

---

## 👈 Previous steps

1. [Getting Started](../getting_started.md)
2. [Naryo Configuration](./index.md)
3. [Core Configuration Overview](../configuration/configuration-core.md)

## 👉 Next steps

1. [Tutorials](../tutorials/index.md)
