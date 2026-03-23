# Naryo

[Naryo](https://naryo.io/) is a lightweight, modular framework for capturing, processing, and broadcasting events from Distributed Ledger Technology (DLT) networks. Whether you're building a dApp, a data analytics pipeline, or a monitoring tool, Naryo simplifies blockchain integration so you can focus on your application's core logic.

Inspired by [Eventeum](https://github.com/eventeum/eventeum), Naryo is designed for enterprise-grade systems, offering a resilient and user-friendly event listener that supports a wide range of DLT protocols.

Naryo is under active development, with a growing community of contributors. We welcome you to join us in building the future of blockchain event processing!

## Table of Contents

- [Why Naryo?](#why-naryo)
- [Architecture](#architecture)
- [Quickstart](#quickstart)
- [Installation](#installation)
- [Configuration](#configuration)
- [Modules](#modules)
- [Contributing](#contributing)
- [License](#license)

## Why Naryo?

- **Multi-Chain Support**: Connect to multiple DLT networks like Ethereum and Hedera simultaneously.
- **Flexible Broadcasting**: Broadcast events to various destinations, including Kafka, RabbitMQ, and HTTP endpoints.
- **Framework-Agnostic Core**: The heart of Naryo is a framework-agnostic core, allowing you to integrate it with any framework of your choice.
- **Powerful Filtering**: Define granular filters for contract events, transactions, and blocks.
- **Extensible and Modular**: Add support for new DLT protocols and broadcasting mechanisms.
- **Resilient and Reliable**: With features like event confirmation and invalidation, Naryo ensures data integrity.
- **Dynamic Configuration**: Add and remove event filters or broadcast recipients at runtime via API.

## Architecture

![Naryo Architecture](docs/images/modules.png)

For a deeper dive into Naryo's architecture and core concepts, check out our [architecture](docs/architecture.md) and [concepts](docs/concepts.md) documentation.

## Quickstart

Want to see Naryo in action? Our Docker-based quickstarts will get you up and running in minutes.

- [Naryo with Besu](docs/tutorials/naryo_quickstart.md)
- [Naryo with Hedera](docs/tutorials/naryo_quickstart_hedera.md)

## Installation

You can integrate Naryo directly into your application. The recommended way is to use our Spring Boot starter.

For detailed instructions on setting up Naryo with Spring Boot, Gradle, or Maven, please refer to our [Getting Started](docs/getting_started.md) guide.

## Configuration

Naryo is highly configurable. You can define your own filters, modules, and broadcasters. The [configuration guide](docs/configuration/index.md) provides detailed information on how to configure Naryo.

## Modules

Naryo's modular design allows you to pick and choose the features you need.

- **core**: The framework-agnostic runtime and APIs.
- **spring-core**: Spring Boot integration and YAML-backed configuration.
- **persistence-spring-mongo**: MongoDB persistence integration.
- **persistence-spring-jpa**: SQL/JPA persistence integration.
- **broadcaster-spring-kafka**: Kafka broadcaster integration.
- **broadcaster-spring-rabbitmq**: RabbitMQ broadcaster integration.
- **broadcaster-spring-api**: Dynamic configuration API integration.

## Contributing

We welcome contributions from the community! Whether it's fixing bugs, improving documentation, or proposing new features, your input is valuable.

Check out our [contribution guide](CONTRIBUTING.md) to get started.

## License

This project is licensed under the [Apache License, Version 2.0](LICENSE).
