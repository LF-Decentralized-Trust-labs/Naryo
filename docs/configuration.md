# 🎉 Naryo Configuration

Naryo supports flexible configuration mechanisms. The new-core module requires manual configuration through custom
implementation due to its framework-agnostic design and wide configuration domain. In contrast, the `core-spring` module
allows traditional Spring Boot configuration using YAML or `.properties` files, based on the `EnvironmentProperties`
model.

## 📚 Overview

The following sections describe the YAML configuration structure for the core-spring module. All configuration must be
placed under the root key `naryo` in the `application.yml` file.

### 🔗️ 1. HTTP Client Configuration

```yaml
httpClient:
  maxIdleConnections: 5
  keepAliveDuration: 5m
  connectTimeout: 10s
  readTimeout: 30s
  writeTimeout: 30s
  callTimeout: 60s
  pingInterval: 15s
  retryOnConnectionFailure: true
```

| Parameter                  | Description                                                            | Value Type | Default Value |
|----------------------------|------------------------------------------------------------------------|------------|---------------|
| `maxIdleConnections`       | The maximum number of idle connections to keep in the connection pool. | Integer    | 5             |
| `keepAliveDuration`        | The maximum duration for which idle connections should be kept alive.  | Duration   | 5m            |
| `connectTimeout`           | The maximum duration for a connection attempt.                         | Duration   | 10s           |
| `readTimeout`              | The maximum duration for a read operation.                             | Duration   | 30s           |
| `writeTimeout`             | The maximum duration for a write operation.                            | Duration   | 30s           |
| `callTimeout`              | The maximum duration for a call operation.                             | Duration   | 60s           |
| `pingInterval`             | The interval at which the connection should be pinged.                 | Duration   | 15s           |
| `retryOnConnectionFailure` | Whether to retry on connection failure.                                | Boolean    | true          |

### 📡 2. Broadcasting Configuration

```yaml
broadcasting:
  configuration:
    - id: "550e8400-e29b-41d4-a716-446655440000"
      type: HTTP # By default, only HTTP is supported
      cache:
        expirationTime: 5m
      endpoint:
        url: "https://example.com/broadcast"
  broadcasters:
    - id: "cad022c2-3e41-426f-bb82-c0b86d58d675"
      configurationId: "550e8400-e29b-41d4-a716-446655440000"
      target:
        type: FILTER # Also supports BLOCK, TRANSACTION, CONTRACT_EVENT or ALL
        destination: "/block" # depends on the type of broadcaster
        filterId: "550e8400-e29b-41d4-a716-446655440001"
```

| Parameter                            | Description                                           | Value Type | Supported by | Values                                          | Default Value  |
|--------------------------------------|-------------------------------------------------------|------------|--------------|-------------------------------------------------|----------------|
| `configuration`                      | List of broadcasting configurations.                  | List       | ALL          | Any                                             | Not applicable |
| `configuration.id`                   | Unique identifier for the broadcasting configuration. | UUID       | ALL          | Any                                             | Not applicable |
| `configuration.type`                 | Type of broadcasting.                                 | String     | ALL          | HTTP, KAFKA, RABBITMQ, etc.                     | Not applicable |
| `configuration.cache`                | Cache configuration.                                  | Object     | ALL          | Any                                             | Not applicable |
| `configuration.cache.expirationTime` | Expiration time for the cache.                        | Duration   | ALL          | Any                                             | 5m             |
| `configuration.endpoint`             | Endpoint configuration.                               | Object     | HTTP         | Any                                             | Not applicable |
| `configuration.endpoint.url`         | URL of the endpoint.                                  | String     | HTTP         | Any                                             | Not applicable |
| `broadcasters`                       | List of broadcasters.                                 | List       | ALL          | Any                                             | Not applicable |
| `broadcasters.id`                    | ID of the broadcasting to use.                        | UUID       | ALL          | Any                                             | Not applicable |
| `broadcasters.configurationId`       | ID of the broadcasting configuration to use.          | UUID       | ALL          | Any                                             | Not applicable |
| `broadcasters.target.type`           | Type of broadcaster.                                  | String     | ALL          | FILTER, CONTRACT_EVENT, BLOCK, TRANSACTION, ALL | Not applicable |
| `broadcasters.target.destination`    | Destination of the broadcaster.                       | String     | ALL          | Any                                             | Not applicable |
| `broadcasters.filterId`              | ID of the filter to broadcast.                        | UUID       | FILTER       | Any                                             | Not applicable |

> **Note**: The broadcaster configuration type only supports `HTTP` by default, but depending on the modules you use, it
> could be extended to support other broadcasting configuration types. Also, the broadcaster type can be set to the
> following:
>
> - `FILTER`: Only broadcasts events based on the filter configuration.
> - `BLOCK`: Broadcasts all block events.
> - `TRANSACTION`: Broadcasts all transaction events.
> - `CONTRACT_EVENT`: Broadcasts all contract events.

### 🌐️ 3. Node Configuration

