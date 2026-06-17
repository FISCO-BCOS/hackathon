package middleware

import (
	"github.com/flipped-aurora/gin-vue-admin/server/model/common/response"
	"github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/tools"
	"github.com/gin-gonic/gin"
	"strings"
)

func JWTAuthMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		// 从请求头获取 token
		authHeader := c.GetHeader("chat-token")
		userHeader := c.GetHeader("user-token")
		if userHeader != "" {
			c.Next() // 如果是前端发送的测试请求直接放行
			return
		}
		if authHeader == "" {
			response.FailWithMessage("参数错误:"+"Authorization header is missing", c)
			c.Abort()
			return
		}

		// 按照格式 "Bearer <token>" 提取 token
		tokenString := strings.TrimSpace(strings.TrimPrefix(authHeader, "Bearer "))
		if tokenString == "" {
			response.FailWithMessage("参数错误:"+"Token is missing", c)
			c.Abort()
			return
		}
		// 验证 token
		claims, err := tools.ValidateToken(tokenString)
		if err != nil {
			response.FailWithMessage("Invalid token："+err.Error(), c)
			c.Abort()
			return
		}

		// 将用户信息存储在上下文中，便于后续处理
		c.Set("service_id", claims.ServiceId)
		//c.Request.URL.Query().Add("service_id", strconv.FormatInt(claims.ServiceId, 10))
		c.Next() // 继续处理请求
	}
}
