package types

import (
	"encoding/json"
	"errors"
	"math/big"

	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/common/hexutil"
)

var _ = (*txdataMarshaling)(nil)

// MarshalJSON marshals as JSON.
func (t txdata) MarshalJSON() ([]byte, error) {
	type txdata struct {
		AccountNonce *hexutil.Big    `json:"nonce"    gencodec:"required"`
		Price        *hexutil.Big    `json:"gasPrice" gencodec:"required"`
		GasLimit     *hexutil.Big    `json:"gas"      gencodec:"required"`
		BlockLimit   *hexutil.Big    `json:"blocklimit" gencodec:"required"`
		Recipient    *common.Address `json:"to"       rlp:"nil"`
		Amount       *hexutil.Big    `json:"value"    gencodec:"required"`
		Payload      hexutil.Bytes   `json:"input"    gencodec:"required"`
		ChainID      *hexutil.Big    `json:"chainId"    gencodec:"required"`
		GroupID      *hexutil.Big    `json:"groupId"    gencodec:"required"`
		ExtraData    hexutil.Bytes   `json:"extraData"  gencodec:"required"`
		V            *hexutil.Big    `json:"v" gencodec:"required"`
		R            *hexutil.Big    `json:"r" gencodec:"required"`
		S            *hexutil.Big    `json:"s" gencodec:"required"`
		Hash         *common.Hash    `json:"hash" rlp:"-"`
	}
	var enc txdata
	enc.AccountNonce = (*hexutil.Big)(t.AccountNonce)
	enc.Price = (*hexutil.Big)(t.Price)
	// enc.GasLimit = hexutil.Uint64(t.GasLimit)
	// enc.BlockLimit = hexutil.Uint64(t.BlockLimit)
	enc.GasLimit = (*hexutil.Big)(t.GasLimit)
	enc.BlockLimit = (*hexutil.Big)(t.BlockLimit)
	enc.Recipient = t.Recipient
	enc.Amount = (*hexutil.Big)(t.Amount)
	enc.Payload = t.Payload
	enc.ChainID = (*hexutil.Big)(t.ChainID)
	enc.GroupID = (*hexutil.Big)(t.GroupID)
	enc.ExtraData = t.ExtraData
	enc.V = (*hexutil.Big)(t.V)
	enc.R = (*hexutil.Big)(t.R)
	enc.S = (*hexutil.Big)(t.S)
	enc.Hash = t.Hash
	return json.Marshal(&enc)
}

// UnmarshalJSON unmarshals from JSON.
func (t *txdata) UnmarshalJSON(input []byte) error {
	type txdata struct {
		AccountNonce *hexutil.Big    `json:"nonce"    gencodec:"required"`
		Price        *hexutil.Big    `json:"gasPrice" gencodec:"required"`
		GasLimit     *hexutil.Big    `json:"gas"      gencodec:"required"`
		BlockLimit   *hexutil.Big    `json:"blocklimit" gencodec:"required"`
		Recipient    *common.Address `json:"to"       rlp:"nil"`
		Amount       *hexutil.Big    `json:"value"    gencodec:"required"`
		Payload      *hexutil.Bytes  `json:"input"    gencodec:"required"`
		ChainID      *hexutil.Big    `json:"chainId"    gencodec:"required"`
		GroupID      *hexutil.Big    `json:"groupId"    gencodec:"required"`
		ExtraData    *hexutil.Bytes  `json:"extraData"  gencodec:"required"`
		V            *hexutil.Big    `json:"v" gencodec:"required"`
		R            *hexutil.Big    `json:"r" gencodec:"required"`
		S            *hexutil.Big    `json:"s" gencodec:"required"`
		Hash         *common.Hash    `json:"hash" rlp:"-"`
	}
	var dec txdata
	if err := json.Unmarshal(input, &dec); err != nil {
		return err
	}
	if dec.AccountNonce == nil {
		return errors.New("missing required field 'nonce' for txdata")
	}
	t.AccountNonce = (*big.Int)(dec.AccountNonce)
	if dec.Price == nil {
		return errors.New("missing required field 'gasPrice' for txdata")
	}
	t.Price = (*big.Int)(dec.Price)
	if dec.GasLimit == nil {
		return errors.New("missing required field 'gas' for txdata")
	}
	t.GasLimit = (*big.Int)(dec.GasLimit)
	if dec.BlockLimit == nil {
		return errors.New("missing required field 'blocklimit' for txdata")
	}
	t.BlockLimit = (*big.Int)(dec.BlockLimit)
	if dec.Recipient != nil {
		t.Recipient = dec.Recipient
	}
	if dec.Amount == nil {
		return errors.New("missing required field 'value' for txdata")
	}
	t.Amount = (*big.Int)(dec.Amount)
	if dec.Payload == nil {
		return errors.New("missing required field 'input' for txdata")
	}
	t.Payload = *dec.Payload
	if dec.ChainID == nil {
		return errors.New("missing required field 'chainId' for txdata")
	}
	t.ChainID = (*big.Int)(dec.ChainID)
	if dec.GroupID == nil {
		return errors.New("missing required field 'groupId' for txdata")
	}
	t.GroupID = (*big.Int)(dec.GroupID)
	if dec.ExtraData == nil {
		return errors.New("missing required field 'extraData' for txdata")
	}
	t.ExtraData = *dec.ExtraData
	if dec.V == nil {
		return errors.New("missing required field 'v' for txdata")
	}
	t.V = (*big.Int)(dec.V)
	if dec.R == nil {
		return errors.New("missing required field 'r' for txdata")
	}
	t.R = (*big.Int)(dec.R)
	if dec.S == nil {
		return errors.New("missing required field 's' for txdata")
	}
	t.S = (*big.Int)(dec.S)
	if dec.Hash != nil {
		t.Hash = dec.Hash
	}
	return nil
}
