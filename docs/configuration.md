# Configuring Naryo

Naryo can be configured in two primary ways:

## 1. YAML Configuration Files

Naryo uses YAML files to define its configuration, and these files can be customized based on the messaging
technology you choose.

- The default configuration is located in `server/src/main/resources/application.yml`.
- Additional configurations are provided for specific messaging technologies, such as:
    - `application-kafka.yml`
    - `application-rabbitmq.yml`
    - `application-sqs.yml`
- You can override these configurations by
  using [Spring Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)
  to load the appropriate file automatically. Alternatively, you can manually specify a profile when running Naryo.
- To customize settings, place an `application.yml` file alongside your built JAR. This will overlay the default values
  from the included configuration files.

## 2. Environment Variables

You can override any YAML-defined property using environment variables. Naryo supports Spring Boot’s syntax for
environment variable expansion, meaning many properties are defined as:

```yaml
property.name: ${ENV_VARIABLE:defaultValue}
```

This allows you to configure the application dynamically without modifying YAML files, making it easier to deploy in
different environments.

> **Note**: You can use the `setup_env.sh` script to set up environment variables automatically before running
> Naryo.
> This
> simplifies the configuration process by loading predefined variables into your shell session.

For more details on available configuration properties, refer to the documentation or the default YAML files in the
`server/src/main/resources` directory.

