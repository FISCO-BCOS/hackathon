package types

import (
	"encoding/json"
	"fmt"
	"math/big"
	"strconv"
)

const (

	// precompile
	BCOS_RC1 string = "2.0.0-rc1"
	BCOS_RC2 string = "2.0.0-rc2"
	BCOS_RC3 string = "2.0.0-rc3"

	// system table for authority control
	USER_TABLE_PREFIX string = "_user_"
	SYS_TABLE         string = "_sys_tables_"
	SYS_TABLE_ACCESS  string = "_sys_table_access_"
	SYS_CONSENSUS     string = "_sys_consensus_"
	SYS_CNS           string = "_sys_cns_"
	SYS_CONFIG        string = "_sys_config_"

	// precompile success
	PreSuccess                      int = 0
	PermissionDenied_RC1            int = 80
	PermissionDenied_RC3            int = -50000
	TableExist                      int = 50001
	TableExist_RC3                  int = -50001
	TableNameAndAddressExist_RC1    int = 56
	TableNameAndAddressExist        int = 51000
	TableNameAndAddressExist_RC3    int = -51000
	TableNameAndAddressNotExist_RC1 int = 57
	TableNameAndAddressNotExist     int = 51001
	TableNameAndAddressNotExist_RC3 int = -51001
	InvalidNodeId                   int = -51100
	LastSealer_RC1                  int = 100
	LastSealer                      int = 51101
	LastSealer_RC3                  int = -51101
	P2pNetwork                      int = -51102
	GroupPeers                      int = -51103
	SealerList                      int = -51104
	ObserverList                    int = -51105
	ContractNameAndVersionExist     int = -51200
	VersionExceeds                  int = -51201
	InvalidKey_RC1                  int = 157
	InvalidKey                      int = 51300
	InvalidKey_RC3                  int = -51300

	TABLE_KEY_MAX_LENGTH int = 255

	BCOS_VERSION string = ""
)

// TransferToJson returns the message json according to the status code
func TransferToJson(code int) (string, error) {
	// adapt fisco-bcos rc3
	msg := ""
	if code == PermissionDenied_RC3 {
		msg = "permission denied"
	} else if code == TableNameAndAddressExist_RC3 {
		msg = "table name and address already exist"
	} else if code == TableNameAndAddressNotExist_RC3 {
		msg = "table name and address does not exist"
	} else if code == LastSealer_RC3 {
		msg = "the last sealer cannot be removed"
	} else if code == TableExist_RC3 {
		msg = "table already exist"
	} else if code == InvalidKey_RC3 {
		msg = "invalid configuration entry"
	}
	if code == PreSuccess {
		msg = "success"
	} else if code == InvalidNodeId {
		msg = "invalid node ID"
	} else if code == P2pNetwork {
		msg = "the node is not reachable"
	} else if code == GroupPeers {
		msg = "the node is not a group peer"
	} else if code == SealerList {
		msg = "the node is already in the sealer list"
	} else if code == ObserverList {
		msg = "the node is already in the observer list"
	} else if code == ContractNameAndVersionExist {
		msg = "contract name and version already exist"
	} else if code == VersionExceeds {
		msg = "version string length exceeds the maximum limit"
	}

	outputJSON, err := json.MarshalIndent(strconv.Itoa(code)+" "+msg, "", "\t")
	if err != nil {
		return "", fmt.Errorf("handle output code error: change output to json struct failed: %v", err)
	}
	return string(outputJSON[:]), nil
}

// GetJsonStr returns the json string according to the output
func GetJsonStr(output string) (string, error) {
	var code int
	i := new(big.Int)
	var flag bool
	i, flag = i.SetString(output[2:], 16)
	if flag == false {
		return "", fmt.Errorf("handleOutput: convert output to Int failed")
	}
	code = int(i.Uint64())
	if code == 1 {
		code = PreSuccess
	}
	return TransferToJson(code)
}
