# 🚀 Naryo Quickstart

This guide explains how to configure, deploy and use Naryo in 2 steps .

## 🧰 Requirements

* Docker & Docker Compose
* Git

## 📦Step 1: Deployment of local environment

Download the repo and setup an example environment:

```bash
  git clone https://github.com/LF-Decentralized-Trust-labs/Naryo naryo
  cd naryo/examples/quickstart
  docker compose up -d anvil sc-deployer mock-http naryo-server
```

You just deployed the following environment:

* An Anvil node
* A _HelloNaryo.sol_ Smart Contract deployed at the node. The contract has one _greetings()_ function, that emits a _HelloCounter(uint256)_ event.
* A Mockoon server, which simulates a backend application that wants to receive the events emitted by the contract.
* A Naryo Server that catches the events from Anvil and sends them to the Mockoon server.

>The Naryo Server is just a backend that imports and configures the Naryo core module.
>When using the reference Docker image of the Naryo server, we must configure Naryo through an _application.yml_ file, because the container is running a SpringBoot application
>The current [_application.yml_](../../examples/quickstart/application.yml) file configures the following:
> 1. The connection to the Anvil node
>2. The connection to the mock http server
>3. The events filters for the deployed contract

> Naryo also supports runtime configuration through a Rest API. Learn more in the [API Configuration documentation](../configuration/configuration-api.md).
 

## 🎉 Step 2: Invoke the contract and see the logs

Open a terminal to see the Mockoon server logs:
```bash
 docker logs -ft mock-http
```

In another terminal, invoke the contract using Foundry's docker:
> Make sure you are in the naryo/examples/quickstart folder.

```bash
 docker compose up sc-invoke
```

Every time you invoke the _HelloNaryo_ contract, Naryo will catch the _HelloCounter_ event, and send it to the Mockoon server. The Mockoon server will display a verbose log with the details of the received request.

## 👉 Conclusion and Next steps
In this quickstart, you have tried Naryo in a local environment. As next steps, you may go through the [Documentation](../). Once you have a better understanding of how Naryo works, feel free to copy the example configurations in the [_examples_ folder ](../../examples/) and tailor it to your needs.

You may also learn how to [get started with Naryo without Docker](../getting_started.md), or follow our tutorials of using Naryo [with Besu](./start_naryo_with_besu.md) or [with Hedera](./start_naryo_with_hedera.md).