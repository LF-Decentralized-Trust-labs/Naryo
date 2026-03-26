# Naryo Configuration Guide

This guide provides a comprehensive overview of how to configure Naryo. Whether you want to connect to a new blockchain node, define a new event filter, or set up a new broadcasting destination, you'll find all the information you need here.

Naryo's configuration is designed to be flexible and modular. You can configure it using a simple YAML file, or you can leverage more advanced features like dynamic configuration from a database.

## Core Configuration

This is the main configuration for Naryo. It's where you define the core components of your event listener, such as nodes, filters, and broadcasters.

-   **[Core Configuration Overview](./configuration-core.md)**: A detailed reference for all the core configuration options.

## Data Persistence

Naryo can be configured to store its state and configuration in a database. This is useful for running Naryo in a distributed environment or for managing dynamic configurations.

-   **[Persistence Overview](./persistence/index.md)**: Learn how to configure Naryo to use MongoDB or a SQL database.

## Broadcasting

Broadcasting is how Naryo sends event data to your systems. Naryo supports several broadcasting mechanisms out of the box.

-   **[Broadcasting Overview](./broadcasting/index.md)**: Learn how to configure broadcasters for Kafka, RabbitMQ, and HTTP.

## Configuration API

For advanced use cases, Naryo provides a powerful API for managing your configuration dynamically at runtime.

-   **[Configuration API Overview](api/configuration-api.md)**: Learn how to use the API to add, remove, and update your configuration on the fly.

## Next Steps

Now that you have an overview of Naryo's configuration, here are some suggestions for what to read next:

-   **[Core Configuration Overview](./configuration-core.md)**: Start here if you want to learn the basics of configuring Naryo.
-   **[Tutorials](../tutorials/index.md)**: See Naryo in action with our hands-on tutorials.
