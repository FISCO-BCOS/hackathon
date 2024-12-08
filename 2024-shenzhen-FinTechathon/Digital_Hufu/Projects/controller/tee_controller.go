package controller

import (
	"bytes"
	"crypto/rand"
	"crypto/rsa"
	"crypto/sha256"
	"encoding/binary"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"math/big"
	"net/http"
	"strconv"
	"strings"

	"golang.org/x/net/proxy"
)

type TeeController struct{}

func NewTeeController() *TeeController {
	return &TeeController{}
}

// 创建代理客户端的通用函数
func (tc *TeeController) createProxyClient() (*http.Client, error) {
	dialer, err := proxy.SOCKS5("tcp", "127.0.0.1:1080", nil, proxy.Direct)
	if err != nil {
		return nil, err
	}

	return &http.Client{
		Transport: &http.Transport{
			Dial: dialer.Dial,
		},
	}, nil
}

// 发送HTTP请求的通用函数
func (tc *TeeController) sendRequest(client *http.Client, method, url string, body io.Reader, contentType string) ([]byte, int, error) {
	req, err := http.NewRequest(method, url, body)
	if err != nil {
		return nil, http.StatusInternalServerError, err
	}

	req.Header.Set("Content-Type", contentType)

	resp, err := client.Do(req)
	if err != nil {
		return nil, http.StatusInternalServerError, err
	}
	defer resp.Body.Close()

	respBody, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, http.StatusInternalServerError, err
	}

	return respBody, resp.StatusCode, nil
}

func (tc *TeeController) Add(add string) ([]byte, int, error) {
	client, err := tc.createProxyClient()
	if err != nil {
		return nil, http.StatusInternalServerError, err
	}

	return tc.sendRequest(
		client,
		"POST",
		"http://10.77.110.184:8080/add",
		strings.NewReader(add),
		"text/plain",
	)
}

func (tc *TeeController) GenerateKey(walletID int) ([]byte, int, error) {
	client, err := tc.createProxyClient()
	if err != nil {
		return nil, http.StatusInternalServerError, err
	}

	request := struct {
		WalletID int `json:"wallet_id"`
	}{
		WalletID: walletID,
	}

	jsonBody, _ := json.Marshal(request)
	return tc.sendRequest(
		client,
		"POST",
		"http://10.77.110.184:8082/api/create_wallet",
		bytes.NewBuffer(jsonBody),
		"application/json",
	)
}

func (tc *TeeController) Warning(from, to int, amount float64) ([]byte, int, error) {
	client, err := tc.createProxyClient()
	if err != nil {
		return nil, http.StatusInternalServerError, err
	}

	request := struct {
		From   int     `json:"from"`
		To     int     `json:"to"`
		Amount float64 `json:"amount"`
	}{
		From:   from,
		To:     to,
		Amount: amount,
	}

	jsonBody, _ := json.Marshal(request)
	return tc.sendRequest(
		client,
		"POST",
		"http://10.77.110.184:8082/api/transaction_warning",
		bytes.NewBuffer(jsonBody),
		"application/json",
	)
}

func (tc *TeeController) Shuffle(from, to int, amount float64) ([]byte, int, error) {
	client, err := tc.createProxyClient()
	if err != nil {
		return nil, http.StatusInternalServerError, err
	}

	request := struct {
		From   int     `json:"from"`
		To     int     `json:"to"`
		Amount float64 `json:"amount"`
	}{
		From:   from,
		To:     to,
		Amount: amount,
	}

	jsonBody, _ := json.Marshal(request)
	return tc.sendRequest(
		client,
		"POST",
		"http://10.77.110.184:8082/api/shuffle_transaction",
		bytes.NewBuffer(jsonBody),
		"application/json",
	)
}

func (tc *TeeController) Decrypt(from, to, amount string) ([]byte, int, error) {
	client, err := tc.createProxyClient()
	if err != nil {
		return nil, http.StatusInternalServerError, err
	}

	request := struct {
		From   string `json:"from"`
		To     string `json:"to"`
		Amount string `json:"amount"`
	}{
		From:   from,
		To:     to,
		Amount: amount,
	}

	jsonBody, _ := json.Marshal(request)
	return tc.sendRequest(
		client,
		"POST",
		"http://10.77.110.184:8082/api/decrypt_transaction",
		bytes.NewBuffer(jsonBody),
		"application/json",
	)
}

