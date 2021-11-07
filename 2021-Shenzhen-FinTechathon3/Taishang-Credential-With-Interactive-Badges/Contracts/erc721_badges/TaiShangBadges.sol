// SPDX-License-Identifier: MIT
pragma solidity 0.6.10;

import "./ERC721.sol";
import "./Counters.sol";
import "./Ownable.sol";

contract TaiShangBadges is ERC721, Ownable {
    using Counters for Counters.Counter;
    Counters.Counter private _tokenIds;
    
    mapping(uint256 =>bool) public tokenIdIsTransfer;
    constructor() ERC721("TaiShang Credentials", "TSC") public{}

    function mintNft(address receiver,string memory tokenURI,bool isTransfer) external onlyOwner returns (uint256) {
        _tokenIds.increment();
        uint256 newNftTokenId = _tokenIds.current();
        _mint(receiver, newNftTokenId);
        _setTokenURI(newNftTokenId, tokenURI);
        tokenIdIsTransfer[newNftTokenId] = isTransfer;
        return newNftTokenId;
    }
    function transferTo(address receiver,uint256 tokenId) external {
        require(msg.sender == ownerOf(tokenId),"do not have this tokenId");
        require(tokenIdIsTransfer[tokenId],"do not transferTo");
        transferFrom(msg.sender,receiver,tokenId);
    }
}
