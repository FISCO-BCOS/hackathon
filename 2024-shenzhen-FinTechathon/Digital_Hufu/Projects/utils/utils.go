package utils

import (
	"bytes"
	"crypto/ecdsa"
	"crypto/rand"
	"crypto/sha256"
	"encoding/base64"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"math/big"
	"net/http"
	"strconv"
	"time"

	"github.com/SSSaaS/sssa-golang"
	"github.com/ethereum/go-ethereum/common/hexutil"
	"github.com/ethereum/go-ethereum/crypto"
	"github.com/ethereum/go-ethereum/crypto/ecies"
	"golang.org/x/net/proxy"
)

const (
	MINIMUM = 3 // 最小份额数
	SHARES5 = 5 // 总份额数
)

const SupervisorPrivateKey = "43878f814c6753e43c1bd91db187e9399551e50876b7d24f7aba2cc467f88458"
const SupervisorPublicKey = "066583fe9369c70280b2af181e9b6d87eb63848f7af4ac1444dcc774e11805630dfc07918bcd80803a38f77f4b6f415e1d4e2596a79ecacc83f9a0ad95645326"

const ProxyPublicKey = "066583fe9369c70280b2af181e9b6d87eb63848f7af4ac1444dcc774e11805630dfc07918bcd80803a38f77f4b6f415e1d4e2596a79ecacc83f9a0ad95645326"
const ProxyPrivateKey = "43878f814c6753e43c1bd91db187e9399551e50876b7d24f7aba2cc467f88458"

// encryptData encrypts data using ECIES with the provided ECDSA public key.
func EncryptData(key string, data string) (string, error) {
	// 将公钥字符串解码为字节数组
	publicKeyBytes, err := hex.DecodeString(key)
	if err != nil {
		return "", fmt.Errorf("failed to decode public key: %v", err)
	}

	// 直接使用解码后的公钥字节
	// 加上0x04前缀，表示未压缩的公钥
	publicKeyBytes = append([]byte{0x04}, publicKeyBytes...)
	publicKey, err := crypto.UnmarshalPubkey(publicKeyBytes)
	if err != nil {
		return "", fmt.Errorf("failed to unmarshal public key: %v", err)
	}

	// Convert to ECIES public key
	eciesPubKey := ecies.ImportECDSAPublic(publicKey)

	// Encrypt the data
	ciphertext, err := ecies.Encrypt(rand.Reader, eciesPubKey, []byte(data), nil, nil)
	if err != nil {
		return "", fmt.Errorf("failed to encrypt data: %v", err)
	}

	// Encode the ciphertext to base64
	return base64.StdEncoding.EncodeToString(ciphertext), nil
}

// DecryptData decrypts data using ECIES with the provided ECDSA private key.
func DecryptData(rawData string, pk string) (string, error) {
	// Decode the base64 encoded ciphertext
	ciphertext, err := base64.StdEncoding.DecodeString(rawData)
	if err != nil {
		return "", fmt.Errorf("failed to decode ciphertext: %v", err)
	}

	// Decode the private key from hex
	privateKeyBytes, err := hex.DecodeString(pk)
	if err != nil {
		return "", fmt.Errorf("failed to decode private key: %v", err)
	}

	// Parse the private key
	privateKey, err := crypto.ToECDSA(privateKeyBytes)
	if err != nil {
		return "", fmt.Errorf("failed to parse private key: %v", err)
	}

	// Convert to ECIES private key
	eciesPrivKey := ecies.ImportECDSA(privateKey)

	// Decrypt the data
	plaintext, err := eciesPrivKey.Decrypt(ciphertext, nil, nil)
	if err != nil {
		return "", fmt.Errorf("failed to decrypt data: %v", err)
	}

	return string(plaintext), nil
}

func GenerateHash(data string) string {
	hash := sha256.New()
	hash.Write([]byte(data))
	return hex.EncodeToString(hash.Sum(nil))
}

// generateKeys 生成随机密钥对
func GenerateKeys() (privateKeyString, publicKeyString, address string) {
	privateKey, err := crypto.GenerateKey()
	if err != nil {
		log.Fatal(err)
	}

	privateKeyBytes := crypto.FromECDSA(privateKey)

	publicKey := privateKey.Public()
	publicKeyECDSA, ok := publicKey.(*ecdsa.PublicKey)
	if !ok {
		log.Fatal("cannot assert type: publicKey is not of type *ecdsa.PublicKey")
	}

	publicKeyBytes := crypto.FromECDSAPub(publicKeyECDSA)

	address = crypto.PubkeyToAddress(*publicKeyECDSA).Hex()

	return hexutil.Encode(privateKeyBytes)[2:], hexutil.Encode(publicKeyBytes)[4:], address
}

