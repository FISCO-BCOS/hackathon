//Contract based on [https://docs.openzeppelin.com/contracts/3.x/erc721](https://docs.openzeppelin.com/contracts/3.x/erc721)
// SPDX-License-Identifier: MIT
pragma solidity ^0.4.25;

import "./openzeppelin-contracts/contracts/token/ERC721/ERC721.sol";
import "./openzeppelin-contracts/contracts/ownership/Ownable.sol";
import "./openzeppelin-contracts/contracts/token/ERC721/ERC721Metadata.sol";
import "./openzeppelin-contracts/contracts/drafts/Counter.sol";

contract SimpleERC721 is ERC721Metadata, Ownable {
    using Counter for Counter.Counter;
    Counter.Counter private _tokenIds;

    constructor() ERC721Metadata("SimpleERC721", "NFT") {}

    function mintNFT(address recipient, string memory tokenURI)
        public
        returns (uint256)
    {
        uint256 newItemId = _tokenIds.next();
        _mint(recipient, newItemId);
        _setTokenURI(newItemId, tokenURI);
        approve(0x1, newItemId);

        return newItemId;
    }
}
