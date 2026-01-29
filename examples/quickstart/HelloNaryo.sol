// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

//This contract is deployed by the sc-deployer container.
contract HelloNaryo {
    
    uint256 i;

    event HelloCounter(uint256 i_);

    function greetings() external {
        emit HelloCounter(++i);
    }
}