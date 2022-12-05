package main

import (
	"os"

	"log"

	"github.com/FISCO-BCOS/go-sdk/bin/crop/commands"
	"github.com/urfave/cli/v2"
)

func main() {

	app := &cli.App{
		Commands: []*cli.Command{
			commands.CreateCommand,
			commands.WatchCommand,
			commands.WeatherInfoCommand,
			commands.InsuranceInfoCommand,
			commands.DeployCommand,
			commands.WebCommand,
		},
	}

	if err := app.Run(os.Args); err != nil {
		log.Fatal(err)
	}

}
