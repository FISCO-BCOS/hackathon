package biz

import (
	"errors"
	"fmt"
	"time"

	"github.com/ZhenTaoWei/bcec/model"
	"github.com/golang-jwt/jwt"
)

// 用于生成 JWT token 的密钥
var jwtKey = []byte("secret")

// Login 校验用户名和密码，并返回一个 JWT token
func Login(username, password string) (string, error) {
	user, err := findUserByUsername(username)
	if err != nil {
		return "", errors.New("user not found")
	}

	// 校验密码
	if user.Password != password {
		return "", errors.New("incorrect password")
	}

	// 生成 JWT token
	token, err := generateJWT(user)
	if err != nil {
		return "", err
	}

	return token, nil
}

// 模拟通过用户名查询用户信息
func findUserByUsername(username string) (*model.User, error) {
	// 这里只是模拟数据，在实际应用中应该查询数据库
	if username == "admin" {
		return &model.User{
			Username: "admin",
			Password: "password", // 模拟密码
		}, nil
	}
	return nil, errors.New("user not found")
}

// 生成 JWT token
func generateJWT(user *model.User) (string, error) {
	// 创建一个包含用户信息的 JWT token
	claims := &jwt.StandardClaims{
		Subject:   user.Username,
		ExpiresAt: time.Now().Add(24 * time.Hour).Unix(),
		Issuer:    "myapp",
	}

	// 创建 JWT token
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	tokenString, err := token.SignedString(jwtKey)
	if err != nil {
		return "", fmt.Errorf("failed to generate token: %v", err)
	}

	return tokenString, nil
}
