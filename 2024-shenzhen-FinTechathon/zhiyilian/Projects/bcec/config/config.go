package config

import (
	"github.com/spf13/viper"
)

var Config *ConfigType

// Config 定义配置文件的结构
type ConfigType struct {
	Server   ServerConfig   `mapstructure:"server"`
	Database DatabaseConfig `mapstructure:"database"`
	Minio    MinioConfig    `mapstructure:"minio"`
	JWT      JWTConfig      `mapstructure:"jwt"`
}

// ServerConfig 定义服务器配置
type ServerConfig struct {
	Port int    `mapstructure:"port"`
	Host string `mapstructure:"host"`
}

// DatabaseConfig 定义数据库配置
type DatabaseConfig struct {
	User     string `mapstructure:"user"`
	Password string `mapstructure:"password"`
	Host     string `mapstructure:"host"`
	Port     string `mapstructure:"port"`
	Database string `mapstructure:"database"`
}

type MinioConfig struct {
	Endpoint        string `mapstructure:"endpoint"`
	AccessKeyID     string `mapstructure:"accessKeyID"`
	SecretAccessKey string `mapstructure:"secretAccessKey"`
	UseSSL          bool   `mapstructure:"useSSL"`
}

type JWTConfig struct {
	Key    string `mapstructure:"key"`
	Expire int    `mapstructure:"expire"`
}

// LoadConfig 加载配置文件
func LoadConfig() {
	v := viper.New()
	v.SetConfigFile("config.yaml")
	v.SetConfigType("yaml")

	if err := v.ReadInConfig(); err != nil {
		panic(err)
	}

	Config = &ConfigType{}
	if err := v.Unmarshal(Config); err != nil {
		panic(err)
	}
}
