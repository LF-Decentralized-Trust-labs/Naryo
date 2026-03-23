# Core Configuration Reference

This document provides a comprehensive reference for all configuration properties within Naryo's core module.

## Configuration Formats

Naryo supports two configuration formats: YAML (`application.yml`) and Java Properties (`application.properties`). You can use either format based on your preference.

### YAML (`.yml`)

YAML is a human-readable data serialization standard. It is often preferred for its clean and indented syntax, which is great for hierarchical data.

```yaml
naryo:
  httpClient:
    maxIdleConnections: 10
    keepAliveDuration: 10m
  nodes:
    - id: "e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e"
      name: "My Ethereum Node"
      # ... more node properties
```

### Java Properties (`.properties`)

The `.properties` format is a simple key-value store. It's straightforward but can be more verbose for complex, nested configurations.

```properties
naryo.httpClient.maxIdleConnections=10
naryo.httpClient.keepAliveDuration=10m

naryo.nodes[0].id=e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e
naryo.nodes[0].name=My Ethereum Node
# ... more node properties
```

In this guide, we'll use dot notation (e.g., `naryo.httpClient.maxIdleConnections`) to describe properties, which maps directly to the `.properties` format and corresponds to the nested structure in YAML.

## Root Configuration (`naryo`)

All Naryo configurations are nested under the `naryo` root property.

| Property       | Type           | Description                                                             |
|----------------|----------------|-------------------------------------------------------------------------|
| `httpClient`   | `Object`       | Global HTTP client settings for Naryo's internal and external requests. |
| `broadcasting` | `Object`       | Configuration for broadcasting blockchain events to external systems.   |
| `nodes[]`      | `List<Object>` | A list of blockchain nodes for Naryo to connect to and monitor.         |
| `filters[]`    | `List<Object>` | A list of filters to capture specific events from the configured nodes. |
| `stores[]`     | `List<Object>` | Configuration for persisting blockchain data and Naryo's state.         |

## HTTP Client (`naryo.httpClient`)

This section defines the global configuration for the HTTP client used throughout Naryo. It's used for connecting to blockchain nodes, sending data to HTTP broadcasters, and other internal tasks.

### Properties

| Property                   | Type       | Description                                                             | Default Value |
|----------------------------|------------|-------------------------------------------------------------------------|---------------|
| `maxIdleConnections`       | `Integer`  | The maximum number of idle connections to keep in the connection pool.  | `5`           |
| `keepAliveDuration`        | `Duration` | How long to keep idle connections alive in the pool.                    | `5m`          |
| `connectTimeout`           | `Duration` | The maximum time to wait for establishing a connection.                 | `10s`         |
| `readTimeout`              | `Duration` | The maximum time to wait for reading data from a connection.            | `30s`         |
| `writeTimeout`             | `Duration` | The maximum time to wait for writing data to a connection.              | `30s`         |
| `callTimeout`              | `Duration` | The maximum total time for a complete HTTP call (connect, write, read). | `60s`         |
| `pingInterval`             | `Duration` | The interval at which to ping a connection to keep it alive.            | `15s`         |
| `retryOnConnectionFailure` | `Boolean`  | Whether to automatically retry a request if the connection fails.       | `true`        |

### Examples

**YAML (`application.yml`)**
```yaml
naryo:
  httpClient:
    maxIdleConnections: 10
    keepAliveDuration: 10m
    connectTimeout: 20s
```

**Properties (`application.properties`)**
```properties
naryo.httpClient.maxIdleConnections=10
naryo.httpClient.keepAliveDuration=10m
naryo.httpClient.connectTimeout=20s
```

## Node (`naryo.nodes[]`)

This section configures the blockchain nodes that Naryo will connect to and monitor.

### Properties

| Property       | Type     | Description                                      | Required | Default                 |
|----------------|----------|--------------------------------------------------|----------|-------------------------|
| `id`           | `UUID`   | A unique identifier for the node.                | Yes      | —                       |
| `name`         | `String` | A human-readable name for the node.              | No       | —                       |
| `type`         | `String` | The type of blockchain (`ETHEREUM` or `HEDERA`). | Yes      | `ETHEREUM`              |
| `connection`   | `Object` | Connection details for the node.                 | Yes      | —                       |
| `subscription` | `Object` | How Naryo subscribes to events from the node.    | No       | Strategy: `BLOCK_BASED` |
| `interaction`  | `Object` | How Naryo interacts with the node.               | No       | Strategy: `BLOCK_BASED` |

