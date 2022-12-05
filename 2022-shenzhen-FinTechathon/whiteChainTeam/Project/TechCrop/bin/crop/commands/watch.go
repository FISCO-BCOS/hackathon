package commands

import (
	"fmt"

	"time"

	"github.com/urfave/cli/v2"

	"log"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/FISCO-BCOS/go-sdk/contracts/oracle"
	"github.com/ethereum/go-ethereum/common"
)

type WeatherInfo struct {
	Status   string `json:"status"`
	Count    string `json:"count"`
	Info     string `json:"info"`
	Infocode string `json:"infocode"`
	Lives    []struct {
		Province      string `json:"province"`
		City          string `json:"city"`
		Adcode        string `json:"adcode"`
		Weather       string `json:"weather"`
		Temperature   string `json:"temperature"`
		Winddirection string `json:"winddirection"`
		Windpower     string `json:"windpower"`
		Humidity      string `json:"humidity"`
		Reporttime    string `json:"reporttime"`
	} `json:"lives"`
}

var WatchCommand = &cli.Command{
	Name:  "watch",
	Usage: "watch the data of Iot and weather.",
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
		instance, err := oracle.NewOracle(contractAddress, client)
		if err != nil {
			log.Fatal(err)
		}
		oracleSession := &oracle.OracleSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}

		ticker := time.NewTicker(1 * time.Second)
		defer ticker.Stop()
		for {
			select {
			case <-ticker.C:

				oracleSession.SetUrl("json(https://restapi.amap.com/v3/weather/weatherInfo?key=f80ceb04d1e696388879aafe9c152ee4&city=510100&extensions=base&output=JSON).lives[0].province")
				oracleSession.Request()
				province, err := oracleSession.Get()

				oracleSession.SetUrl("json(https://restapi.amap.com/v3/weather/weatherInfo?key=f80ceb04d1e696388879aafe9c152ee4&city=510100&extensions=base&output=JSON).lives[0].city")
				oracleSession.Request()
				city, err := oracleSession.Get()

				oracleSession.SetUrl("json(https://restapi.amap.com/v3/weather/weatherInfo?key=f80ceb04d1e696388879aafe9c152ee4&city=510100&extensions=base&output=JSON).lives[0].weather")
				oracleSession.Request()
				weather, err := oracleSession.Get()

				oracleSession.SetUrl("json(https://restapi.amap.com/v3/weather/weatherInfo?key=f80ceb04d1e696388879aafe9c152ee4&city=510100&extensions=base&output=JSON).lives[0].temperature")
				oracleSession.Request()
				temperature, err := oracleSession.Get()

				oracleSession.SetUrl("json(https://restapi.amap.com/v3/weather/weatherInfo?key=f80ceb04d1e696388879aafe9c152ee4&city=510100&extensions=base&output=JSON).lives[0].winddirection")
				oracleSession.Request()
				winddirection, err := oracleSession.Get()

				oracleSession.SetUrl("json(https://restapi.amap.com/v3/weather/weatherInfo?key=f80ceb04d1e696388879aafe9c152ee4&city=510100&extensions=base&output=JSON).lives[0].windpower")
				oracleSession.Request()
				windpower, err := oracleSession.Get()

				oracleSession.SetUrl("json(https://restapi.amap.com/v3/weather/weatherInfo?key=f80ceb04d1e696388879aafe9c152ee4&city=510100&extensions=base&output=JSON).lives[0].humidity")
				oracleSession.Request()
				humidity, err := oracleSession.Get()

				// 上传天气数据
				tx, _, err := oracleSession.Push(province, city, weather, temperature, winddirection, windpower, humidity)
				if err != nil {
					return err
				}
				fmt.Println("transaction hash: ", tx.Hash().Hex())

			}
		}
	},
}
