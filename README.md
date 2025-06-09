# Naryo

Naryo is a multi-chain tool for capturing and processing events from Distributed Ledger Technology (DLT) networks.
This project is built upon [Eventeum](https://github.com/eventeum/eventeum) and aims to provide a user-friendly,
resilient event listener that supports a wide range of DLT protocols. It is designed to simplify event handling by
reducing the complexity of developing, deploying, and maintaining application logic for capturing, processing, and
broadcasting events, all while meeting the security and performance requirements of enterprise-grade systems.

> ⚠️ **Disclaimer**:
>
> This project is a work in progress (WIP). The documentation is being actively updated. Please be
> aware that some features might not be fully implemented, and certain parts of the documentation may not reflect the
> latest changes.

## Features

* **Configurable Event Filters**. Supports filtering by smart contract events content, transaction fields (e.g., from,
  to) and more.
* **Resilient Event Extraction**. Designed to handle high volumes of operations while remaining tolerant to issues such
  as node connection failures.
* **Configurable Broadcast Recipients**. Events can be broadcast to databases, queues, in-memory objects when embedded
  in another application, among other options. New recipients can be easily added.
* **Flexible Deployment**. Can be deployed as a standalone microservice with a REST API for event filter configuration
  or embedded within another backend component, exposing Java interfaces for seamless integration.
* **Multi-DLT Support**. Compatible with multiple DLT protocols, including Ethereum clients, Hedera mirror nodes, and
  more.

## Supported Broadcast Mechanisms

* Kafka
* HTTP Post
* RabbitMQ
* Pulsar

For **RabbitMQ**, you can configure the following extra values

* `rabbitmq.blockNotification`
* `rabbitmq.routingKey.contractEvents`
* `rabbitmq.routingKey.blockEvents`
* `rabbitmq.routingKey.transactionEvents`

## Documentation

- [Getting started](./docs/getting_started.md)
- [Configuration](./docs/configuration.md)
- [Usage](./docs/usage.md)
- [Metrics](./docs/metrics.md)
- [Known Caveats / Issues](./docs/issues.md)
