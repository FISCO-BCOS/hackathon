package consensus

import (
	"context"
	"encoding/json"
	"fmt"

	"github.com/FISCO-BCOS/go-sdk/abi/bind"
	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/core/types"
	"github.com/FISCO-BCOS/go-sdk/precompiled"
	"github.com/ethereum/go-ethereum/common"
)

// consensus precompiled contract error code
const (
	lastSealer    int64 = -51101
	invalidNodeID int64 = -51100
)

// getErrorMessage returns the message of error code
func getErrorMessage(errorCode int64) string {
	var message string
	switch errorCode {
	case lastSealer:
		message = "last sealer"
	case invalidNodeID:
		message = "invalid nodeID"
	default:
		message = ""
	}
	return message
}

// errorCodeToError judges whether the error code represents an error
func errorCodeToError(errorCode int64) error {
	var errorCodeMessage string
	errorCodeMessage = precompiled.GetCommonErrorCodeMessage(errorCode)
	if errorCodeMessage != "" {
		return fmt.Errorf("error code: %v, error code message: %v", errorCode, errorCodeMessage)
	}
	errorCodeMessage = getErrorMessage(errorCode)
	if errorCodeMessage != "" {
		return fmt.Errorf("error code: %v, error code message: %v", errorCode, errorCodeMessage)
	}
	return nil
}

// Service is a precompile contract service.
type Service struct {
	consensus     *Consensus
	consensusAuth *bind.TransactOpts
	client        *client.Client
}

// contract address
var consensusPrecompileAddress = common.HexToAddress("0x0000000000000000000000000000000000001003")

// NewConsensusService returns ptr of ConsensusService
func NewConsensusService(client *client.Client) (*Service, error) {
	instance, err := NewConsensus(consensusPrecompileAddress, client)
	if err != nil {
		return nil, fmt.Errorf("construct ConsensusService failed: %+v", err)
	}
	auth := client.GetTransactOpts()
	return &Service{consensus: instance, consensusAuth: auth, client: client}, nil
}

// AddObserver add a new observe node according to the node ID
func (service *Service) AddObserver(nodeID string) (int64, error) {
	flag, err := service.isValidNodeID(nodeID)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("AddObserver failed, err: %v", err)
	}
	if !flag {
		return precompiled.DefaultErrorCode, fmt.Errorf("the node is not reachable")
	}

	observerRaw, err := service.client.GetObserverList(context.Background())
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("get the observer list failed: %v", err)
	}

	var nodeIDs []string
	err = json.Unmarshal(observerRaw, &nodeIDs)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("unmarshal the observer list failed: %v", err)
	}

	for _, nID := range nodeIDs {
		if nID == nodeID {
			return precompiled.DefaultErrorCode, fmt.Errorf("the node is already in the observer list")
		}
	}
	_, receipt, err := service.consensus.AddObserver(service.consensusAuth, nodeID)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ConsensusService addObserver failed: %+v", err)
	}
	return parseReturnValue(receipt, "addObserver")
}

// AddSealer add a new sealer node according to the node ID
func (service *Service) AddSealer(nodeID string) (int64, error) {
	flag, err := service.isValidNodeID(nodeID)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("AddSealer failed, err: %v", err)
	}
	if !flag {
		return precompiled.DefaultErrorCode, fmt.Errorf("the node is not reachable")
	}

	sealerRaw, err := service.client.GetSealerList(context.Background())
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("get the sealer list failed: %v", err)
	}

	var nodeIDs []string
	err = json.Unmarshal(sealerRaw, &nodeIDs)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("unmarshal the sealer list failed: %v", err)
	}

	for _, nID := range nodeIDs {
		if nID == nodeID {
			return precompiled.DefaultErrorCode, fmt.Errorf("the node is already in the sealer list")
		}
	}

	tx, receipt, err := service.consensus.AddSealer(service.consensusAuth, nodeID)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ConsensusService addSealer failed: %+v", err)
	}
	_ = tx
	return parseReturnValue(receipt, "addSealer")
}

// RemoveNode remove a sealer node according to the node ID
func (service *Service) RemoveNode(nodeID string) (int64, error) {
	peersRaw, err := service.client.GetGroupPeers(context.Background())
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("get the group peers failed: %v", err)
	}

	var nodeIDs []string
	err = json.Unmarshal(peersRaw, &nodeIDs)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("unmarshal the group peers failed: %v", err)
	}

	var flag = true
	for _, nID := range nodeIDs {
		if nID == nodeID {
			flag = false
			break
		}
	}
	if flag {
		return precompiled.DefaultErrorCode, fmt.Errorf("the node is not a group peer")
	}

	_, receipt, err := service.consensus.Remove(service.consensusAuth, nodeID)
	// maybe will occur something wrong
	// when request the receipt from the SDK since the connected node of SDK is removed
	//TODO: how to handle the problem that can't get the tx receipt when remove the connected node of SDK
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ConsensusService Remove failed: %+v", err)
	}
	return parseReturnValue(receipt, "remove")
}

// isValidNodeID returns true if the nodeID exits in NodeIDList.
func (service *Service) isValidNodeID(nodeID string) (bool, error) {
	nodeIDRaw, err := service.client.GetNodeIDList(context.Background())
	if err != nil {
		return false, fmt.Errorf("get the valid Node IDs failed: %v", err)
	}
	var nodeIDs []string
	err = json.Unmarshal(nodeIDRaw, &nodeIDs)
	if err != nil {
		return false, fmt.Errorf("unmarshal the valid Node IDs failed: %v", err)
	}
	var flag = false
	for _, nID := range nodeIDs {
		if nID == nodeID {
			flag = true
		}
	}
	return flag, nil
}

func parseReturnValue(receipt *types.Receipt, name string) (int64, error) {
	errorMessage := receipt.GetErrorMessage()
	if errorMessage != "" {
		return int64(receipt.GetStatus()), fmt.Errorf("receipt.Status err: %v", errorMessage)
	}
	bigNum, err := precompiled.ParseBigIntFromOutput(receipt)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("parseReturnValue failed, err: %v", err)
	}
	errorCode, err := precompiled.BigIntToInt64(bigNum)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("parseReturnValue failed, err: %v", err)
	}
	return errorCode, errorCodeToError(errorCode)
}
