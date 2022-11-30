package types

import (
	"bytes"
	"errors"
	"fmt"
	"io"
	"math/big"
	"sync/atomic"

	"github.com/FISCO-BCOS/go-sdk/smcrypto/sm3"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/common/hexutil"
	"github.com/ethereum/go-ethereum/crypto"
	"github.com/ethereum/go-ethereum/rlp"
	"golang.org/x/crypto/sha3"
)

//go:generate gencodec -type txdata -field-override txdataMarshaling -out gen_tx_json.go

var (
	ErrInvalidSig = errors.New("invalid raw transaction v, r, s values")
)

type Transaction struct {
	data txdata
	// caches
	hash     atomic.Value
	size     atomic.Value
	from     atomic.Value
	smcrypto bool
}

type txdata struct {
	AccountNonce *big.Int        `json:"nonce"    gencodec:"required"`
	Price        *big.Int        `json:"gasPrice"   gencodec:"required"`
	GasLimit     *big.Int        `json:"gas"        gencodec:"required"`
	BlockLimit   *big.Int        `json:"blocklimit" gencodec:"required"`
	Recipient    *common.Address `json:"to"         rlp:"nil"` // nil means contract creation
	Amount       *big.Int        `json:"value"      gencodec:"required"`
	Payload      []byte          `json:"input"      gencodec:"required"`
	// for fisco bcos 2.0
	ChainID   *big.Int `json:"chainId"    gencodec:"required"`
	GroupID   *big.Int `json:"groupId"    gencodec:"required"`
	ExtraData []byte   `json:"extraData"  gencodec:"required"` // rlp:"nil"

	// Signature values
	V *big.Int `json:"v" gencodec:"required"`
	R *big.Int `json:"r" gencodec:"required"`
	S *big.Int `json:"s" gencodec:"required"`

	// This is only used when marshaling to JSON.
	Hash *common.Hash `json:"hash" rlp:"-"`
}

type txdataMarshaling struct {
	AccountNonce *hexutil.Big
	Price        *hexutil.Big
	GasLimit     *hexutil.Big
	BlockLimit   *hexutil.Big
	Amount       *hexutil.Big
	Payload      hexutil.Bytes
	ChainID      *hexutil.Big
	GroupID      *hexutil.Big
	ExtraData    hexutil.Bytes
	V            *hexutil.Big
	R            *hexutil.Big
	S            *hexutil.Big
}

// NewTransaction returns a new transaction
func NewTransaction(nonce *big.Int, to common.Address, amount *big.Int, gasLimit *big.Int, gasPrice *big.Int, blockLimit *big.Int, data []byte, chainId *big.Int, groupId *big.Int, extraData []byte, smcrypto bool) *Transaction {
	return newTransaction(nonce, &to, amount, gasLimit, gasPrice, blockLimit, data, chainId, groupId, extraData, smcrypto)
}

// NewContractCreation creates a contract transaction
func NewContractCreation(nonce *big.Int, amount *big.Int, gasLimit *big.Int, gasPrice *big.Int, blockLimit *big.Int, data []byte, chainId *big.Int, groupId *big.Int, extraData []byte, smcrypto bool) *Transaction {
	return newTransaction(nonce, nil, amount, gasLimit, gasPrice, blockLimit, data, chainId, groupId, extraData, smcrypto)
}

func newTransaction(nonce *big.Int, to *common.Address, amount *big.Int, gasLimit *big.Int, gasPrice *big.Int, blockLimit *big.Int, data []byte, chainId *big.Int, groupId *big.Int, extraData []byte, smcrypto bool) *Transaction {
	if len(data) > 0 {
		data = common.CopyBytes(data)
	}
	d := txdata{
		AccountNonce: nonce,
		Recipient:    to,
		Payload:      data,
		Amount:       new(big.Int),
		GasLimit:     gasLimit,
		BlockLimit:   blockLimit,
		Price:        new(big.Int),
		ChainID:      new(big.Int),
		GroupID:      new(big.Int),
		ExtraData:    extraData,
		V:            new(big.Int),
		R:            new(big.Int),
		S:            new(big.Int),
	}
	if amount != nil {
		d.Amount.Set(amount)
	}
	if gasPrice != nil {
		d.Price.Set(gasPrice)
	}
	if chainId != nil {
		d.ChainID.Set(chainId)
	}
	if groupId != nil {
		d.GroupID.Set(groupId)
	}
	if extraData != nil {
		d.ExtraData = extraData
	}

	return &Transaction{data: d, smcrypto: smcrypto}
}

