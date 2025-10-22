# 🚀 Start Naryo with Besu

This guide explains how to deploy a local [Hyperledger Besu](https://besu.hyperledger.org/) node and a smart contract
that emits events, allowing you to test event consumption in **Naryo**.

## 🧰 Requirements

Ensure you have the following installed:

* Java 21
* Docker & Docker Compose
* [Node.js 20.19.4 LTS & npm 10.8.2](https://nodejs.org/en/download)
* [Hardhat 2.26.0](https://hardhat.org/)
* Git

## 🎉 Step 0: Prepare your environment

Create a directory for the tutorial, for example: `naryo-besu-tutorial` and run the following command:

```bash
  mkdir naryo-besu-tutorial # or whatever name you like
  cd naryo-besu-tutorial
```

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

   > ⚠️ Remember to have docker running on your machine.

4. Use the following pre-funded test account in your deployment scripts:

    ```txt
    Private Key: 0x8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63
    Address:    0xfe3b557e8fb62b89f4916b721be55ceb828dbd73
    ```

   > ⚠️ Only use this key for local testing. Never use it in production environments.

## 📦 Step 2: Deploy an ERC-20 Contract that Emits Events

> Make sure you are in the tutorial directory before following the steps below.

### 1. Create a Hardhat project:

```bash
mkdir erc20-contract && cd erc20-contract
npm init -y
npm install --save-dev hardhat
npx hardhat --init
```

- Choose Hardhat 2
- then when prompted with "Please provide either a relative or an absolute path:", type . and press Enter (this is correct).
- Next, select "A JavaScript project using Mocha and Ethers.js," skip the automatic installation, and install the dependencies manually afterward.
```bash
npm i -D hardhat@^2.26 \
  @nomicfoundation/hardhat-toolbox@^6 \
  @nomicfoundation/hardhat-ignition@^0.15 \
  @nomicfoundation/hardhat-ignition-ethers@^0.15
```

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

### 3. Configure Hardhat to connect to Besu:

You can use the following `hardhat.config.js` file:

```js
require("@nomicfoundation/hardhat-toolbox");

/** @type import('hardhat/config').HardhatUserConfig */
module.exports = {
  solidity: "0.8.28",
  networks: {
    besu: {
      url: "http://localhost:8545",
      accounts: ["0x8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63"],
      chainId: 1337
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

Replace the generated file `src/main/resources/application.yml` to include your Besu node and contract:

```yaml
naryo:
  nodes:
    - id: eadc75b2-4217-4018-95af-f67c13058976
      name: besu-node
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
          url: http://localhost:8080
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
        signature: Transfer(address indexed, address indexed, uint256)
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

## 🪵 Step 8: Check the Naryo listener logs

1. Open a new terminal window.
2. Navigate to the `naryo-listener/` directory.
3. Run the following command to monitor incoming events:
   ```bash
    tail -f event-receiver.log
     ```

> You should see the event payload printed whenever a `Transfer` event is emitted from your contract and every mined
> block.

## ✅ Verify

You can now verify that Naryo is correctly forwarding events to your local HTTP server.

> Make sure you are in the `erc20-contract/` directory before following the steps below.

Let's trigger the event from the Hardhat console:

1. Open a new terminal.
2. Start the Hardhat console:

    ```bash
    npx hardhat console --network besu
    ```

3. Execute the following commands to interact with your contract (replace `DEPLOYED_CONTRACT_ADDRESS` if needed):

```js
var token = await ethers.getContractAt("MyToken", DEPLOYED_CONTRACT_ADDRESS);

await token.transfer("0x9ba8c0dF156194706942a86f49081542F13EbF42", 1);
```

> You can run the above commands multiple times to trigger the event.

You should see the output in the Node.js event listener server.

## 📚 Resources

* [Besu Quickstart](https://github.com/hyperledger/besu-quickstart)
* [Hardhat Docs](https://hardhat.org/)
* [OpenZeppelin Contracts](https://docs.openzeppelin.com/contracts)
* [Naryo Getting Started](../getting_started.md)
* [Naryo Configuration](../configuration/index.md)