### Connection (`naryo.nodes[].connection`)

Defines how Naryo reaches the node.

#### Properties

| Property        | Type       | Description                                                                                          | Required | Default |
|-----------------|------------|------------------------------------------------------------------------------------------------------|----------|---------|
| `type`          | `Enum`     | Connection transport: `HTTP` or `WS`.                                                                | Yes      | —       |
| `endpoint.url`  | `String`   | Full URL to the node endpoint (e.g., `https://mainnet.infura.io/v3/<key>` or `ws://localhost:8546`). | Yes      | —       |
| `retry.times`   | `Integer`  | Number of reconnect attempts before failing.                                                         | No       | `3`     |
| `retry.backoff` | `Duration` | Delay between reconnect attempts (e.g., `1s`, `500ms`, `2m`).                                        | No       | `30s`   |

Example (YAML):
```yaml
naryo:
  nodes:
    - id: "e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e"
      name: "My Ethereum Node"
      type: ETHEREUM
      connection:
        type: HTTP
        endpoint:
          url: "https://mainnet.infura.io/v3/<api-key>"
        retry:
          times: 5
          backoff: 2s
```

Example (properties):
```properties
naryo.nodes[0].id=e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e
naryo.nodes[0].name=My Ethereum Node
naryo.nodes[0].type=ETHEREUM
naryo.nodes[0].connection.type=HTTP
naryo.nodes[0].connection.endpoint.url=https://mainnet.infura.io/v3/<api-key>
naryo.nodes[0].connection.retry.times=5
naryo.nodes[0].connection.retry.backoff=2s
```

### Subscription (`naryo.nodes[].subscription`)

Controls how new blocks are discovered and processed. Strategy is `BLOCK_BASED`.

#### Properties (common)

| Property                          | Type         | Description                                                   | Required | Default |
|-----------------------------------|--------------|---------------------------------------------------------------|----------|---------|
| `method`                          | `Enum`       | `POLL` or `PUBSUB`.                                           | Yes      | `POLL`  |
| `initialBlock`                    | `BigInteger` | Starting block for processing/synchronization.                | No       | `-1`    |
| `confirmationBlocks`              | `BigInteger` | Number of confirmations before marking events as `CONFIRMED`. | No       | `12`    |
| `missingTxRetryBlocks`            | `BigInteger` | Retry depth for missing transaction lookups.                  | No       | `200`   |
| `eventInvalidationBlockThreshold` | `BigInteger` | Depth threshold for considering reorg invalidations.          | No       | `2`     |
| `replayBlockOffset`               | `BigInteger` | Number of blocks to step back on (re)start.                   | No       | `12`    |
| `syncBlockLimit`                  | `BigInteger` | Maximum number of blocks to process in a single sync batch.   | No       | `20000` |

#### POLL – Specific properties

| Property   | Type       | Description                               | Required | Default |
|------------|------------|-------------------------------------------|----------|---------|
| `interval` | `Duration` | Poll interval (e.g., `1s`, `500ms`, `2s`) | No       | `5s`    |

#### PUBSUB – Specific properties

| Property | Type | Description                                                        | Required | Default |
|----------|------|--------------------------------------------------------------------|----------|---------|
| —        | —    | Uses node push notifications; no additional method-specific fields | —        | —       |

Examples (YAML):
```yaml
naryo:
  nodes:
    - id: "e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e"
      type: ETHEREUM
      connection:
        type: HTTP
        endpoint:
          url: "https://mainnet.infura.io/v3/<api-key>"
      subscription:
        # POLL method
        method: POLL
        initialBlock: 19000000
        confirmationBlocks: 12
        interval: 1s
        syncBlockLimit: 500
    - id: "11111111-1111-1111-1111-111111111111"
      type: ETHEREUM
      connection:
        type: WS
        endpoint:
          url: "wss://mainnet.infura.io/ws/v3/<api-key>"
      subscription:
        # PUBSUB method
        method: PUBSUB
        confirmationBlocks: 12
        replayBlockOffset: 2
```

