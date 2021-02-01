package rsa

import (
	"crypto"
	"crypto/rand"
	"crypto/rsa"
	"crypto/sha256"
	"crypto/x509"
	"encoding/hex"
	"encoding/pem"
	"fmt"
	"os"
)

func main()  {
	fp, _ := os.Open("吴志辉_专利申请受理通知书.pdf")
	defer fp.Close()
	fileinfo, _ := fp.Stat()
	buf := make([]byte, fileinfo.Size())
	fp.Read(buf)

	private,public,err := GeneratePrivateKey(512)
	if err != nil{
		fmt.Println("error")
	}

	fmt.Println("publicKey len is :"+string(len(public)))

	sign := Signname(private,buf)
	fmt.Println(sign)
	encodedStr := hex.EncodeToString(sign)
	fmt.Println(encodedStr)

	test, _ := hex.DecodeString(encodedStr)
	fmt.Println(test)
	//cc := []byte("cccccc")

	code := VerifySign(public,[]byte(test),buf)
	fmt.Println(code)
}

//使用私钥签名，path是私钥路径，msg是要签名的信息
//返回签名后的信息
func Signname(privateKey string, msg []byte) []byte {
	////签名函数中需要的数据散列值
	////首先从文件中提取公钥
	//fp, _ := os.Open(path)
	//defer fp.Close()
	//fileinfo, _ := fp.Stat()
	//buf := make([]byte, fileinfo.Size())
	//fp.Read(buf)
	buf := []byte(privateKey)
	block, _ := pem.Decode(buf)
	PrivateKey, _ := x509.ParsePKCS1PrivateKey(block.Bytes)
	//加密操作,需要将接口类型的pub进行类型断言得到公钥类型
	hash := sha256.Sum256(msg)
	//调用签名函数,得到签名
	//即，对数据的哈希摘要进行私钥签名
	sign, _ := rsa.SignPKCS1v15(rand.Reader, PrivateKey, crypto.SHA256, hash[:])
	fmt.Printf("sign:%x\n", sign)
	return sign

}

//publicKey是公钥，signText是已经签名过的数据，plainText是原始数据
func VerifySign(publicKey string, signText []byte, plainText []byte) string {
	////首先从文件中提取公钥
	//fp, _ := os.Open(path)
	//defer fp.Close()
	////测量文件长度以便于保存
	//fileinfo, _ := fp.Stat()
	//buf := make([]byte, fileinfo.Size())
	//fp.Read(buf)
	//下面的操作是与创建秘钥保存时相反的
	//pem解码
	buf := []byte(publicKey)
	block, _ := pem.Decode(buf)
	//x509解码,得到一个interface类型的pub
	pub, _ := x509.ParsePKIXPublicKey(block.Bytes)
	//签名函数中需要的数据散列值
	hash := sha256.Sum256(plainText)
	//验证签名
	err := rsa.VerifyPKCS1v15(pub.(*rsa.PublicKey), crypto.SHA256, hash[:], signText)
	if err != nil {
		return "认证失败"
	} else {
		return "认证成功"
	}

}
