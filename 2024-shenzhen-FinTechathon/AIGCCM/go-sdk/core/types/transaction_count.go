package types

type TransactionCount struct {
	BlockNumber string `json:"blockNumber"`
	FailedTxSum string `json:"failedTxSum"`
	TxSum       string `json:"txSum"`
}

// GetBlockNumber returns the transaction block height string
func (t *TransactionCount) GetBlockNumber() string {
	return t.BlockNumber
}

// GetFailedTxSum returns the transaction failed sum string
func (t *TransactionCount) GetFailedTxSum() string {
	return t.FailedTxSum
}

// GetTxSum returns the transaction sum string
func (t *TransactionCount) GetTxSum() string {
	return t.TxSum
}