```yaml
nodes:
  - id: "550e8400-e29b-41d4-a716-446655440000"
    name: "hedera-mainnet"
    type: HEDERA # Also supports ETHEREUM
    subscription:
      strategy: BLOCK_BASED # Only supports BLOCK_BASED
      method:
        type: POLL # Also supports PUBSUB
        interval: 1s
      initialBlock: 1
      confirmationBlocks: 12
      missingTxBlocks: 200
      eventInvalidationBlockThreshold: 2
      replayBlockOffset: 12
      syncBlockLimit: 20000
    interaction:
      strategy: BLOCK_BASED
      mode: HEDERA_MIRROR_NODE
      limitPerRequest: 100 # Only necessary for HEDERA_MIRROR_NODE
      retriesPerRequest: 3 # Only necessary for HEDERA_MIRROR_NODE
    connection:
      type: HTTP # Also supports WS
      retry:
        times: 3
        backoff: 1s
      endpoint:
        url: "https://mainnet.hashio.io/api"
      maxIdleConnections: 5
      keepAliveDuration: 5m
      connectionTimeout: 10s
      readTimeout: 30s
```

| Parameter                                            | Description                         | Value Type | Supported by            | Values                           | Default Value  |
|------------------------------------------------------|-------------------------------------|------------|-------------------------|----------------------------------|----------------|
| `nodes`                                              | List of nodes.                      | List       | ALL                     | Any                              | Not applicable |
| `nodes.id`                                           | Unique identifier for the node.     | UUID       | ALL                     | Any                              | Not applicable |
| `nodes.name`                                         | Name of the node.                   | String     | ALL                     | Any                              | Not applicable |
| `nodes.type`                                         | Type of node.                       | String     | ALL                     | ETHEREUM, HEDERA                 | ETHEREUM       |
| `nodes.subscription`                                 | Subscription configuration.         | Object     | ALL                     | Any                              | Not applicable |
| `nodes.subscription.strategy`                        | Subscription strategy.              | String     | ALL                     | BLOCK_BASED                      | BLOCK_BASED    |
| `nodes.subscription.method`                          | Method configuration.               | Object     | ALL                     | Any                              | Not applicable |
| `nodes.subscription.method.type`                     | Type of method.                     | String     | ALL                     | POLL                             | POLL           |
| `nodes.subscription.method.interval`                 | Interval for polling.               | Duration   | POLL method             | Any                              | 5s             |
| `nodes.subscription.initialBlock`                    | Initial block.                      | Integer    | BLOCK_BASED strategy    | Any                              | -1 (Latest)    |
| `nodes.subscription.confirmationBlocks`              | Confirmation blocks.                | Integer    | BLOCK_BASED strategy    | Any                              | 12             |
| `nodes.subscription.missingTxBlocks`                 | Missing transaction blocks.         | Integer    | BLOCK_BASED strategy    | Any                              | 200            |
| `nodes.subscription.eventInvalidationBlockThreshold` | Event invalidation block threshold. | Integer    | BLOCK_BASED strategy    | Any                              | 2              |
| `nodes.subscription.replayBlockOffset`               | Replay block offset.                | Integer    | BLOCK_BASED strategy    | Any                              | 12             |
| `nodes.subscription.syncBlockLimit`                  | Sync block limit.                   | Integer    | BLOCK_BASED strategy    | Any                              | 20000          |
| `nodes.interaction`                                  | Interaction configuration.          | Object     | ALL                     | Any                              | Not applicable |
| `nodes.interaction.strategy`                         | Interaction strategy.               | String     | ALL                     | BLOCK_BASED                      | BLOCK_BASED    |
| `nodes.interaction.mode`                             | Interaction mode.                   | String     | ALL                     | ETHEREUM_RPC, HEDERA_MIRROR_NODE | ETHEREUM_RPC   |
| `nodes.interaction.limitPerRequest`                  | Limit per request.                  | Integer    | HEDERA_MIRROR_NODE mode | Any                              | 100            |
| `nodes.interaction.retriesPerRequest`                | Retries per request.                | Integer    | HEDERA_MIRROR_NODE mode | Any                              | 3              |
| `nodes.connection`                                   | Connection configuration.           | Object     | ALL                     | Any                              | Not applicable |
| `nodes.connection.type`                              | Type of connection.                 | String     | ALL                     | HTTP, WS                         | Not applicable |
| `nodes.connection.retry`                             | Retry configuration.                | Object     | ALL                     | Any                              | Not applicable |
| `nodes.connection.retry.times`                       | Number of times to retry.           | Integer    | ALL                     | Any                              | 3              |
| `nodes.connection.retry.backoff`                     | Backoff duration.                   | Duration   | ALL                     | Any                              | 1s             |
| `nodes.connection.endpoint`                          | Endpoint configuration.             | Object     | ALL                     | Any                              | Not applicable |
| `nodes.connection.endpoint.url`                      | URL of the endpoint.                | String     | ALL                     | Any                              | Not applicable |
| `nodes.connection.maxIdleConnections`                | Maximum idle connections.           | Integer    | HTTP                    | Any                              | 5              |
| `nodes.connection.keepAliveDuration`                 | Keep-alive duration.                | Duration   | HTTP                    | Any                              | 5m             |
| `nodes.connection.connectionTimeout`                 | Connection timeout.                 | Duration   | HTTP                    | Any                              | 10s            |
| `nodes.connection.readTimeout`                       | Read timeout.                       | Duration   | HTTP                    | Any                              | 30s            |



