package types

type SyncStatus struct {
	BlockNumber        int    `json:"blockNumber"`
	GenesisHash        string `json:"genesisHash"`
	IsSyncing          bool   `json:"isSyncing"`
	KnownHighestNumber int    `json:"knownHighestNumber"`
	KnownLatestHash    string `json:"knownLatestHash"`
	LatestHash         string `json:"latestHash"`
	NodeId             string `json:"nodeId"`
	Peers              []Peer `json:"peers"`
	ProtocolId         int    `json:"protocolId"`
	TxPoolSize         string `json:"txPoolSize"`
}

type Peer struct {
	BlockNumber int    `json:"blockNumber"`
	GenesisHash string `json:"genesisHash"`
	LatestHash  string `json:"latestHash"`
	NodeId      string `json:"nodeId"`
}

// GetBlockNumber returns the laster block number int
func (p *Peer) GetBlockNumber() int {
	return p.BlockNumber
}

// GetGenesisHash returns the original block hash string
func (p *Peer) GetGenesisHash() string {
	return p.GenesisHash
}

// GetLatestHash returns the laster block hash string
func (p *Peer) GetLatestHash() string {
	return p.LatestHash
}

// GetNodeId returns the node id string
func (p *Peer) GetNodeId() string {
	return p.NodeId
}

// GetBlockNumber returns the block number int
func (s *SyncStatus) GetBlockNumber() int {
	return s.BlockNumber
}

// GetGenesisHash returns the original block hash string
func (s *SyncStatus) GetGenesisHash() string {
	return s.GenesisHash
}

// GetIsSyncing returns sync status
func (s *SyncStatus) GetIsSyncing() bool {
	return s.IsSyncing
}

// GetKnownHighestNumber returns the known highest number int
func (s *SyncStatus) GetKnownHighestNumber() int {
	return s.KnownHighestNumber
}

// GetKnownLatestHash returns the known latest Hash string
func (s *SyncStatus) GetKnownLatestHash() string {
	return s.KnownLatestHash
}

// GetLatestHash returns the last hash string
func (s *SyncStatus) GetLatestHash() string {
	return s.LatestHash
}

// GetNodeId returns the node id string
func (s *SyncStatus) GetNodeId() string {
	return s.NodeId
}

// GetPeers returns the peers
func (s *SyncStatus) GetPeers() []Peer {
	return s.Peers
}

// GetProtocolId returns the transaction protocol id int
func (s *SyncStatus) GetProtocolId() int {
	return s.ProtocolId
}

// GetTxPoolSize returns the transaction pool size
func (s *SyncStatus) GetTxPoolSize() string {
	return s.TxPoolSize
}
