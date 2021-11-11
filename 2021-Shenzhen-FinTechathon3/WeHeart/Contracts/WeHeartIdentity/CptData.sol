pragma solidity ^ 0.4.4;

import "./GovernmentData.sol";

contract CptData{
    // CPT ID has been categorized into 3 zones: 0 - 999 are reserved for system CPTs,
    //  1000-2000000 for Authority Issuer's CPTs, and the rest for common WeIdentiy DIDs.
    uint constant public GOVERNMENT_START_ID = 1000;
    uint constant public NONE_GOVERNMENT_START_ID = 2000000;
    uint private government_current_id = 1000;
    uint private none_government_current_id = 2000000;

    GovernmentData private governmentData;

    function CptData(
        address governmentDataAddress
    )
        public
    {
        governmentData = GovernmentData(governmentDataAddress);
    }

    struct Signature {
        uint8 v;
        bytes32 r;
        bytes32 s;
    }

    struct Cpt {
        //store the weid address of cpt publisher
        address publisher;
        // [0]: cpt version, [1]: created, [2]: updated, [3]: the CPT ID
        int[8] intArray;
        // [0]: desc
        bytes32[8] bytes32Array;
        //store json schema
        bytes32[128] jsonSchemaArray;
        //store the government signature
        Signature goversignature;
        //store signature
        Signature signature;

    }

    mapping(uint => Cpt) private cptMap;
    uint[] private cptIdList;

    function putCptnoGovernmentSignature(
        uint cptId,
        address cptPublisher,
        int[8] cptIntArray,
        bytes32[8] cptBytes32Array,
        bytes32[128] cptJsonSchemaArray,
        uint8 cptV,
        bytes32 cptR,
        bytes32 cptS
    )
        public
        returns(bool)
    {
        Signature memory cptSignature = Signature({v: cptV, r : cptR, s : cptS});
        cptMap[cptId].publisher = cptPublisher;
        cptMap[cptId].intArray = cptIntArray;
        cptMap[cptId].bytes32Array = cptBytes32Array;
        cptMap[cptId].jsonSchemaArray = cptJsonSchemaArray;
        cptMap[cptId].signature = cptSignature;
        return true;
        }


    function putgovernmentsignature(
        uint cptId,
        uint8 goverV,
        bytes32 goverR,
        bytes32 goverS
    )
        public
        returns(bool)
    {
        Signature memory cptGoverSignature = Signature({v: goverV, r : goverR , s : goverS });
        cptMap[cptId].goversignature = cptGoverSignature;
        cptIdList.push(cptId);
        return true;
    }

    function getCptId(
        address publisher
    )
        public
        constant
        returns
        (uint cptId)
    {
        if (governmentData.isGovernment(publisher)) {
            while (isCptExist(government_current_id)) {
                government_current_id++;
            }
            cptId = government_current_id++;
            if (cptId >= NONE_GOVERNMENT_START_ID) {
                cptId = 0;
            }
        }
     else {
      while (isCptExist(none_government_current_id)) {
          none_government_current_id++;
      }
      cptId = none_government_current_id++;
    }
    }
    
    function getCpt(
        uint cptId
    )
        public
        constant
        returns(
        address publisher,
        int[8] intArray,
        bytes32[8] bytes32Array,
        bytes32[128] jsonSchemaArray,
        uint8 goverv,
        bytes32 goverr,
        bytes32 govers,
        uint8 v,
        bytes32 r,
        bytes32 s)
    {
        Cpt memory cpt = cptMap[cptId];
        publisher = cpt.publisher;
        intArray = cpt.intArray;
        bytes32Array = cpt.bytes32Array;
        jsonSchemaArray = cpt.jsonSchemaArray;
        goverv = cpt.goversignature.v;
        goverr = cpt.goversignature.r;
        govers = cpt.goversignature.s;
        v = cpt.signature.v;
        r = cpt.signature.r;
        s = cpt.signature.s;
    }
    
    function getCptPublisher(
        uint cptId
    )
        public
        constant
        returns(address publisher)
    {
        Cpt memory cpt = cptMap[cptId];
        publisher = cpt.publisher;
    }
    
    function getCptIntArray(
        uint cptId
    )
        public
        constant
        returns(int[8] intArray)
    {
        Cpt memory cpt = cptMap[cptId];
        intArray = cpt.intArray;
    }
    
    function getCptJsonSchemaArray(
        uint cptId
    )
        public
        constant
        returns(bytes32[128] jsonSchemaArray)
    {
        Cpt memory cpt = cptMap[cptId];
        jsonSchemaArray = cpt.jsonSchemaArray;
    }
    
    function getCptBytes32Array(
        uint cptId
    )
        public
        constant
        returns(bytes32[8] bytes32Array)
    {
        Cpt memory cpt = cptMap[cptId];
        bytes32Array = cpt.bytes32Array;
    }
    
    function getGoverSignature(
        uint cptId
    )
    public
    constant
    returns(uint8 goverv, bytes32 goverr, bytes32 govers)
    {
        Cpt memory cpt = cptMap[cptId];
        goverv = cpt.goversignature.v;
        goverr = cpt.goversignature.r;
        govers = cpt.goversignature.s;
    }
    
    
    function getCptSignature(
        uint cptId
    )
        public
        constant
        returns(uint8 v, bytes32 r, bytes32 s)
    {
        Cpt memory cpt = cptMap[cptId];
        v = cpt.signature.v;
        r = cpt.signature.r;
        s = cpt.signature.s;
    }
    
    
    function isCptExist(
        uint cptId
    )
        public
        constant
        returns(bool)
    {
        int[8] memory intArray = getCptIntArray(cptId);
        if (intArray[0] != 0) {
            return true;
        }
    else {
     return false;
    }
    }
    
    function getDatasetLength() public constant returns(uint) {
        return cptIdList.length;
    }
    
    function getCptIdFromIndex(uint index) public constant returns(uint) {
        return cptIdList[index];
    }
    }