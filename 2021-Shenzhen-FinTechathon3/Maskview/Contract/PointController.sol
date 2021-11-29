pragma solidity >=0.4.22 <0.6.0;

contract PointController {

     mapping(uint64 => uint64) idToPoint;

     mapping(string => string) tagToHash;

     mapping(uint64 => uint64) accountState; // state 0 is normal, other states are the frozen amounts


     event registerId(uint64 userId, uint64 value);
     event putOnTheShelf(string tag, string hash);
     event recordTheResult(uint64 userId, string result, string imgName);
     event recordTheEvidence(uint64 userPhone, uint64 tag, string imgName, string key);

     function register(uint64 userId) public returns (uint64) {
         idToPoint[userId] = 500;
         accountState[userId] = 0;
         emit registerId(userId, idToPoint[userId]);
         return idToPoint[userId];
     }

     function queryPoint(uint64 userId) public view returns (uint64) {
         return idToPoint[userId];
     }

     //return true is success, return false is fault
     function recharge(uint64 userId, uint64 value) public returns (bool) {
         if(idToPoint[userId] < 0) {
             return false;
         }
         idToPoint[userId] += value;
         return true;
     }

     //hash is the base64hash
     function putImgOnTheShelf(string tag, string hash) public {
         tagToHash[tag] = hash;
         emit putOnTheShelf(tag, hash);
     }

     function queryHash(string tag) public view returns (string) {
         return tagToHash[tag];
     }

     function normalTrade(uint64 buyerId, uint64 sellerId, uint64 value) public returns (bool) {
         if(idToPoint[buyerId] < value) {
         return false;
         }
         idToPoint[buyerId] -= value;
         idToPoint[sellerId] += value;
         return true;
     }

     //return true is success, return false is fault
     function freezeAccount(uint64 userId, uint64 value) public returns (bool) {
         if(idToPoint[userId] < value || accountState[userId] != 0) {
             return false;
         }
         idToPoint[userId] -= value;
         accountState[userId] = value;
         return true;
     }

     function returnPoint(uint64 userId) public {
         idToPoint[userId] += accountState[userId];
         accountState[userId] = 0;
     }

     function succeedTrade(uint64 buyerId, uint64 sellerId) public {
         idToPoint[sellerId] += accountState[buyerId];
         accountState[buyerId] = 0;
     }

     function recordResult(uint64 userId, string result, string imgName) public {
         recordTheResult(userId, result, imgName);
     }
     
     function recordEvidence(uint64 userPhone, uint64 tag, string imgName, string key) public {
         recordTheEvidence(userPhone, tag, imgName, key);
     }


}