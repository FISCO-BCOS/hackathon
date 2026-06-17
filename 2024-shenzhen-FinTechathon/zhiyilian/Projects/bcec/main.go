package main

import (
	"fmt"
	"log"

	"github.com/ZhenTaoWei/bcec/config"
	"github.com/ZhenTaoWei/bcec/model"
	"github.com/ZhenTaoWei/bcec/route"
	"github.com/ZhenTaoWei/bcec/utils"
	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
)

func main() {
	config.LoadConfig()
	model.InitDB(config.Config.Database.User + ":" + config.Config.Database.Password + "@tcp(" +
		config.Config.Database.Host + ":" + config.Config.Database.Port + ")/" + config.Config.Database.Database)
	err := utils.Initialize(config.Config.Minio.Endpoint, config.Config.Minio.AccessKeyID,
		config.Config.Minio.SecretAccessKey, config.Config.Minio.UseSSL)
	if err != nil {
		log.Fatal("Failed to initialize MinIO client")
	}
	r := gin.Default()
	r.Use(cors.Default())
	// 路由
	route.SetupRoutes(r)

	// 启动服务器
	r.Run(fmt.Sprintf(":%d", config.Config.Server.Port))
}