// ChainID returns which chain id this transaction was signed for (if at all)
func (tx *Transaction) ChainID() *big.Int {
	return deriveChainID(tx.data.V)
}

// Protected returns whether the transaction is protected from replay protection.
func (tx *Transaction) Protected() bool {
	return isProtectedV(tx.data.V)
}

func isProtectedV(V *big.Int) bool {
	if V.BitLen() <= 8 {
		v := V.Uint64()
		return v != 27 && v != 28
	}
	// anything not 27 or 28 is considered protected
	return true
}

// EncodeRLP implements rlp.Encoder
func (tx *Transaction) EncodeRLP(w io.Writer) error {
	return rlp.Encode(w, &tx.data)
}

// DecodeRLP implements rlp.Decoder
func (tx *Transaction) DecodeRLP(s *rlp.Stream) error {
	_, size, _ := s.Kind()
	err := s.Decode(&tx.data)
	if err == nil {
		tx.size.Store(common.StorageSize(rlp.ListSize(size)))
	}

	return err
}

// MarshalJSON encodes the web3 RPC transaction format.
func (tx *Transaction) MarshalJSON() ([]byte, error) {
	hash := tx.Hash()
	data := tx.data
	data.Hash = &hash
	return data.MarshalJSON()
}

// UnmarshalJSON decodes the web3 RPC transaction format.
func (tx *Transaction) UnmarshalJSON(input []byte) error {
	var dec txdata
	if err := dec.UnmarshalJSON(input); err != nil {
		return err
	}

	withSignature := dec.V.Sign() != 0 || dec.R.Sign() != 0 || dec.S.Sign() != 0
	if withSignature {
		var V byte
		if isProtectedV(dec.V) {
			chainID := deriveChainID(dec.V).Uint64()
			V = byte(dec.V.Uint64() - 35 - 2*chainID)
		} else {
			V = byte(dec.V.Uint64() - 27)
		}
		if !crypto.ValidateSignatureValues(V, dec.R, dec.S, false) {
			return ErrInvalidSig
		}
	}

	*tx = Transaction{data: dec}
	return nil
}

func (tx *Transaction) Data() []byte       { return common.CopyBytes(tx.data.Payload) }
func (tx *Transaction) Gas() *big.Int      { return tx.data.GasLimit }
func (tx *Transaction) GasPrice() *big.Int { return new(big.Int).Set(tx.data.Price) }
func (tx *Transaction) Value() *big.Int    { return new(big.Int).Set(tx.data.Amount) }
func (tx *Transaction) Nonce() *big.Int    { return tx.data.AccountNonce }
func (tx *Transaction) CheckNonce() bool   { return true }

// To returns the recipient address of the transaction.
// It returns nil if the transaction is a contract creation.
func (tx *Transaction) To() *common.Address {
	if tx.data.Recipient == nil {
		return nil
	}
	to := *tx.data.Recipient
	return &to
}

// Hash hashes the RLP encoding of tx.
// It uniquely identifies the transaction.
func (tx *Transaction) Hash() common.Hash {
	if hash := tx.hash.Load(); hash != nil {
		return hash.(common.Hash)
	}
	if tx.smcrypto {
		return tx.sm3HashWithSig()
	}
	v := rlpHash(tx)
	tx.hash.Store(v)
	return v
}

// SM3HashNonSig hashes the RLP encoding of tx use sm3.
// It uniquely identifies the transaction.
func (tx *Transaction) SM3HashNonSig() (h common.Hash) {
	var src []byte
	buf := bytes.NewBuffer(src)
	rlp.Encode(buf, []interface{}{
		tx.data.AccountNonce,
		tx.data.Price,
		tx.data.GasLimit,
		tx.data.BlockLimit,
		tx.data.Recipient,
		tx.data.Amount,
		tx.data.Payload,
		tx.data.ChainID,
		tx.data.GroupID,
		tx.data.ExtraData,
	})
	v := sm3.Hash(buf.Bytes())
	copy(h[:], v)
	return h
}

func (tx *Transaction) sm3HashWithSig() (h common.Hash) {

	var src []byte
	buf := bytes.NewBuffer(src)
	rlp.Encode(buf, tx)
	v := sm3.Hash(buf.Bytes())
	copy(h[:], v)
	tx.hash.Store(h)
	return h
}

