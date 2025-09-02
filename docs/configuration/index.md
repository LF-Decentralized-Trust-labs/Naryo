# 🎉 Naryo Configuration

- [Configuration Sources](#configuration-sources)
  - [How is priority handled?](#how-is-priority-handled)
- [Core Configuration Overview](#-core-configuration-overview)
- [MongoDB Configuration Overview](#-mongo-configuration-overview)

Naryo supports flexible configuration mechanisms. The core module requires manual configuration through custom
implementation due to its framework-agnostic design and wide configuration domain. Configuration can be loaded from
different sources thanks to the implementation of the `SourceProvider` interface.

## Configuration Sources

Naryo allows you to use multiple configuration sources, which are resolved based on the priority of each source.

Currently, two configuration sources are supported:

- **Core**
  - The **`spring-core`** module allows traditional Spring Boot configuration using YAML or `.properties` files, based on the `EnvironmentProperties` model.

- **MongoDB**
  - The **`persistence-spring-mongo`** module enables dynamic configuration loading from MongoDB, based on each domain's model.

### How is priority handled?
In order to manage configurations flexibly, Naryo implements a priority system among configuration sources. Priority is defined as follows:

- **`persistence-spring-mongo` (Priority 0)**: Configuration stored in MongoDB has the **highest priority**. If a property is defined in both MongoDB and `application.yml`, Naryo will take the value from MongoDB.
- **`spring-core` (Priority 1)**: The configuration in the `application.yml` file has secondary priority. If a property is not defined in MongoDB, the value will be taken from `application.yml`.

## 📚 Core Configuration Overview

[Core Configuration Overview](./configuration-core.md)

## 📚 Mongo Configuration Overview

[Mongo Configuration Overview](./configuration-mongo.md)

## 👈 Previous steps

1. [Getting Started](../getting_started.md)

## 👉 Next steps

1. [Tutorials](../tutorials/index.md)
