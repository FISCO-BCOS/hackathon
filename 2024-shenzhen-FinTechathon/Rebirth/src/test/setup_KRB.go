package test

import (
	"bytes"
	"crypto/aes"
	"crypto/cipher"
	"crypto/sha256"
	"encoding/base64"
	"encoding/hex"
	"fmt"
	"golang.org/x/crypto/pbkdf2"
	"math/big"
	"time"

	"github.com/fentec-project/gofe/abe"
)

type User struct {
	Addr     string
	ID       string
	Key      string
	PL       string
	Lifetime string
}

func Setup() {
	//fmt.Println("--------------------初始化开始！---------------------")
	////startTime := time.Now()
	//
	//KDC := User{Addr: "", ID: "KRB_KDC@GDUT", Key: "King0fDiandongche_@2022", Lifetime: "2022-12-31"}
	//Clients := []User{
	//	{Addr: "", ID: "Alice_2112105116@GDUT", Key: "Shuaige_isM3@2022", Lifetime: "2022-12-31"},
	//}
	//Servers := []User{
	//	{Addr: "", ID: "Printer_CS_No.22@GDUT", Key: "Im_notDay1ngji@2022", Lifetime: "2022-12-31"},
	//}
	//
	//fmt.Println("Key Generation Begin!")
	//
	//_, _, _, _, kdc_key, ctxt := abe_keygen(KDC.Key)
	//KDC.Key = kdc_key.String()
	////fmt.Println(KDC)
	//Clients[0].Key = AESkey_gen(Clients[0].Key)
	//Servers[0].Key = AESkey_gen(Servers[0].Key)
	//fmt.Println("当前的所有密钥：")
	//fmt.Println("KDC 密钥：", KDC.Key)
	//fmt.Println("Clients[0] 密钥：", Clients[0].Key)
	//fmt.Println("Servers[0] 密钥：", Servers[0].Key)
	//
	//fmt.Println("Key Generation End!")
	//fmt.Println("--------------------------------------------------")
	//
	//fmt.Println("Contents Encrypt Begin!")
	//Clients[0].PL, _ = encrypt_AES(Clients[0].Key, "KRB_KDC@GDUT,Printer_CS_No.22@GDUT")
	//Clients[0].Key, _ = encrypt_AES(KDC.Key, Clients[0].Key)
	//Servers[0].Key, _ = encrypt_AES(KDC.Key, Servers[0].Key)
	//KDC.Key = string(ctxt.SymEnc)
	//fmt.Println("KDC 密钥：", KDC.Key)
	//fmt.Println("Contents Encrypt Over!")

	// 示例使用
	key := "0123456789abcdef0123456789abcdef" // 示例密钥，32字符十六进制
	content := "GoFE 是一个加密库，提供不同的最先进的功能加密方案实现，特别是针对线性（例如内积）和二次多项式的 FE 方案。"

	encrypted, err := encrypt_AES(key, content)
	if err != nil {
		fmt.Println("Error encrypting:", err)
		return
	}

	fmt.Println("Encrypted:", encrypted)

	decrypted, err := decryptAES(key, encrypted)

	if err != nil {
		fmt.Println("Error decrypting:", err)
		return
	}

	fmt.Println("Decrypted:", decrypted)
}

// decryptAES 执行AES解密
func decryptAES(key string, ciphertext string) (string, error) {
	// 将十六进制字符串密钥转换为字节数组
	keyBytes, err := hex.DecodeString(key)
	if err != nil {
		return "", err
	}

	// 获取初始化向量（IV），使用密钥的前16字节
	iv := keyBytes[:aes.BlockSize]

	// 创建AES解密器
	block, err := aes.NewCipher(keyBytes)
	if err != nil {
		return "", err
	}

	// Base64解码密文
	encryptBytes, err := base64.StdEncoding.DecodeString(ciphertext)
	if err != nil {
		return "", err
	}

	// 解密
	decryptBytes := make([]byte, len(encryptBytes))
	mode := cipher.NewCBCDecrypter(block, iv)
	mode.CryptBlocks(decryptBytes, encryptBytes)

	// 去除PKCS7填充
	decryptedText := PKCS7Unpadding(decryptBytes)

	return string(decryptedText), nil
}

