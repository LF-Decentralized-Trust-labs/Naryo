# 🚀 Start Naryo with Hedera

This guide explains how to deploy a local [Hedera](https://hedera.com/) node and a smart contract
that emits events, allowing you to test event consumption in **Naryo**.

## 🧰 Requirements

Ensure you have the following installed:

* Java 21
* Docker & Docker Compose
* Node.js & npm
* [Hardhat](https://hardhat.org/)
* Git

## 🎉 Step 0: Prepare your environment

Create a directory for the tutorial, for example: `naryo-hedera-tutorial` and run the following command:

```bash
  mkdir naryo-hedera-tutorial # or whatever name you like
  cd naryo-hedera-tutorial
```

## ⚙️ Step 1: Start Hedera Dev Network

The easiest way to start a local Hedera node is using
the [Hiero Local Node](https://github.com/hiero-ledger/hiero-local-node#official-npm-release), which supports Hedera
networks with minimal setup.

1. Run the following command to download the setup:

    ```bash
    npm install @hashgraph/hedera-local -g
    ```

2. Once the hedera-local package is installed, run the following command:

    ```bash
    hedera start
    ```

   This command launches the network using Docker Compose and exposes the following JSON-RPC endpoint:
   `http://localhost:8545`

   > ⚠️ Don't forget to have docker running on your machine.

3. Use the following pre-funded test account in your deployment scripts:

    ```txt
    Private Key: 0x105d050185ccb907fba04dd92d8de9e32c18305e097ab41dadda21489a211524
    Address:    0x67D8d32E9Bf1a9968a5ff53B87d777Aa8EBBEe69
    ```

   > ⚠️ Only use this key for local testing. Never use it in production environments.

## 📦 Step 2: Deploy an ERC-20 Contract that Emits Events

> Make sure you are in the tutorial directory before following the steps below.

### 1. Create a Hardhat project:

```bash
mkdir erc20-contract && cd erc20-contract
npm init -y
npm install --save-dev hardhat
npx hardhat init
```

Choose: “Create a JavaScript project” and accept the defaults.

### 2. Define your contract:

Install OpenZeppelin contracts:

```bash
npm install @openzeppelin/contracts
```

Create `contracts/MyToken.sol`:

```solidity
// SPDX-License-Identifier: MIT
pragma solidity ^0.8.28;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";

contract MyToken is ERC20 {
    constructor(uint256 initialSupply) ERC20("MyToken", "MTK") {
        _mint(msg.sender, initialSupply);
    }
}
```

### 3. Configure Hardhat to connect to Hedera:

You can use the following `hardhat.config.js` file:

```js
require("@nomicfoundation/hardhat-toolbox");

/** @type import('hardhat/config').HardhatUserConfig */
module.exports = {
  solidity: "0.8.28",
  defaultNetwork: 'local',
    networks: {
      local: {
        url: 'http://localhost:7546',
        accounts: [
          "0x105d050185ccb907fba04dd92d8de9e32c18305e097ab41dadda21489a211524",
          "0x2e1d968b041d84dd120a5860cee60cd83f9374ef527ca86996317ada3d0d03e7"
        ],
        chainId: 298,
      },
      testnet: {
        url: 'https://testnet.hashio.io/api',
        accounts: [],
        chainId: 296,
      },
    }
};
```

### 4. Deploy the contract:

Add a new ignition module `ignition/modules/MyToken.js`:

```js
const { buildModule } = require("@nomicfoundation/hardhat-ignition/modules");

const INITIAL_SUPPLY = 1000000;

module.exports = buildModule("MyToken", (m) => {
  const initialSupply = m.getParameter("initialSupply", INITIAL_SUPPLY);

  const myToken = m.contract("MyToken", [initialSupply]);

  return { myToken };
});
```

Then run:

```bash
npx hardhat ignition deploy ignition/modules/MyToken.js
```

The output should look like this:

```bash
[ MyToken ] successfully deployed 🚀

Deployed Addresses

MyToken#MyToken - <YOUR_CONTRACT_ADDRESS>
```

Save the contract address, you will need it later.

## 🏗️ Step 3: Publish Naryo Modules Locally

> Make sure you are in the tutorial directory before following the steps below.

Before generating your testing project, make sure to publish the Naryo modules to your local Maven repository:

```bash
git clone https://github.com/LF-Decentralized-Trust-labs/Naryo naryo # skip if already cloned
cd naryo
./gradlew publishToMavenLocal
```

> This is required so that your new project can resolve the Naryo dependencies.

## 🧬 Step 4: Generate a Naryo Project

Use the Naryo CLI initializer to scaffold your project:

```bash
./gradlew :initializer:run --args="--name test-naryo --output <YOUR_TUTORIAL_DIRECTORY>"
```

> Replace `<YOUR_TUTORIAL_DIRECTORY>` with the path to your tutorial directory.

This will generate a folder `test-naryo/` in your tutorial directory with:

* Project structure
* `build.gradle` with correct dependencies
* `Application.java` class
* `application.yml` with prefilled node and broadcaster config

> ℹ️ You can modify the YAML to use your deployed contract address and filters.

## 🔧 Step 5: Configure `application.yml`

> Make sure you are in the `test-naryo/` directory before following the steps below.

Replace the generated file `src/main/resources/application.yml` to include your Hedeera node and contract:

```yaml
naryo:
  nodes:
    - id: eadc75b2-4217-4018-95af-f67c13058976
      name: hedera-node
      type: HEDERA
      connection:
        type: HTTP
        endpoint:
          url: http://localhost:5551
      interaction:
        type: BLOCK_BASED
        mode: HEDERA_MIRROR_NODE
      subscription:
        type: BLOCK_BASED
        method:
          type: POLL
          interval: 2s
  broadcasting:
    configuration:
      - id: cd1bec0d-2998-46bc-828f-94459d42c17a
        type: HTTP
        endpoint:
          url: http://localhost:8080
    broadcasters:
      - id: cd1bec0d-2998-46bc-828f-94459d43c17b
        configurationId: cd1bec0d-2998-46bc-828f-94459d42c17a
        target:
          type: ALL
          destination: /events
  filters:
    - id: a5605668-7a88-4e5c-b4ee-4a8417b7184d
      name: my-transfer-filter
      type: EVENT
      nodeId: eadc75b2-4217-4018-95af-f67c13058976
      scope: GLOBAL
      specification:
        signature: Transfer(address indexed, address indexed, uint256)
```

You can also use the RPC-Relay with the Hedera node. Simply replace the node in the following lines:

```yaml
naryo:
  nodes:
    - id: eadc75b2-4217-4018-95af-f67c13058976
      name: hedera-node
      type: HEDERA
      connection:
        type: HTTP
        endpoint:
          url: http://localhost:7546
```

## 📁 Step 6: Expose Event Notifications via HTTP

> Make sure you are in the tutorial directory before following the steps below.

Create a new folder for your event receiver script:

```bash
mkdir naryo-listener
cd naryo-listener
```

This script will allow Naryo to forward received events to your local HTTP server.

To observe how Naryo processes and reacts to events, you can expose a simple HTTP endpoint using Node.js and Express.

Create a new file called `server.js` with the following content:

```js
const express = require('express');
const bodyParser = require('body-parser');

const app = express();
const port = 8080;

app.use(bodyParser.json());

app.post('/events', (req, res) => {
  console.log('✅ Event received:', JSON.stringify(req.body, null, 2));
  res.status(200).send('OK');
});

app.listen(port, () => {
  console.log(`🚀 Event receiver listening at http://localhost:${port}/events`);
});
```

Install dependencies and start the server:

```bash
npm install express body-parser
nohup node server.js > event-receiver.log 2>&1 &
```

Now every received event will be forwarded and printed in your local Node.js server.

> You can check the logs using the command `tail -f event-receiver.log`.

## 🥪 Step 7: Run the Naryo Application

1. Go to your generated project folder:

    ```bash
    cd test-naryo
    ```

2. Run the application:

    ```bash
    ./gradlew bootRun
    ```

> Naryo will start and listen to the configured event (`Transfer`) using polling from your Hedera node.

## ✅ Verify

You can now verify that Naryo is correctly forwarding events to your local HTTP server.

> Make sure you are in the `erc20-contract/` directory before following the steps below.

Let's trigger the event from the Hardhat console:

1. Start the Hardhat console:

    ```bash
    npx hardhat console
    ```

2. Execute the following commands to interact with your contract (replace `DEPLOYED_CONTRACT_ADDRESS` if needed):

```js
var token = await ethers.getContractAt("MyToken", DEPLOYED_CONTRACT_ADDRESS);

await token.transfer("0x9ba8c0dF156194706942a86f49081542F13EbF42", 1);
```

> You can run the above commands multiple times to trigger the event.

You should see the output in the Node.js event listener server.

## 📚 Resources

* [Hiero Local Node](https://github.com/hiero-ledger/hiero-local-node#official-npm-release)
* [Hardhat Docs](https://hardhat.org/)
* [OpenZeppelin Contracts](https://docs.openzeppelin.com/contracts)
* [Naryo Getting Started](../getting_started.md)
* [Naryo Configuration](../configuration.md)
