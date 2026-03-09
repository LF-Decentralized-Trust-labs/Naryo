// SPDX-License-Identifier: Apache-2.0
pragma solidity ^0.8.0;

contract HederaQuickstart {
    event HCSMessage(address topic, string message);
    event HTSTransfer(address token, address sender, address receiver, uint256 amount);

    function emitHcsEvent(address topic, string memory message) external {
        emit HCSMessage(topic, message);
    }

    function emitHtsEvent(address token, address sender, address receiver, uint256 amount) external {
        emit HTSTransfer(token, sender, receiver, amount);
    }
}