Examples (properties):
```properties
# POLL
naryo.nodes[0].subscription.method=POLL
naryo.nodes[0].subscription.initialBlock=19000000
naryo.nodes[0].subscription.confirmationBlocks=12
naryo.nodes[0].subscription.interval=1s
naryo.nodes[0].subscription.syncBlockLimit=500

# PUBSUB
naryo.nodes[1].subscription.method=PUBSUB
naryo.nodes[1].subscription.confirmationBlocks=12
naryo.nodes[1].subscription.replayBlockOffset=2
```

### Interaction (`naryo.nodes[].interaction`)

Specifies the implementation used to fetch and interpret blockchain data after subscription triggers a block to process.

| Mode                 | Default for | Notes |
|----------------------|-------------|-------|
| `ETHEREUM_RPC`       | `ETHEREUM`  | —     |
| `HEDERA_MIRROR_NODE` | `HEDERA`    | —     |

#### Properties

| Property | Type   | Description                                 | Required | Default        |
|----------|--------|---------------------------------------------|----------|----------------|
| `mode`   | `Enum` | One of `ETHEREUM_RPC`, `HEDERA_MIRROR_NODE` | Yes      | `ETHEREUM_RPC` |

#### HEDERA_MIRROR_NODE – Specific properties

| Property            | Type      | Description                      | Required | Default |
|---------------------|-----------|----------------------------------|----------|---------|
| `limitPerRequest`   | `Integer` | Page size for mirror node calls. | No       | —       |
| `retriesPerRequest` | `Integer` | Retries per page request.        | No       | —       |

Examples (YAML):
```yaml
naryo:
  nodes:
    - id: "8c2f2a96-7b4a-4a54-9d2d-4b62e5f8e9a0"
      type: HEDERA
      name: "Hedera Mirror"
      connection:
        type: HTTP
        endpoint:
          url: "https://testnet.mirrornode.hedera.com"
      subscription:
        method: POLL
        interval: 2s
      interaction:
        mode: HEDERA_MIRROR_NODE
        limitPerRequest: 100
        retriesPerRequest: 3
```

Examples (properties):
```properties
naryo.nodes[2].id=8c2f2a96-7b4a-4a54-9d2d-4b62e5f8e9a0
naryo.nodes[2].type=HEDERA
naryo.nodes[2].name=Hedera Mirror
naryo.nodes[2].connection.type=HTTP
naryo.nodes[2].connection.endpoint.url=https://testnet.mirrornode.hedera.com
naryo.nodes[2].subscription.method=POLL
naryo.nodes[2].subscription.interval=2s
naryo.nodes[2].interaction.mode=HEDERA_MIRROR_NODE
naryo.nodes[2].interaction.limitPerRequest=100
naryo.nodes[2].interaction.retriesPerRequest=3
```

## Filter (`naryo.filters[]`)

This section configures the filters that Naryo uses to capture specific blockchain events.

### Properties

| Property         | Type           | Description                                                     | Required                         | Default |
|------------------|----------------|-----------------------------------------------------------------|----------------------------------|---------|
| `id`             | `UUID`         | A unique identifier for the filter.                             | Yes                              | —       |
| `name`           | `String`       | A human-readable name for the filter.                           | No                               | —       |
| `type`           | `String`       | The type of filter (`EVENT` or `TRANSACTION`).                  | Yes                              | `EVENT` |
| `nodeId`         | `UUID`         | The ID of the node this filter is attached to.                  | Yes                              | —       |
| `scope`          | `String`       | The scope of an `EVENT` filter (`GLOBAL` or `CONTRACT`).        | Yes, if `type` is `EVENT`.       | `GLOBAL` |
| `address`        | `String`       | The contract address for a `CONTRACT` scoped `EVENT` filter.    | Yes, if `scope` is `CONTRACT`.   | —       |
| `specification`  | `Object`       | The event specification for an `EVENT` filter.                  | Yes, if `type` is `EVENT`.       | —       |
| `identifierType` | `String`       | The identifier type for a `TRANSACTION` filter.                 | Yes, if `type` is `TRANSACTION`. | —       |
| `value`          | `String`       | The value to filter on for a `TRANSACTION` filter.              | Yes, if `type` is `TRANSACTION`. | —       |
| `statuses`       | `List<String>` | The statuses to filter on.                                      | No                               | —       |
| `sync`           | `Object`       | Historical synchronization settings for an `EVENT` filter.      | No                               | Type: `BLOCK_BASED` |
| `visibility`     | `Object`       | Visibility settings for an `EVENT` filter on a private network. | No                               | —       |

