package precompiled

import (
	"fmt"
	"math"
	"math/big"

	"github.com/FISCO-BCOS/go-sdk/core/types"
	"github.com/ethereum/go-ethereum/common"
)

const (
	// common precompiled contract error code
	permissionDenied  int64 = -50000
	tableAlreadyExist int64 = -50001

	// non-precompiled contract error code, only used when logic errors occur
	DefaultErrorCode int64 = -1

	bigIntHexStrLen int = 66
)

var (
	// MaxUint256 is the maximum value that can be represented by a uint256.
	MaxUint256 = new(big.Int).Sub(new(big.Int).Lsh(common.Big1, 256), common.Big1)
)

// GetCommonErrorCodeMessage returns the message of common error code
func GetCommonErrorCodeMessage(errorCode int64) string {
	var message string
	switch errorCode {
	case permissionDenied:
		message = "permission denied"
	case tableAlreadyExist:
		message = "table already exist"
	default:
		message = ""
	}
	return message
}

func ParseBigIntFromOutput(receipt *types.Receipt) (*big.Int, error) {
	var ret *big.Int
	if len(receipt.Output) != bigIntHexStrLen {
		return nil, fmt.Errorf("the leghth of receipt.Output %v is inconsistent with %v", len(receipt.Output), bigIntHexStrLen)
	}
	ret, success := new(big.Int).SetString(receipt.Output, 0)
	if !success {
		return nil, fmt.Errorf("parse output as big.Int failed: %v", receipt.Output)
	}
	if ret.Bit(255) == 1 {
		ret.Add(MaxUint256, new(big.Int).Neg(ret))
		ret.Add(ret, common.Big1)
		ret.Neg(ret)
	}
	return ret, nil
}

func BigIntToUint64(bigNum *big.Int) (uint64, error) {
	boolean := bigNum.IsUint64()
	if !boolean {
		return 0, fmt.Errorf("bigNum %v can't transfer to Uint64", bigNum)
	}
	return bigNum.Uint64(), nil
}

func BigIntToInt64(bigNum *big.Int) (int64, error) {
	boolean := bigNum.IsInt64()
	if !boolean {
		return 0, fmt.Errorf("BigIntToint64 failed, bigNum %v can't transfer to Int64", bigNum)
	}
	return bigNum.Int64(), nil
}

func Uint64ToInt64(num uint64) (int64, error) {
	if num > math.MaxInt64 {
		return 0, fmt.Errorf("uint64 %v can't tranfer to int64", num)
	}
	return int64(num), nil
}