> **Note**: The configuration may vary depending on the selected options:
> - **Connection types**: e.g., `HTTP`, `WS`
> - **Interaction modes**: e.g., `HEDERA_MIRROR_NODE`, `ETHEREUM_RPC`
> - **Node types**: e.g., `ETHEREUM`, `HEDERA`
> - **Subscription strategies**: e.g., `POLL`, `PUBSUB`
>
> Make sure to configure each component appropriately based on your specific setup.

### ☁️ 4. Filter Configuration

```yaml
filters:
  - id: "550e8400-e29b-41d4-a716-446655440001"
    name: "my-first-filter"
    type: EVENT
    nodeId: "550e8400-e29b-41d4-a716-446655440000"
    scope: CONTRACT # Also supports GLOBAL
    specification:
      signature: Transfer(address,address,uint256)
      correlationId: 0
    statuses:
      - CONFIRMED
      - UNCONFIRMED
      - INVALIDATED
    sync:
      type: BLOCK_BASED
      initialBlock: 1
    visibility: # Only necessary for ETHEREUM private network
      visible: false
      privacyGroupId: "550e8400-e29b-41d4-a716-446655440000"
    address: "0x68002107b36a11aa8661319449f5cd64fe0d3ecf"
  - id: "550e8400-e29b-41d4-a716-446655440002"
    name: "my-second-filter"
    type: TRANSACTION
    nodeId: "550e8400-e29b-41d4-a716-446655440000"
    identifierType: FROM_ADDRESS
    value: "0xf39Fd6e51aad88F6F4ce6aB8827279cffFb92266"
    statuses:
      - FAILED
      - CONFIRMED
      - UNCONFIRMED
```

| Parameter                             | Description                       | Value Type | Supported by                | Values                              | Default Value  |
|---------------------------------------|-----------------------------------|------------|-----------------------------|-------------------------------------|----------------|
| `filters`                             | List of filters.                  | List       | ALL                         | Any                                 | Not applicable |
| `filters.id`                          | Unique identifier for the filter. | UUID       | ALL                         | Any                                 | Not applicable |
| `filters.name`                        | Name of the filter.               | String     | ALL                         | Any                                 | Not applicable |
| `filters.type`                        | Type of filter.                   | String     | ALL                         | EVENT, TRANSACTION                  | Not applicable |
| `filters.nodeId`                      | Node ID.                          | UUID       | ALL                         | Any                                 | Not applicable |
| `filters.scope`                       | Scope of the filter.              | String     | EVENT type                  | CONTRACT, GLOBAL                    | Not applicable |
| `filters.specification`               | Specification of the filter.      | Object     | EVENT type                  | Any                                 | Not applicable |
| `filters.specification.signature`     | Signature of the filter.          | String     | EVENT type                  | Any                                 | Not applicable |
| `filters.specification.correlationId` | Correlation ID of the filter.     | Integer    | EVENT type                  | Any                                 | Not applicable |
| `filters.statuses` (event)            | Statuses of the filter.           | List       | EVENT type                  | CONFIRMED, UNCONFIRMED, UNVALIDATED | All statuses   |
| `filters.sync.type`                   | Type of sync.                     | String     | EVENT type                  | BLOCK_BASED                         | BLOCK_BASED    |
| `filters.sync.initialBlock`           | Initial block.                    | Integer    | EVENT type                  | Any                                 | Not applicable |
| `filters.visibility.visible`          | Visibility of the filter.         | Boolean    | EVENT type                  | Any                                 | true           |
| `filters.visibility.privacyGroupId`   | Privacy group ID of the filter.   | UUID       | EVENT type                  | Any                                 | Not applicable |
| `filters.address`                     | Address of the filter.            | String     | EVENT type & CONTRACT scope | Any                                 | Not applicable |
| `filters.statuses` (transaction)      | Statuses of the filter.           | List       | TRANSACTION type            | FAILED, CONFIRMED, UNCONFIRMED      | All statuses   |
| `filters.identifierType`              | Identifier type of the filter.    | String     | TRANSACTION type            | Any                                 | Not applicable |
| `filters.value`                       | Value of the filter.              | String     | TRANSACTION type            | Any                                 | Not applicable |

> **Note**: Keep in mind that there are multiple filter types, such as EVENT and TRANSACTION. Also, there are
> multiple node types, such as ETHEREUM and HEDERA.

## 👈 Previous steps

1. [Getting Started](./getting_started.md)

## 👉 Next steps

1. [Tutorials](./tutorials/index.md)
