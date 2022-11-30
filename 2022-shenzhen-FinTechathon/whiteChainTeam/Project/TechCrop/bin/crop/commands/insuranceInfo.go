package commands

import (
	"fmt"

	"github.com/urfave/cli/v2"

	"log"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	insurances "github.com/FISCO-BCOS/go-sdk/contracts/insurances"
	"github.com/ethereum/go-ethereum/common"
)

var InsuranceInfoCommand = &cli.Command{
	Name:  "insurance",
	Usage: "look up insurance info",
	Flags: []cli.Flag{
		&cli.StringFlag{
			Name:     "insurancerAddress",
			Aliases:  []string{"ia"},
			Value:    "",
			Usage:    "the insurancer address",
			Required: true,
		},
		&cli.StringFlag{
			Name:     "contractAddress",
			Aliases:  []string{"ca"},
			Value:    "",
			Usage:    "the contract address",
			Required: true,
		},
	},
	Action: func(ctx *cli.Context) error {
		address := ctx.String("contractAddress")
		insurancerAddress := ctx.String("insurancerAddress")
		insurancerAddr := common.BytesToAddress([]byte(insurancerAddress))

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
		instance, err := insurances.NewInsurances(contractAddress, client)
		if err != nil {
			log.Fatal(err)
		}
		insuranceSession := &insurances.InsurancesSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}

		id, err := insuranceSession.InsuranceIds(insurancerAddr)
		if err != nil {
			log.Fatal(err)
		}
		fmt.Println(id)

		info, err := insuranceSession.FindInsurance(id)
		if err != nil {
			log.Fatal(err)
		}
		fmt.Printf("%+v", info)
		return nil
	},
}
