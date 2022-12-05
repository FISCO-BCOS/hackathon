package mobile

import (
	"context"
	"encoding/hex"
	"encoding/json"
	"errors"
	"fmt"
	"math/big"
	"strconv"
	"sync/atomic"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/core/types"
	"github.com/ethereum/go-ethereum"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/common/hexutil"
	"github.com/ethereum/go-ethereum/rlp"
)

const (
	indent = "  "
)

type ContractProxy struct {
	groupID   int
	chainID   *big.Int
	smCrypto  bool
	callback  PostCallback
	idCounter uint32
}

// CodeAt returns the code of the given account. This is needed to differentiate
// between contract internal errors and the local chain being out of sync.
func (c *ContractProxy) CodeAt(ctx context.Context, contract common.Address, blockNumber *big.Int) ([]byte, error) {
	return []byte{}, nil
}

// ContractCall executes a Solidity contract call with the specified data as the
// input.
func (c *ContractProxy) CallContract(ctx context.Context, call ethereum.CallMsg, blockNumber *big.Int) ([]byte, error) {
	return c.Call(ctx, c.groupID, call)
}

// PendingCodeAt returns the code of the given account in the pending state.
func (c *ContractProxy) PendingCodeAt(ctx context.Context, account common.Address) ([]byte, error) {
	return []byte{}, nil
}

// SendTransaction injects the transaction into the pending pool for execution.
func (c *ContractProxy) SendTransaction(ctx context.Context, tx *types.Transaction) (*types.Receipt, error) {
	data, err := rlp.EncodeToBytes(tx)
	if err != nil {
		fmt.Printf("rlp encode tx error!")
		return nil, err
	}
	msg, err := c.newMessage("sendRawTransaction", c.groupID, hexutil.Encode(data))
	if err != nil {
		fmt.Printf("rlp encode tx error!")
		return nil, err
	}
	respString := c.callback.SendRequest(msg.String())
	jsonResponse, err := c.toRPCMessage(respString)
	if err != nil {
		return nil, errors.New("")
	}
	var anonymityReceipt = &struct {
		types.Receipt
		Status       string          `json:"status"`
		TxProof      json.RawMessage `json:"txProof"`
		ReceiptProof json.RawMessage `json:"receiptProof"`
		StatusOK     bool            `json:"statusOK"`
		Message      json.RawMessage `json:"message"`
	}{}
	err = json.Unmarshal(jsonResponse.Result, &anonymityReceipt)
	if err != nil {
		return nil, errors.New("unmarshal receipt error :" + err.Error())
	}
	status, err := strconv.ParseInt(anonymityReceipt.Status[2:], 16, 32)
	if err != nil {
		return nil, fmt.Errorf("SendRawTransaction failed, strconv.ParseInt err: " + fmt.Sprint(err))
	}
	receipt := &anonymityReceipt.Receipt
	receipt.Status = int(status)
	return receipt, err
}

// AsyncSendTransaction injects the transaction into the pending pool for execution.
func (c *ContractProxy) AsyncSendTransaction(ctx context.Context, tx *types.Transaction, handler func(*types.Receipt, error)) error {
	receipt, err := c.SendTransaction(ctx, tx)
	handler(receipt, err)
	return err
}

// GetBlockLimit returns the blocklimit for current blocknumber
func (c *ContractProxy) GetBlockLimit(ctx context.Context) (*big.Int, error) {
	var raw string
	err := c.CallContext(ctx, &raw, "getBlockNumber", c.groupID)
	if err != nil {
		fmt.Println(raw)
		return nil, err
	}
	blockNumber, err := strconv.ParseInt(raw, 0, 64)
	if err != nil {
		return nil, fmt.Errorf("parse block number failed, err: %v", err)
	}
	blockLimit := big.NewInt(blockNumber + client.BlockLimit)
	return blockLimit, err
}

// GetGroupID returns the groupID of the client
func (c *ContractProxy) GetGroupID() *big.Int {
	return big.NewInt(int64(c.groupID))
}

// GetChainID returns the chainID of the blockchain
func (c *ContractProxy) GetChainID(ctx context.Context) (*big.Int, error) {
	return c.chainID, nil
}

// GetContractAddress returns the contract address once it was deployed
func (c *ContractProxy) GetContractAddress(ctx context.Context, txHash common.Hash) (common.Address, error) {
	panic("implement me")
}

// SMCrypto returns true if use sm crypto
func (c *ContractProxy) SMCrypto() bool {
	return c.smCrypto
}

