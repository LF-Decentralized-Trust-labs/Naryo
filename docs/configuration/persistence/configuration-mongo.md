# MongoDB Persistence Configuration

This document describes how to configure Naryo to use MongoDB for data persistence. This is achieved by using the `persistence-spring-mongo` module.

## Enabling MongoDB Persistence

To enable MongoDB persistence, you need to include the necessary dependencies in your project and configure the database connection.

### 1. Add Dependencies

Add the following dependency to your `build.gradle` file.

```groovy
// build.gradle

dependencies {
    // Add the Naryo MongoDB module
    implementation "io.naryo:persistence-spring-mongo:<version>"
}
```

### 2. Configure Data Source

Next, configure the connection to your MongoDB database in your `application.yml` or `application.properties` file.

**YAML (`application.yml`)**
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/naryo_db
      auto-index-creation: true
```

**Properties (`application.properties`)**
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/naryo_db
spring.data.mongodb.auto-index-creation=true
```

---

## Store Configuration (`naryo.stores[]`)

Once MongoDB is enabled, you can configure a data store for a specific blockchain node. To do this, add an entry to the `naryo.stores` list and set the `type` to `mongo`.

### Properties

| Property               | Type     | Description                                                   | Required | Default |
|------------------------|----------|---------------------------------------------------------------|----------|---------|
| `nodeId`               | `UUID`   | The ID of the node this store is for.                         | Yes      | —       |
| `type`                 | `String` | Set to `mongo` to enable MongoDB persistence.                 | Yes      | —       |
| `features`             | `Map`    | Configures what data to store (e.g., `EVENT`, `FILTER_SYNC`). | No       | —       |


### Examples

**YAML (`application.yml`)**
```yaml
naryo:
  nodes:
    - id: "e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e"
      # ... other node properties
  stores:
    - nodeId: "e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e"
      type: "mongo"
      additionalProperties:
        collections.BLOCK.name: "block_event"
        collections.TRANSACTION.name: "transaction_event"
        fields.TRANSACTION.hash: "txHash"
        routing.partitionKey: "nodeId"
      features:
        - type: EVENT
          strategy: "BLOCK_BASED"
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
naryo.nodes[0].id=e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e

naryo.stores[0].nodeId=e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e
naryo.stores[0].type=mongo
naryo.stores[0].additionalProperties.collections.BLOCK.name=block_event
naryo.stores[0].additionalProperties.collections.TRANSACTION.name=transaction_event
naryo.stores[0].additionalProperties.fields.TRANSACTION.hash=txHash
naryo.stores[0].additionalProperties.routing.partitionKey=nodeId
naryo.stores[0].features[0].type=EVENT
naryo.stores[0].features[0].strategy=BLOCK_BASED
naryo.stores[0].features[0].targets[0].type=BLOCK
naryo.stores[0].features[0].targets[0].destination=blocks
naryo.stores[0].features[0].targets[1].type=TRANSACTION
naryo.stores[0].features[0].targets[1].destination=transactions
naryo.stores[0].features[1].type=FILTER_SYNC
naryo.stores[0].features[1].destination=filters
```
