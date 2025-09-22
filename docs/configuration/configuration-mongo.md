# 📚 Mongo Configuration Overview

This document describes how to configure **Naryo** using **MongoDB** via the `persistence-spring-mongo` module. It
complements the YAML-based configuration provided by **`spring-core`**, enabling **partial configuration** and
**priority-based merging** across multiple configuration sources.

> **Priority & Merge**
> - **MongoDB** has priority **0** (highest).
> - **spring-core** (`application.yml`) has priority **1**.
> - If the same key exists in both, **MongoDB wins**. If a key is **missing in MongoDB**, the value is taken from YAML.

> **Conventions**
> - Documents are **polymorphic** and use Spring Data **`@TypeAlias`**. The discriminator is stored as `_class`.
> - **Durations** are ISO‑8601 strings (e.g., `PT5S`, `PT5M`).
> - **IDs** are UUID strings unless otherwise indicated.
> - **Partial overrides** are supported: include only the fields you want to override in MongoDB.
> - **Default values** can be seen in [Core Configuration Overview](./configuration-core.md).

---

## Table of Contents

- [Persistence Spring MongoDB Configuration](#persistence-spring-mongodb-configuration)
- [Polymorphism & TypeAlias](#polymorphism--typealias)
- [🌐️ Node Configuration](#-node-configuration)
- [📡 Broadcasting Configuration](#-broadcasting-configuration)
- [☁️ Filter Configuration](#-filter-configuration)
- [🗄️ Store Configuration](#-store-configuration)

---

## Persistence Spring MongoDB Configuration

To use MongoDB as a configuration source in a Naryo server, first add the persistence-spring-mongo module to your build,
then configure the MongoDB connection.

### 1) Add the module

Add the dependency to your `build.gradle` file with desired version:

```groovy
dependencies {
    implementation "io.naryo:persistence-spring-mongo:<version>"
}
```

### 2) Configure Spring to connect to MongoDB

Add the following to your application.yaml:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/naryo
      auto-index-creation: true
      # Additional MongoDB-specific configurations can be added here
```

> Replace `localhost:27017/naryo` with your MongoDB server, port, and database name.

---

## Polymorphism & TypeAlias

When configuration is read from MongoDB, documents may represent different subtypes (polymorphism).
Naryo uses Spring Data's type metadata:

- Each document/sub-document includes a discriminator field named **`_class`**.
- Subtypes are annotated with **`@TypeAlias`** to store a short, stable token instead of the full Java class name.

**Example**

```java
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.TypeAlias;

@Document("nodes")
@TypeAlias("hedera_node")
class HederaNode extends Node { /* ... */
}
```

Stored document excerpt:

```json
{
  "_class": "hedera_node",
  "_id": "...",
  "name": "hedera-mainnet"
}
```

---

## 📧 HttpClient Configuration

The `persistence-spring-mongo` module retrieves HttpClient configuration from the `HttpClient` collection,
allowing the configuration of HTTP client used across Naryo.
If a value is missing in Mongo database, the default values defined in Core will apply.

>**Conventions**
> - All durations are expressed as ISO-8601 values (e.g., PT30S = 30 seconds).

### Example
`_id` is required. All other sections are optional.
```json
{
"id": "550e8400-e29b-41d4-a716-446655440000",
"maxIdleConnections": 66,
"keepAliveDuration": "PT5M",
"connectTimeout": "PT10S",
"readTimeout": "PT30S",
"writeTimeout": "PT30S",
"callTimeout": "PT60S",
"pingInterval": "PT15S",
"retryOnConnectionFailure": true
}
```

###  Field reference - HttpClient

| Path                           | Type          | Required | Default (if omitted) | Notes                                                                 |
|--------------------------------|---------------|:--------:|----------------------|-----------------------------------------------------------------------|
| `id`                           | String (UUID) |    ✅     | —                    | Identifier.                                                           |
| `maxIdleConnections`           | number        |    ❌     | `5`                  | Must be `>= 0`.                                                       |
| `keepAliveDuration`            | duration      |    ❌     | `PT5M`               | Must be strictly positive.                                            |
| `connectTimeout`               | duration      |    ❌     | `PT10S`              | Must be strictly positive.                                            |
| `readTimeout`                  | duration      |    ❌     | `PT30S`              | Must be strictly positive.                                            |
| `writeTimeout`                 | duration      |    ❌     | `PT30S`              | Must be strictly positive.                                            |
| `callTimeout`                  | duration      |    ❌     | `PT1M`               | Must be strictly positive.                                            |
| `pingInterval`                 | duration      |    ❌     | `PT30S`              | Must be strictly positive.                                            |
| `retryOnConnectionFailure`     | boolean       |    ❌     | `true`               | Whether to retry automatically when a connection fails.               |

---

## 🌐️ Node Configuration

The `persistence-spring-mongo` module retrieves node configuration from the **`nodes`** collection.

### Collections & Type aliases

- **Collection**: `nodes`
- **Node types** (`_class` at root): `public_ethereum_node`, `private_ethereum_node`, `hedera_node`
- **Subscription types** (`subscription._class`): `poll_block_subscription`, `pub_sub_block_subscription`
- **Interaction types** (`interaction._class`): `ethereum_rpc_block_interaction`, `hedera_mirror_node_block_interaction`
- **Connection types** (`connection._class`): `http_connection`, `ws_connection`

### Minimal document

`_id` and `name` are required. All other sections are optional.

```json
{
  "_class": "public_ethereum_node",
  "_id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "ethereum-mainnet"
}
```

### Example A — Public Ethereum node (HTTP + Poll subscription + Ethereum RPC interaction)

```json
{
  "_class": "public_ethereum_node",
  "_id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "ethereum-mainnet",
  "subscription": {
    "_class": "poll_block_subscription",
    "initialBlock": -1,
    "confirmationBlocks": 12,
    "missingTxRetryBlocks": 200,
    "eventInvalidationBlockThreshold": 2,
    "replayBlockOffset": 12,
    "syncBlockLimit": 20000,
    "interval": "PT5S"
  },
  "interaction": {
    "_class": "ethereum_rpc_block_interaction"
  },
  "connection": {
    "_class": "http_connection",
    "retry": {
      "_class": "connection_retry",
      "times": 3,
      "backoff": "PT1S"
    },
    "endpoint": {
      "_class": "connection_endpoint",
      "url": "https://mainnet.infura.io/v3/<project-id>"
    },
    "maxIdleConnections": 5,
    "keepAliveDuration": "PT5M",
    "connectionTimeout": "PT10S",
    "readTimeout": "PT30S"
  }
}
```

### Example B — Hedera node (HTTP + Hedera Mirror Node interaction)

```json
{
  "_class": "hedera_node",
  "_id": "cad022c2-3e41-426f-bb82-c0b86d58d675",
  "name": "hedera-mainnet",
  "subscription": {
    "_class": "poll_block_subscription",
    "initialBlock": 1,
    "confirmationBlocks": 12,
    "missingTxRetryBlocks": 200,
    "eventInvalidationBlockThreshold": 2,
    "replayBlockOffset": 12,
    "syncBlockLimit": 20000,
    "interval": "PT1S"
  },
  "interaction": {
    "_class": "hedera_mirror_node_block_interaction",
    "limitPerRequest": 100,
    "retriesPerRequest": 3
  },
  "connection": {
    "_class": "http_connection",
    "retry": {
      "_class": "connection_retry",
      "times": 3,
      "backoff": "PT1S"
    },
    "endpoint": {
      "_class": "connection_endpoint",
      "url": "https://mainnet.hashio.io/api"
    },
    "maxIdleConnections": 5,
    "keepAliveDuration": "PT5M",
    "connectionTimeout": "PT10S",
    "readTimeout": "PT30S"
  }
}
```

### Example C — Private Ethereum node (WS + Pub/Sub subscription)

```json
{
  "_class": "private_ethereum_node",
  "_id": "5a2a55f6-5c7c-4a35-8a3a-8e9b1b4c0a11",
  "name": "quorum-dev",
  "groupId": "1",
  "precompiledAddress": "0x000000000000000000000000000000000000007c",
  "subscription": {
    "_class": "pub_sub_block_subscription",
    "initialBlock": -1,
    "confirmationBlocks": 12,
    "missingTxRetryBlocks": 200,
    "eventInvalidationBlockThreshold": 2,
    "replayBlockOffset": 12,
    "syncBlockLimit": 20000
  },
  "interaction": {
    "_class": "ethereum_rpc_block_interaction"
  },
  "connection": {
    "_class": "ws_connection",
    "retry": {
      "_class": "connection_retry",
      "times": 3,
      "backoff": "PT1S"
    },
    "endpoint": {
      "_class": "connection_endpoint",
      "url": "ws://localhost:8546"
    }
  }
}
```

### Field reference — Node

| Path                                           | Type   | Required | Default (if omitted) | Notes                                                                       |
|------------------------------------------------|--------|:--------:|----------------------|-----------------------------------------------------------------------------|
| `_class`                                       | string |    ✅     | —                    | `public_ethereum_node` \| `private_ethereum_node` \| `hedera_node`          |
| `id`                                           | string |    ✅     | —                    | Identifier (UUID string recommended).                                       |
| `name`                                         | string |    ✅     | —                    | Human-readable node name.                                                   |
| `subscription`                                 | object |    ❌     | —                    | Polymorphic section.                                                        |
| `subscription._class`                          | string |    ❌     | —                    | `poll_block_subscription` \| `pub_sub_block_subscription`.                  |
| `subscription.initialBlock`                    | number |    ❌     | `-1`                 | `-1` means latest.                                                          |
| `subscription.confirmationBlocks`              | number |    ❌     | `12`                 |                                                                             |
| `subscription.missingTxRetryBlocks`            | number |    ❌     | `200`                |                                                                             |
| `subscription.eventInvalidationBlockThreshold` | number |    ❌     | `2`                  |                                                                             |
| `subscription.replayBlockOffset`               | number |    ❌     | `12`                 |                                                                             |
| `subscription.syncBlockLimit`                  | number |    ❌     | `20000`              |                                                                             |
| `subscription.interval` *(poll only)*          | string |    ❌     | `"PT5S"`             | ISO-8601 duration.                                                          |
| `interaction`                                  | object |    ❌     | —                    | Polymorphic section.                                                        |
| `interaction._class`                           | string |    ❌     | —                    | `ethereum_rpc_block_interaction` \| `hedera_mirror_node_block_interaction`. |
| `interaction.limitPerRequest` *(hedera)*       | number |    ❌     | `100`                |                                                                             |
| `interaction.retriesPerRequest` *(hedera)*     | number |    ❌     | `3`                  |                                                                             |
| `connection`                                   | object |    ❌     | —                    | See **Connection** component.                                               |
| `groupId` *(private ethereum)*                 | string |    ❌     | —                    | Only for `private_ethereum_node`.                                           |
| `precompiledAddress` *(private ethereum)*      | string |    ❌     | —                    | Only for `private_ethereum_node`.                                           |

---

## 📡 Broadcasting Configuration

The `persistence-spring-mongo` module retrieves the broadcasting **configurations** from two collections:

- **`broadcasters_configuration`** — reusable **broadcasting configurations** (type, cache, and type-specific
  properties).
- **`broadcasters`** — individual **broadcasters** that reference a configuration and define a **target** (what to
  broadcast).

### 1) Broadcasting **Configuration** documents

**Collection:** `broadcasters_configuration`

Each document represents a reusable configuration.

#### Example — HTTP configuration

```json
{
  "_id": "550e8400-e29b-41d4-a716-446655440000",
  "type": "HTTP",
  "cache": {
    "expirationTime": "PT5M"
  },
  "endpoint": {
    "url": "https://example.com/broadcast"
  }
}
```

#### Field reference — Broadcasting **Configuration**

| Path                    | Type              | Required | Default (if omitted) | Notes                                                           |
|-------------------------|-------------------|:--------:|----------------------|-----------------------------------------------------------------|
| `_id`                   | string (UUID)     |    ✅     | —                    | Mongo `_id` (referenced by broadcasters).                       |
| `type`                  | string            |    ✅     | —                    | Broadcasting type, e.g., `HTTP`.                                |
| `cache`                 | object            |    ❌     | —                    | Cache settings.                                                 |
| `cache.expirationTime`  | string (duration) |    ❌     | `PT5M`               | ISO-8601 duration.                                              |
| `endpoint`              | object            |    ❌     | —                    | Endpoint configuration.                                         |
| `endpoint.url`          | string            |    ❌     | —                    | URL of the endpoint.                                            |

### 2) **Broadcaster** documents

**Collection:** `broadcasters`
Each broadcaster links to a configuration (`configurationId`) and defines a **target** to broadcast.

#### Examples — Broadcasters

**Block target**

```json
{
  "_id": "cad022c2-3e41-426f-bb82-c0b86d58d675",
  "configurationId": "550e8400-e29b-41d4-a716-446655440000",
  "target": {
    "_class": "block_broadcaster_targets",
    "destinations": ["/block"]
  }
}
```

**Transaction target**

```json
{
  "_id": "9b3a6d22-6a22-4f7f-9a19-2b1a2ab21e00",
  "configurationId": "550e8400-e29b-41d4-a716-446655440000",
  "target": {
    "_class": "transaction_broadcaster_targets",
    "destinations": ["/tx"]
  }
}
```

**Contract event target**

```json
{
  "_id": "c3a9c0d2-5fbd-4d9a-8f77-d1e3cc0e0a01",
  "configurationId": "550e8400-e29b-41d4-a716-446655440000",
  "target": {
    "_class": "contract_event_broadcaster_targets",
    "destinations": ["/contract-event"]
  }
}
```

**Filter target**

```json
{
  "_id": "3f666c9a-57a0-4c5e-8a22-022f31d62c77",
  "configurationId": "550e8400-e29b-41d4-a716-446655440000",
  "target": {
    "_class": "filter_broadcaster_targets",
    "destinations": ["/filter"],
    "filterId": "550e8400-e29b-41d4-a716-446655440001"
  }
}
```

**All events target**

```json
{
  "_id": "8b87d8e0-40ac-4e20-9cf5-5f3b8b635c11",
  "configurationId": "550e8400-e29b-41d4-a716-446655440000",
  "target": {
    "_class": "all_broadcaster_targets",
    "destinations": ["/all"]
  }
}
```

> **Linking**: `configurationId` must reference an existing document in `broadcasters_configuration`.

#### Field reference — **Broadcaster**

| Path                         | Type          | Required | Default (if omitted) | Notes                                                  |
|------------------------------|---------------|:--------:|----------------------|--------------------------------------------------------|
| `_id`                        | string (UUID) |    ✅     | —                    | Broadcaster_id". Used to merge with YAML by_id".       |
| `configurationId`            | string (UUID) |    ❌     | —                    | References a document in `broadcasters_configuration`. |
| `target`                     | object        |    ❌     | —                    | Polymorphic target.                                    |
| `target._class`              | string        |    ❌     | —                    | Alias: one of the values listed above.                 |
| `target.destinations`        | Set<string>   |    ❌     | —                    | Optional destination path/topic/queue.                 |
| `target.filterId` *(filter)* | string (UUID) |    ❌     | —                    | Required only for `filter_broadcaster_targets`.        |

---

## ☁️ Filter Configuration

The `persistence-spring-mongo` module retrieves filter configuration from the **`filters`** collection.

### Collections & type aliases

- **Collection:** `filters`
- **Filter types** (`_class` at root): `event_filter`, `transaction_filter`

### Example A — Event filter (CONTRACT scope)

```json
{
  "_class": "event_filter",
  "_id": "550e8400-e29b-41d4-a716-446655440001",
  "name": "my-first-filter",
  "nodeId": "550e8400-e29b-41d4-a716-446655440000",
  "type": "EVENT",
  "scope": "CONTRACT",
  "specification": {
    "signature": "Transfer(address,address,uint256)",
    "correlationId": 0
  },
  "statuses": [
    "CONFIRMED",
    "UNCONFIRMED",
    "INVALIDATED"
  ],
  "sync": {
    "type": "BLOCK_BASED",
    "initialBlock": 1
  },
  "visibility": {
    "visible": false,
    "privacyGroupId": "550e8400-e29b-41d4-a716-446655440000"
  },
  "address": "0x68002107b36a11aa8661319449f5cd64fe0d3ecf"
}
```

### Example B — Event filter (GLOBAL scope)

```json
{
  "_class": "event_filter",
  "_id": "f8e2a7b9-7a24-41cf-86d0-bc29a1e1d233",
  "name": "global-events",
  "nodeId": "550e8400-e29b-41d4-a716-446655440000",
  "type": "EVENT",
  "scope": "GLOBAL",
  "specification": {
    "signature": "Transfer(address,address,uint256)",
    "correlationId": 0
  },
  "statuses": [
    "CONFIRMED",
    "UNCONFIRMED"
  ],
  "sync": {
    "type": "BLOCK_BASED",
    "initialBlock": -1
  }
}
```

### Example C — Transaction filter

```json
{
  "_class": "transaction_filter",
  "_id": "550e8400-e29b-41d4-a716-446655440002",
  "name": "my-second-filter",
  "nodeId": "550e8400-e29b-41d4-a716-446655440000",
  "type": "TRANSACTION",
  "identifierType": "FROM_ADDRESS",
  "value": "0xf39Fd6e51aad88F6F4ce6aB8827279cffFb92266",
  "statuses": [
    "FAILED",
    "CONFIRMED",
    "UNCONFIRMED"
  ]
}
```

### Minimal document — Partial override

```json
{
  "_class": "event_filter",
  "_id": "550e8400-e29b-41d4-a716-446655440001",
  "statuses": [
    "CONFIRMED"
  ]
}
```

### Field reference — Filter

| Path                                  | Type          | Required | Default (if omitted) | Notes                                                                                       |
|---------------------------------------|---------------|:--------:|----------------------|---------------------------------------------------------------------------------------------|
| `_class`                              | string        |    ✅     | —                    | `"event_filter"` \| `"transaction_filter"`                                                  |
| `_id`                                 | string (UUID) |    ✅     | —                    | Unique filter_id. Used to merge with YAML by_id.                                            |
| `name`                                | string        |    ✅     | —                    | Human-friendly name.                                                                        |
| `nodeId`                              | string (UUID) |    ✅     | —                    | Must reference an existing **Node**.                                                        |
| `type`                                | string        |    ✅     | —                    | `EVENT` \| `TRANSACTION` (redundant with `_class`, kept for readability).                   |
| `scope` *(EVENT only)*                | string        |    ❌     | `CONTRACT`           | `CONTRACT` \| `GLOBAL`.                                                                     |
| `address` *(EVENT + CONTRACT)*        | string        |    ❌     | —                    | Contract address (hex for Ethereum).                                                        |
| `specification` *(EVENT only)*        | object        |    ❌     | —                    | Event signature & correlation.                                                              |
| `specification.signature`             | string        |    ❌     | —                    | e.g., `Transfer(address,address,uint256)`.                                                  |
| `specification.correlationId`         | number        |    ❌     | `0`                  | Domain-defined correlation_id.                                                              |
| `statuses`                            | array<string> |    ❌     | all statuses         | EVENT: `CONFIRMED`, `UNCONFIRMED`, `INVALIDATED`. TX: `FAILED`, `CONFIRMED`, `UNCONFIRMED`. |
| `sync` *(EVENT only)*                 | object        |    ❌     | —                    | Sync strategy.                                                                              |
| `sync.type`                           | string        |    ❌     | `BLOCK_BASED`        | Currently only `BLOCK_BASED`.                                                               |
| `sync.initialBlock`                   | number        |    ❌     | `-1`                 | `-1` means latest; otherwise positive block number.                                         |
| `visibility` *(EVENT on private ETH)* | object        |    ❌     | —                    | Only for Ethereum private networks.                                                         |
| `visibility.visible`                  | boolean       |    ❌     | `true`               | Whether the event is visible.                                                               |
| `visibility.privacyGroupId`           | string (UUID) |    ❌     | —                    | Privacy group identifier.                                                                   |
| `identifierType` *(TRANSACTION only)* | string (enum) |    ❌     | —                    | e.g., `FROM_ADDRESS`.                                                                       |
| `value` *(TRANSACTION only)*          | string        |    ❌     | —                    | Value matching `identifierType` (e.g., hex address).                                        |

---

## 🗄️ Store Configuration

The `persistence-spring-mongo` module retrieves store configuration documents from the `event_stores`
collection. Stores are **per node** (the document `_id` equals the **Node ID**).

> **Polymorphism & `@TypeAlias`**
> Root store documents are polymorphic and include a discriminator field `_class`.
> **Aliases**: `active_event_store` (active store with features), `inactive_event_store` (disabled).
> Store **features** are also polymorphic sub-documents:
> - `block_event_store` — event store feature for block-based strategies and targets.
> - `filter_store` — filter-sync feature (optional destination).

### Collections & Repository

- **Collection:** `stores_configuration`

### Example A — Active store (with features)

```json
{
  "_class": "active_store",
  "_id": "550e8400-e29b-41d4-a716-446655440000",
  "type": "http",
  "features": {
    "EVENT": {
      "_class": "block_event_store",
      "targets": [
        {
          "type": "BLOCK",
          "destination": "/blocks"
        },
        {
          "type": "TRANSACTION",
          "destination": "/txs"
        }
      ]
    },
    "FILTER_SYNC": {
      "_class": "filter_store",
      "destination": "/filters"
    }
  },
  "additionalProperties": {
    "retention": {
      "days": 7
    }
  },
  "propertiesSchema": {
    "type": "event_store_mongo",
    "fields": [
      {
        "name": "retention.days",
        "type": "int",
        "required": false,
        "defaultValue": 7
      }
    ]
  }
}
```

### Example B — Inactive store

```json
{
  "_class": "inactive_store",
  "_id": "cad022c2-3e41-426f-bb82-c0b86d58d675"
}
```

### Example C — Minimal partial override (merge with YAML by priority)

```json
{
  "_class": "active_store",
  "_id": "550e8400-e29b-41d4-a716-446655440000",
  "features": {
    "FILTER_SYNC": {
      "_class": "filter_store",
      "destination": "/filters-updated"
    }
  }
}
```

### Field reference — Store (root)

| Path                              | Type          | Required | Default (if omitted) | Notes                                                      |
|-----------------------------------|---------------|:--------:|----------------------|------------------------------------------------------------|
| `_class`                          | string        |    ✅     | —                    | `active_event_store` \| `inactive_event_store`             |
| `_id`                             | string (UUID) |    ✅     | —                    | **Node ID**. Used as Mongo `_id`.                          |
| `type` *(active)*                 | string        |    ✅     | —                    | Store type (string form of `StoreType`, e.g., `"mongo"`).  |
| `features` *(active)*             | object (map)  |    ❌     | —                    | Map `StoreFeatureType` → feature doc (see below).          |

### Field reference — Feature documents

#### Event feature

| Path       | Type          | Required | Default (if omitted) | Notes                            |
|------------|---------------|:--------:|----------------------|----------------------------------|
| `_class`   | string        |    ✅     | —                    | `block_event_store`.             |
| `targets`  | array<object> |    ❌     | —                    | Set of event target descriptors. |

**Event target descriptor**

| Path          | Type          | Required | Default (if omitted) | Notes                                                                           |
|---------------|---------------|:--------:|----------------------|---------------------------------------------------------------------------------|
| `type`        | string (enum) |    ✅     | —                    | `TargetType` (e.g., `BLOCK`, `TRANSACTION`, `CONTRACT_EVENT`, `FILTER`, `ALL`). |
| `destination` | string        |    ✅     | —                    | Path/topic/queue to store/broadcast events.                                     |

#### Filter-sync feature

| Path          | Type   | Required | Default (if omitted) | Notes                                                                                                                                                    |
|---------------|--------|:--------:|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| `_class`      | string |    ✅     | —                    | `filter_store`.                                                                                                                                          |
| `destination` | string |    ❌     | —                    | Optional destination for filter-sync output.<br/>Ignored if store's `type` is `mongo` or `jpa`, as the db schema details are defined by the application. |

---

### Notes & Best Practices

- **One store document per node**: `_id` equals the **Node ID**; update the same document to change the store for that
  node.
- **Partial overrides**: Put only the fields you want to override in MongoDB; remaining will be read from YAML.
- **Do not rename aliases**: Changing `@TypeAlias` values after data exists will break deserialization.
- **Consistency**: Keep enum values consistent with project types (e.g., `StoreType`, `StoreState`, `StoreFeatureType`,
  `EventStoreStrategy`, `TargetType`).


## 👈 Previous steps

1. [Getting Started](../getting_started.md)
2. [Naryo Configuration](./index.md)
3. [Core Configuration Overview](./configuration-core.md)

## 👉 Next steps

1. [Tutorials](../tutorials/index.md)
