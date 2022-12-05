// Copyright FISCO-BCOS go-sdk
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// Package client provides a client for the FISCO BCOS RPC API.
package client

import (
	"context"
	"crypto/ecdsa"
	"errors"
	"fmt"
	"io/ioutil"
	"math/big"
	"strconv"
	"strings"

	"github.com/FISCO-BCOS/go-sdk/abi/bind"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/FISCO-BCOS/go-sdk/conn"
	"github.com/FISCO-BCOS/go-sdk/core/types"
	"github.com/ethereum/go-ethereum"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/common/hexutil"
	"github.com/ethereum/go-ethereum/crypto"
	"github.com/sirupsen/logrus"
)

// Client defines typed wrappers for the Ethereum RPC API.
type Client struct {
	apiHandler        *APIHandler
	groupID           int
	chainID           int64
	compatibleVersion int
	auth              *bind.TransactOpts
	callOpts          *bind.CallOpts
	smCrypto          bool
}

const (
	//V2_5_0 is node version v2.5.0
	V2_5_0 int = 0x02050000
)

// Dial connects a client to the given URL and groupID.
func Dial(config *conf.Config) (*Client, error) {
	return DialContext(context.Background(), config)
}

// DialContext pass the context to the rpc client
func DialContext(ctx context.Context, config *conf.Config) (*Client, error) {
	var c *conn.Connection
	var err error
	if config.IsHTTP {
		c, err = conn.DialContextHTTP(config.NodeURL)
	} else {
		// try to parse use file
		if config.TLSCAContext == nil {
			config.TLSCAContext, err = ioutil.ReadFile(config.CAFile)
			if err != nil {
				return nil, fmt.Errorf("parse tls root certificate %v failed, err:%v", config.CAFile, err)
			}
		}
		if config.TLSCertContext == nil {
			config.TLSCertContext, err = ioutil.ReadFile(config.Cert)
			if err != nil {
				return nil, fmt.Errorf("parse tls certificate %v failed, err:%v", config.Cert, err)
			}
		}
		if config.TLSKeyContext == nil {
			config.TLSKeyContext, err = ioutil.ReadFile(config.Key)
			if err != nil {
				return nil, fmt.Errorf("parse tls key %v failed, err:%v", config.Key, err)
			}
		}
		c, err = conn.DialContextChannel(config.NodeURL, config.TLSCAContext, config.TLSCertContext, config.TLSKeyContext, config.GroupID)
	}
	if err != nil {
		return nil, err
	}
	apiHandler := NewAPIHandler(c)

	cv, err := apiHandler.GetClientVersion(ctx)
	if err != nil {
		return nil, fmt.Errorf("%v", err)
	}

	// get supported FISCO BCOS version
	var compatibleVersionStr string
	if cv.GetSupportedVersion() == "" {
		return nil, errors.New("JSON response does not contains the key : Supported Version")
	} else {
		compatibleVersionStr = cv.GetSupportedVersion()
	}
	compatibleVersion, err := getVersionNumber(compatibleVersionStr)
	if err != nil {
		return nil, fmt.Errorf("DialContext failed, err: %v", err)
	}

	// determine whether FISCO-BCOS Version is consistent with SMCrypto configuration item
	var fiscoBcosVersion string
	if cv.SupportedVersion == "" {
		return nil, errors.New("JSON response does not contains the key : FISCO-BCOS Version")
	} else {
		fiscoBcosVersion = cv.GetFiscoBcosVersion()
	}
	nodeIsSupportedSM := strings.Contains(fiscoBcosVersion, "gm") || strings.Contains(fiscoBcosVersion, "GM")
	if nodeIsSupportedSM != config.IsSMCrypto {
		return nil, fmt.Errorf("the SDK set SMCrypt=%v, but the node is mismatched", config.IsSMCrypto)
	}

	// get node chain ID
	var nodeChainID int64
	nodeChainID, err = strconv.ParseInt(cv.GetChainId(), 10, 64)
	if err != nil {
		return nil, errors.New("JSON response does not contains the key : Chain Id")
	}
	if config.ChainID != nodeChainID {
		return nil, errors.New("The chain ID of node is " + fmt.Sprint(nodeChainID) + ", but configuration is " + fmt.Sprint(config.ChainID))
	}

	client := Client{apiHandler: apiHandler, groupID: config.GroupID, compatibleVersion: compatibleVersion, chainID: config.ChainID, smCrypto: config.IsSMCrypto}
	if config.IsSMCrypto {
		client.auth = bind.NewSMCryptoTransactor(config.PrivateKey)
	} else {
		privateKey, err := crypto.ToECDSA(config.PrivateKey)
		if err != nil {
			logrus.Fatal(err)
		}
		client.auth = bind.NewKeyedTransactor(privateKey)
	}
	client.auth.GasLimit = big.NewInt(30000000)
	client.callOpts = &bind.CallOpts{From: client.auth.From}
	return &client, nil
}