func rlpHash(x interface{}) (h common.Hash) {
	hw := sha3.NewLegacyKeccak256()
	rlp.Encode(hw, x)
	hw.Sum(h[:0])
	return h
}

// Size returns the true RLP encoded storage size of the transaction, either by
// encoding and returning it, or returning a previsouly cached value.
func (tx *Transaction) Size() common.StorageSize {
	if size := tx.size.Load(); size != nil {
		return size.(common.StorageSize)
	}
	c := writeCounter(0)
	rlp.Encode(&c, &tx.data)
	tx.size.Store(common.StorageSize(c))
	return common.StorageSize(c)
}

type writeCounter common.StorageSize

func (c *writeCounter) Write(b []byte) (int, error) {
	*c += writeCounter(len(b))
	return len(b), nil
}

// AsMessage returns the transaction as a core.Message.
//
// AsMessage requires a signer to derive the sender.
//
// XXX Rename message to something less arbitrary?
func (tx *Transaction) AsMessage(s Signer) (Message, error) {
	msg := Message{
		nonce:      tx.data.AccountNonce,
		gasLimit:   tx.data.GasLimit,
		gasPrice:   new(big.Int).Set(tx.data.Price),
		blockLimit: tx.data.BlockLimit,
		to:         tx.data.Recipient,
		amount:     tx.data.Amount,
		data:       tx.data.Payload,
		checkNonce: true,
	}

	var err error
	msg.from, err = Sender(s, tx)
	return msg, err
}

// WithSignature returns a new transaction with the given signature.
// This signature needs to be in the [R || S || V] format where V is 0 or 1.
func (tx *Transaction) WithSignature(signer Signer, sig []byte) (*Transaction, error) {
	r, s, v, err := signer.SignatureValues(tx, sig)
	if err != nil {
		return nil, err
	}
	cpy := &Transaction{data: tx.data}
	cpy.data.R, cpy.data.S, cpy.data.V = r, s, v
	cpy.smcrypto = tx.smcrypto
	return cpy, nil
}

// WithSM2Signature returns a new transaction with the given signature.
// This signature needs to be in the [R || S || V] format where V is 0 or 1.
func (tx *Transaction) WithSM2Signature(signer Signer, sig []byte) (*Transaction, error) {
	if len(sig) != 128 {
		panic(fmt.Sprintf("wrong size for SM2Signature: got %d, want 128", len(sig)))
	}
	r := new(big.Int).SetBytes(sig[:32])
	s := new(big.Int).SetBytes(sig[32:64])
	v := new(big.Int).SetBytes(sig[64:])
	cpy := &Transaction{data: tx.data}
	cpy.data.R, cpy.data.S, cpy.data.V = r, s, v
	cpy.smcrypto = tx.smcrypto
	return cpy, nil
}

// Cost returns amount + gasprice * gaslimit.
func (tx *Transaction) Cost() *big.Int {
	total := new(big.Int).Mul(tx.data.Price, new(big.Int).Set(tx.data.GasLimit))
	total.Add(total, tx.data.Amount)
	return total
}

// SignatureValues returns the V, R, S signature values of the transaction.
// The return values should not be modified by the caller.
func (tx *Transaction) SignatureValues() (v, r, s *big.Int) {
	return tx.data.V, tx.data.R, tx.data.S
}

// Transactions is a Transaction slice type for basic sorting.
type Transactions []*Transaction

// Len returns the length of s.
func (s Transactions) Len() int { return len(s) }

// Swap swaps the i'th and the j'th element in s.
func (s Transactions) Swap(i, j int) { s[i], s[j] = s[j], s[i] }

// GetRlp implements Rlpable and returns the i'th element of s in rlp.
func (s Transactions) GetRlp(i int) []byte {
	enc, _ := rlp.EncodeToBytes(s[i])
	return enc
}

// TxDifference returns a new set which is the difference between a and b.
func TxDifference(a, b Transactions) Transactions {
	keep := make(Transactions, 0, len(a))

	remove := make(map[common.Hash]struct{})
	for _, tx := range b {
		remove[tx.Hash()] = struct{}{}
	}

	for _, tx := range a {
		if _, ok := remove[tx.Hash()]; !ok {
			keep = append(keep, tx)
		}
	}

	return keep
}

