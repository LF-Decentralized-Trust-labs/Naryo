# Start Naryo with Hedera

This guide will walk you through setting up Naryo to monitor events from a local Hedera blockchain network using Hiero Local Node. You'll deploy a sample smart contract, configure Naryo to listen for its events, and see Naryo broadcasting those events to a local HTTP endpoint.

## Requirements

Before you begin, ensure you have the following installed:

*   **Java 17 or higher**
*   **Docker & Docker Compose**
*   **Git**
*   **Node.js & npm**: Recommended for Hardhat and the event receiver.
*   **Hardhat**: For deploying smart contracts. Install globally: `npm install -g hardhat`

## Step 0: Project Setup

Create a new directory for this tutorial and navigate into it:

```bash
mkdir naryo-hedera-tutorial
cd naryo-hedera-tutorial
```

Clone the Naryo repository into this directory. We'll need it later to publish Naryo modules locally.

```bash
git clone https://github.com/LF-Decentralized-Trust-labs/Naryo naryo-project
```

## Step 1: Start a Hedera Development Network (Hiero)

We'll use the [Hiero Local Node](https://github.com/hiero-ledger/hiero-local-node) to easily spin up a local Hedera network.

1.  Install the Hedera Local Node CLI globally:

    ```bash
    npm install @hashgraph/hedera-local -g
    ```

2.  Start the Hedera network:

    ```bash
    hedera start
    ```

    This command launches a full local Hedera environment with Consensus Node, Mirror Node, and JSON-RPC relay in Docker. Keep this terminal open.

    > **Pre-funded Account**: For deploying contracts, you can use the following pre-funded test account (only for local development!):
    > *   **Private Key**: `0x105d050185ccb907fba04dd92d8de9e32c18305e097ab41dadda21489a211524`
    > *   **Address**: `0x67D8d32E9Bf1a9968a5ff53B87d777Aa8EBBEe69`

3.  **Open a new terminal** and navigate back to your `naryo-hedera-tutorial` directory for the next steps.

    ```bash
    cd ../
    ```

## Step 2: Deploy an ERC-20 Contract

We will deploy a simple ERC-20 token contract that emits `Transfer` events, which Naryo will monitor.

1.  Create a Hardhat project for your contract:

    ```bash
    mkdir erc20-contract && cd erc20-contract
    npx hardhat init
    ```
    *   Choose "Create a JavaScript project".
    *   When prompted for the project root, enter `.` and press Enter.
    *   Select "Add a .gitignore".
    *   Confirm installation of `hardhat-toolbox`.

2.  Install OpenZeppelin contracts:

    ```bash
    npm install @openzeppelin/contracts
    ```

3.  Create the contract file `contracts/MyToken.sol`:

    ```solidity
    // SPDX-License-Identifier: MIT
    pragma solidity ^0.8.20; // Use a recent Solidity version supported by Hardhat

    import "@openzeppelin/contracts/token/ERC20/ERC20.sol";

    contract MyToken is ERC20 {
        constructor(uint256 initialSupply) ERC20("MyToken", "MTK") {
            _mint(msg.sender, initialSupply);
        }
    }
    ```

4.  Configure Hardhat to connect to your local Hedera network. Replace `hardhat.config.js` with:

    ```javascript
    require("@nomicfoundation/hardhat-toolbox");

    module.exports = {
      solidity: "0.8.20", // Match your contract's Solidity version
      defaultNetwork: 'local',
      networks: {
        local: {
          url: "http://localhost:7546", // Hedera JSON-RPC Relay
          accounts: [
            "0x105d050185ccb907fba04dd92d8de9e32c18305e097ab41dadda21489a211524" // Private key from Hiero
          ],
          chainId: 298 // Default chain ID for Hiero local node
        }
      }
    };
    ```

5.  Deploy the contract:

    ```bash
    npx hardhat run scripts/deploy.js --network local
    ```
    *   If `scripts/deploy.js` doesn't exist, create it with similar content to the Besu tutorial's `deploy.js` script, adapted for `MyToken`. For example:
        ```javascript
        const hre = require("hardhat");

        async function main() {
          const initialSupply = 1000000;
          const MyToken = await hre.ethers.getContractFactory("MyToken");
          const myToken = await MyToken.deploy(initialSupply);

          await myToken.waitForDeployment();

          console.log(`MyToken deployed to: ${myToken.target}`);
        }

        main().catch((error) => {
          console.error(error);
          process.exitCode = 1;
        });
        ```

    **Important**: Save the deployed contract address (e.g., `0x...`), you will need it in Step 5.

6.  **Open a new terminal** and navigate back to your `naryo-hedera-tutorial` directory.

    ```bash
    cd ../
    ```

## Step 3: Publish Naryo Modules Locally

Before generating your Naryo project, you need to publish Naryo's artifacts to your local Maven repository (`~/.m2/repository`). This allows your new project to resolve Naryo dependencies.

Navigate to the cloned `naryo-project` directory and run the Gradle publish task:

```bash
cd naryo-project
./gradlew publishToMavenLocal
```

Once complete, navigate back to your `naryo-hedera-tutorial` directory.

```bash
cd ../
```

## Step 4: Generate a Naryo Spring Boot Project

Use the Naryo CLI Initializer to scaffold a new Spring Boot project. This will create a basic project structure with Naryo dependencies and a sample `application.yml`.