### Filter Types

- `EVENT` filters capture contract or global events (logs). See `Event filters` below.
- `TRANSACTION` filters capture transactions by identifier. See `Transaction filters` below.

### Event filters (`type: EVENT`)

**Scope**:

| Value       | Description                                   |
|-------------|-----------------------------------------------|
| `GLOBAL`    | Match events emitted by any address.          |
| `CONTRACT`  | Restrict to a specific `address`.             |

**Statuses**:

| Value         | Description                     |
|---------------|---------------------------------|
| `CONFIRMED`   | Confirmed by N block depth.     |
| `UNCONFIRMED` | Seen but not yet confirmed.     |
| `INVALIDATED` | Later invalidated (e.g. reorg). |

**Specification**:

| Property         | Type       | Description                                                                 | Required | Default |
|------------------|------------|-----------------------------------------------------------------------------|----------|---------|
| `signature`      | `String`   | Event signature/topic selector (e.g., `Transfer(address,address,uint256)`). | No       | —       |
| `correlationId`  | `Integer`  | Arbitrary ID to tag/route events downstream.                                | No       | —       |

**Sync**:

| Property       | Type         | Description                                               | Required | Default       |
|----------------|--------------|-----------------------------------------------------------|----------|---------------|
| `strategy`     | `Enum`       | Must be `BLOCK_BASED`.                                    | Yes      | `BLOCK_BASED` |
| `initialBlock` | `BigInteger` | First block to start historical catch-up for this filter. | No       | —             |

**Visibility**:

| Property          | Type      | Description                                                                 | Required | Default |
|-------------------|-----------|-----------------------------------------------------------------------------|----------|---------|
| `visible`         | `Boolean` | Whether the filter is visible on public outputs.                            | No       | —       |
| `privacyGroupId`  | `String`  | Group ID for private networks (e.g., Orion/Tessera-like privacy contexts).  | No       | —       |

Examples — Event filter (YAML):
```yaml
naryo:
  filters:
    - id: "6d3b0b3c-3a77-4dfe-8d2c-3f4b5a6c7d8e"
      name: "ERC20 Transfer on USDC"
      type: EVENT
      nodeId: "e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e"
      scope: CONTRACT
      address: "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48"
      statuses: [CONFIRMED, UNCONFIRMED]
      specification:
        signature: "Transfer(address,address,uint256)"
        correlationId: 101
      sync:
        strategy: BLOCK_BASED
        initialBlock: 16000000
      visibility:
        visible: true
```

Examples — Event filter (properties):
```properties
naryo.filters[0].id=6d3b0b3c-3a77-4dfe-8d2c-3f4b5a6c7d8e
naryo.filters[0].name=ERC20 Transfer on USDC
naryo.filters[0].type=EVENT
naryo.filters[0].nodeId=e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e
naryo.filters[0].scope=CONTRACT
naryo.filters[0].address=0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48
naryo.filters[0].statuses[0]=CONFIRMED
naryo.filters[0].statuses[1]=UNCONFIRMED
naryo.filters[0].specification.signature=Transfer(address,address,uint256)
naryo.filters[0].specification.correlationId=101
naryo.filters[0].sync.strategy=BLOCK_BASED
naryo.filters[0].sync.initialBlock=16000000
naryo.filters[0].visibility.visible=true
```

### Transaction filters (`type: TRANSACTION`)

**Identifier type**:

| Value          | Description                                    |
|----------------|------------------------------------------------|
| `HASH`         | Match a transaction by its hash.               |
| `TO_ADDRESS`   | Match transactions by recipient address.       |
| `FROM_ADDRESS` | Match transactions by sender address.          |
| `IDENTITY_ID`  | Match by identity/entity id (Hedera-specific). |

