package types

type TransactionDetail struct {
	BlockHash        string `json:"blockHash"`
	BlockNumber      string `json:"blockNumber"`
	From             string `json:"from"`
	Gas              string `json:"gas"`
	GasPrice         string `json:"gasPrice"`
	Hash             string `json:"hash"`
	Input            string `json:"input"`
	Nonce            string `json:"nonce"`
	To               string `json:"to"`
	TransactionIndex string `json:"transactionIndex"`
	Value            string `json:"value"`
}

// GetBlockHash returns the block hash string
func (t *TransactionDetail) GetBlockHash() string {
	return t.BlockHash
}

// GetBlockNumber returns the blcok number string
func (t *TransactionDetail) GetBlockNumber() string {
	return t.BlockNumber
}

// GetValue returns the transaction pfrom address string
func (t *TransactionDetail) GetFrom() string {
	return t.From
}

// GetValue returns the transaction gas string
func (t *TransactionDetail) GetGas() string {
	return t.Gas
}

// GetValue returns the transaction gas price string
func (t *TransactionDetail) GetGasPrice() string {
	return t.GasPrice
}

// GetValue returns the transaction hash string
func (t *TransactionDetail) GetHash() string {
	return t.Hash
}

// GetValue returns the transaction input string
func (t *TransactionDetail) GetInput() string {
	return t.Input
}

// GetValue returns the transaction once string
func (t *TransactionDetail) GetNonce() string {
	return t.Nonce
}

// GetValue returns the transaction to address string
func (t *TransactionDetail) GetTo() string {
	return t.To
}

// GetValue returns the transaction index string
func (t *TransactionDetail) GetTransactionIndex() string {
	return t.TransactionIndex
}

// GetValue returns the transaction value string
func (t *TransactionDetail) GetValue() string {
	return t.Value
}
