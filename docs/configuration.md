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
      configuration:
        endpoint:
          url: "https://example.com/broadcast"
  broadcasters:
    - configurationId: "550e8400-e29b-41d4-a716-446655440000"
      type: FILTER # Also supports BLOCK, TRANSACTION, CONTRACT_EVENT or ALL
      destination: "/block" # depends on the type of broadcaster
      configuration:
        filterId: "550e8400-e29b-41d4-a716-446655440001"
```

| Parameter                                  | Description                                           | Value Type | Default Value |
|--------------------------------------------|-------------------------------------------------------|------------|---------------|
| `configuration`                            | List of broadcasting configurations.                  | List       |               |
| `configuration.id`                         | Unique identifier for the broadcasting configuration. | UUID       |               |
| `configuration.type`                       | Type of broadcasting.                                 | String     |               |
| `configuration.cache`                      | Cache configuration.                                  | Object     |               |
| `configuration.cache.expirationTime`       | Expiration time for the cache.                        | Duration   | 5m            |
| `configuration.configuration`              | Configuration for the broadcasting.                   | Object     |               |
| `configuration.configuration.endpoint`     | Endpoint configuration.                               | Object     |               |
| `configuration.configuration.endpoint.url` | URL of the endpoint.                                  | String     |               |
| `broadcasters`                             | List of broadcasters.                                 | List       |               |
| `broadcasters.configurationId`             | ID of the broadcasting configuration to use.          | UUID       |               |
| `broadcasters.type`                        | Type of broadcaster.                                  | String     |               |
| `broadcasters.destination`                 | Destination of the broadcaster.                       | String     |               |
| `broadcasters.configuration`               | Configuration for the broadcaster.                    | Object     |               |
| `broadcasters.configuration.filterId`      | ID of the filter to broadcast.                        | UUID       |               |

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
      configuration:
        method:
          type: POLL # Also supports PUBSUB
          configuration:
            interval: 1s
        initialBlock: 1
        confirmationBlocks: 12
        missingTxBlocks: 200
        eventInvalidationBlockThreshold: 2
        replayBlockOffset: 12
        syncBlockLimit: 20000
    interaction:
      strategy: BLOCK_BASED
      configuration:
        mode: HEDERA_MIRROR_NODE
        configuration:
          limitPerRequest: 100 # Only necessary for HEDERA_MIRROR_NODE
          retriesPerRequest: 3 # Only necessary for HEDERA_MIRROR_NODE
    connection:
      type: HTTP # Also supports WS
      retry:
        times: 3
        backoff: 1s
      endpoint:
        url: "https://mainnet.hashio.io/api"
      configuration: # Only necessary for HTTP
        maxIdleConnections: 5
        keepAliveDuration: 5m
        connectionTimeout: 10s
        readTimeout: 30s
```

