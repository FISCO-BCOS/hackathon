package biz

import (
	"archive/zip"
	"bytes"
	"context"
	"crypto/sha256"
	"fmt"
	"io"
	"log"
	"math/rand"
	"math/big"
	"mime/multipart"
	"os"
	"time"

	"github.com/ZhenTaoWei/bcec/config"
	"github.com/ZhenTaoWei/bcec/model"
	"github.com/ZhenTaoWei/bcec/utils"
	"github.com/minio/minio-go/v7"
	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/ZhenTaoWei/bcec/modelregistry"
	"github.com/ZhenTaoWei/bcec/trade"
	"github.com/ethereum/go-ethereum/common"
)

type SearchRes struct {
	IDRes  string `form:"idres" json:"idres"`
	CosRes string `form:"cosres" json:"cosres"`
}

func Upload(username, modelparam string) error {
	// 上传区块链
	return nil
}

func Transaction(username, modelparam string) error {
	// 交易
	return nil
}

func Verify(username, modelparam string) (bool, error) {
	// 验证
	return false, nil
}

func Search(modelType string, modelName string) ([]SearchRes, error) {
	idres, cosres, err := model.GetModelsIDAndCosByTypeAndName(modelType, modelName)
	res := []SearchRes{}
	for i := range idres {
		res = append(res, SearchRes{IDRes: idres[i], CosRes: cosres[i]})
	}
	return res, err
}

func ModelUpload(files *multipart.Form, modelType, modelName string) (string, error) {
	buf := new(bytes.Buffer)
	zipWriter := zip.NewWriter(buf)
	mID := GenerateRandomString(16, "abcdef0123456789")
	zipFileName := mID + ".zip"

	// 创建一个新的 zip 文件
	zipFile, err := os.Create(zipFileName)
	if err != nil {
		return "", err
	}
	// 遍历上传的文件并将它们写入 ZIP 文件
	for _, file := range files.File["files"] {
		f, err := file.Open()
		if err != nil {
			return "", err
		}
		defer f.Close()

		// 为每个上传的文件创建一个 ZIP 条目，使用上传的文件名
		zipFileWriter, err := zipWriter.Create(file.Filename)
		if err != nil {
			return "", err
		}

		// 将文件内容写入 ZIP 条目
		_, err = io.Copy(zipFileWriter, f)
		if err != nil {
			return "", err
		}
	}

	// 关闭 ZIP 写入器
	err = zipWriter.Close()
	if err != nil {
		return "", err
	}

	// 上传压缩包到 MinIO
	_, err = utils.MinIOClient.PutObject(context.Background(), "zhiyilian", zipFileName, buf, -1, minio.PutObjectOptions{})

	if err != nil {
		log.Println("Error uploading file to MinIO:", err)
		return "", err
	}
	zipFile.Close()
	// 求报告压缩包哈希
	hash := sha256.New()
	if _, err := io.Copy(hash, buf); err != nil {
    	log.Println("Error calculating hash:", err)
    	return "", err
	}
	hashBytes := hash.Sum(nil)
	hashString := fmt.Sprintf("%x", hashBytes)
	// 删除临时ZIP文件
	err = os.Remove(zipFileName)
	if err != nil {
		log.Fatal(err)
	}
	cos := "https://" + config.Config.Minio.Endpoint + "/zhiyilian/" + zipFileName
	err = model.Insert("tbl_models", map[string]interface{}{
		"modelID":       mID,
		"modeltype":     modelType,
		"modeltypename": modelName,
		"modelcos":      cos,
	})
	if err != nil {
		return "", err
	}
	// 上链
	configs, err := conf.ParseConfigFile("config.toml")
	if err != nil {
		log.Fatal(err)
	}
	config := &configs[0]
	defaultclient, err := client.Dial(config)
	if err != nil {
		log.Fatal(err)
	}
	contractAddress := common.HexToAddress("0x860584823282F5CcD1375203E720b655b0d3f981")
	instance, err := modelregistry.NewModelRegistry(contractAddress, defaultclient)
	if err != nil {
		log.Fatal(err)
	}
	modelRegistrySession := &modelregistry.ModelRegistrySession{Contract: instance, CallOpts: *defaultclient.GetCallOpts(), TransactOpts: *defaultclient.GetTransactOpts()}
	proposalOwner := "0x83309d045a19c44dc3722d15a6abd472f95866ac"
	tx, receipt, err := modelRegistrySession.CreateProposal(mID, "atestmodelhashstring", hashString, common.HexToAddress(proposalOwner))
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
	fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
	// 评估机构投票上链
	evaluatorPrivateKeyPaths := []string{
		".ci/0x1fe1129f1cef820beb94d40101360df0da6a3879.pem",
		".ci/0xaf816e9bc6f805e10405c0eecc260179d631d3c7.pem",
		".ci/0xbec5fea6a971423e3e27cda3417307b765bf7aab.pem",
	}

	// 切换账户，查询提案，进行投票
	for _, path := range evaluatorPrivateKeyPaths {
		evaluatorKey, _, err := conf.LoadECPrivateKeyFromPEM(path)
		if err != nil {
			log.Fatal(err)
		}
		config.PrivateKey = evaluatorKey

		evaluatorClient, err := client.Dial(config)
		if err != nil {
			log.Fatal(err)
		}
		evaluatorSession := &modelregistry.ModelRegistrySession{
			Contract: instance,
			CallOpts: *evaluatorClient.GetCallOpts(),
			TransactOpts: *evaluatorClient.GetTransactOpts(),
		}

		// 进行投票
		tx, receipt, err = evaluatorSession.VoteForProposal(mID)
		if err != nil {
			log.Fatal(err)
		}
		fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
		fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
		fmt.Printf("Voted with evaluator %s\n", evaluatorSession.CallOpts.From.Hex())
		model, err := evaluatorSession.GetModel(mID)
		if err != nil {
			fmt.Printf("Model not passing vote yet.\n")
			continue
		}
		fmt.Printf("Model %s is now on chain with hash: %s, report hash: %s, owner: %s\n", mID, model.ModelHash, model.ReportHash, model.Owner.Hex())
	}
	return mID, nil
}

