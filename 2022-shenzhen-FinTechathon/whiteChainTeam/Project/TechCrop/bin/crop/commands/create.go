package commands

import (
	"fmt"

	"github.com/urfave/cli/v2"

	"log"
	"math/big"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	insurances "github.com/FISCO-BCOS/go-sdk/contracts/insurances"
	"github.com/ethereum/go-ethereum/common"
)

var CreateCommand = &cli.Command{
	Name:  "create",
	Usage: "create insurance",
	Flags: []cli.Flag{
		&cli.StringFlag{
			Name:     "address",
			Aliases:  []string{"addr"},
			Value:    "",
			Usage:    "the beneficiary address",
			Required: true,
		},
		&cli.StringFlag{
			Name:     "contractAddress",
			Aliases:  []string{"ca"},
			Value:    "",
			Usage:    "the contract address",
			Required: true,
		},
		&cli.Int64Flag{
			Name:     "amount",
			Usage:    "the amount of compensation",
			Required: true,
		},
		&cli.Int64Flag{
			Name:     "duration",
			Aliases:  []string{"d"},
			Usage:    "the valid duration of insurance",
			Required: true,
		},
		&cli.StringFlag{
			Name:     "uri",
			Value:    "",
			Usage:    "ipfs uri",
			Required: true,
		},
	},
	Action: func(ctx *cli.Context) error {
		address1 := ctx.String("contractAddress")
		address2 := ctx.String("address")
		uri := ctx.String("uri")
		amount := ctx.Int64("amount")
		duration := ctx.Int64("duration")

		if address2 != "" {
			configs, err := conf.ParseConfigFile("config.toml")
			if err != nil {
				log.Fatalf("ParseConfigFile failed, err: %v", err)
			}
			client, err := client.Dial(&configs[0])
			if err != nil {
				log.Fatal(err)
			}

			//load the contract
			contractAddress := common.HexToAddress(address1)
			instance, err := insurances.NewInsurances(contractAddress, client)
			if err != nil {
				log.Fatal(err)
			}
			insuranceSession := &insurances.InsurancesSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}

			tx, rx, err := insuranceSession.CreateInsurance(common.BytesToAddress([]byte(address2)), big.NewInt(amount), big.NewInt(duration), uri)
			if err != nil {
				log.Fatal(err)
			}
			fmt.Println("transaction hash: ", tx.Hash().Hex())
			fmt.Println("receipt output: ", rx.String())

			id, err := insuranceSession.InsuranceIds(common.BytesToAddress([]byte(address2)))
			if err != nil {
				log.Fatal(err)
			}
			fmt.Println("InsuranceId: ", id)
		}

		return nil
	},
}
