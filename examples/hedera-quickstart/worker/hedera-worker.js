const {
    Client,
    PrivateKey,
    TopicCreateTransaction,
    TopicMessageSubmitTransaction,
    TokenCreateTransaction,
    TransferTransaction,
    AccountCreateTransaction,
    TokenAssociateTransaction,
    Hbar,
    AccountId,
} = require("@hashgraph/sdk");
const { ethers } = require("ethers");
const fs = require("fs");
const path = require("path");
const solc = require("solc");

async function main() {
    // 1. Setup Hedera Client
    const myAccountId = process.env.RELAY_OPERATOR_ID_MAIN || process.env.OPERATOR_ID_MAIN || "0.0.2";
    const myPrivateKeyRaw = process.env.RELAY_OPERATOR_KEY_MAIN || process.env.OPERATOR_KEY_MAIN;
    if (!myPrivateKeyRaw) throw new Error("Missing operator key in .env");

    // Clean key for ethers (must be 32 bytes / 64 hex chars)
    let cleanKey = myPrivateKeyRaw.replace(/^0x/, "");
    if (cleanKey.length === 96) {
        // Hedera DER header is 32 hex chars, followed by 64 hex chars of raw key
        cleanKey = cleanKey.slice(32);
    }
    const ethersPrivateKey = "0x" + cleanKey;

    const myPrivateKey = PrivateKey.fromString(myPrivateKeyRaw);

    const client = Client.forNetwork({ "host.docker.internal:35211": new AccountId(3) });
    client.setOperator(myAccountId, myPrivateKey);

    // 1.1 Activate Ethers Wallet on Hedera
    const provider = new ethers.JsonRpcProvider("http://host.docker.internal:37546");
    const wallet = new ethers.Wallet(ethersPrivateKey, provider);

    console.log(`Funding EVM account ${wallet.address}...`);
    const fundingTx = await new TransferTransaction()
        .addHbarTransfer(myAccountId, new Hbar(-100))
        .addHbarTransfer(wallet.address, new Hbar(100))
        .execute(client);
    await fundingTx.getReceipt(client);
    console.log("EVM account funded.");

    console.log("Waiting for Hedera node...");
    let topicId, tokenId, receiverId;
    
    // 2. Setup Phase
    for (let i = 0; i < 30; i++) {
        try {
            // Create Topic
            const topicResponse = await new TopicCreateTransaction().execute(client);
            topicId = (await topicResponse.getReceipt(client)).topicId;
            console.log(`Created Topic: ${topicId}`);

            // Create Token
            const tokenResponse = await new TokenCreateTransaction()
                .setTokenName("Naryo Token").setTokenSymbol("NRO").setDecimals(0).setInitialSupply(10000)
                .setTreasuryAccountId(myAccountId).setAdminKey(myPrivateKey).setSupplyKey(myPrivateKey)
                .execute(client);
            tokenId = (await tokenResponse.getReceipt(client)).tokenId;
            console.log(`Created Token: ${tokenId}`);

            // Create Receiver & Associate
            const newKey = PrivateKey.generateED25519();
            const accountResponse = await new AccountCreateTransaction().setKey(newKey.publicKey).setInitialBalance(new Hbar(10)).execute(client);
            receiverId = (await accountResponse.getReceipt(client)).accountId;
            console.log(`Created Receiver: ${receiverId}`);

            await (await (await new TokenAssociateTransaction().setAccountId(receiverId).setTokenIds([tokenId]).freezeWith(client).sign(newKey)).execute(client)).getReceipt(client);
            console.log("Setup Assets OK");
            break;
        } catch (err) {
            console.log(`Retrying setup (${i+1}/30): ${err.message}`);
            await new Promise(r => setTimeout(r, 5000));
        }
    }

    // 3. Deploy Contract
    console.log("Compiling & Deploying Contract...");
    const contractSource = fs.readFileSync(path.join(__dirname, "contracts", "HederaQuickstart.sol"), "utf8");
    const input = {
        language: "Solidity",
        sources: { "HederaQuickstart.sol": { content: contractSource } },
        settings: { outputSelection: { "*": { "*": ["*"] } } },
    };
    const output = JSON.parse(solc.compile(JSON.stringify(input)));
    const contractData = output.contracts["HederaQuickstart.sol"]["HederaQuickstart"];

    const factory = new ethers.ContractFactory(contractData.abi, contractData.evm.bytecode.object, wallet);
    const contract = await factory.deploy();
    await contract.waitForDeployment();
    const contractAddress = await contract.getAddress();
    console.log(`Deployed Contract: ${contractAddress}`);

    // 4. Save IDs for Naryo
    const propsContent = [
        `TOPIC_ID=${topicId}`,
        `TOKEN_ID=${tokenId}`,
        `CONTRACT_ADDRESS=${contractAddress}`
    ].join("\n");
    fs.writeFileSync(path.join(__dirname, "shared", "generated.properties"), propsContent);
    console.log("IDs saved to shared volume.");

    // 5. Periodic Loop
    const toEvmAddr = (id) => "0x" + AccountId.fromString(id.toString()).toSolidityAddress().replace(/^0x/, "");
    const topicAddr = toEvmAddr(topicId);
    const tokenAddr = toEvmAddr(tokenId);
    const receiverAddr = toEvmAddr(receiverId);

    console.log("Starting periodic transactions (every 30s)...");
    while (true) {
        try {
            const timestamp = new Date().toISOString();
            console.log(`--- Cycle started at ${timestamp} ---`);

            // a. Native HCS
            process.stdout.write("Native HCS... ");
            await (await new TopicMessageSubmitTransaction({ topicId, message: `Native msg at ${timestamp}` }).execute(client)).getReceipt(client);
            console.log("OK");

            // b. Native HTS
            process.stdout.write("Native HTS... ");
            await (await new TransferTransaction().addTokenTransfer(tokenId, myAccountId, -1).addTokenTransfer(tokenId, receiverId, 1).execute(client)).getReceipt(client);
            console.log("OK");

            // c. Contract Events
            process.stdout.write("EVM Events... ");
            const tx1 = await contract.emitHcsEvent(topicAddr, `Event msg at ${timestamp}`);
            await tx1.wait();
            const tx2 = await contract.emitHtsEvent(tokenAddr, wallet.address, receiverAddr, 1);
            await tx2.wait();
            console.log("OK");

            console.log("Cycle finished.\n");
        } catch (err) {
            console.error("Error:", err.message);
        }
        await new Promise(r => setTimeout(r, 30000));
    }
}

main().catch(err => { console.error(err); process.exit(1); });