// TxByNonce implements the sort interface to allow sorting a list of transactions
// by their nonces. This is usually only useful for sorting transactions from a
// single account, otherwise a nonce comparison doesn't make much sense.
type TxByNonce Transactions

func (s TxByNonce) Len() int { return len(s) }
func (s TxByNonce) Less(i, j int) bool {
	return s[i].data.AccountNonce.Cmp(s[j].data.AccountNonce) > 0
}                                 //{ return s[i].data.AccountNonce < s[j].data.AccountNonce }
func (s TxByNonce) Swap(i, j int) { s[i], s[j] = s[j], s[i] }

// TxByPrice implements both the sort and the heap interface, making it useful
// for all at once sorting as well as individually adding and removing elements.
type TxByPrice Transactions

func (s TxByPrice) Len() int           { return len(s) }
func (s TxByPrice) Less(i, j int) bool { return s[i].data.Price.Cmp(s[j].data.Price) > 0 }
func (s TxByPrice) Swap(i, j int)      { s[i], s[j] = s[j], s[i] }

func (s *TxByPrice) Push(x interface{}) {
	*s = append(*s, x.(*Transaction))
}

func (s *TxByPrice) Pop() interface{} {
	old := *s
	n := len(old)
	x := old[n-1]
	*s = old[0 : n-1]
	return x
}

// Message is a fully derived transaction and implements core.Message
//
// NOTE: In a future PR this will be removed.
type Message struct {
	to         *common.Address
	from       common.Address
	nonce      *big.Int
	amount     *big.Int
	gasLimit   *big.Int
	gasPrice   *big.Int
	blockLimit *big.Int
	data       []byte
	checkNonce bool
}

func NewMessage(from common.Address, to *common.Address, nonce *big.Int, amount *big.Int, gasLimit *big.Int, gasPrice *big.Int, blockLimit *big.Int, data []byte, checkNonce bool) Message {
	return Message{
		from:       from,
		to:         to,
		nonce:      nonce,
		amount:     amount,
		gasLimit:   gasLimit,
		gasPrice:   gasPrice,
		blockLimit: blockLimit,
		data:       data,
		checkNonce: checkNonce,
	}
}

func (m Message) From() common.Address { return m.from }
func (m Message) To() *common.Address  { return m.to }
func (m Message) GasPrice() *big.Int   { return m.gasPrice }
func (m Message) Value() *big.Int      { return m.amount }
func (m Message) Gas() *big.Int        { return m.gasLimit }
func (m Message) Nonce() *big.Int      { return m.nonce }
func (m Message) Data() []byte         { return m.data }
func (m Message) CheckNonce() bool     { return m.checkNonce }

type newTransactionStruct struct {
	data newrawtxdata
	// caches
	hash atomic.Value
	size atomic.Value
	from atomic.Value
}

type newrawtxdata struct {
	AccountNonce *big.Int `json:"nonce"    gencodec:"required"`
	Price        *big.Int `json:"gasPrice"   gencodec:"required"`
	GasLimit     *big.Int `json:"gas"        gencodec:"required"`
	BlockLimit   *big.Int `json:"blocklimit" gencodec:"required"`
	Recipient    string   `json:"to"         rlp:"nil"` // nil means contract creation
	Amount       *big.Int `json:"value"      gencodec:"required"`
	Payload      string   `json:"input"      gencodec:"required"`

	// Signature values
	V *big.Int `json:"v" gencodec:"required"`
	R *big.Int `json:"r" gencodec:"required"`
	S *big.Int `json:"s" gencodec:"required"`

	// This is only used when marshaling to JSON.
	Hash *common.Hash `json:"hash" rlp:"-"`
}

func (tx *Transaction) ConverToNewRawTx() *newTransactionStruct {
	return &newTransactionStruct{
		data: newrawtxdata{
			AccountNonce: tx.data.AccountNonce,
			Price:        tx.data.Price,
			GasLimit:     tx.data.GasLimit,
			BlockLimit:   tx.data.BlockLimit,
			Recipient:    tx.data.Recipient.String(),
			Amount:       tx.data.Amount,
			Payload:      hexutil.Bytes(tx.data.Payload).String(),
			V:            tx.data.V,
			R:            tx.data.R,
			S:            tx.data.S,
		},
		hash: tx.hash,
		size: tx.size,
		from: tx.from,
	}
}