// encryptAES 执行AES加密
func encrypt_AES(key string, content string) (string, error) {
	// 将十六进制字符串密钥转换为字节数组
	keyBytes, err := hex.DecodeString(key)
	if err != nil {
		return "", err
	}

	// 获取初始化向量（IV），使用密钥的前16字节
	iv := keyBytes[:aes.BlockSize]

	// 创建AES加密器
	block, err := aes.NewCipher(keyBytes)
	if err != nil {
		return "", err
	}

	// 使用CBC模式
	ciphertext := make([]byte, len(PKCS7Padding(content)))
	mode := cipher.NewCBCEncrypter(block, iv)

	// 对明文进行PKCS7填充后加密
	mode.CryptBlocks(ciphertext, []byte(PKCS7Padding(content)))

	// 返回Base64编码的加密结果
	return base64.StdEncoding.EncodeToString(ciphertext), nil
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

// AESkey_gen 生成 AES 加密密钥
func AESkey_gen(seed string) string {
	// 固定的盐值
	salt := []byte{0x75, 0xea, 0x96, 0x05, 0xbc, 0x0f, 0x2e, 0x27}
	// 使用 PBKDF2 生成 256 位（32 字节）的密钥
	key := pbkdf2.Key([]byte(seed), salt, 1000, 32, sha256.New)
	// 将密钥转换为十六进制字符串
	return hex.EncodeToString(key)
}

/*
ABEKeygen 的返回结果：
公钥 (pk)：公钥结合特定的访问策略（如 (GDUT AND COMPUTER) AND (TEACHER OR STUDENT)）来加密消息。只有属性集合满足该策略的用户才能解密密文。
主密钥 (sk)：在生成属性密钥时，主密钥与用户的属性集合一起使用，生成特定用户的解密密钥。
属性密钥 (attribKeys)：当用户尝试解密密文时，属性密钥会与密文中的加密策略进行匹配。如果匹配成功，用户就能解密数据。
KDC 密钥 (kdcKey)：kdcKey 可以作为对称加密的密钥，用于对敏感数据进行额外的加密。
密文 (cipher)：只有符合条件的用户才能解密和读取原始消息。
*/
func abe_keygen(seed string) (*abe.FAME, *abe.FAMEPubKey, *abe.FAMESecKey, *abe.FAMEAttribKeys, *big.Int, *abe.FAMECipher) {
	// 初始化 FAME 方案
	fame := abe.NewFAME()

	// 生成主密钥对
	pk, sk, err := fame.GenerateMasterKeys()
	if err != nil {
		panic(fmt.Sprintf("error generating master keys: %v", err))
	}

	// 定义用户的属性集合
	attributes := []string{"COMPUTER", "STUDENT", "TEACHER", "GDUT"}

	// 为用户生成属性密钥
	attribKeys, err := fame.GenerateAttribKeys(attributes, sk)
	if err != nil {
		panic(fmt.Sprintf("error generating attribute keys: %v", err))
	}

	// 生成随机消息作为加密内容
	// 在 Go 中，我们使用大整数 (big.Int) 来表示消息
	// 使用字符串 seed 生成一个大整数消息
	hash := sha256.Sum256([]byte(seed))
	msg := new(big.Int).SetBytes(hash[:]) // 使用 seed 的 SHA-256 哈希值作为消息

	fmt.Println("msg:", msg)

	// 直接使用消息作为 KDC 密钥
	kdcKey := msg

	// 定义访问策略
	msp, err := abe.BooleanToMSP("((GDUT AND COMPUTER) AND (TEACHER OR STUDENT))", false)
	if err != nil {
		panic(fmt.Sprintf("error converting boolean to MSP: %v", err))
	}

	// 开始加密
	startTime := time.Now()
	cipher, err := fame.Encrypt(msg.String(), msp, pk)
	if err != nil {
		panic(fmt.Sprintf("error encrypting message: %v", err))
	}
	endTime := time.Now()

	fmt.Printf("Encryption completed in %v\n", endTime.Sub(startTime))

	// 返回公钥、主密钥、属性密钥、KDC 密钥和密文
	return fame, pk, sk, attribKeys, kdcKey, cipher
}