Properties

| Property          | Type     | Description                                   | Required | Default |
|-------------------|----------|-----------------------------------------------|----------|---------|
| `identifierType`  | `Enum`   | One of the values listed above.               | Yes      | —       |
| `value`           | `String` | The concrete hash/address/id value to match.  | Yes      | —       |

Examples — Transaction filter (YAML):
```yaml
naryo:
  filters:
    - id: "7e8f9a0b-1c2d-3e4f-5a6b-7c8d9e0f1a2b"
      name: "Monitor Tx by Hash"
      type: TRANSACTION
      nodeId: "e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e"
      identifierType: HASH
      value: "0xabc123..."
```

Examples — Transaction filter (properties):
```properties
naryo.filters[1].id=7e8f9a0b-1c2d-3e4f-5a6b-7c8d9e0f1a2b
naryo.filters[1].name=Monitor Tx by Hash
naryo.filters[1].type=TRANSACTION
naryo.filters[1].nodeId=e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e
naryo.filters[1].identifierType=HASH
naryo.filters[1].value=0xabc123...
```

## Broadcasting (`naryo.broadcasting`)

Broadcasting is the mechanism Naryo uses to send blockchain events to other systems, such as a message queue or a webhook.

The broadcasting configuration is split into two parts:
1.  **`configuration[]`**: A list of reusable broadcaster setups (e.g., connection details for a Kafka cluster).
2.  **`broadcasters[]`**: A list of active broadcasters that link an event target (like a specific contract event) to a configuration.

### `naryo.broadcasting.configuration[]`

Each item in this list defines a reusable configuration for a specific type of broadcaster.

**Properties**

| Property               | Type     | Description                                                                                                                                                                                                        | Required | Default  |
|------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|----------|
| `id`                   | `UUID`   | A unique identifier for this configuration.                                                                                                                                                                        | Yes      | —        |
| `type`                 | `String` | The type of broadcaster. This determines the `additionalProperties` required. See the specific documentation for each type.                                                                                        | Yes      | —        |
| `cache`                | `Object` | Optional caching settings for broadcast messages.                                                                                                                                                                  | No       | disabled |
| `additionalProperties` | `Map`    | Type-specific properties. For details, refer to the broadcaster documentation: [Kafka](./broadcasting/configuration-kafka.md), [RabbitMQ](./broadcasting/configuration-rabbit.md), [HTTP](./broadcasting/http.md). | No       | —        |

**Cache Properties**

| Property         | Type       | Description                                              | Default |
|------------------|------------|----------------------------------------------------------|---------|
| `expirationTime` | `Duration` | How long to cache broadcast messages before they expire. | `5m`    |

### `naryo.broadcasting.broadcasters[]`

Each item in this list defines an active broadcaster, linking a target to a configuration.

**Properties**

| Property          | Type     | Description                                                                   | Required | Default |
|-------------------|----------|-------------------------------------------------------------------------------|----------|---------|
| `id`              | `UUID`   | A unique identifier for this broadcaster.                                     | Yes      | —       |
| `configurationId` | `UUID`   | The `id` of a configuration from the `naryo.broadcasting.configuration` list. | Yes      | —       |
| `target`          | `Object` | Defines which events this broadcaster should handle.                          | Yes      | —       |

**Target Properties**

| Property       | Type           | Description                                                                                 | Default |
|----------------|----------------|---------------------------------------------------------------------------------------------|---------|
| `type`         | `String`       | The type of event to broadcast (`BLOCK`, `TRANSACTION`, `CONTRACT_EVENT`, `FILTER`, `ALL`). | —       |
| `destinations` | `List<String>` | A list of destinations (e.g., Kafka topics, RabbitMQ routing keys).                         | —       |

### Examples

**YAML (`application.yml`)**
```yaml
naryo:
  broadcasting:
    configuration:
      - id: "a1a2a3a4-b1b2-c1c2-d1d2-d3d4d5d6d7d8"
        type: "kafka"
        additionalProperties:
          bootstrap.servers: "localhost:9092"
    broadcasters:
      - id: "f1f2f3f4-e1e2-f1f2-e1e2-f3f4f5f6f7f8"
        configurationId: "a1a2a3a4-b1b2-c1c2-d1d2-d3d4d5d6d7d8"
        target:
          type: "CONTRACT_EVENT"
          destinations:
            - "my-contract-events"
```

