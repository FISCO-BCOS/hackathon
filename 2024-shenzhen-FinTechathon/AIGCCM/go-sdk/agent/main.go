package main

import (
	"github.com/FISCO-BCOS/go-sdk/agent/router"
)

func main() {
	r := router.SetupRouter()
	r.Run("0.0.0.0:10100") // listen and serve on 0.0.0.0:8080
}
