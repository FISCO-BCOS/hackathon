package rsa

import (
	"crypto/rand"
	"crypto/rsa"
	"crypto/x509"
	"encoding/pem"
	"os"
)

//func main() {
//	//rsa 密钥文件产生
//	fmt.Println(GeneratePrivateKey(1024))
//}

//函数返回一个string类型的公钥
func GeneratePrivateKey(bits int) (string,string,error) {
	// 生成私钥文件
	privateKey, err := rsa.GenerateKey(rand.Reader, bits)
	if err != nil {
		return "error","error",err
	}
	derStream := x509.MarshalPKCS1PrivateKey(privateKey)
	privateBlock := &pem.Block{
		Type:  "RSA PRIVATE KEY",
		Bytes: derStream,
	}

	//fmt.Printf("=======私钥文件内容=========%v", string(pem.EncodeToMemory(block)))
	file, err := os.Create("private.pem")
	if err != nil {
		return "error","error",err
	}
	err = pem.Encode(file, privateBlock)
	if err != nil {
		return "error","error",err
	}
	// 生成公钥文件
	publicKey := &privateKey.PublicKey
	derPkix, err := x509.MarshalPKIXPublicKey(publicKey)
	if err != nil {
		return "error","error",err
	}
	publicBlock := &pem.Block{
		Type:  "PUBLIC KEY",
		Bytes: derPkix,
	}
	//fmt.Printf("=======公钥文件内容=========%v", string(pem.EncodeToMemory(block)))
	file, err = os.Create("public.pem")
	if err != nil {
		return "error","error",err
	}
	err = pem.Encode(file, publicBlock)
	if err != nil {
		return "error","error",err
	}
	return string(pem.EncodeToMemory(privateBlock)),string(pem.EncodeToMemory(publicBlock)),nil
}
