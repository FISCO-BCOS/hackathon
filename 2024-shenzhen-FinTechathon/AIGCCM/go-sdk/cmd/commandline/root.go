package commandline

import (
	"fmt"
	"log"
	"os"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/spf13/cobra"
)

var cfgFile string

// RPC is the client connected to the blockchain
var RPC *client.Client

// GroupID default
var GroupID uint

// ChainID default
var ChainID int64

// URL default
var URL string

// GetClient is used for test, it will be init by a config file later.
func getClient(config *conf.Config) *client.Client {
	// RPC API
	c, err := client.Dial(config) // change to your RPC and groupID
	if err != nil {
		fmt.Println("can not dial to FISCO node, please check ./config.toml. error message: ", err)
		os.Exit(1)
	}
	return c
}

// rootCmd represents the base command when called without any subcommands
var rootCmd = &cobra.Command{
	Use:     "console",
	Short:   "console is a command line tool for FISCO BCOS 2.2.0",
	Version: "0.10.0",
	Long: `console is a Golang client for FISCO BCOS 2.2.0 and it supports the JSON-RPC
service and the contract operations(e.g. deploying && writing contracts).

Also, console can be used as a Go package for FISCO BCOS that just simply adding
the import statement:

    import "github.com/FISCO-BCOS/go-sdk"
or
    import "github.com/FISCO-BCOS/go-sdk/client" # using the client package

Please access the github site for more details:
	https://github.com/FISCO-BCOS/go-sdk.`,
	PersistentPreRun: func(cmd *cobra.Command, args []string) {
		initConfig()
	},
	// Uncomment the following line if your bare application
	// has an action associated with it:
	//	Run: func(cmd *cobra.Command, args []string) { },
}

// Execute adds all child commands to the root command and sets flags appropriately.
// This is called by main.main(). It only needs to happen once to the rootCmd.
func Execute() {
	if err := rootCmd.Execute(); err != nil {
		fmt.Println(err)
		os.Exit(1)
	}
}

// initConfig reads in config file and ENV variables if set.
func initConfig() {
	configs, err := conf.ParseConfigFile("config.toml")
	if err != nil {
		log.Fatalf("iniConfig failed, err: %v", err)
	}
	RPC = getClient(&configs[0])
}
