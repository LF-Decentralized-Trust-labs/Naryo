# 🚀 Start Naryo with Hedera (Quickstart)

This guide explains how to run a complete, containerized environment to monitor **Hedera Consensus Service (HCS)**, **Hedera Token Service (HTS)**, and **Smart Contract Events** using **Naryo**.

## 🧰 Requirements

Ensure you have the following installed:

* **Docker** & **Docker Compose**
* **Java 21** (for building Naryo)
* **Node.js** (for the Hedera local node CLI)

## 🎉 Step 1: Start Hedera Dev Network

The easiest way to start a local Hedera node is using the [Hiero Local Node CLI](https://github.com/hiero-ledger/hiero-local-node).

1. Install the CLI globally:
    ```bash
    npm install @hashgraph/hedera-local -g
    ```

2. Start the network:
    ```bash
    hedera start
    ```
    *This starts the Consensus Node, Mirror Node, and JSON-RPC Relay in Docker.*

## 🏗️ Step 2: Build Naryo

Before running the quickstart, you need to build the Naryo server JAR:

```bash
./gradlew :server:bootJar -x test
```

## 🚀 Step 3: Launch the Quickstart

Navigate to the Hedera quickstart directory and launch the environment:

```bash
cd examples/hedera-quickstart
docker-compose up -d --build
```

### What's happening?
* **`hedera-worker`**: A containerized Node.js script that:
    1. Creates a native **HCS Topic** and **HTS Token**.
    2. Deploys a **Solidity Smart Contract** (`HederaQuickstart.sol`).
    3. Enters a loop to perform HCS/HTS actions and trigger EVM events every 30 seconds.
* **`naryo-server`**: Starts automatically once the worker provides the IDs. It monitors the Hedera network for all three types of activity.
* **`mongodb`**: Persists the captured data.

## ✅ Step 4: Verify Results

Naryo captures data and can also broadcast it to external services. You can verify the results using several methods:

### 1. Using MongoDB
Naryo stores captured data in MongoDB. You can explore the collections using **MongoDB Compass** (connect to `mongodb://localhost:27017`) or via the command line:

#### Smart Contract Events
Naryo decoded these from the Solidity logs:
```bash
docker exec -it mongodb mongosh naryo --eval "db['contract-events'].find().pretty()"
```

#### Native Transactions (HCS/HTS)
Naryo captured these by monitoring specific entity IDs:
```bash
docker exec -it mongodb mongosh naryo --eval "db['transactions'].find().pretty()"
```

### 2. Using Mockoon (HTTP Broadcaster)
Naryo is configured to broadcast events to a mock HTTP server. You can verify the outgoing traffic by checking the logs of the `mock-http` container:

```bash
docker-compose logs -f mock-http
```
*You should see incoming POST requests containing the event payloads as Naryo processes each block.*

## 🔍 How it Works

### `application.yml`
Naryo is configured with multiple filters to demonstrate its flexibility:
* **Transaction Filters**: Use `IDENTITY_ID` to capture native HCS/HTS traffic.
* **Event Filters**: Use Solidity signatures (e.g., `HCSMessage(address,string)`) to capture EVM logs.

### Atomic Monitoring
By monitoring both the native layer and the EVM layer, Naryo provides a complete audit trail for hybrid applications that use Hedera's advanced features alongside traditional smart contracts.

## 📚 Resources

* [Hedera Documentation](https://docs.hedera.com/)
* [Hiero Local Node](https://github.com/hiero-ledger/hiero-local-node)
* [Naryo Configuration Guide](../configuration/configuration-core.md)
