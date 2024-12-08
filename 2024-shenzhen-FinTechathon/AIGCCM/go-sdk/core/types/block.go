package types

type Block struct {
	DbHash           string        `json:"dbHash"`
	ExtraData        []string      `json:"extraData"`
	GasLimit         string        `json:"gasLimit"`
	GasUsed          string        `json:"gasUsed"`
	Hash             string        `json:"hash"`
	LogsBloom        string        `json:"logsBloom"`
	Number           string        `json:"number"`
	ParentHash       string        `json:"parentHash"`
	ReceiptsRoot     string        `json:"receiptsRoot"`
	Sealer           string        `json:"sealer"`
	SealerList       []string      `json:"sealerList"`
	SignatureList    []Signature   `json:"signatureList"`
	StateRoot        string        `json:"stateRoot"`
	Timestamp        string        `json:"timestamp"`
	Transactions     []interface{} `json:"transactions"`
	TransactionsRoot string        `json:"transactionsRoot"`
}

type Signature struct {
	Index     string `json:"index"`
	Signature string `json:"signature"`
}

// GetIndex returns the signature index string
func (s *Signature) GetIndex() string {
	return s.Index
}

// GetSignature returns signature string
func (s *Signature) GetSignature() string {
	return s.Signature
}

// GetDbHash returns  records changes to transaction data hash string
func (B *Block) GetDbHash() string {
	return B.DbHash
}

// GetGasLimit returns the block max gas limit string
func (B *Block) GetGasLimit() string {
	return B.GasLimit
}

// GetGasUsed returns the block gas used string
func (B *Block) GetGasUsed() string {
	return B.GasUsed
}

// GetHash returns the block hash string
func (B *Block) GetHash() string {
	return B.Hash
}

// GetLogsBloom returns the block logs bloom string
func (B *Block) GetLogsBloom() string {
	return B.LogsBloom
}

// GetNumber returns the block number string
func (B *Block) GetNumber() string {
	return B.Number
}

// GetParentHash returns parent block hash string
func (B *Block) GetParentHash() string {
	return B.ParentHash
}

// GetReceiptsRoot returns the block  receipts root string
func (B *Block) GetReceiptsRoot() string {
	return B.ReceiptsRoot
}

// GetSealer returns the sealer node sequence number string
func (B *Block) GetSealer() string {
	return B.Sealer
}

// GetSealerList returns the sealer node list
func (B *Block) GetSealerList() []string {
	return B.SealerList
}

// GetSignatureList returns the block  signature list
func (B *Block) GetSignatureList() []Signature {
	return B.SignatureList
}

// GetTimestamp returns the block timestamp string
func (B *Block) GetTimestamp() string {
	return B.Timestamp
}

// GetTransactions returns the blcok transcation list
func (B *Block) GetTransactions() []interface{} {
	return B.Transactions
}

// GetTransactionsRoot returns the block all transcation root string
func (B *Block) GetTransactionsRoot() string {
	return B.TransactionsRoot
}
