package config

import (
	"encoding/hex"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"os"
	"path/filepath"

	"github.com/FISCO-BCOS/go-sdk/v3/client"
	"gopkg.in/yaml.v2"
)

type Config struct {
	Database struct {
		Username string `yaml:"username"`
		Password string `yaml:"password"`
		Host     string `yaml:"host"`
		Port     int    `yaml:"port"`
		DBName   string `yaml:"dbname"`
		Charset  string `yaml:"charset"`
	} `yaml:"database"`

	Fisco struct {
		IsSMCrypto         bool   `yaml:"is_sm_crypto"`
		GroupID            string `yaml:"group_id"`
		DisableSsl         bool   `yaml:"disable_ssl"`
		Host               string `yaml:"host"`
		Port               int    `yaml:"port"`
		TLSCaFile          string `yaml:"tls_ca_file"`
		TLSKeyFile         string `yaml:"tls_key_file"`
		TLSCertFile        string `yaml:"tls_cert_file"`
		Authorizationtoken string `yaml:"Authorizationtoken"`
	} `yaml:"fisco"`

	Contract struct {
		KeyShareContract string `yaml:"key_share_contract"`
		DecisionContract string `yaml:"decision_contract"`
	} `yaml:"contract"`

	Tee struct {
		PrivateKey string `yaml:"private_key"`
		PublicKey  string `yaml:"public_key"`
	} `yaml:"tee"`
}

var GlobalConfig Config

func LoadConfig(configPath string) (*Config, error) {
	config := &Config{}

	file, err := os.ReadFile(configPath)
	if err != nil {
		return nil, fmt.Errorf("error reading config file: %v", err)
	}

	err = yaml.Unmarshal(file, config)
	if err != nil {
		return nil, fmt.Errorf("error parsing config file: %v", err)
	}

	GlobalConfig = *config
	return config, nil
}

func ReadConfig(pk string) *client.Config {
	privateKey, _ := hex.DecodeString(pk)

	config := &client.Config{
		IsSMCrypto:  GlobalConfig.Fisco.IsSMCrypto,
		GroupID:     GlobalConfig.Fisco.GroupID,
		DisableSsl:  GlobalConfig.Fisco.DisableSsl,
		PrivateKey:  privateKey,
		Host:        GlobalConfig.Fisco.Host,
		Port:        GlobalConfig.Fisco.Port,
		TLSCaFile:   GlobalConfig.Fisco.TLSCaFile,
		TLSKeyFile:  GlobalConfig.Fisco.TLSKeyFile,
		TLSCertFile: GlobalConfig.Fisco.TLSCertFile,
	}

	return config
}

type KeyData struct {
	PublicKey  string `json:"publicKey"`
	PrivateKey string `json:"privateKey"`
}

func ReadJuryKeys() ([]string, []string) {
	juryPrivateKeys := []string{}
	juryPublicKeys := []string{}

	// Define the directory containing the key files
	dir := "config/keys"

	// Read all files in the directory
	files, err := os.ReadDir(dir)
	if err != nil {
		log.Fatalf("Failed to read directory: %v", err)
	}

	// Iterate over each file
	for _, file := range files {
		// Construct the full file path
		filePath := filepath.Join(dir, file.Name())

		// Open the file
		jsonFile, err := os.Open(filePath)
		if err != nil {
			log.Printf("Failed to open file %s: %v", filePath, err)
			continue
		}

		// Read the file contents
		byteValue, err := io.ReadAll(jsonFile)
		if err != nil {
			log.Printf("Failed to read file %s: %v", filePath, err)
			jsonFile.Close()
			continue
		}

		// Close the file
		jsonFile.Close()

		// Parse the JSON
		var keyData KeyData
		err = json.Unmarshal(byteValue, &keyData)
		if err != nil {
			log.Printf("Failed to parse JSON from file %s: %v", filePath, err)
			continue
		}

		// Append the keys to the respective slices
		juryPrivateKeys = append(juryPrivateKeys, keyData.PrivateKey)
		juryPublicKeys = append(juryPublicKeys, keyData.PublicKey)
	}

	return juryPrivateKeys, juryPublicKeys
}
