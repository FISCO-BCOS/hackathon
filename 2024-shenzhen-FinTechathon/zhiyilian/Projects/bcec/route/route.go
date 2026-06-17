package route

import (
	"github.com/ZhenTaoWei/bcec/controller"

	"github.com/gin-gonic/gin"
)

func SetupRoutes(r *gin.Engine) {
	r.POST("/login", controller.Login)
	authGroup := r.Group("/api")
	// 上传模型
	authGroup.POST("/upload", controller.Upload)
	// 交易模型
	authGroup.POST("/transaction", controller.Transaction)
	// 验证模型
	authGroup.POST("/verify", controller.Verify)

	r.GET("/search", controller.Search)
	r.POST("/model-upload", controller.ModelUpload)
	r.POST("/buyer-submit", controller.BuySubmit)
	r.POST("/solder-submit", controller.SoldSubmit)
}
