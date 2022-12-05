package commands

import (
	"github.com/gin-gonic/gin"
	"github.com/urfave/cli/v2"

	"github.com/FISCO-BCOS/go-sdk/bin/crop/commands/web/controller"
)

var WebCommand = &cli.Command{
	Name:  "web",
	Usage: "web service for infos",

	Action: func(ctx *cli.Context) error {
		r := gin.Default()
		r.POST("/insuranceInfos", controller.InsuranceHandler)
		r.POST("/weatherInfos", controller.WeatherHandler)
		r.Static("/", "./build")

		r.Run(":8080")
		return nil
	},
}
