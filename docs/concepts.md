# Concepts

This page explains the core concepts you will encounter when using Naryo.

## Node

Represents a connection to a blockchain (DLT) endpoint, such as an Ethereum RPC or a Hedera Mirror node. A deployment may define multiple nodes.

## Filter

A filter describes which on-chain data should be captured. The most common types are:

- Contract event filter (by signature/topic and optional contract address)
- Transaction filter (by properties such as sender, recipient, entityId, status)

Filters can be added at startup via configuration and, depending on your setup, also at runtime via APIs/backends.

## Event

The normalized representation of a captured occurrence that matched a filter. It may include:

- Event type (CONTRACT, TRANSACTION, BLOCK)
- Node ID
- Parameters (decoded ABI values for contract events)
- Transaction and block context (hashes, numbers, timestamps)
- Status and confirmation state

## Broadcaster

Responsible for delivering events to a destination (HTTP endpoint, Kafka topic, RabbitMQ exchange, etc.). A broadcaster references a destination configuration and a target (e.g., a specific filter’s events routed to a path or topic).

## Persistence

Optional storage for durability, replay, and auditability. Provided integrations include MongoDB and JPA/SQL. When configured, the event data and context can be stored in a database.

## Confirmation and invalidation

Naryo can be configured to mark events as confirmed after a specified number of block confirmations and to invalidate events if reorgs or certain conditions occur.

## Runtime model

At runtime, Naryo:

1. Connects to configured nodes
2. Subscribes or polls for data
3. Applies filters and decodes data into events
4. Enriches with context (transaction, block, timestamps)
5. Broadcasts to one or more destinations
6. Optionally persists and updates confirmation states

For configuration details see the [configuration guide](configuration/index.md).