| Parameter                                                          | Description                                                 | Value Type | Default Value |
|--------------------------------------------------------------------|-------------------------------------------------------------|------------|---------------|
| `nodes`                                                            | List of nodes.                                              | List       |               |
| `nodes.id`                                                         | Unique identifier for the node.                             | UUID       |               |
| `nodes.name`                                                       | Name of the node.                                           | String     |               |
| `nodes.type`                                                       | Type of node.                                               | String     | ETHEREUM      |
| `nodes.subscription`                                               | Subscription configuration.                                 | Object     |               |
| `nodes.subscription.strategy`                                      | Subscription strategy.                                      | String     | BLOCK_BASED   |
| `nodes.subscription.configuration`                                 | Subscription configuration.                                 | Object     |               |
| `nodes.subscription.configuration.method`                          | Method configuration.                                       | Object     |               |
| `nodes.subscription.configuration.method.type`                     | Type of method.                                             | String     | POLL          |
| `nodes.subscription.configuration.method.configuration`            | Method configuration.                                       | Object     |               |
| `nodes.subscription.configuration.method.configuration.interval`   | Interval for polling.                                       | Duration   | 1s            |
| `nodes.subscription.configuration.initialBlock`                    | Initial block.                                              | Integer    | -1 (Latest)   |
| `nodes.subscription.configuration.confirmationBlocks`              | Confirmation blocks.                                        | Integer    | 12            |
| `nodes.subscription.configuration.missingTxBlocks`                 | Missing transaction blocks.                                 | Integer    | 200           |
| `nodes.subscription.configuration.eventInvalidationBlockThreshold` | Event invalidation block threshold.                         | Integer    | 2             |
| `nodes.subscription.configuration.replayBlockOffset`               | Replay block offset.                                        | Integer    | 12            |
| `nodes.subscription.configuration.syncBlockLimit`                  | Sync block limit.                                           | Integer    | 20000         |
| `nodes.interaction`                                                | Interaction configuration.                                  | Object     |               |
| `nodes.interaction.strategy`                                       | Interaction strategy.                                       | String     | BLOCK_BASED   |
| `nodes.interaction.configuration`                                  | Interaction configuration.                                  | Object     |               |
| `nodes.interaction.configuration.mode`                             | Interaction mode.                                           | String     | ETHEREUM_RPC  |
| `nodes.interaction.configuration.configuration`                    | Interaction configuration.                                  | Object     |               |
| `nodes.interaction.configuration.configuration.limitPerRequest`    | Limit per request. Only necessary for HEDERA_MIRROR_NODE.   | Integer    | 100           |
| `nodes.interaction.configuration.configuration.retriesPerRequest`  | Retries per request. Only necessary for HEDERA_MIRROR_NODE. | Integer    | 3             |
| `nodes.connection`                                                 | Connection configuration.                                   | Object     |               |
| `nodes.connection.type`                                            | Type of connection.                                         | String     |               |
| `nodes.connection.retry`                                           | Retry configuration.                                        | Object     |               |
| `nodes.connection.retry.times`                                     | Number of times to retry.                                   | Integer    | 3             |
| `nodes.connection.retry.backoff`                                   | Backoff duration.                                           | Duration   | 1s            |
| `nodes.connection.endpoint`                                        | Endpoint configuration.                                     | Object     |               |
| `nodes.connection.endpoint.url`                                    | URL of the endpoint.                                        | String     |               |
| `nodes.connection.configuration`                                   | Connection configuration.                                   | Object     |               |
| `nodes.connection.configuration.maxIdleConnections`                | Maximum idle connections. Only necessary for HTTP.          | Integer    | 5             |
| `nodes.connection.configuration.keepAliveDuration`                 | Keep-alive duration. Only necessary for HTTP.               | Duration   | 5m            |
| `nodes.connection.configuration.connectionTimeout`                 | Connection timeout. Only necessary for HTTP.                | Duration   | 10s           |
| `nodes.connection.configuration.readTimeout`                       | Read timeout. Only necessary for HTTP.                      | Duration   | 30s           |

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
    configuration:
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
        configuration:
          initialBlock: 1
      visibilityConfiguration: # Only necessary for ETHEREUM private network
        visible: false
        privacyGroupId: "550e8400-e29b-41d4-a716-446655440000"
      configuration: # Only necessary for CONTRACT type
        address: "0x68002107b36a11aa8661319449f5cd64fe0d3ecf"
  - id: "550e8400-e29b-41d4-a716-446655440002"
    name: "my-second-filter"
    type: TRANSACTION
    nodeId: "550e8400-e29b-41d4-a716-446655440000"
    configuration:
      identifierType: FROM_ADDRESS
      value: "0xf39Fd6e51aad88F6F4ce6aB8827279cffFb92266"
      statuses:
        - FAILED
        - CONFIRMED
        - UNCONFIRMED
```

| Parameter                                                      | Description                       | Value Type | Default Value |
|----------------------------------------------------------------|-----------------------------------|------------|---------------|
| `filters`                                                      | List of filters.                  | List       |               |
| `filters.id`                                                   | Unique identifier for the filter. | UUID       |               |
| `filters.name`                                                 | Name of the filter.               | String     |               |
| `filters.type`                                                 | Type of filter.                   | String     |               |
| `filters.nodeId`                                               | Node ID.                          | UUID       |               |
| `filters.configuration`                                        | Filter configuration.             | Object     |               |
| `filters.configuration.scope`                                  | Scope of the filter.              | String     |               |
| `filters.configuration.specification`                          | Specification of the filter.      | Object     |               |
| `filters.configuration.specification.signature`                | Signature of the filter.          | String     |               |
| `filters.configuration.specification.correlationId`            | Correlation ID of the filter.     | Integer    |               |
| `filters.configuration.statuses`                               | Statuses of the filter.           | List       | All statuses  |
| `filters.configuration.sync`                                   | Sync configuration.               | Object     |               |
| `filters.configuration.sync.type`                              | Type of sync.                     | String     | BLOCK_BASED   |
| `filters.configuration.sync.configuration`                     | Sync configuration.               | Object     |               |
| `filters.configuration.sync.configuration.initialBlock`        | Initial block.                    | Integer    |               |
| `filters.configuration.visibilityConfiguration`                | Visibility configuration.         | Object     |               |
| `filters.configuration.visibilityConfiguration.visible`        | Visibility of the filter.         | Boolean    | true          |
| `filters.configuration.visibilityConfiguration.privacyGroupId` | Privacy group ID of the filter.   | UUID       |               |
| `filters.configuration.configuration`                          | Configuration of the filter.      | Object     |               |
| `filters.configuration.configuration.address`                  | Address of the filter.            | String     |               |
| `filters.configuration.configuration.identifierType`           | Identifier type of the filter.    | String     |               |
| `filters.configuration.configuration.value`                    | Value of the filter.              | String     |               |

> **Note**: Keep in mind that there are multiple filter types, such as EVENT and TRANSACTION. Also, there are
> multiple node types, such as ETHEREUM and HEDERA.

## 👈 Previous steps

1. [Getting Started](./getting_started.md)

## 👉 Next steps

1. [Tutorials](./tutorials/index.md)