| Env Variable                                                          | Default                           | Description                                                                                                                                                                                 |
|-----------------------------------------------------------------------|-----------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| SERVER_PORT                                                           | 8060                              | The port for the naryo instance.                                                                                                                                                       |
| ETHEREUM_BLOCK_STRATEGY                                               | POLL                              | The strategy for obtaining block events from an ethereum node (POLL or PUBSUB). It will be overwritten by the specific node configuration.                                                  |
| ETHEREUM_NODE_URL                                                     | http://localhost:8545             | The default ethereum node url.                                                                                                                                                              |
| ETHEREUM_NODE_BLOCK_STRATEGY                                          | POLL                              | The strategy for obtaining block events for the ethereum node (POLL or PUBSUB).                                                                                                             |
| ETHEREUM_NODE_HEALTHCHECK_POLL_INTERVAL                               | 2000                              | The interval time in ms, in which a request is made to the ethereum node, to ensure that the node is running and functional.                                                                |
| ETHEREUM_NODE_ADD_TRANSACTION_REVERT_REASON                           | false                             | In case of a failing transaction it indicates if Naryo should get the revert reason. Currently not working for Ganache and Parity.                                                     |
| ETHEREUM_NUM_BLOCKS_TO_REPLAY                                         | 12                                | Number of blocks to replay on node or service failure (ensures no blocks / events are missed on chain reorg)                                                                                |
| POLLING_INTERVAL                                                      | 10000                             | The polling interval used by Web3j to get events from the blockchain.                                                                                                                       |
| EVENT_STORE_TYPE                                                      | DB                                | The type of event store used in Naryo. (See the Advanced section for more details)                                                                                                     |
| EVENT_STORE_URL                                                       | http://localhost:8081/api/rest/v1 | The URL of the event store endpoint to be queried.                                                                                                                                          |
| EVENT_STORE_EVENT_PATH                                                | /event                            | The path to query the event store's events.                                                                                                                                                 |
| EVENT_STORE_LATEST_BLOCK_PATH                                         | /latest-block                     | The path to query the event store's latest block.                                                                                                                                           |
| BROADCASTER_TYPE                                                      | RABBIT                            | The broadcast mechanism to use.  (KAFKA or HTTP or RABBIT)                                                                                                                                  |
| BROADCASTER_CACHE_EXPIRATION_MILLIS                                   | 6000000                           | The naryo broadcaster has an internal cache of sent messages, which ensures that duplicate messages are not broadcast.  This is the time that a message should live within this cache. |
| BROADCASTER_EVENT_CONFIRMATION_NUM_BLOCKS_TO_WAIT                     | 12                                | The number of blocks to wait (after the initial mined block) before broadcasting a CONFIRMED event                                                                                          |
| BROADCASTER_EVENT_CONFIRMATION_NUM_BLOCKS_TO_WAIT_FOR_MISSING_TX      | 200                               | After a fork, a transaction may disappear, and this is the number of blocks to wait on the new fork, before assuming that an event emitted during this transaction has been INVALIDATED     |
| BROADCASTER_EVENT_CONFIRMATION_NUM_BLOCKS_TO_WAIT_BEFORE_INVALIDATING | 2                                 | Number of blocks to wait before considering a block as invalid.                                                                                                                             |
| BROADCASTER_MULTI_INSTANCE                                            | false                             | If multiple instances of naryo are to be deployed in your system, this should be set to true so that the naryo communicates added/removed filters to other instances, via kafka.  |
| BROADCASTER_HTTP_CONTRACT_EVENTS_URL                                  |                                   | The http url for posting contract events (for HTTP broadcasting)                                                                                                                            |
| BROADCASTER_HTTP_BLOCK_EVENTS_URL                                     |                                   | The http url for posting block events (for HTTP broadcasting)                                                                                                                               |
| BROADCASTER_BYTES_TO_ASCII                                            | false                             | If any bytes values within events should be converted to ascii (default is hex)                                                                                                             |
| BROADCASTER_ENABLE_BLOCK_NOTIFICATION                                 | true                              | Boolean that indicates if want to receive block notifications or not. Set false to not receive that event.                                                                                  |
| ZOOKEEPER_ADDRESS                                                     | localhost:2181                    | The zookeeper address                                                                                                                                                                       |
| KAFKA_ADDRESSES                                                       | localhost:9092                    | Comma seperated list of kafka addresses                                                                                                                                                     |
| KAFKA_TOPIC_CONTRACT_EVENTS                                           | contract-events                   | The topic name for broadcast contract event messages                                                                                                                                        |
| KAFKA_TOPIC_BLOCK_EVENTS                                              | block-events                      | The topic name for broadcast block event messages                                                                                                                                           |
| KAFKA_TOPIC_TRANSACTION_EVENTS                                        | transaction-events                | The topic name for broadcast transaction messages                                                                                                                                           |
| KAFKA_TOPIC_NARYO_EVENTS                                              | naryo-events                 | The topic name for broadcast naryo event messages                                                                                                                                      |
| KAFKA_TOPIC_PARTITIONS                                                | 1                                 | The number of kafka partitions                                                                                                                                                              |
| KAFKA_TOPIC_REPLICATION_SETS                                          | 1                                 | The number of replication sets                                                                                                                                                              |
| KAFKA_REQUEST_TIMEOUT_MS                                              | 20000                             | The duration after which a request timeouts                                                                                                                                                 |
| KAFKA_ENDPOINT_IDENTIFICATION_ALGORITHM                               | null                              | The endpoint identification algorithm to validate server hostname using server certificate                                                                                                  |
| KAFKA_SASL_MECHANISM                                                  | PLAIN                             | The mechanism used for SASL authentication                                                                                                                                                  |
| KAFKA_USERNAME                                                        | ""                                | The username used to connect to a SASL secured Kafka cluster                                                                                                                                |
| KAFKA_PASSWORD                                                        | ""                                | The password used to connect to a SASL secured Kafka cluster                                                                                                                                |
| KAFKA_SECURITY_PROTOCOL                                               | PLAINTEXT                         | Protocol used to communicate with Kafka brokers                                                                                                                                             |
| KAFKA_RETRIES                                                         | 10                                | The number of times a Kafka consumer will try to publish a message before throwing an error                                                                                                 |
| KAFKA_RETRY_BACKOFF_MS                                                | 500                               | The duration between each retry                                                                                                                                                             ||                                   |                                                                                                                                                                                           |
| KEEP_ALIVE_DURATION                                                   | 15000                             | Rpc http idle threads keep alive timeout in ms                                                                                                                                              |
| MAX_IDLE_CONNECTIONS                                                  | 10                                | The max number of HTTP rpc idle threads at the pool                                                                                                                                         |
| SYNCING_THRESHOLD                                                     | 60                                | Number of blocks of difference to consider that naryo is "syncing" with a node                                                                                                         |
| SPRING_DATA_MONGODB_HOST                                              | localhost                         | The mongoDB host (used when event store is set to DB)                                                                                                                                       |
| SPRING_DATA_MONGODB_PORT                                              | 27017                             | The mongoDB post (used when event store is set to DB)                                                                                                                                       |
| RABBIT_HOST                                                           | localhost                         | Property spring.rabbitmq.host                                                                                                                                                               |
| RABBIT_VIRTUAL_HOST                                                   | /                                 | Property spring.rabbitmq.virtualHost                                                                                                                                                        |
| RABBIT_USER                                                           | guest                             | Property spring.rabbitmq.username                                                                                                                                                           |
| RABBIT_PASSWORD                                                       | guest                             | Property spring.rabbitmq.password                                                                                                                                                           |
| RABBIT_CONNECTION_TIMEOUT                                             | 30000                             | Property spring.rabbitmq.connectionTimeout                                                                                                                                                  |
| RABBIT_PORT                                                           | 5672                              | Property spring.rabbitmq.port                                                                                                                                                               |
| RABBIT_SSL_ENABLED                                                    | false                             | Property spring.rabbitmq.ssl.enabled                                                                                                                                                        |
| RABBIT_SSL_ALGORITHM                                                  | TLSv1.2                           | Property spring.rabbitmq.ssl.algorithm                                                                                                                                                      |
| RABBIT_EXCHANGE                                                       | ThisIsAExchange                   | Property rabbitmq.exchange                                                                                                                                                                  |
| RABBIT_ROUTING_KEY                                                    | thisIsRoutingKey                  | Property rabbitmq.routingKeyPrefix                                                                                                                                                          |
| MONGO_PROTOCOL                                                        | mongodb                           | MongoDB protocol                                                                                                                                                                            |
| MONGO_HOST                                                            | localhost                         | MongoDB host                                                                                                                                                                                |
| MONGO_PORT                                                            | 27017                             | MongoDB port                                                                                                                                                                                |
| MONGO_DATABASE                                                        | mongodb                           | MongoDB database name                                                                                                                                                                       |
| DATABASE_TYPE                                                         | MONGO                             | The database to use.  Either MONGO or SQL.                                                                                                                                                  |
| CONNECTION_TIMEOUT                                                    | 7000                              | RPC, http connection timeout in millis                                                                                                                                                      |
| READ_TIMEOUT                                                          | 35000                             | RPC, http read timeout in millis                                                                                                                                                            |