**Properties (`application.properties`)**
```properties
naryo.broadcasting.configuration[0].id=a1a2a3a4-b1b2-c1c2-d1d2-d3d4d5d6d7d8
naryo.broadcasting.configuration[0].type=kafka
naryo.broadcasting.configuration[0].additionalProperties.bootstrap.servers=localhost:9092

naryo.broadcasting.broadcasters[0].id=f1f2f3f4-e1e2-f1f2-e1e2-f3f4f5f6f7f8
naryo.broadcasting.broadcasters[0].configurationId=a1a2a3a4-b1b2-c1c2-d1d2-d3d4d5d6d7d8
naryo.broadcasting.broadcasters[0].target.type=CONTRACT_EVENT
naryo.broadcasting.broadcasters[0].target.destinations[0]=my-contract-events
```

## Data Persistence (`naryo.stores[]`)

This section configures how Naryo persists blockchain data and its internal state. Each item in the `stores` list corresponds to a persistence setup for a specific blockchain node.

For detailed information on configuring different database types, see the specific persistence documentation:
- [JPA (SQL) Persistence](./persistence/configuration-jpa.md)
- [MongoDB Persistence](./persistence/configuration-mongo.md)

### Properties

| Property               | Type     | Description                                                                 | Required | Default |
|------------------------|----------|-----------------------------------------------------------------------------|----------|---------|
| `nodeId`               | `UUID`   | The ID of the node this configuration applies to.                           | Yes      | —       |
| `type`                 | `String` | The type of persistence store (`jpa` or `mongo`).                           | Yes      | —       |
| `features`             | `Map`    | A map of feature-specific storage configurations.                           | No       | —       |
| `additionalProperties` | `Map`    | Type-specific properties for the store (e.g., database connection details). | No       | —       |

### Features (`naryo.stores[].features`)

This allows you to control what data is stored.

| Feature       | Description                                                            |
|---------------|------------------------------------------------------------------------|
| `EVENT`       | Configures storage for blockchain events (blocks, transactions, etc.). |
| `FILTER_SYNC` | Configures storage for filter synchronization data.                    |

**EVENT Feature Properties**

| Property    | Type           | Description                                          | Default       |
|-------------|----------------|------------------------------------------------------|---------------|
| `strategy`  | `String`       | The storage strategy (currently only `BLOCK_BASED`). | `BLOCK_BASED` |
| `targets[]` | `List<Object>` | A list of event types to store.                      | —             |

**FILTER_SYNC Feature Properties**

| Property      | Description                                                                    | Default |
|---------------|--------------------------------------------------------------------------------|---------|
| `destination` | A logical name for the storage destination (e.g., a collection or table name). | —       |

### Examples

**YAML (`application.yml`)**
```yaml
naryo:
  stores:
    - nodeId: "e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e"
      type: "JPA"
      features:
        - type: EVENT
          strategy: BLOCK_BASED
          targets:
            - type: "BLOCK"
              destination: "blocks"
            - type: "TRANSACTION"
              destination: "transactions"
        - type: FILTER_SYNC
          destination: "filters"
```

**Properties (`application.properties`)**
```properties
naryo.stores[0].nodeId=e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e
naryo.stores[0].type=mongo
naryo.stores[0].features[0].type=EVENT
naryo.stores[0].features[0].strategy=BLOCK_BASED
naryo.stores[0].features[0].targets[0].type=BLOCK
naryo.stores[0].features[0].targets[0].destination=blocks
naryo.stores[0].features[0].targets[1].type=TRANSACTION
naryo.stores[0].features[0].targets[1].destination=transactions
naryo.stores[0].features[1].type=FILTER_SYNC
naryo.stores[0].features[1].destination=filters
```

## Conclusion & Next Steps

This section covers the core configuration options for Naryo. For more details on specific features,
refer to the relevant documentation:

- [Broadcasting](./broadcasting/index.md)
- [Persistence](./persistence/index.md)
- [Tutorial](../tutorials/index.md)
