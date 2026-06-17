package utils

import (
	"bytes"
	"crypto/aes"
	"crypto/cipher"
	"crypto/sha256"
	"encoding/base64"
	"encoding/hex"
	"fmt"
	"golang.org/x/crypto/pbkdf2"
)

// AES_Key_Generate 通过用户的密码生成 AES 加密密钥
func AES_Key_Generate(Password string) string {
	// 固定的盐值
	salt := []byte{0x75, 0xea, 0x96, 0x05, 0xbc, 0x0f, 0x2e, 0x27}
	// 使用 PBKDF2 生成 256 位（32 字节）的密钥
	key := pbkdf2.Key([]byte(Password), salt, 1000, 32, sha256.New)
	// 将密钥转换为十六进制字符串
	return hex.EncodeToString(key)
}

// AES_Decrypt 执行AES解密
func AES_Decrypt(key string, ciphertext string) string {
	// 将十六进制字符串密钥转换为字节数组
	keyBytes, err := hex.DecodeString(key)
	if err != nil {
		fmt.Println("AES 密钥转换失败：", err)
		return ""
	}

	// 获取初始化向量（IV），使用密钥的前16字节
	iv := keyBytes[:aes.BlockSize]

	// 创建AES解密器
	block, err := aes.NewCipher(keyBytes)
	if err != nil {
		fmt.Println("AES 解密器创建失败：", err)
		return ""
	}

	// Base64解码密文
	encryptBytes, err := base64.StdEncoding.DecodeString(ciphertext)
	if err != nil {
		fmt.Println("Base 64 解密失败：", err)
		return ""
	}

	// 解密
	decryptBytes := make([]byte, len(encryptBytes))
	mode := cipher.NewCBCDecrypter(block, iv)
	mode.CryptBlocks(decryptBytes, encryptBytes)

	// 去除PKCS7填充
	decryptedText := PKCS7Unpadding(decryptBytes)

	return string(decryptedText)
}

// AES_Encrypt 执行AES加密
func AES_Encrypt(key string, content string) string {
	// 将十六进制字符串密钥转换为字节数组
	keyBytes, err := hex.DecodeString(key)
	if err != nil {
		fmt.Println("AES 密钥转换失败！")
		return ""
	}

	// 获取初始化向量（IV），使用密钥的前16字节
	iv := keyBytes[:aes.BlockSize]

	// 创建AES加密器
	block, err := aes.NewCipher(keyBytes)
	if err != nil {
		fmt.Println("AES 加密器创建失败！")
		return ""
	}

	// 使用CBC模式
	ciphertext := make([]byte, len(PKCS7Padding(content)))
	mode := cipher.NewCBCEncrypter(block, iv)

	// 对明文进行PKCS7填充后加密
	mode.CryptBlocks(ciphertext, []byte(PKCS7Padding(content)))

	// 返回Base64编码的加密结果
	return base64.StdEncoding.EncodeToString(ciphertext)
}

// PKCS7Unpadding 去除PKCS7填充
func PKCS7Unpadding(text []byte) []byte {
	padding := int(text[len(text)-1])
	return text[:len(text)-padding]
}

// PKCS7Padding 添加填充
func PKCS7Padding(text string) string {
	bs := 16
	padding := bs - len(text)%bs
	padtext := bytes.Repeat([]byte{byte(padding)}, padding)
	return text + string(padtext)
}