// Close disconnects the rpc
func (c *Client) Close() {
	c.apiHandler.Close()
}

// ============================================== FISCO BCOS Blockchain Access ================================================

// GetTransactOpts return *bind.TransactOpts
func (c *Client) GetTransactOpts() *bind.TransactOpts {
	return c.auth
}

// GetCallOpts return *bind.CallOpts
func (c *Client) GetCallOpts() *bind.CallOpts {
	return c.callOpts
}

// WaitMined is wrapper of bind.WaitMined
func (c *Client) WaitMined(tx *types.Transaction) (*types.Receipt, error) {
	return bind.WaitMined(context.Background(), c, tx)
}

// SMCrypto returns true if use sm crypto
func (c *Client) SMCrypto() bool {
	return c.smCrypto
}

// CodeAt returns the contract code of the given account.
// The block number can be nil, in which case the code is taken from the latest known block.
func (c *Client) CodeAt(ctx context.Context, account common.Address, blockNumber *big.Int) ([]byte, error) {
	return c.apiHandler.GetCode(ctx, c.groupID, account)
}

// Filters
func toBlockNumArg(number *big.Int) string {
	if number == nil {
		return "latest"
	}
	return hexutil.EncodeBig(number)
}

func toFilterArg(q ethereum.FilterQuery) (interface{}, error) {
	arg := map[string]interface{}{
		"address": q.Addresses,
		"topics":  q.Topics,
	}
	if q.BlockHash != nil {
		arg["blockHash"] = *q.BlockHash
		if q.FromBlock != nil || q.ToBlock != nil {
			return nil, fmt.Errorf("cannot specify both BlockHash and FromBlock/ToBlock")
		}
	} else {
		if q.FromBlock == nil {
			arg["fromBlock"] = "0x0"
		} else {
			arg["fromBlock"] = toBlockNumArg(q.FromBlock)
		}
		arg["toBlock"] = toBlockNumArg(q.ToBlock)
	}
	return arg, nil
}

// Pending State

// PendingCodeAt returns the contract code of the given account in the pending state.
func (c *Client) PendingCodeAt(ctx context.Context, account common.Address) ([]byte, error) {
	return c.apiHandler.GetCode(ctx, c.groupID, account)
}

// Contract Calling

// CallContract invoke the call method of rpc api
func (c *Client) CallContract(ctx context.Context, msg ethereum.CallMsg, blockNumber *big.Int) ([]byte, error) {
	return c.apiHandler.Call(ctx, c.groupID, msg)
}

// PendingCallContract executes a message call transaction using the EVM.
// The state seen by the contract call is the pending state.
func (c *Client) PendingCallContract(ctx context.Context, msg ethereum.CallMsg) ([]byte, error) {
	return c.apiHandler.Call(ctx, c.groupID, msg)
}

// SendTransaction injects a signed transaction into the pending pool for execution.
//
// If the transaction was a contract creation use the TransactionReceipt method to get the
// contract address after the transaction has been mined.
func (c *Client) SendTransaction(ctx context.Context, tx *types.Transaction) (*types.Receipt, error) {
	return c.apiHandler.SendRawTransaction(ctx, c.groupID, tx)
}

// AsyncSendTransaction send transaction async
func (c *Client) AsyncSendTransaction(ctx context.Context, tx *types.Transaction, handler func(*types.Receipt, error)) error {
	return c.apiHandler.AsyncSendRawTransaction(ctx, c.groupID, tx, handler)
}

// TransactionReceipt returns the receipt of a transaction by transaction hash.
// Note that the receipt is not available for pending transactions.
func (c *Client) TransactionReceipt(ctx context.Context, txHash common.Hash) (*types.Receipt, error) {
	return c.apiHandler.GetTransactionReceipt(ctx, c.groupID, txHash)
}

func (c *Client) SubscribeEventLogs(eventLogParams types.EventLogParams, handler func(int, []types.Log)) error {
	return c.apiHandler.SubscribeEventLogs(eventLogParams, handler)
}

func (c *Client) SubscribeTopic(topic string, handler func([]byte, *[]byte)) error {
	return c.apiHandler.SubscribeTopic(topic, handler)
}

func (c *Client) SendAMOPMsg(topic string, data []byte) ([]byte, error) {
	return c.apiHandler.SendAMOPMsg(topic, data)
}

func (c *Client) BroadcastAMOPMsg(topic string, data []byte) error {
	return c.apiHandler.BroadcastAMOPMsg(topic, data)
}

