package commands

import (
	"fmt"

	"github.com/urfave/cli/v2"

	"log"
	"math/big"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/FISCO-BCOS/go-sdk/contracts/oracle"
	"github.com/ethereum/go-ethereum/common"
)

var WeatherInfoCommand = &cli.Command{
	Name:  "weather",
	Usage: "look up weather info",
	Flags: []cli.Flag{
		&cli.StringFlag{
			Name:     "address",
			Aliases:  []string{"addr"},
			Value:    "",
			Usage:    "the contract address",
			Required: true,
		},
	},
	Action: func(ctx *cli.Context) error {
		address := ctx.String("address")

		configs, err := conf.ParseConfigFile("config.toml")
		if err != nil {
			log.Fatalf("ParseConfigFile failed, err: %v", err)
		}
		client, err := client.Dial(&configs[0])
		if err != nil {
			log.Fatal(err)
		}
		//load the contract
		contractAddress := common.HexToAddress(address)
		if err != nil {
			log.Fatal(err)
		}
		instance, err := oracle.NewOracle(contractAddress, client)
		if err != nil {
			log.Fatal(err)
		}
		oracleSession := &oracle.OracleSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}

		var i int64
		for i = 0; i >= 0; i++ {

			info, err := oracleSession.Info(big.NewInt(i))
			if err != nil {
				break
			}
			fmt.Printf("%v", info)
		}
		return nil
	},
}