func GenerateRandomString(length int, charset string) string {
	seed := rand.NewSource(time.Now().UnixNano())
	random := rand.New(seed)
	result := make([]byte, length)

	for i := range result {
		result[i] = charset[random.Intn(len(charset))]
	}
	return string(result)
}

func BuySubmit(money, modelID string) (string, error) {
	tranID := GenerateRandomString(20, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")
	err := model.Insert("tbl_trans", map[string]interface{}{
		"tranID":  tranID,
		"money":   money,
		"modelID": modelID,
	})
	configs, err := conf.ParseConfigFile("config.toml")
	if err != nil {
		log.Fatal(err)
	}
	config := &configs[0]	
    contractAddress := common.HexToAddress("0x493Cb81dAeE9d739a33235cC1F9bd37C2280550A")
	buyerpath := ".ci/0xc89979c91f21f1b01df59440e954533d6f7978aa.pem"
	buyerKey, _, err := conf.LoadECPrivateKeyFromPEM(buyerpath)
	if err != nil {
		log.Fatal(err)
	}
	config.PrivateKey = buyerKey
	buyerClient, err := client.Dial(config)
	if err != nil {
		log.Fatal(err)
	}
	buyerinstance, err := trade.NewModelTransaction(contractAddress, buyerClient)
    if err != nil {
        log.Fatal(err)
    }
	buyerSession := &trade.ModelTransactionSession{
		Contract: buyerinstance,
		CallOpts: *buyerClient.GetCallOpts(),
		TransactOpts: *buyerClient.GetTransactOpts(),
	}
	seller := common.HexToAddress("0x83309d045a19c44dc3722d15a6abd472f95866ac")
	buyer := common.HexToAddress("0xc89979c91f21f1b01df59440e954533d6f7978aa")
	amount := new(big.Int)
	if amount, ok := amount.SetString(money, 10); !ok {
		fmt.Println("转换失败")
	} else {
		fmt.Println(amount)
	}
	tx, receipt, err := buyerSession.CreateTransaction(modelID, seller, buyer, amount)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
	fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
	fmt.Printf("Trade Created by buyer %s\n", buyerSession.CallOpts.From.Hex())
	
	tx, receipt, err = buyerSession.VoteForTransaction(modelID, big.NewInt(0))
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
	fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
	fmt.Printf("Voted with buyer %s\n", buyerSession.CallOpts.From.Hex())
	return tranID, err
}

func SoldSubmit(tranID string, file *multipart.FileHeader) error {
	res, err := model.Search("tbl_trans", map[string]interface{}{
		"tranID": tranID,
	})
	if err != nil {
		return err
	}
	if len(res) == 0 {
		return fmt.Errorf("未查询到交易")
	}
	fileName := GenerateRandomString(32, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789") + ".pdf"
	openedFile, err := file.Open()
	if err != nil {
		return err
	}

	_, err = utils.MinIOClient.PutObject(context.Background(), "zhiyilian", fileName, openedFile, -1, minio.PutObjectOptions{})

	if err != nil {
		log.Println("Error uploading file to MinIO:", err)
		return err
	}
	openedFile.Close()
	cosurl := "https://" + config.Config.Minio.Endpoint + "/zhiyilian/" + fileName
	h, err := calculateFileHash(openedFile)
	if err != nil {
		return err
	}
	fmt.Println(h)
	err = model.Update("tbl_trans", map[string]interface{}{
		"cosurl": cosurl,
	}, map[string]interface{}{
		"tranID": tranID,
	})
	if err != nil {
		return err
	}
	// 上链
	configs, err := conf.ParseConfigFile("config.toml")
	if err != nil {
		log.Fatal(err)
		return err
	}
	config := &configs[0]	
    contractAddress := common.HexToAddress("0x493Cb81dAeE9d739a33235cC1F9bd37C2280550A")
	sellerpath := ".ci/0x83309d045a19c44dc3722d15a6abd472f95866ac.pem"
	sellerKey, _, err := conf.LoadECPrivateKeyFromPEM(sellerpath)
	if err != nil {
		log.Fatal(err)
		return err
	}
	config.PrivateKey = sellerKey
	sellerClient, err := client.Dial(config)
	if err != nil {
		log.Fatal(err)
		return err
	}
	sellerinstance, err := trade.NewModelTransaction(contractAddress, sellerClient)
    if err != nil {
        log.Fatal(err)
		return err
    }
	sellerSession := &trade.ModelTransactionSession{
		Contract: sellerinstance,
		CallOpts: *sellerClient.GetCallOpts(),
		TransactOpts: *sellerClient.GetTransactOpts(),
	}
	res, err = model.Search("tbl_trans", map[string]interface{}{
		"tranID": tranID,
	})
	if err != nil {
		log.Fatal(err)
		return err
	}
	modelID := string(res[0]["modelID"].([]byte))
	tx, receipt, err := sellerSession.VoteForTransaction(modelID, big.NewInt(0))
	if err != nil {
		log.Fatal(err)
		return err
	}
	fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
	fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
	fmt.Printf("Voted with seller %s\n", sellerSession.CallOpts.From.Hex())
	certainTx, err := sellerSession.GetTransaction(modelID, big.NewInt(0))
	fmt.Printf("Final test:\n")
	fmt.Printf("Seller: %s\n", certainTx.Seller.Hex())
	fmt.Printf("Buyer: %s\n", certainTx.Buyer.Hex())
	fmt.Printf("Amount: %d\n", certainTx.Amount.Int64())
	fmt.Printf("Timestamp: %s\n", certainTx.Timestamp.String())
	fmt.Printf("Completion: %v\n", certainTx.IsCompleted)
	return err
}

// 计算文件的 SHA256 哈希值
func calculateFileHash(file io.Reader) (string, error) {
	hash := sha256.New()
	_, err := io.Copy(hash, file) // 将文件内容传递给哈希函数
	if err != nil {
		return "", err
	}
	return fmt.Sprintf("%x", hash.Sum(nil)), nil
}
