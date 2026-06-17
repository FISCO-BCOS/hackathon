package types

type TransactionPending struct {
	From     string `json:"from"`
	Gas      string `json:"gas"`
	GasPrice string `json:"gasPrice"`
	Hash     string `json:"hash"`
	Input    string `json:"input"`
	Nonce    string `json:"nonce"`
	To       string `json:"to"`
	Value    string `json:"value"`
}

// GetFrom returns the transaction from address string
func (t *TransactionPending) GetFrom() string {
	return t.From
}

// GetGas returns the transaction gas string
func (t *TransactionPending) GetGas() string {
	return t.Gas
}

// GetGasPrice returns the transaction gas price string
func (t *TransactionPending) GetGasPrice() string {
	return t.GasPrice
}

// GetHash returns the transaction hash string
func (t *TransactionPending) GetHash() string {
	return t.Hash
}

// GetInput returns the transaction input string
func (t *TransactionPending) GetInput() string {
	return t.Input
}

// GetNonce returns the transaction nonce string
func (t *TransactionPending) GetNonce() string {
	return t.Nonce
}

// GetTo returns the transaction to address string
func (t *TransactionPending) GetTo() string {
	return t.To
}

// GetValue returns the transaction pengding string
func (t *TransactionPending) GetValue() string {
	return t.Value
}
