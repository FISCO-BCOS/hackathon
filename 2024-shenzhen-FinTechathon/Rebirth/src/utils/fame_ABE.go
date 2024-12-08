package utils

import (
	"bytes"
	"compress/gzip"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"github.com/fentec-project/gofe/abe"
	"io/ioutil"
	"strings"
)

// bytesToSecKey 反序列化函数：将字符串反序列化为 FAMESecKey 对象
func bytesToSecKey(data string) *abe.FAMESecKey {
	// 解码 Base64 字符串
	decodedData, err := base64.StdEncoding.DecodeString(data)
	if err != nil {
		return nil
	}

	// 使用 gzip 解压缩
	buf := bytes.NewReader(decodedData)
	zr, err := gzip.NewReader(buf)
	if err != nil {
		return nil
	}
	jsonData, err := ioutil.ReadAll(zr)
	if err != nil {
		return nil
	}

	// 反序列化为 FAMESecKey 结构体
	var secKey abe.FAMESecKey
	err = json.Unmarshal(jsonData, &secKey)
	if err != nil {
		fmt.Println("JSON 格式的主密钥反序列化失败：", err)
		return nil
	}

	return &secKey
}

// serializeFAMESecKey 函数：序列化 FAMESecKey 对象
func serializeFAMESecKey(secKey *abe.FAMESecKey) string {
	// 将主密钥结构体序列化为 JSON 字符串
	jsonData, err := json.Marshal(secKey)
	if err != nil {
		fmt.Println("主密钥序列化为 JSON 失败：", err)
		return ""
	}

	// 使用 gzip 压缩 JSON 字符串
	var buf bytes.Buffer
	zw := gzip.NewWriter(&buf)
	_, err = zw.Write(jsonData)
	if err != nil {
		return ""
	}
	if err := zw.Close(); err != nil {
		return ""
	}

	// 将压缩后的数据编码为 Base64 字符串
	compressedEncodedData := base64.StdEncoding.EncodeToString(buf.Bytes())
	return compressedEncodedData
}

// bytesToPubKey 反序列化函数：将字符串反序列化为 FAMEPubKey 对象
func bytesToPubKey(data string) *abe.FAMEPubKey {
	// 解码 Base64 字符串
	decodedData, err := base64.StdEncoding.DecodeString(data)
	if err != nil {
		return nil
	}

	// 使用 gzip 解压缩
	buf := bytes.NewReader(decodedData)
	zr, err := gzip.NewReader(buf)
	if err != nil {
		return nil
	}
	jsonData, err := ioutil.ReadAll(zr)
	if err != nil {
		return nil
	}

	// 反序列化为 FAMEPubKey 结构体
	var pubKey abe.FAMEPubKey
	err = json.Unmarshal(jsonData, &pubKey)
	if err != nil {
		fmt.Println("JSON 格式的公钥反序列化失败：", err)
		return nil
	}

	return &pubKey
}

// serializeFAMEPubKey 函数：序列化 FAMEPubKey 对象
func serializeFAMEPubKey(pubKey *abe.FAMEPubKey) string {
	// 将公钥结构体序列化为 JSON 字符串
	jsonData, err := json.Marshal(pubKey)
	if err != nil {
		fmt.Println("公钥序列化为 JSON 失败：", err)
		return ""
	}

	// 使用 gzip 压缩 JSON 字符串
	var buf bytes.Buffer
	zw := gzip.NewWriter(&buf)
	_, err = zw.Write(jsonData)
	if err != nil {
		return ""
	}
	if err := zw.Close(); err != nil {
		return ""
	}

	// 将压缩后的数据编码为 Base64 字符串
	compressedEncodedData := base64.StdEncoding.EncodeToString(buf.Bytes())
	return compressedEncodedData
}

// bytesToCipher 反序列化函数：将字符串反序列化为 FAMECipher 对象
func bytesToCipher(data string) *abe.FAMECipher {
	// 解码 Base64 字符串
	decodedData, err := base64.StdEncoding.DecodeString(data)
	if err != nil {
		return nil
	}

	// 使用 gzip 解压缩
	buf := bytes.NewReader(decodedData)
	zr, err := gzip.NewReader(buf)
	if err != nil {
		return nil
	}
	jsonData, err := ioutil.ReadAll(zr)
	if err != nil {
		return nil
	}

	// 反序列化为 FAMECipher 结构体
	var cipher abe.FAMECipher
	err = json.Unmarshal(jsonData, &cipher)
	if err != nil {
		fmt.Println("JSON 格式的密文反序列化失败：", err)
		return nil
	}

	return &cipher
}

// serializeFAMECipher 函数：序列化 FAMECipher 对象
func serializeFAMECipher(cipher *abe.FAMECipher) string {
	// 将结构体序列化为 JSON 字符串
	jsonData, err := json.Marshal(cipher)
	if err != nil {
		fmt.Println("密文序列化为 JSON 失败：", err)
		return ""
	}

	// 使用 gzip 压缩 JSON 字符串
	var buf bytes.Buffer
	zw := gzip.NewWriter(&buf)
	_, err = zw.Write(jsonData)
	if err != nil {
		return ""
	}
	if err := zw.Close(); err != nil {
		return ""
	}

	// 将压缩后的数据编码为 Base64 字符串
	compressedEncodedData := base64.StdEncoding.EncodeToString(buf.Bytes())
	return compressedEncodedData
}

func ABE_Encrypt(plaintext string, policy string) string {
	// 实例化 FAME 方案
	fame := abe.NewFAME()

	// 生成公钥和主密钥
	pubKey, secKey, err := fame.GenerateMasterKeys()
	if err != nil {
		fmt.Println("生成主密钥和公钥失败：", err)
		return ""
	}

	// 将策略转换为 MSP
	msp, err := abe.BooleanToMSP(policy, false)
	if err != nil {
		fmt.Println("策略转换为 MSP 失败：", err)
		return ""
	}

	// 加密消息
	cipher, err := fame.Encrypt(plaintext, msp, pubKey)
	if err != nil {
		fmt.Println("加密失败：", err)
		return ""
	}

	// 序列化公钥、主密钥和密文
	serializedPubKey := serializeFAMEPubKey(pubKey)
	serializedSecKey := serializeFAMESecKey(secKey)
	serializedCipher := serializeFAMECipher(cipher)

	// 将序列化后的内容拼接成一个字符串，使用 "," 分隔
	finalString := fmt.Sprintf("%s,%s,%s", serializedPubKey, serializedSecKey, serializedCipher)

	return finalString
}

func ABE_Decrypt(encryptedString string, attributes []string) string {
	// 切割字符串，获取公钥、主密钥和密文的序列化字符串
	parts := strings.Split(encryptedString, ",")
	if len(parts) != 3 {
		fmt.Println("加密字符串格式错误")
		return ""
	}

	// 反序列化公钥、主密钥和密文
	pubKey := bytesToPubKey(parts[0])
	secKey := bytesToSecKey(parts[1])
	cipher := bytesToCipher(parts[2])

	if pubKey == nil || secKey == nil || cipher == nil {
		fmt.Println("反序列化失败")
		return ""
	}

	// 实例化 FAME 方案
	fame := abe.NewFAME()

	// 使用用户的属性生成解密密钥
	userAttribKeys, err := fame.GenerateAttribKeys(attributes, secKey)
	if err != nil {
		fmt.Println("生成用户属性密钥失败：", err)
		return ""
	}

	// 解密消息
	plaintext, err := fame.Decrypt(cipher, userAttribKeys, pubKey)
	if err != nil {
		fmt.Println("解密失败：", err)
		return ""
	}

	return plaintext
}