func GenerateRSAKey(walletID int) (privateKey, publicKey string) {
	// 创建 SOCKS5 代理
	dialer, err := proxy.SOCKS5("tcp", "127.0.0.1:1080", nil, proxy.Direct)
	if err != nil {
		log.Println(err)
		return "", ""
	}

	// 创建带有代理的 HTTP 客户端
	httpClient := &http.Client{
		Transport: &http.Transport{
			Dial: dialer.Dial,
		},
	}

	// 准备请求
	jsonBody, _ := json.Marshal(map[string]int{"wallet_id": walletID})
	req, err := http.NewRequest("POST", "http://10.77.110.184:8082/api/create_wallet", bytes.NewBuffer(jsonBody))
	if err != nil {
		log.Println("创建请求失败: ", err)
		return "", ""
	}

	// 设置 Content-Type
	req.Header.Set("Content-Type", "application/json")

	// 发送请求
	resp, err := httpClient.Do(req)
	if err != nil {
		log.Println("发送请求失败: ", err)
		return "", ""
	}
	defer resp.Body.Close()

	// 读取响应
	respBody, err := io.ReadAll(resp.Body)
	if err != nil {
		log.Println("读取响应失败: ", err)
		return "", ""
	}

	type KeyResponse struct {
		Message    string `json:"message"`
		PublicKey  string `json:"public_key"`
		PrivateKey string `json:"private_key"`
	}

	var keyResp KeyResponse
	if err := json.Unmarshal(respBody, &keyResp); err != nil {
		log.Println("解析响应失败: ", err)
		return "", ""
	}
	log.Println("创建钱包响应: ", keyResp)

	if keyResp.Message != "wallet created successfully" {
		log.Println("创建钱包失败: ", keyResp.Message)
		return "", ""
	}

	return keyResp.PrivateKey, keyResp.PublicKey
}

// SharePrivateKey Shamir's secret sharing https://en.wikipedia.org/wiki/Shamir%27s_secret_sharing
func SharePrivateKey(PrivateKey string) ([]string, error) {
	result, err := sssa.Create(MINIMUM, SHARES5, PrivateKey)
	return result, err
}

func SignData(privateKey string, data string) (string, error) {
	// Decode the private key from hex
	privateKeyBytes, err := hex.DecodeString(privateKey)
	if err != nil {
		return "", fmt.Errorf("failed to decode private key: %v", err)
	}

	// Parse the private key
	ecPrivateKey, err := crypto.ToECDSA(privateKeyBytes)
	if err != nil {
		return "", fmt.Errorf("failed to parse private key: %v", err)
	}

	// Generate the hash of the data
	hash := sha256.New()
	hash.Write([]byte(data))
	hashedData := hash.Sum(nil)

	// Sign the hashed data
	r, s, err := ecdsa.Sign(rand.Reader, ecPrivateKey, hashedData)
	if err != nil {
		return "", fmt.Errorf("failed to sign data: %v", err)
	}

	// Combine r and s into a single byte slice
	rBytes := r.Bytes()
	sBytes := s.Bytes()
	signature := append(rBytes, sBytes...)

	// Encode the signature to hex
	return hex.EncodeToString(signature), nil
}

// VerifySignature verifies the ECDSA signature for the given data.
func VerifySignature(publicKey string, data string, signature string) (bool, error) {
	// Decode the public key from hex
	publicKeyBytes, err := hex.DecodeString(publicKey)
	if err != nil {
		return false, fmt.Errorf("failed to decode public key: %v", err)
	}

	// Create an ECDSA public key from the bytes
	ecPublicKey, err := crypto.UnmarshalPubkey(publicKeyBytes)
	if err != nil {
		return false, fmt.Errorf("failed to unmarshal public key: %v", err)
	}

	// Generate the hash of the data
	hash := sha256.New()
	hash.Write([]byte(data))
	hashedData := hash.Sum(nil)

	// Decode the signature from hex
	sigBytes, err := hex.DecodeString(signature)
	if err != nil {
		return false, fmt.Errorf("failed to decode signature: %v", err)
	}

	// Split the signature into r and s
	r := new(big.Int).SetBytes(sigBytes[:len(sigBytes)/2])
	s := new(big.Int).SetBytes(sigBytes[len(sigBytes)/2:])

	// Verify the signature
	valid := ecdsa.Verify(ecPublicKey, hashedData, r, s)
	return valid, nil
}

func GenerateRandomNumber(length int) string {
	// 创建一个包含所有数字的字符串
	const numbers = "0123456789"

	// 使用crypto/rand生成安全的随机数
	result := make([]byte, length)
	for i := 0; i < length; i++ {
		// 生成随机索引
		randomIndex, err := rand.Int(rand.Reader, big.NewInt(int64(len(numbers))))
		if err != nil {
			// 如果出错，使用time作为后备方案
			randomIndex = big.NewInt(int64(time.Now().UnixNano() % int64(len(numbers))))
		}
		result[i] = numbers[randomIndex.Int64()]
	}
	return string(result)
}

// FormatFloat 格式化浮点数到指定小数位数
func FormatFloat(num float64, decimal int) float64 {
	// 将数字转为字符串，保留指定小数位
	format := fmt.Sprintf("%%.%df", decimal)
	str := fmt.Sprintf(format, num)

	// 将字符串转回float64
	result, _ := strconv.ParseFloat(str, 64)
	return result
}

// StringToUint 将字符串转换为uint
func StringToUint(s string) uint {
	i, err := strconv.ParseUint(s, 10, 32)
	if err != nil {
		return 0
	}
	return uint(i)
}
