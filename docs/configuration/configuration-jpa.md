# 📚 JPA Configuration Overview

This document describes how to configure **Naryo** using a **JPA-supported** database via the `persistence-spring-jpa` module.
It complements the YAML-based configuration provided by **`spring-core`**, enabling **partial configuration** and
**priority-based merging** across multiple configuration sources.

> **Priority & Merge**
> - **JPA** has priority **0** (highest).
> - **spring-core** (`application.yml`) has priority **1**.
> - If the same key exists in both, **JPA wins**. If a key is **missing in JPA**, the value is taken from YAML.

> **Conventions**
> - **Partial overrides** are supported: include only the fields you want to override in the JPA-supported database.
> - **Default values** can be seen in [Core Configuration Overview](./configuration-core.md).

---

## Table of Contents

- [Persistence Spring JPA Configuration](#persistence-spring-jpa-configuration)
- [Polymorphism & TypeAlias](#polymorphism--typealias)
- [🌐️ Node Configuration](#-node-configuration)
- [📡 Broadcasting Configuration](#-broadcasting-configuration)
- [☁️ Filter Configuration](#-filter-configuration)
- [🗄️ Store Configuration](#-store-configuration)

---

## Persistence Spring JPA Configuration

To use a JPA-supported database as a configuration source in a Naryo server, first add the persistence-spring-jpa
module to your build, then configure the JPA connection.

### 1) Add the module

Add the dependency to your `build.gradle` file with desired version:

```groovy
dependencies {
    implementation "io.naryo:persistence-spring-jpa:<version>"
}
```

### 2) Add JPA dependencies

Add the JPA-related dependencies to your `build.gradle` file with desired version.

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'org.postgresql:postgresql' // Example for PostgreSQL, replace if using another SQL database
}
```

### 3) Configure Spring to connect to JPA

Add the following to your application.yaml:

```yaml
spring:
  datasource:
    url: jdbc:{database_type}://{host}:{port}/{db_name} # e.g. jdbc:postgresql://localhost:5432/naryo
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: {ddl_type} # e.g. validate, create, update, etc.
  # Additional JPA-specific configurations can be added here
```

---

## Polymorphism & TypeAlias

When configuration is read from JPA, a single table may represent different subtypes (polymorphism).
In this scenarios, Naryo uses a single table approach via a discriminator column that specifies the concrete type of the tuple.

---
## 📧 HttpClient Configuration

The `persistence-spring-jpa` module allows configuring the HTTP client used across Naryo.
If a value is missing in JPA, the default values defined in Core will apply.

>**Conventions**
> - All durations are expressed as ISO-8601 values (e.g., PT30S = 30 seconds).

###  Table reference - `http_client`

| Path                          | Type     | Required | Default (if omitted) | Notes                                                                 |
|-------------------------------|----------|:--------:|----------------------|-----------------------------------------------------------------------|
| `id`                          | UUID     |    ✅     | —                    | Identifier.                                                           |
| `max_idle_connections`        | number   |    ❌     | `5`                  | Must be `>= 0`.                                                       |
| `keep_alive_duration`         | duration |    ❌     | `PT5M`               | Must be strictly positive.                                            |
| `connect_timeout`             | duration |    ❌     | `PT10S`              | Must be strictly positive.                                            |
| `read_timeout`                | duration |    ❌     | `PT30S`              | Must be strictly positive.                                            |
| `write_timeout`               | duration |    ❌     | `PT30S`              | Must be strictly positive.                                            |
| `call_timeout`                | duration |    ❌     | `PT1M`               | Must be strictly positive.                                            |
| `ping_interval`               | duration |    ❌     | `PT30S`              | Must be strictly positive.                                            |
| `retry_on_connection_failure` | boolean  |    ❌     | `true`               | Whether to retry automatically when a connection fails.               |

---

## 🌐️ Node Configuration

The `persistence-spring-jpa` module retrieves node configuration from the following tables:

### Table reference — `node`

| Path                                       | Type   | Required | Notes                                                             |
|--------------------------------------------|--------|:--------:|-------------------------------------------------------------------|
| `id`                                       | UUID   |    ✅     | Identifier of the node                                            |
| `node_type`                                | string |    ✅     | `ethereum` \| `private_ethereum` \| `public_ethereum` \| `hedera` |
| `name`                                     | string |    ✅     | Human-readable node name                                          |
| `subscription_id`                          | UUID   |    ❌     | ID of a `node_subscription` table's tuple                         |
| `interaction_id`                           | UUID   |    ❌     | ID of a `node_interaction` table's tuple                          |
| `connection_id`                            | object |    ❌     | ID of a `node_connection` table's tuple                           |
| `group_id` *(private ethereum)*            | string |    ❌     | Only for `private_ethereum_node`                                  |
| `precompiled_address` *(private ethereum)* | string |    ❌     | Only for `private_ethereum_node`                                  |

### Table reference — `node_subscription`

| Path                                 | Type   | Required | Notes                       |
|--------------------------------------|--------|:--------:|-----------------------------|
| `id`                                 | UUID   |    ✅     | Identifier (auto-generated) |
| `subscription_type`                  | string |    ✅     | `pub-sub` \| `poll`         |
| `initial_block`                      | number |    ❌     | `-1` means latest           |
| `confirmation_blocks`                | number |    ❌     |                             |
| `missing_tx_retry_blocks`            | number |    ❌     |                             |
| `event_invalidation_block_threshold` | number |    ❌     |                             |
| `replay_block_offset`                | number |    ❌     |                             |
| `sync_block_limit` *(poll only)*     | number |    ❌     | Duration in nanoseconds     |

### Table reference — `node_interaction`

| Path                                   | Type   | Required | Notes                       |
|----------------------------------------|--------|:--------:|-----------------------------|
| `id`                                   | UUID   |    ✅     | Identifier (auto-generated) |
| `interaction_type`                     | string |    ✅     | `ethereum` \| `hedera`      |
| `limit_per_request` *(hedera only)*    | number |    ❌     |                             |
| `retries_per_request` *(hedera  only)* | number |    ❌     |                             |

### Table reference — `node_connection`

| Path                                 | Type   | Required | Notes                       |
|--------------------------------------|--------|:--------:|-----------------------------|
| `id`                                 | UUID   |    ✅     | Identifier (auto-generated) |
| `connection_type`                    | string |    ✅     | `http` \| `ws`              |
| `times`                              | number |    ❌     |                             |
| `backoff`                            | number |    ❌     | ISO-8601 duration           |
| `url`                                | string |    ❌     |                             |
| `max_idle_connections` *(http only)* | number |    ❌     |                             |
| `keep_alive_duration` *(http only)*  | number |    ❌     | Duration in nanoseconds     |
| `connection_timeout` *(http only)*   | number |    ❌     | Duration in nanoseconds     |
| `read_timeout` *(http only)*         | number |    ❌     | Duration in nanoseconds     |

---

## 📡 Broadcasting Configuration

The `persistence-spring-jpa` module retrieves the broadcasting **configurations** from the following tables:

### Table reference — `broadcaster`

| Path               | Type | Required | Notes                                             |
|--------------------|------|:--------:|---------------------------------------------------|
| `id`               | UUID |    ✅     | Identifier                                        |
| `configuration_id` | UUID |    ✅     | ID of a `broadcaster_configuration` table's tuple |
| `target_id`        | UUID |    ✅     | ID of a `broadcaster_target` table's tuple        |

### Table reference — `broadcaster_configuration`

| Path                    | Type   | Required | Notes                                                      |
|-------------------------|--------|:--------:|------------------------------------------------------------|
| `_id`                   | UUID   |    ✅     | Identifier                                                 |
| `broadcaster_type`      | string |    ✅     | Broadcasting type, e.g., `http`, `rabbitmq`, etc.          |
| `expiration_time`       | number |    ✅     | Duration in nanoseconds                                    |
| `additional_properties` | JSON   |    ❌     | Additional properties, depending on the `broadcaster_type` |

### Table reference — `broadcaster_target`

| Path                        | Type   | Required | Notes                                                             |
|-----------------------------|--------|:--------:|-------------------------------------------------------------------|
| `id`                        | UUID   |    ✅     | Identifier (auto-generated)                                       |
| `target_type`               | string |    ✅     | `all` \| `block` \| `contract_event` \| `filter` \| `transaction` |
| `filter_id` *(filter only)* | UUID   |    ❌     |                                                                   |

### Table reference — `broadcaster_target_destination`

| Path                    | Type   | Required | Notes                                      |
|-------------------------|--------|:--------:|--------------------------------------------|
| `broadcaster_target_id` | UUID   |    ✅     | ID of a `broadcaster_target` table's tuple |
| `destination`           | string |    ✅     |                                            |

---

## ☁️ Filter Configuration

The `persistence-spring-jpa` module retrieves filter configuration from the following tables:

### Table reference — `filter`

| Path                                                      | Type    | Required | Notes                                               |
|-----------------------------------------------------------|---------|:--------:|-----------------------------------------------------|
| `id`                                                      | UUID    |    ✅     | Identifier                                          |
| `type`                                                    | string  |    ✅     | `event_contract` \| `event_global` \| `transaction` |
| `name`                                                    | string  |    ❌     |                                                     |
| `nodeId`                                                  | UUID    |    ❌     |                                                     |
| `signature` *(event_contract & event_global only)*        | string  |    ❌     |                                                     |
| `correlation_id` *(event_contract & event_global only)*   | number  |    ❌     |                                                     |
| `visible` *(event_contract & event_global only)*          | boolean |    ❌     |                                                     |
| `privacy_group_id` *(event_contract & event_global only)* | string  |    ❌     |                                                     |
| `sync_id` *(event_contract & event_global only)*          | UUID    |    ❌     | ID of a `filter_sync` table's tuple                 |
| `address` *(event_contract only)*                         | string  |    ❌     |                                                     |
| `identifierType` *(transaction only)*                     | string  |    ❌     | `HASH` \| `TO_ADDRESS` \| `TO_ADDRESS`              |
| `value` *(transaction only)*                              | string  |    ❌     |                                                     |

### Table reference — `event_filter_status` (only for event_contract & event_global tuples under `filter` table)

| Path              | Type   | Required | Notes                                         |
|-------------------|--------|:--------:|-----------------------------------------------|
| `event_filter_id` | UUID   |    ✅     | ID of a `filter` table's tuple                |
| `status`          | string |    ✅     | `CONFIRMED` \| `UNCONFIRMED` \| `INVALIDATED` |

### Table reference — `event_filter_sync`

| Path                           | Type   | Required | Notes                       |
|--------------------------------|--------|:--------:|-----------------------------|
| `id`                           | UUID   |    ✅     | Identifier (auto-generated) |
| `sync_type`                    | string |    ✅     | `block`                     |
| `initial_block` *(block only)* | number |    ❌     |                             |

### Table reference — `transaction_filter_status` (only for transaction tuples under `filter` table)

| Path                    | Type   | Required | Notes                                    |
|-------------------------|--------|:--------:|------------------------------------------|
| `transaction_filter_id` | UUID   |    ✅     | ID of a `filter` table's tuple           |
| `status`                | string |    ✅     | `FAILED` \| `CONFIRMED` \| `UNCONFIRMED` |

---

## 🗄️ Store Configuration

The `persistence-spring-jpa` module retrieves store configuration documents from the following tables:

### Table reference — `store_configuration`

| Path                                    | Type   | Required | Default (if omitted) | Notes                                          |
|-----------------------------------------|--------|:--------:|----------------------|------------------------------------------------|
| `node_id`                               | UUID   |    ✅     | —                    | Identifier of the node                         |
| `state`                                 | string |    ✅     | —                    | `active` \| `inactive`.                        |
| `type` *(active only)*                  | string |    ❌     | —                    | Store type (e.g., `mongo`, `jpa`, etc.)        |
| `additional_properties` *(active only)* | JSON   |    ❌     | —                    | Additional properties, depending on the `type` |

### Table reference — `store_feature`

| Path                           | Type   | Required | Default (if omitted) | Notes                                                                                                          |
|--------------------------------|--------|:--------:|----------------------|----------------------------------------------------------------------------------------------------------------|
| `id`                           | UUID   |    ✅     | —                    | Identifier (auto-generated)                                                                                    |
| `store_configuration_id`       | UUID   |    ✅     | —                    | ID of a `store_configuration` table's tuple                                                                    |
| `store_feature`                | string |    ✅     | —                    | `event_block` \| `filter`                                                                                      |
| `destination`  *(filter only)* | string |    ❌     | —                    | Ignored if related store's `type` is `mongo` or `jpa`, as the db schema details are defined by the application |

### Table reference — `event_store_target`

| Path          | Type   | Required | Default (if omitted) | Notes                                |
|---------------|--------|:--------:|----------------------|--------------------------------------|
| `id`          | UUID   |    ✅     | —                    | Identifier (auto-generated)          |
| `type`        | string |    ✅     | —                    | `BLOCK` \| `TRANSACTION ` \| `EVENT` |
| `destination` | string |    ✅     | —                    |                                      |

---

## 👈 Previous steps

1. [Getting Started](../getting_started.md)
2. [Naryo Configuration](./index.md)
3. [Core Configuration Overview](./configuration-core.md)

## 👉 Next steps

1. [Tutorials](../tutorials/index.md)
