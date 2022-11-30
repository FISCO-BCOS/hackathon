package controller

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"

	"github.com/ethereum/go-ethereum/common"
	"github.com/gin-gonic/gin"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	insurances "github.com/FISCO-BCOS/go-sdk/contracts/insurances"
)

type Data struct {
	ContractAddress   string `json:"contractAddress"`
	InsurancerAddress string `json:"insurancerAddress"`
}

func InsuranceHandler(c *gin.Context) {
	var data Data
	decoder := json.NewDecoder(c.Request.Body)
	err := decoder.Decode(&data)
	if err != nil {
		log.Println(err)
	}
	contractAddress := data.ContractAddress
	insurancerAddress := data.InsurancerAddress
	contractAddr := common.HexToAddress(contractAddress)
	insurancerAddr := common.BytesToAddress([]byte(insurancerAddress))

	configs, err := conf.ParseConfigFile("config.toml")
	if err != nil {
		log.Fatalf("ParseConfigFile failed, err: %v", err)
	}
	client, err := client.Dial(&configs[0])
	if err != nil {
		log.Fatal(err)
	}

	instance, err := insurances.NewInsurances(contractAddr, client)
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
		fmt.Println(info)
		log.Fatal(err)
	}

	fmt.Println("hahahaha")
	c.JSON(http.StatusOK, info)
}