```bash
./naryo-project/gradlew :initializer:run --args="--name naryo-app --output ."
```
This command generates a folder `naryo-app/` in your current directory with a Spring Boot application ready to be configured.

## Step 5: Configure `application.yml`

Navigate into your newly generated `naryo-app` project:

```bash
cd naryo-app
```

Now, update the `src/main/resources/application.yml` file. You will need to add your Hedera node connection details and a filter for your deployed `MyToken` contract.

Replace the contents of `application.yml` with the following, making sure to update `${YOUR_CONTRACT_ADDRESS}` with the address you saved in Step 2:

```yaml
naryo:
  nodes:
    - id: hedera-node-id
      name: hedera-local
      type: HEDERA
      connection:
        type: HTTP
        endpoint:
          url: http://localhost:7546 # Hedera JSON-RPC Relay
      interaction:
        mode: HEDERA_MIRROR_NODE # Use Mirror Node for interaction
      subscription:
        method: POLL
        interval: 1s

  broadcasting:
    configuration:
      - id: event-receiver-http
        type: HTTP
        endpoint:
          url: http://localhost:8080 # This will be our Node.js event receiver
    broadcasters:
      - id: my-hedera-broadcaster
        configurationId: event-receiver-http
        target:
          type: FILTER
          filterId: my-transfer-filter-id
          destinations:
            - /events # Endpoint path on our Node.js receiver

  filters:
    - id: my-transfer-filter-id
      name: my-transfer-filter
      type: EVENT
      nodeId: hedera-node-id
      scope: CONTRACT
      specification:
        signature: Transfer(address indexed, address indexed, uint256)
      address: ${YOUR_CONTRACT_ADDRESS} # <--- UPDATE THIS
```

## Step 6: Set up an HTTP Event Receiver

We'll set up a simple Node.js HTTP server to act as a receiver for the events Naryo broadcasts.

1.  **Open a new terminal** and navigate to your `naryo-hedera-tutorial` directory.

    ```bash
    cd naryo-hedera-tutorial
    ```

2.  Create a new directory for the receiver and navigate into it:

    ```bash
    mkdir event-receiver && cd event-receiver
    ```

3.  Create `server.js` with the following content:

    ```javascript
    const express = require('express');
    const bodyParser = require('body-parser');

    const app = express();
    const port = 8080; // Must match the endpoint.url in application.yml

    app.use(bodyParser.json());

    app.post('/events', (req, res) => {
      console.log('✅ Event received:', JSON.stringify(req.body, null, 2));
      res.status(200).send('OK');
    });

    app.listen(port, () => {
      console.log(`🚀 Event receiver listening at http://localhost:${port}/events`);
    });
    ```

4.  Install dependencies and start the server in the background:

    ```bash
    npm install express body-parser
    nohup node server.js > event-receiver.log 2>&1 &
    ```

    Now, every event Naryo receives and broadcasts will be logged by this server.

5.  **Open a new terminal** and navigate back to your `naryo-app` project directory for the next step.

    ```bash
    cd ../naryo-app
    ```

## Step 7: Run the Naryo Application

1.  Ensure you are in the `naryo-app/` directory.
2.  Run the Naryo Spring Boot application:

    ```bash
    ./gradlew bootRun
    ```

    Naryo will start, connect to your Hedera node, and begin polling for `Transfer` events.

## Step 8: Verify Event Consumption

You can verify that Naryo is correctly forwarding events to your HTTP receiver by triggering a `Transfer` event from your `MyToken` contract.

1.  **Monitor Event Receiver Logs**: Open a new terminal and navigate to your `event-receiver` directory.

    ```bash
    cd naryo-hedera-tutorial/event-receiver
    tail -f event-receiver.log
    ```

    Keep this terminal open to see incoming events.

2.  **Trigger a Transfer Event**: Go to your `erc20-contract` directory (from a new terminal) and open the Hardhat console:

    ```bash
    cd naryo-hedera-tutorial/erc20-contract
    npx hardhat console --network local
    ```

    In the Hardhat console, execute the following commands (replace `DEPLOYED_CONTRACT_ADDRESS` and the recipient address as needed):

    ```javascript
    const MyToken = await ethers.getContractFactory("MyToken");
    const myToken = await MyToken.attach("DEPLOYED_CONTRACT_ADDRESS"); // Attach to your deployed contract

    await myToken.transfer("0x9ba8c0dF156194706942a86f49081542F13EbF42", 1); // Transfer 1 token
    ```

    You should immediately see the `Transfer` event payload printed in your `event-receiver.log` terminal.

## Troubleshooting

*   **`Connection refused` errors**: Ensure your Hedera network (started with `hedera start`) and Node.js event receiver (`event-receiver`) are running and accessible on their respective ports. The Hedera JSON-RPC relay typically runs on `localhost:7546`.
*   **No events in logs**: Double-check your `application.yml` in `naryo-app` for correct contract address, filter signature, and node ID. Also ensure Naryo application is running.

## Resources

*   [Hiero Local Node](https://github.com/hiero-ledger/hiero-local-node)
*   [Hardhat Documentation](https://hardhat.org/)
*   [OpenZeppelin Contracts](https://docs.openzeppelin.com/contracts)
*   [Naryo Getting Started Guide](../getting_started.md)
*   [Naryo Configuration Guide](../configuration/index.md)
