# Persistence configuration

Naryo supports flexible configuration mechanisms. The core module requires manual configuration through custom
implementation due to its framework-agnostic design and wide configuration domain. Configuration can be loaded from
different sources thanks to the implementation of the `SourceProvider` interface.

## Table of Contents

- [Configuration Sources](#configuration-sources)
  - [How is priority handled?](#how-is-priority-handled)
- [Mongo Configuration](#mongo-configuration)
- [JPA Configuration](#jpa-configuration)
- [Next Steps](#next-steps)

## Configuration Sources

Naryo allows you to use multiple configuration sources, which are resolved based on the priority of each source.

Currently, two configuration sources are supported:

- **Core**
    - The **`spring-core`** module allows traditional Spring Boot configuration using YAML or `.properties` files, based
      on the `EnvironmentProperties` model.

- **MongoDB**
    - The **`persistence-spring-mongo`** module enables dynamic configuration loading from MongoDB, based on each
      domain's model.

- **JPA**
    - The **`persistence-spring-jpa`** module enables dynamic configuration loading from the SQL Database of your
      choice, based on each domain's model.

### How is priority handled?

To manage configurations flexibly, Naryo implements a priority system among configuration sources. Priority is defined
as follows:

- **`persistence-spring-mongo` or `persistence-spring-jpa` (Priority 0)**:
  Configuration stored in DB has the **highest priority**. If a property is defined in both DB and `application.yml`,
  Naryo will take the value from DB.
- **`spring-core` (Priority 1)**: The configuration in the `application.yml` file has secondary priority. If a property
  is not defined in MongoDB, the value will be taken from `application.yml`.

## Mongo Configuration

The `persistence-spring-mongo` module allows you to load configuration from MongoDB.
It is a powerful tool for managing dynamic configurations, which can be particularly useful in distributed systems and
microservices architectures.

Follow the [Mongo Configuration Guide](./configuration-mongo.md) to learn more about the MongoDB module.

## JPA Configuration

The `persistence-spring-jpa` module allows you to load configuration from the SQL Database of your choice.
It is a powerful tool for managing dynamic configurations, which can be particularly useful in distributed systems and
microservices architectures.

Follow the [JPA Configuration Guide](./configuration-jpa.md) to learn more about the JPA module.

## Next Steps

1. [Core Configuration Guide](../configuration-core.md)
2. [Mongo Configuration Guide](./configuration-mongo.md)
3. [JPA Configuration Guide](./configuration-jpa.md)
