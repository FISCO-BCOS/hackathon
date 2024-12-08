package tools

import (
	"errors"
	"github.com/golang-jwt/jwt/v4"
	"time"
)

var jwtKey = []byte("your-256-bit-secret")

// CustomClaims 结构体可以根据需要添加自定义的声明
type CustomClaims struct {
	ServiceId int64 `json:"service_id"`
	jwt.RegisteredClaims
}

func GenerateToken(serviceId int64) (int64, string, error) {
	// 设置JWT的声明
	claims := CustomClaims{
		ServiceId: serviceId,
		RegisteredClaims: jwt.RegisteredClaims{
			Audience:  jwt.ClaimStrings{"GVA"},                   // 受众
			NotBefore: jwt.NewNumericDate(time.Now().Add(-1000)), // 签名生效时间
			Issuer:    "gva",
			ExpiresAt: jwt.NewNumericDate(time.Now().Add(72 * time.Hour)), // token 72小时后过期
		},
	}
	// 生成JWT token
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	tokenStr, err := token.SignedString(jwtKey)
	return time.Now().Add(72 * time.Hour).Unix(), tokenStr, err
}

func ValidateToken(tokenString string) (*CustomClaims, error) {
	claims := &CustomClaims{}

	token, err := jwt.ParseWithClaims(tokenString, claims, func(token *jwt.Token) (interface{}, error) {
		// 验证签名方法
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, errors.New("unexpected signing method")
		}
		return jwtKey, nil
	})

	if err != nil {
		return nil, err
	}

	if !token.Valid {
		return nil, errors.New("invalid token")
	}

	return claims, nil
}
