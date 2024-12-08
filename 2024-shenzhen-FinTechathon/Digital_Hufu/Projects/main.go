package main

import (
	"fmt"
	"hufu/config"
	"hufu/controller"
	"hufu/model"
	"hufu/router"
	"hufu/supervisor"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
)

func main() {
	if _, err := config.LoadConfig("config/config.yaml"); err != nil {
		panic(fmt.Sprintf("Error loading config: %v", err))
	}
	model.SetupDB()
	controller.InitWalletPool()
	supervisor.InitJury()
	r := gin.Default()
	r.Use(cors.New(cors.Config{
		AllowOrigins:     []string{"*"},
		AllowMethods:     []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowHeaders:     []string{"Origin", "Content-Type", "Content-Length", "Accept-Encoding", "X-CSRF-Token", "Authorization"},
		ExposeHeaders:    []string{"Content-Length"},
		AllowCredentials: true,
	}))

	r.GET("/ping", func(c *gin.Context) {
		c.JSON(200, gin.H{"message": "pong"})
	})

	router.InitHufuRouter(r)
	router.InitRegulatorRouter(r)
	r.Run(":3338")
}
