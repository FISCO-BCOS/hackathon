package commands

import (
	"fmt"

	"github.com/urfave/cli/v2"

	"log"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	insurances "github.com/FISCO-BCOS/go-sdk/contracts/insurances"

	// "github.com/FISCO-BCOS/go-sdk/contracts/oracle"
	"github.com/FISCO-BCOS/go-sdk/contracts/weather"
)

var DeployCommand = &cli.Command{
	Name:  "deploy",
	Usage: "deploy the contract",
	Subcommands: []*cli.Command{
		{
			Name:  "insurance",
			Usage: "deploy an insurance contract",
			Action: func(ctx *cli.Context) error {

				configs, err := conf.ParseConfigFile("config.toml")
				if err != nil {
					log.Fatalf("ParseConfigFile failed, err: %v", err)
				}
				client, err := client.Dial(&configs[0])
				if err != nil {
					log.Fatal(err)
				}
				contractAddress, tx, _, err := insurances.DeployInsurances(client.GetTransactOpts(), client)
				if err != nil {
					log.Fatal(err)
				}
				fmt.Println("contract address: ", contractAddress.Hex())
				fmt.Println("transaction hash: ", tx.Hash().Hex())

				return nil
			},
		},
		// {
		// 	Name:  "weather",
		// 	Usage: "deploy a weather contract",
		// 	Flags: []cli.Flag{
		// 		&cli.StringFlag{
		// 			Name:  "oracleAddress",
		// 			Value: "0x99c4ba6fa86531499b75a3bc06476605b1025bcb",
		// 			Usage: "the oracle contract address",
		// 		},
		// 	},
		// 	Action: func(c *cli.Context) error {
		// 		oracleAddress := c.String("oracleAddress")
		// 		address := common.HexToAddress(oracleAddress)
		//
		// 		configs, err := conf.ParseConfigFile("config.toml")
		// 		if err != nil {
		// 			log.Fatalf("ParseConfigFile failed, err: %v", err)
		// 		}
		// 		client, err := client.Dial(&configs[0])
		// 		if err != nil {
		// 			log.Fatal(err)
		// 		}
		// 		contractAddress, tx, _, err := oracle.DeployOracle(client.GetTransactOpts(), client, address)
		// 		if err != nil {
		// 			log.Fatal(err)
		// 		}
		// 		fmt.Println("contract address: ", contractAddress.Hex())
		// 		fmt.Println("transaction hash: ", tx.Hash().Hex())
		//
		// 		return nil
		// 	},
		// },

		{
			Name:  "weather",
			Usage: "deploy a weather contract",
			Action: func(c *cli.Context) error {
				configs, err := conf.ParseConfigFile("config.toml")
				if err != nil {
					log.Fatalf("ParseConfigFile failed, err: %v", err)
				}
				client, err := client.Dial(&configs[0])
				if err != nil {
					log.Fatal(err)
				}
				contractAddress, tx, _, err := weather.DeployWeather(client.GetTransactOpts(), client)
				if err != nil {
					log.Fatal(err)
				}
				fmt.Println("contract address: ", contractAddress.Hex())
				fmt.Println("transaction hash: ", tx.Hash().Hex())

				return nil
			},
		},
	},
}
