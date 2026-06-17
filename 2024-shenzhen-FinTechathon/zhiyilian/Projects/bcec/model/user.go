package model

import (
	"database/sql"
	"log"
	"time"

	"github.com/ZhenTaoWei/bcec/config"
	_ "github.com/go-sql-driver/mysql"
	"github.com/golang-jwt/jwt"
)

var DB *sql.DB

// InitDB 初始化数据库连接
func InitDB(dataSourceName string) {
	var err error
	DB, err = sql.Open("mysql", dataSourceName)
	if err != nil {
		log.Fatal(err)
	}
	if err = DB.Ping(); err != nil {
		log.Fatal(err)
	}
}

// User 用户结构体
type User struct {
	Username string
	Password string
	Token    string
	Expires  int64 // Unix 时间戳
}

func GetUserByName(username string) (*User, error) {
	var user User
	query := "SELECT username, password FROM users WHERE username = ?"
	err := DB.QueryRow(query, username).Scan(&user.Username, &user.Password)
	if err != nil {
		return nil, err
	}
	return &user, nil
}

// GenerateToken 生成 JWT token
func GenerateToken(user *User) (string, error) {
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, jwt.MapClaims{
		"username": user.Username,
		"exp":      time.Now().Add(time.Duration(config.Config.JWT.Expire) * time.Hour).Unix(), // 24小时后过期
	})
	tokenString, err := token.SignedString([]byte("secret"))
	if err != nil {
		return "", err
	}
	return tokenString, nil
}

// UpdateToken 更新数据库中的 token 和过期时间
func UpdateToken(username string, tokenString string) error {
	query := "UPDATE users SET token = ?, expires = ? WHERE username = ?"
	_, err := DB.Exec(query, tokenString, time.Now().Add(time.Duration(config.Config.JWT.Expire)*time.Hour).Unix(), username)
	return err
}
