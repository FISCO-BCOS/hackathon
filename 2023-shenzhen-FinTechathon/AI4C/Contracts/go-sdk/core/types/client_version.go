package types

type ClientVersion struct {
	BuildTime        string `json:"Build Time"`
	BuildType        string `json:"Build Type"`
	ChainId          string `json:"Chain Id"`
	FiscoBcosVersion string `json:"FISCO-BCOS Version"`
	GitBranch        string `json:"Git Branch"`
	GitCommitHash    string `json:"Git Commit Hash"`
	SupportedVersion string `json:"Supported Version"`
}

// GetBuildTime returns the client build time string
func (c *ClientVersion) GetBuildTime() string {
	return c.BuildTime
}

// GetBuildType returns Compile machine environment string
func (c *ClientVersion) GetBuildType() string {
	return c.BuildType
}

// GetChainId returns chain id string
func (c *ClientVersion) GetChainId() string {
	return c.ChainId
}

// GetFISCOBCOSVersion returns the node version string
func (c *ClientVersion) GetFiscoBcosVersion() string {
	return c.FiscoBcosVersion
}

// GetGitBranch returns versions branch string
func (c *ClientVersion) GetGitBranch() string {
	return c.GitBranch
}

// GetGitCommitHash returns git commit hash string
func (c *ClientVersion) GetGitCommitHash() string {
	return c.GitCommitHash
}

// GetSupportedVersion returns the node supported version string
func (c *ClientVersion) GetSupportedVersion() string {
	return c.SupportedVersion
}
