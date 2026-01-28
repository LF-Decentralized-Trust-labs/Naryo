# 🚀 Naryo Quickstart

This guide explains how to quickly deploy a local environment, allowing you to test **Naryo**. The local environment is composed by:

* A server running Naryo (Docker container built locally)
* An Etherem dev node using
* An ERC20 Smart Contract deployed to the dev node.

   > Tip: You can use the configuration......

## 🧰 Requirements

Ensure you have the following installed:

* Foundry
* Docker & Docker Compose
* Git

## 🎉 Step 0: Prepare your environment

Clone the repo and go to the quickstart folder:

```bash
  git clone https://github.com/LF-Decentralized-Trust-labs/Naryo naryo
  cd naryo/examples/quickstart
```

## ⚙️ Step 1: Create Naryo Config file

> Tip: You may use this same configuration for your actual application.

```yaml
naryo:
  nodes:
    - id: eadc75b2-4217-4018-95af-f67c13058976
      name: evm-node
      type: ETHEREUM
      connection:
        type: HTTP
        endpoint:
          url: http://localhost:8545
  broadcasting:
    configuration:
      - id: cd1bec0d-2998-46bc-828f-94459d42c17a
        type: HTTP
        endpoint:
          url: http://localhost:7070
    broadcasters:
      - id: cd1bec0d-2998-46bc-828f-94459d43c17b
        configurationId: cd1bec0d-2998-46bc-828f-94459d42c17a
        target:
          type: ALL
          destinations:
            - /events
  filters:
    - id: a5605668-7a88-4e5c-b4ee-4a8417b7184d
      name: my-transfer-filter
      type: EVENT
      nodeId: eadc75b2-4217-4018-95af-f67c13058976
      scope: GLOBAL
      specification:
        signature: HelloCounter(uint256 i_)
```

You can learn more about the configuration parameters here.


## 📦 Step 2: Deploy everything

1. Open a terminal and deploy an Anvil node

```bash
  anvil
```

2. Open another terminal. From the folder naryo/examples/quickstart, run the following commands:

Deploy the HelloNaryo contract:

forge create $(PWD)/HelloNaryo.sol:HelloNaryo --rpc-url localhost:8545 \
    --private-key 0xac0974bec39a17e36ba4a6b4d238ff944bacb478cbed5efcae784d7bf4f2ff80 \
--broadcast                       

store the SC address

Deploy the mock http server:

docker run --rm --name your_backend --mount type=bind,source=${PWD}/mock_http_server.yaml,target=/data,readonly -p 7070:7070 mockoon/cli:latest --data data --port 7070 --log-transaction

In a third (and last) terminal:

Deploy Naryo using Docker:

docker run --rm --name naryo-server --mount type=bind,source=${PWD}/application.yaml,target=/data,readonly -p 6060:6060 naryo-server:latest

## 🥪 Step 3: Invoke the contract and see the logs

cast send 0x5FbDB2315678afecb367f032d93F642f64180aa3 "greetings()" --private-key 0xac0974bec39a17e36ba4a6b4d238ff944bacb478cbed5efcae784d7bf4f2ff80 --rpc-url localhost:8545

In the 2nd terminal, in the logs from the Mockoon server, you will see a new entry everytime you invoke the contract. Thats because Naryo is catching the event from the Anvil node, and sending it to the Mockoon Rest API.