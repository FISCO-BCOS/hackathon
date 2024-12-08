package types

import (
	"encoding/hex"
	"encoding/json"
	"fmt"
	"strconv"

	"github.com/ethereum/go-ethereum/common"
)

// Receipt represents the results of a transaction.
type Receipt struct {
	TransactionHash  string         `json:"transactionHash"`
	TransactionIndex string         `json:"transactionIndex"`
	BlockHash        string         `json:"blockHash"`
	BlockNumber      string         `json:"blockNumber"`
	GasUsed          string         `json:"gasUsed"`
	ContractAddress  common.Address `json:"contractAddress"`
	Root             string         `json:"root"`
	Status           int            `json:"status"`
	From             string         `json:"from"`
	To               string         `json:"to"`
	Input            string         `json:"input"`
	Output           string         `json:"output"`
	Logs             []*NewLog      `json:"logs"`
	LogsBloom        string         `json:"logsBloom"`
}

const (
	Success = iota
	Unknown
	BadRLP
	InvalidFormat
	OutOfGasIntrinsic
	InvalidSignature
	InvalidNonce
	NotEnoughCash
	OutOfGasBase
	BlockGasLimitReached
	BadInstruction
	BadJumpDestination
	OutOfGas
	OutOfStack
	StackUnderflow
	NonceCheckFail
	BlockLimitCheckFail
	FilterCheckFail
	NoDeployPermission
	NoCallPermission
	NoTxPermission
	PrecompiledError
	RevertInstruction
	InvalidZeroSignatureFormat
	AddressAlreadyUsed
	PermissionDenied
	CallAddressError
)

// GetStatusMessage returns the status message
func getStatusMessage(status int) string {
	var message string
	switch status {
	case Success:
		message = "success"
	case Unknown:
		message = "unknown"
	case BadRLP:
		message = "bad RLP"
	case InvalidFormat:
		message = "invalid format"
	case OutOfGasIntrinsic:
		message = "out of gas intrinsic"
	case InvalidSignature:
		message = "invalid signature"
	case InvalidNonce:
		message = "invalid nonce"
	case NotEnoughCash:
		message = "not enough cash"
	case OutOfGasBase:
		message = "out of gas base"
	case BlockGasLimitReached:
		message = "block gas limit reached"
	case BadInstruction:
		message = "bad instruction"
	case BadJumpDestination:
		message = "bad jump destination"
	case OutOfGas:
		message = "out of gas"
	case OutOfStack:
		message = "out of stack"
	case StackUnderflow:
		message = "stack underflow"
	case NonceCheckFail:
		message = "nonce check fail"
	case BlockLimitCheckFail:
		message = "block limit check fail"
	case FilterCheckFail:
		message = "filter check fail"
	case NoDeployPermission:
		message = "no deploy permission"
	case NoCallPermission:
		message = "no call permission"
	case NoTxPermission:
		message = "no tx permission"
	case PrecompiledError:
		message = "precompiled error"
	case RevertInstruction:
		message = "revert instruction"
	case InvalidZeroSignatureFormat:
		message = "invalid zero signature format"
	case AddressAlreadyUsed:
		message = "address already used"
	case PermissionDenied:
		message = "permission denied"
	case CallAddressError:
		message = "call address error"
	default:
		message = strconv.Itoa(status)
	}

	return message
}

// GetTransactionHash returns the transaction hash string
func (r *Receipt) GetTransactionHash() string {
	return r.TransactionHash
}

// GetTransactionIndex returns the transaction index string
func (r *Receipt) GetTransactionIndex() string {
	return r.TransactionIndex
}

// GetBlockHash returns the block hash string
func (r *Receipt) GetBlockHash() string {
	return r.BlockHash
}

// GetBlockNumber returns the block number string
func (r *Receipt) GetBlockNumber() string {
	return r.BlockNumber
}

// GetGasUsed returns the used gas
func (r *Receipt) GetGasUsed() string {
	return r.GasUsed
}

// GetContractAddress returns the contract address
func (r *Receipt) GetContractAddress() common.Address {
	return r.ContractAddress
}

// GetRoot returns the transactions root
func (r *Receipt) GetRoot() string {
	return r.Root
}

// GetStatus returns the transaction status
func (r *Receipt) GetStatus() int {
	return r.Status
}

// GetFrom returns the transaction sender address
func (r *Receipt) GetFrom() string {
	return r.From
}

// GetTo returns the transaction receiver address
func (r *Receipt) GetTo() string {
	return r.To
}

// GetInput returns the transaction content
func (r *Receipt) GetInput() string {
	return r.Input
}

// GetOutput returns the transaction output
func (r *Receipt) GetOutput() string {
	return r.Output
}

// ParseErrorMessage gets unusual output value from Receipt
func (r *Receipt) GetErrorMessage() string {
	var errorMessage string
	if r.GetStatus() == Success {
		return ""
	}
	errorMessage = getStatusMessage(r.Status)
	if len(r.Output) >= 138 { // 0x + 4 bytes funcName + 32 bytes offset + 32 bytes string length
		outputBytes, err := hex.DecodeString(r.Output[2:])
		if err != nil {
			panic(fmt.Sprintf("GetErrorMessage failed, hex.DecodeString err: %v", err))
		}
		errorMessage += ", " + string(outputBytes[68:])
		return fmt.Sprintf("receipt error code: %v, receipt error message: %v", r.Status, errorMessage)
	}
	return fmt.Sprintf("receipt error code: %v, receipt error message: %v", r.Status, errorMessage)
}

// String returns the string representation of Receipt sturct.
func (r *Receipt) String() string {
	out, err := json.MarshalIndent(r, "", "\t")
	if err != nil {
		return fmt.Sprintf("%v", err)
	}
	return string(out)
}
