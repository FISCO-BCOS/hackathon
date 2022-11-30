package controller

import (
	"encoding/json"
	"net/http"

	"log"
	"math/big"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/FISCO-BCOS/go-sdk/contracts/oracle"
	"github.com/ethereum/go-ethereum/common"
	"github.com/gin-gonic/gin"
)

type WData struct {
	ContractAddress string `json:"contractAddress"`
}

func WeatherHandler(c *gin.Context) {
	var data WData
	var weathers []struct {
		Province      string
		City          string
		Weather       string
		Temperature   string
		Winddirection string
		Windpower     string
		Humidity      string
	}
	decoder := json.NewDecoder(c.Request.Body)
	err := decoder.Decode(&data)
	if err != nil {
		log.Println(err)
	}
	address := data.ContractAddress

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
		weathers = append(weathers, info)
	}
	c.JSON(http.StatusOK, weathers)
}
