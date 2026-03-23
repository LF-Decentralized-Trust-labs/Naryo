# JPA Persistence Configuration

This document describes how to configure Naryo to use a JPA-compatible (SQL) database for data persistence. This is achieved by using the `persistence-spring-jpa` module.

## Enabling JPA Persistence

To enable JPA persistence, you need to include the necessary dependencies in your project and configure the database connection.

### 1. Add Dependencies

Add the following dependencies to your `build.gradle` file.

```groovy
// build.gradle

dependencies {
    // Add the Naryo JPA module
    implementation "io.naryo:persistence-spring-jpa:<version>"

    // Add Spring Data JPA and a JDBC driver for your database
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'org.postgresql:postgresql' // Example for PostgreSQL
}
```

### 2. Configure Data Source

Next, configure the connection to your SQL database in your `application.yml` or `application.properties` file.

**YAML (`application.yml`)**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/naryo_db
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update # Or 'validate', 'create', etc.
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

**Properties (`application.properties`)**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/naryo_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## Store Configuration (`naryo.stores[]`)

Once JPA is enabled, you can configure a data store for a specific blockchain node. To do this, add an entry to the `naryo.stores` list and set the `type` to `jpa`.

### Properties

| Property   | Type     | Description                                                   | Required | Default |
|------------|----------|---------------------------------------------------------------|----------|---------|
| `nodeId`   | `UUID`   | The ID of the node this store is for.                         | Yes      | —       |
| `type`     | `String` | Set to `jpa` to enable JPA persistence.                       | Yes      | —       |
| `features` | `Map`    | Configures what data to store (e.g., `EVENT`, `FILTER_SYNC`). | No       | —       |

### Examples

**YAML (`application.yml`)**
```yaml
naryo:
  nodes:
    - id: "e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e"
      # ... other node properties
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
naryo.nodes[0].id=e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e

naryo.stores[0].nodeId=e4e298e8-6e54-4f5c-97f7-3a11a7f72a3e
naryo.stores[0].type=jpa
naryo.stores[0].features[0].type=EVENT
naryo.stores[0].features[0].strategy=BLOCK_BASED
naryo.stores[0].features[0].targets[0].type=BLOCK
naryo.stores[0].features[0].targets[0].destination=blocks
naryo.stores[0].features[0].targets[1].type=TRANSACTION
naryo.stores[0].features[0].targets[1].destination=transactions
naryo.stores[0].features[1].type=FILTER_SYNC
naryo.stores[0].features[1].destination=filters
```