func (tc *TeeController) Encrypt(from, to string, amount string) (map[string]string, error) {
	fromInt, err := strconv.Atoi(from)
	if err != nil {
		return nil, fmt.Errorf("无效的 from: %v", err)
	}
	toInt, err := strconv.Atoi(to)
	if err != nil {
		return nil, fmt.Errorf("无效的 to: %v", err)
	}
	amountFloat, err := strconv.ParseFloat(amount, 64)
	if err != nil {
		return nil, fmt.Errorf("无效的 amount: %v", err)
	}

	dataLittleEndian := map[string][]byte{
		"from":   tc.intToBytesLittleEndian(fromInt),
		"to":     tc.intToBytesLittleEndian(toInt),
		"amount": tc.floatToBytesLittleEndian(amountFloat),
	}

	nHex := "922e9ebb9ede6554aadb495da961f38775c226259adf3bd18e00ddc5c099c40d8a714be8f6dbdcac72e1adb21675966a2f541c8f50a0a1ea3cf3d429134b699ddce0b675ee87ac1bc4adae441b0f00ae4fddd0e7b59b058a52357e37a65963f943f2706c678900e7bbb68d6b3eade4ad8ee2c73f3da5244e543f6a6082f2eb31a8c13200945c56980829d0ab9f00261655fc86fd25269f6748dc85b6d7debb03d00c392e04aafef09b41b87d0f1737f66a739d83717d7ef0c3040e10b9202c06b63bd5733626035f657de8dc3e581e237d5b3bbdaa46bde47a06883cf37166b328958217dbccc20c3807099b6a6c8b2dbdf94281a8ff0d29a4053a85572906ede8e141ae3ced4719e8f7fefcd9057435b1aa0e5e84e7a31cf126a84e00d8be983c3939704011174a1f6c2a6ad0808a17eadbc312570a69ee662631a0441bd5eaa96bb9693587cb351015ba1eed3cecf872243ecd822764128ebb338e03a883df35912e75aafb39052d2ecddc2df1270561f7624f7b7aef3dae05a64a7385e99d"
	e := 65537

	n, err := tc.hexToBytes(nHex)
	if err != nil {
		return nil, fmt.Errorf("Error converting hex to bytes: %v", err)
	}

	publicKey := &rsa.PublicKey{
		N: new(big.Int).SetBytes(n),
		E: e,
	}

	encryptedData := make(map[string]string)
	for key, value := range dataLittleEndian {
		encrypted, err := tc.rsaEncrypt(publicKey, value)
		if err != nil {
			return nil, fmt.Errorf("Error encrypting data: %v", err)
		}
		encryptedData[key] = encrypted
	}

	return encryptedData, nil
}

// 辅助方法
func (tc *TeeController) hexToBytes(hexStr string) ([]byte, error) {
	return hex.DecodeString(hexStr)
}

func (tc *TeeController) rsaEncrypt(publicKey *rsa.PublicKey, plaintext []byte) (string, error) {
	cipherText, err := rsa.EncryptOAEP(sha256.New(), rand.Reader, publicKey, plaintext, nil)
	if err != nil {
		return "", err
	}
	return hex.EncodeToString(cipherText), nil
}

func (tc *TeeController) intToBytesLittleEndian(n int) []byte {
	buf := new(bytes.Buffer)
	err := binary.Write(buf, binary.LittleEndian, int32(n))
	if err != nil {
		log.Fatal(err)
	}
	return buf.Bytes()
}

func (tc *TeeController) floatToBytesLittleEndian(f float64) []byte {
	buf := new(bytes.Buffer)
	err := binary.Write(buf, binary.LittleEndian, f)
	if err != nil {
		log.Fatal(err)
	}
	return buf.Bytes()
}

func (tc *TeeController) GetEncryptedTransaction(id string) ([]byte, int, error) {
	client, err := tc.createProxyClient()
	if err != nil {
		return nil, http.StatusInternalServerError, err
	}

	return tc.sendRequest(
		client,
		"POST",
		"http://10.77.110.184:38080/getfile",
		bytes.NewBuffer([]byte(fmt.Sprintf(`{"id": "%s"}`, id))),
		"application/json",
	)
}
