package utils

import (
	"log"

	"github.com/minio/minio-go/v7"
	"github.com/minio/minio-go/v7/pkg/credentials"
)

// MinIOClient 是 MinIO 客户端的封装
var MinIOClient *minio.Client

// Initialize 初始化 MinIO 客户端
func Initialize(endpoint, accessKeyID, secretAccessKey string, useSSL bool) error {
	client, err := minio.New(endpoint, &minio.Options{
		Creds:  credentials.NewStaticV4(accessKeyID, secretAccessKey, ""),
		Secure: useSSL,
	})
	if err != nil {
		log.Println("Error initializing MinIO client:", err)
		return err
	}

	// 将客户端保存在全局变量中，供其他包使用
	MinIOClient = client
	return nil
}
