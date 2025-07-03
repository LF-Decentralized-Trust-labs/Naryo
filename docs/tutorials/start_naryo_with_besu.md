# 🚀 Start Naryo with Besu

This guide explains how to deploy a local [Hyperledger Besu](https://besu.hyperledger.org/) node and a smart contract
that emits events, allowing you to test event consumption in **Naryo**.

---

## 🧰 Requirements

Ensure you have the following installed:

* Java 21
* Docker & Docker Compose
* Node.js & npm
* [Hardhat](https://hardhat.org/)
* Git

---

## ⚙️ Step 1: Start Besu Dev Network

The easiest way to start a local Besu node is using
the [Quorum Dev Quickstart](https://github.com/ConsenSys/quorum-dev-quickstart), which supports Besu networks with
minimal setup.

1. Run the following command to download the setup:

    ```bash
    npx quorum-dev-quickstart --clientType besu --outputPath ./quorum-test-network --monitoring default --privacy false
    ```

2. Once the project is initialized, navigate to the directory:

    ```bash
    cd quorum-test-network
    ```

3. Start the Besu network:

    ```bash
    ./run.sh
    ```

   This command launches the network using Docker Compose and exposes the following JSON-RPC endpoint:
   `http://localhost:8545`

   > ⚠️ Don't forget to have docker running on your machine.

4. Use the following pre-funded test account in your deployment scripts:

    ```txt
    Private Key: 0x8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63
    Address:    0xfe3b557e8fb62b89f4916b721be55ceb828dbd73
    ```

   > ⚠️ Only use this key for local testing. Never use it in production environments.

---

## 📦 Step 2: Deploy an ERC-20 Contract that Emits Events

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

### 3. Configure `hardhat.config.js` to connect to Besu:

```js
require("@nomicfoundation/hardhat-toolbox");

/** @type import('hardhat/config').HardhatUserConfig */
module.exports = {
  solidity: "0.8.28",
  networks: {
    besu: {
      url: "http://localhost:8545",
      accounts: ["0x8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63"]
    }
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
npx hardhat ignition deploy ignition/modules/MyToken.js --network besu
```

---

## 🏗️ Step 3: Publish Naryo Modules Locally

Before generating your Naryo project, make sure to publish the Naryo modules to your local Maven repository:

```bash
git clone https://github.com/LF-Decentralized-Trust-labs/Naryo naryo # skip if already cloned
cd naryo
./gradlew publishToMavenLocal
```

> This is required so that your new project can resolve the Naryo dependencies.

---

## 🧬 Step 4: Generate a Naryo Project

Use the Naryo CLI initializer to scaffold your project:

```bash
./gradlew :initializer:run --args="--name test-naryo --output <YOUR_WORKING_DIRECTORY>"
```

> Replace `<YOUR_WORKING_DIRECTORY>` with the path to your working directory.

This will generate a folder `test-naryo/` with:

* Project structure
* `build.gradle` with correct dependencies
* `Application.java` class
* `application.yml` with prefilled node and broadcaster config

> ℹ️ You can modify the YAML to use your deployed contract address and filters.

---

## 🔧 Step 5: Configure `application.yml`

Edit the generated file `src/main/resources/application.yml` to include your Besu node and contract:

```yaml
naryo:
  nodes:
    - id: 8a2e3579-9145-4af7-b386-e20f1f7489c7
      name: besu-node
      type: ETHEREUM
      connection:
        type: HTTP
        endpoint:
          url: http://localhost:8545
  broadcasting:
    configuration:
      - id: 3d770c64-dc99-4c04-a456-134663b59f18
        type: HTTP
        configuration:
          endpoint:
            url: http://localhost:3000
    broadcasters:
      - configurationId: 3d770c64-dc99-4c04-a456-134663b59f18
        type: ALL
        destination: /events
  filters:
    - id: a6a775da-f181-46a7-a340-e31eb0535361
      name: my-transfer-filter
      type: EVENT
      nodeId: 8a2e3579-9145-4af7-b386-e20f1f7489c7
      configuration:
        scope: GLOBAL
        specification:
          signature: Transfer(address,address,uint256)

```

---

## 📁 Step 6: Expose Event Notifications via HTTP

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
const port = 3000;

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
node server.js
```

Now every received event will be forwarded and printed in your local Node.js server.

---

## 🥪 Step 7: Run the Naryo Application

1. Go to your generated project folder:

    ```bash
    cd test-naryo
    ```

2. Run the application:

    ```bash
    ./gradlew bootRun
    ```

> Naryo will start and listen to the configured event (`Transfer`) using polling from your Besu node.

---

## ✅ Verify

Let's trigger the event from the Hardhat console:

1. Start the Hardhat console:

    ```bash
    npx hardhat console --network besu
    ```

2. Execute the following commands to interact with your contract (replace `DEPLOYED_CONTRACT_ADDRESS` if needed):

```js
const contractAddress = 'DEPLOYED_CONTRACT_ADDRESS';
const abi = ["function transfer(address to, uint amount) public returns (bool)"];
const provider = new ethers.providers.JsonRpcProvider("http://localhost:8545");
const wallet = new ethers.Wallet("0xa94f5374fce5edbc8e2a8697c15331677e6ebf0b4e1e7c0a49d2f52b92c2efb4", provider);
const token = new ethers.Contract(contractAddress, abi, wallet);

await token.transfer("0x000000000000000000000000000000000000dead", 1000);
```

You should see the output in the Node.js event listener server.

---

## 📚 Resources

* [Besu Quickstart](https://github.com/hyperledger/besu-quickstart)
* [Hardhat Docs](https://hardhat.org/)
* [OpenZeppelin Contracts](https://docs.openzeppelin.com/contracts)
* [Naryo Getting Started](../getting_started.md)
* [Naryo Configuration](../configuration.md)