## INFURA Support Configuration

Connecting to an INFURA node is only supported if connecting via websockets (`wss://<...>` node url). The block strategy
must also be set to PUBSUB.

# Advanced

## Correlation ID Strategies (Kafka Broadcasting)

Each subscribed event can have a correlation id strategy association with it, during subscription. A correlation id
strategy defines what the kafka message key for a broadcast event should be, and allows the system to be configured so
that events with particular parameter values are always sent to the same partition.

Currently supported correlation id strategies are:

**Indexed Parameter Strategy** - An indexed parameter within the event is used as the message key when broadcasting.
**Non Indexed Parameter Strategy** - An non-indexed parameter within the event is used as the message key when
broadcasting.

## Event Store

Naryo utilises an event store in order to establish the block number to start event subscriptions from, in the
event
of a failover. For example, if the last event broadcast for event with id X had a block number of 123, then on a
failover, naryo will subscribe to events from block 124.

There are currently 2 supported event store implementations:

#### MongoDB

Broadcast events are saved and retrieved from a mongoDB database.

**Required Configuration**

| Env Variable             | Default   | Description                 |
|--------------------------|-----------|-----------------------------|
| EVENT_STORE_TYPE         | DB        | MongoDB event store enabled |
| SPRING_DATA_MONGODB_HOST | localhost | The mongoDB host            |
| SPRING_DATA_MONGODB_PORT | 27017     | The mongoDB post            |

