package utils

import (
	"crypto/rand"
	"crypto/rsa"
	"encoding/hex"
	"math/big"
	"strings"
)

// RSADecryptWithHexKey 使用十六进制私钥进行RSA解密
func RSADecryptWithHexKey(encryptedData string, hexPrivateKey string, hexPublicKey string) (string, error) {
	// 解码
	ciphertext, err := hex.DecodeString(encryptedData)
	if err != nil {
		return "", err
	}

	// 转换十六进制私钥
	privateKey, err := hexToPrivateKey(hexPrivateKey, hexPublicKey)
	if err != nil {
		return "", err
	}

	// 解密
	plaintext, err := rsa.DecryptPKCS1v15(rand.Reader, privateKey, ciphertext)
	if err != nil {
		return "", err
	}

	return string(plaintext), nil
}

// RSAEncryptWithHexKey 使用十六进制公钥进行RSA加密
func RSAEncryptWithHexKey(data string, hexPublicKey string) (string, error) {
	// 转换十六进制公钥
	publicKey, err := hexToPublicKey(hexPublicKey)
	if err != nil {
		return "", err
	}

	// 加密
	ciphertext, err := rsa.EncryptPKCS1v15(rand.Reader, publicKey, []byte(data))
	if err != nil {
		return "", err
	}

	// 返回十六进制字符串
	return hex.EncodeToString(ciphertext), nil
}

// hexToPublicKey 将十六进制字符串转换为RSA公钥
func hexToPublicKey(hexStr string) (*rsa.PublicKey, error) {
	hexStr = strings.ReplaceAll(hexStr, " ", "")

	// 解码十六进制字符串
	modBytes, err := hex.DecodeString(hexStr)
	if err != nil {
		return nil, err
	}

	// 创建公钥
	pub := &rsa.PublicKey{
		N: new(big.Int).SetBytes(modBytes),
		E: 65537, // 通常使用65537作为公钥指数
	}

	return pub, nil
}

// hexToPrivateKey 将十六进制字符串转换为RSA私钥
func hexToPrivateKey(hexPriKey string, hexPubKey string) (*rsa.PrivateKey, error) {
	hexPriKey = strings.ReplaceAll(hexPriKey, " ", "")
	hexPubKey = strings.ReplaceAll(hexPubKey, " ", "")

	// 解码十六进制字符串
	privBytes, err := hex.DecodeString(hexPriKey)
	if err != nil {
		return nil, err
	}

	pubBytes, err := hex.DecodeString(hexPubKey)
	if err != nil {
		return nil, err
	}

	// 创建私钥结构
	priv := &rsa.PrivateKey{
		PublicKey: rsa.PublicKey{
			N: new(big.Int).SetBytes(pubBytes),
			E: 65537,
		},
		D: new(big.Int).SetBytes(privBytes),
	}

	return priv, nil
}