func (c *ContractProxy) Call(ctx context.Context, groupID int, msg ethereum.CallMsg) ([]byte, error) {
	var hexBytes hexutil.Bytes
	var cr *callResult
	err := c.CallContext(ctx, &cr, "call", groupID, c.toCallArg(msg))
	if err != nil {
		return nil, err
	}
	if cr.Status != "0x0" {
		var errorMessage string
		if len(cr.Output) >= 138 {
			outputBytes, err := hex.DecodeString(cr.Output[2:])
			if err != nil {
				return nil, fmt.Errorf("call error of status %s, hex.DecodeString failed", cr.Status)
			}
			errorMessage = string(outputBytes[68:])
		}
		return nil, fmt.Errorf("call error of status %s, %v", cr.Status, errorMessage)
	}
	hexBytes = common.FromHex(cr.Output)
	return hexBytes, err
}

func (c *ContractProxy) CallContext(ctx context.Context, result interface{}, method string, args ...interface{}) error {
	msg, err := c.newMessage(method, args...)
	// in order to proxy
	if msg.Params == nil {
		empty := []byte("[1]")
		msg.Params = empty
	}
	if err != nil {
		return errors.New("create rpcRequest message error: " + err.Error())
	}
	respString := c.callback.SendRequest(msg.String())
	resp, err := c.toRPCMessage(respString)
	if err != nil {
		return errors.New("to rpc message error: " + err.Error())
	}
	if resp.Error != nil {
		return resp.Error
	} else if len(resp.Result) == 0 {
		return ErrNoResult
	} else {
		return json.Unmarshal(resp.Result, &result)
	}
}

// SubscribeEventLogs
func (c *ContractProxy) SubscribeEventLogs(eventLogParams types.EventLogParams, handler func(int, []types.Log)) error {
	panic("implement me")
}

func (c *ContractProxy) nextID() json.RawMessage {
	id := atomic.AddUint32(&c.idCounter, 1)
	return strconv.AppendUint(nil, uint64(id), 10)
}
func (c *ContractProxy) newMessage(method string, paramsIn ...interface{}) (*jsonrpcMessage, error) {
	msg := &jsonrpcMessage{Version: vsn, ID: c.nextID(), Method: method}
	if paramsIn != nil { // prevent sending "params":null
		var err error
		if msg.Params, err = json.Marshal(paramsIn); err != nil {
			return nil, err
		}
	}
	return msg, nil
}

func (c *ContractProxy) toCallArg(msg ethereum.CallMsg) interface{} {
	arg := map[string]interface{}{
		"from": msg.From.String(),
		"to":   msg.To.String(),
	}
	if len(msg.Data) > 0 {
		arg["data"] = hexutil.Bytes(msg.Data).String()
	}
	if msg.Value != nil {
		arg["value"] = (*hexutil.Big)(msg.Value).String()
	}

	if msg.Gas != 0 {
		arg["gas"] = hexutil.Uint64(msg.Gas)
	}
	if msg.GasPrice != nil {
		arg["gasPrice"] = (*hexutil.Big)(msg.GasPrice)
	}

	return arg
}

func (c *ContractProxy) toRPCMessage(msg string) (*jsonrpcMessage, error) {
	networkResponse := NetworkResponse{}
	err := json.Unmarshal([]byte(msg), &networkResponse)
	if err != nil {
		return nil, err
	}
	// in order to decode message from proxy of {"error":null}
	var anonymityJsonrpcMessage = &struct {
		jsonrpcMessage
		Error json.RawMessage `json:"error"`
	}{}
	err = json.Unmarshal(networkResponse.Result, &anonymityJsonrpcMessage)
	if err != nil {
		return nil, err
	}
	jsonrpcResponse := &anonymityJsonrpcMessage.jsonrpcMessage
	if networkResponse.Code != 0 {
		jsonrpcResponse.Error = &jsonError{
			Code:    networkResponse.Code,
			Message: networkResponse.Message,
		}
		err = errors.New(strconv.Itoa(networkResponse.Code) + ":" + networkResponse.Message)
	}

	if string(anonymityJsonrpcMessage.Error) != "null" {
		errObj := jsonError{}
		err = json.Unmarshal(anonymityJsonrpcMessage.Error, &errObj)
		if err != nil {
			return nil, err
		}
		jsonrpcResponse.Error = &errObj
	}
	return jsonrpcResponse, err
}