#### REST Service

Naryo polls an external REST service in order to obtain a list of events broadcast for a specific event
specification. It is assumed that this REST service listens for broadcast events on the kafka topic and updates its
internal state...broadcast events are not directly sent to the REST service by naryo.

The implemented REST service should have a pageable endpoint which accepts a request with the following specification:

- **URL:** Configurable, defaults to `/api/rest/v1/event`
- **Method:** `GET`
- **Headers:**

| Key          | Value            |
|--------------|------------------|
| content-type | application/json |

- **URL Params:**

| Key       | Value                                              |
|-----------|----------------------------------------------------|
| page      | The page number                                    |
| size      | The page size                                      |
| sort      | The results sort field                             |
| dir       | The results sort direction                         |
| signature | Retrieve events with the specified event signature |

- **Body:** `N/A`

- **Success Response:**
    - **Code:** 200
      **Content:**

```json
{
  "content": [
    {
      "blockNumber": 10,
      "id": "<unique event id>"
    }
  ],
  "page": 1,
  "size": 1,
  "totalElements": 1,
  "first": false,
  "last": true,
  "totalPages": 1,
  "numberOfElements": 1,
  "hasContent": true
}
```

**Required Configuration**

| Env Variable           | Default                           | Description                         |
|------------------------|-----------------------------------|-------------------------------------|
| EVENT_STORE_TYPE       | REST                              | REST event store enabled            |
| EVENT_STORE_URL        | http://localhost:8081/api/rest/v1 | The REST endpoint url               |
| EVENT_STORE_EVENT_PATH | /event                            | The path to the event REST endpoint |

## Integrating Naryo into Third Party Spring Application

Naryo can be embedded into an existing Spring Application via an annotation.

#### Steps to Embed

1. Add the GitHub Packages repository to your `pom.xml` file:

    ```xml
    <repositories>
        <repository>
            <id>github-naryo</id>
            <url>https://maven.pkg.github.com/IoBuilders/naryo</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    ```

2. Add authentication for GitHub Packages by including your GitHub token in your `settings.xml` file (typically located
   in `~/.m2/settings.xml`):

    ```xml
    <servers>
        <server>
            <id>github-naryo</id>
            <username>YOUR_GITHUB_USERNAME</username>
            <password>YOUR_GITHUB_TOKEN</password>
        </server>
    </servers>
    ```

3. Add the `naryo-core` dependency to your `pom.xml` file:

    ```xml
    <dependency>
        <groupId>io.naryo</groupId>
        <artifactId>naryo-core</artifactId>
        <version>*LATEST_NARYO_VERSION*</version>
    </dependency>
    ```

4. Within your Application class or a `@Configuration` annotated class, add the `@EnableNaryo` annotation.

#### Health check endpoint

Naryo offers a healthcheck url where you can ask for the status of the systems you are using. It will look like:

```json
{
  "status": "UP",
  "details": {
    "rabbit": {
      "status": "UP",
      "details": {
        "version": "3.7.13"
      }
    },
    "mongo": {
      "status": "UP",
      "details": {
        "version": "4.0.8"
      }
    }
  }
}
```

Returning this information it is very easy to create alerts over the status of the system.

The endpoint is: GET /monitoring/health

## Next Steps

- [Usage](usage.md)
- [Metrics](metrics.md)
- [Known Caveats / Issues](issues.md)

## Previous Steps

- [Getting started](getting_started.md)