func (c *Client) UnsubscribeTopic(topic string) error {
	return c.apiHandler.UnsubscribeTopic(topic)
}

func (c *Client) SubscribePrivateTopic(topic string, privateKey *ecdsa.PrivateKey, handler func([]byte, *[]byte)) error {
	return c.apiHandler.SubscribePrivateTopic(topic, privateKey, handler)
}

func (c *Client) PublishPrivateTopic(topic string, publicKey []*ecdsa.PublicKey) error {
	return c.apiHandler.PublishPrivateTopic(topic, publicKey)
}

func (c *Client) SendAMOPPrivateMsg(topic string, data []byte) ([]byte, error) {
	return c.apiHandler.SendAMOPPrivateMsg(topic, data)
}

func (c *Client) BroadcastAMOPPrivateMsg(topic string, data []byte) error {
	return c.apiHandler.BroadcastAMOPPrivateMsg(topic, data)
}

func (c *Client) UnsubscribePrivateTopic(topic string) error {
	return c.apiHandler.UnsubscribePrivateTopic(topic)
}

func (c *Client) SubscribeBlockNumberNotify(handler func(int64)) error {
	return c.apiHandler.SubscribeBlockNumberNotify(uint64(c.groupID), handler)
}

func (c *Client) UnsubscribeBlockNumberNotify() error {
	return c.apiHandler.UnsubscribeBlockNumberNotify(uint64(c.groupID))
}

// GetGroupID returns the groupID of the client
func (c *Client) GetGroupID() *big.Int {
	return big.NewInt(int64(c.groupID))
}

// SetGroupID sets the groupID of the client
func (c *Client) SetGroupID(newID int) {
	c.groupID = newID
}

// GetCompatibleVersion returns the compatible version of FISCO BCOS
func (c *Client) GetCompatibleVersion() int {
	return c.compatibleVersion
}

// GetClientVersion returns the version of FISCO BCOS running on the nodes.
func (c *Client) GetClientVersion(ctx context.Context) (*types.ClientVersion, error) {
	return c.apiHandler.GetClientVersion(ctx)
}

// GetChainID returns the Chain ID of the FISCO BCOS running on the nodes.
func (c *Client) GetChainID(ctx context.Context) (*big.Int, error) {
	convertor := new(big.Int)
	var chainid = convertor.SetInt64(c.chainID)
	return chainid, nil
}

// GetBlockNumber returns the latest block height(hex format) on a given groupID.
func (c *Client) GetBlockNumber(ctx context.Context) (int64, error) {
	return c.apiHandler.GetBlockNumber(ctx, c.groupID)
}

// GetBlockLimit returns the blocklimit for current blocknumber
func (c *Client) GetBlockLimit(ctx context.Context) (*big.Int, error) {
	return c.apiHandler.GetBlockLimit(ctx, c.groupID)
}

// GetPBFTView returns the latest PBFT view(hex format) of the specific group and it will returns a wrong sentence
// if the consensus algorithm is not the PBFT.
func (c *Client) GetPBFTView(ctx context.Context) ([]byte, error) {
	return c.apiHandler.GetPBFTView(ctx, c.groupID)
	// TODO
	// Raft consensus
}

// GetSealerList returns the list of consensus nodes' ID according to the groupID
func (c *Client) GetSealerList(ctx context.Context) ([]byte, error) {
	return c.apiHandler.GetSealerList(ctx, c.groupID)
}

// GetObserverList returns the list of observer nodes' ID according to the groupID
func (c *Client) GetObserverList(ctx context.Context) ([]byte, error) {
	return c.apiHandler.GetObserverList(ctx, c.groupID)
}

// GetConsensusStatus returns the status information about the consensus algorithm on a specific groupID
func (c *Client) GetConsensusStatus(ctx context.Context) ([]byte, error) {
	return c.apiHandler.GetConsensusStatus(ctx, c.groupID)
}

// GetSyncStatus returns the synchronization status of the group
func (c *Client) GetSyncStatus(ctx context.Context) (*types.SyncStatus, error) {
	return c.apiHandler.GetSyncStatus(ctx, c.groupID)
}

// GetPeers returns the information of the connected peers
func (c *Client) GetPeers(ctx context.Context) (*[]types.Node, error) {
	return c.apiHandler.GetPeers(ctx, c.groupID)
}

// GetGroupPeers returns the nodes and the overser nodes list on a specific group
func (c *Client) GetGroupPeers(ctx context.Context) ([]byte, error) {
	return c.apiHandler.GetGroupPeers(ctx, c.groupID)
}

// GetNodeIDList returns the ID information of the connected peers and itself
func (c *Client) GetNodeIDList(ctx context.Context) ([]byte, error) {
	return c.apiHandler.GetNodeIDList(ctx, c.groupID)
}

// GetGroupList returns the groupID list that the node belongs to
func (c *Client) GetGroupList(ctx context.Context) ([]byte, error) {
	return c.apiHandler.GetGroupList(ctx)
}

// GetBlockByHash returns the block information according to the given block hash
func (c *Client) GetBlockByHash(ctx context.Context, blockHash common.Hash, includeTx bool) (*types.Block, error) {
	return c.apiHandler.GetBlockByHash(ctx, c.groupID, blockHash, includeTx)
}

// GetBlockByNumber returns the block information according to the given block number(hex format)
func (c *Client) GetBlockByNumber(ctx context.Context, blockNumber int64, includeTx bool) (*types.Block, error) {
	return c.apiHandler.GetBlockByNumber(ctx, c.groupID, blockNumber, includeTx)
}

// GetBlockHashByNumber returns the block hash according to the given block number
func (c *Client) GetBlockHashByNumber(ctx context.Context, blockNumber int64) (*common.Hash, error) {
	return c.apiHandler.GetBlockHashByNumber(ctx, c.groupID, blockNumber)
}

// GetTransactionByHash returns the transaction information according to the given transaction hash
func (c *Client) GetTransactionByHash(ctx context.Context, txHash common.Hash) (*types.TransactionDetail, error) {
	return c.apiHandler.GetTransactionByHash(ctx, c.groupID, txHash)
}

// GetTransactionByBlockHashAndIndex returns the transaction information according to
// the given block hash and transaction index
func (c *Client) GetTransactionByBlockHashAndIndex(ctx context.Context, blockHash common.Hash, txIndex int) (*types.TransactionDetail, error) {
	return c.apiHandler.GetTransactionByBlockHashAndIndex(ctx, c.groupID, blockHash, txIndex)
}

// GetTransactionByBlockNumberAndIndex returns the transaction information according to
// the given block number and transaction index
func (c *Client) GetTransactionByBlockNumberAndIndex(ctx context.Context, blockNumber int64, txIndex int) (*types.TransactionDetail, error) {
	return c.apiHandler.GetTransactionByBlockNumberAndIndex(ctx, c.groupID, blockNumber, txIndex)
}

// GetTransactionReceipt returns the transaction receipt according to the given transaction hash
func (c *Client) GetTransactionReceipt(ctx context.Context, txHash common.Hash) (*types.Receipt, error) {
	return c.apiHandler.GetTransactionReceipt(ctx, c.groupID, txHash)
}

// GetContractAddress returns a contract address according to the transaction hash
func (c *Client) GetContractAddress(ctx context.Context, txHash common.Hash) (common.Address, error) {
	return c.apiHandler.GetContractAddress(ctx, c.groupID, txHash)
}

// GetPendingTransactions returns information of the pending transactions
func (c *Client) GetPendingTransactions(ctx context.Context) (*[]types.TransactionPending, error) {
	return c.apiHandler.GetPendingTransactions(ctx, c.groupID)
}

// GetPendingTxSize returns amount of the pending transactions
func (c *Client) GetPendingTxSize(ctx context.Context) ([]byte, error) {
	return c.apiHandler.GetPendingTxSize(ctx, c.groupID)
}

// GetCode returns the contract code according to the contract address
func (c *Client) GetCode(ctx context.Context, address common.Address) ([]byte, error) {
	return c.apiHandler.GetCode(ctx, c.groupID, address)
}

// GetTotalTransactionCount returns the total amount of transactions and the block height at present
func (c *Client) GetTotalTransactionCount(ctx context.Context) (*types.TransactionCount, error) {
	return c.apiHandler.GetTotalTransactionCount(ctx, c.groupID)
}

// GetSystemConfigByKey returns value according to the key(only tx_count_limit, tx_gas_limit could work)
func (c *Client) GetSystemConfigByKey(ctx context.Context, configKey string) ([]byte, error) {
	return c.apiHandler.GetSystemConfigByKey(ctx, c.groupID, configKey)
}

func getVersionNumber(strVersion string) (int, error) {
	strList := strings.Split(strVersion, ".")
	if len(strList) != 3 {
		return 0, fmt.Errorf("strList length must be 3")
	}
	var versionNumber int
	for i := 0; i < len(strList); i++ {
		num, err := strconv.Atoi(strList[i])
		if err != nil {
			return 0, fmt.Errorf("getVersionNumber failed, err: %v", err)
		}
		versionNumber += num
		versionNumber = versionNumber << 8
	}
	return versionNumber, nil
}
