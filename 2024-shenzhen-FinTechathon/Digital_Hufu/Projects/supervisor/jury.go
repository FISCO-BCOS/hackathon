package supervisor

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"hufu/config"
	"hufu/contract/Decision"
	"hufu/contract/KeyShare"
	"hufu/utils"
	"io"
	"log"
	"math/big"
	"net/http"
	"strconv"
	"strings"

	"github.com/FISCO-BCOS/go-sdk/v3/client"
	"github.com/ethereum/go-ethereum/accounts/abi"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/common/hexutil"
)

type Jury struct {
	Nodes            []*Node
	KeyShareContract string
	DecisionContract string
}

type Node struct {
	NodeID     string
	PublicKey  string
	PrivateKey string
}

var JuryInstance *Jury

func InitJury() {
	// keys
	juryPrivateKeys, juryPublicKeys := config.ReadJuryKeys()
	nodes := make([]*Node, 5)
	for i := 0; i < 5; i++ {
		nodes[i] = &Node{
			NodeID:     fmt.Sprintf("node%d", i),
			PublicKey:  juryPublicKeys[i],
			PrivateKey: juryPrivateKeys[i],
		}
	}
	JuryInstance = &Jury{
		Nodes:            nodes,
		KeyShareContract: config.GlobalConfig.Contract.KeyShareContract,
		DecisionContract: config.GlobalConfig.Contract.DecisionContract,
	}
	log.Println(JuryInstance.KeyShareContract)
	log.Println(JuryInstance.DecisionContract)
}

// 处理监管机构的请求
func (j *Jury) HandleRegulatoryRequest(evidence string) (map[string]bool, error) {
	// 存储同意提供密钥的节点的密钥碎片
	res := make(map[string]bool)
	// 每个节点独立判断证据是否合法
	for _, node := range j.Nodes {
		approved, err := node.EvaluateEvidence(evidence)
		res[node.NodeID] = approved
		if err != nil {
			log.Printf("Node evaluation failed: %v", err)
			continue
		}

		if approved {
			// 记录操作到区块链
			if err := node.RecordDecision(evidence); err != nil {
				log.Printf("Failed to record decision: %v", err)
				continue
			}
		}
	}
	return res, nil
}

// 节点评估证据
func (n *Node) EvaluateEvidence(evidence string) (bool, error) {
	config := config.ReadConfig(n.PrivateKey)
	client, err := client.DialContext(context.Background(), config)
	if err != nil {
		return false, err
	}

	contractAddr := common.HexToAddress(JuryInstance.DecisionContract)
	instance, err := Decision.NewDecisionStorage(contractAddr, client)
	if err != nil {
		return false, err
	}

	session := &Decision.DecisionStorageSession{
		Contract:     instance,
		CallOpts:     *client.GetCallOpts(),
		TransactOpts: *client.GetTransactOpts(),
	}

	// 调用智能合约验证证据
	approved, err := session.VerifyEvidence(evidence)
	if err != nil {
		return false, err
	}

	return approved, nil
}

// 记录决定到区块链
func (n *Node) RecordDecision(evidence string) error {
	config := config.ReadConfig(n.PrivateKey)
	client, err := client.DialContext(context.Background(), config)
	if err != nil {
		return err
	}

	contractAddr := common.HexToAddress(JuryInstance.DecisionContract)
	instance, err := Decision.NewDecisionStorage(contractAddr, client)
	if err != nil {
		return err
	}

	session := &Decision.DecisionStorageSession{
		Contract:     instance,
		CallOpts:     *client.GetCallOpts(),
		TransactOpts: *client.GetTransactOpts(),
	}

	_, _, _, err = session.RecordDecision(evidence, n.NodeID, true)
	if err != nil {
		return err
	}

	// log.Println(receipt)

	return nil
}

// 存储加密后的密钥碎片
func (n *Node) StoreEncryptedKeyShare(id, name, encryptedPart string) error {
	// 初始化客户端连接
	config := config.ReadConfig(n.PrivateKey)
	client, err := client.DialContext(context.Background(), config)
	if err != nil {
		return fmt.Errorf("failed to connect to client: %v", err)
	}

	// 获取KeyShare合约实例
	contractAddr := common.HexToAddress(JuryInstance.KeyShareContract)
	instance, err := KeyShare.NewKeyShare(contractAddr, client)
	if err != nil {
		return fmt.Errorf("failed to instantiate contract: %v", err)
	}

	// 创建合约会话
	session := &KeyShare.KeyShareSession{
		Contract:     instance,
		CallOpts:     *client.GetCallOpts(),
		TransactOpts: *client.GetTransactOpts(),
	}

	// 调用合约存储密钥碎片
	_, _, _, err = session.Insert(id, name, encryptedPart)
	return err
}

// 获取密钥碎片
func (n *Node) GetDecryptedKeyShare(id string) (string, error) {
	config := config.ReadConfig(n.PrivateKey)
	client, err := client.DialContext(context.Background(), config)
	if err != nil {
		return "", err
	}

	contractAddr := common.HexToAddress(JuryInstance.KeyShareContract)
	instance, err := KeyShare.NewKeyShare(contractAddr, client)
	if err != nil {
		return "", err
	}

	session := &KeyShare.KeyShareSession{
		Contract: instance,
		CallOpts: *client.GetCallOpts(),
	}

	_, encryptedPart, err := session.Select(id)
	if err != nil {
		return "", err
	}

	decryptedPart, err := utils.DecryptData(encryptedPart, n.PrivateKey)
	if err != nil {
		return "", err
	}

	return decryptedPart, nil
}

// 获取决策
func GetDecision() ([]string, error) {
	// 获取决策合约实例
	config := config.ReadConfig(JuryInstance.Nodes[0].PrivateKey)
	client, err := client.DialContext(context.Background(), config)
	if err != nil {
		return nil, err
	}

	contractAddr := common.HexToAddress(JuryInstance.DecisionContract)
	instance, err := Decision.NewDecisionStorage(contractAddr, client)
	if err != nil {
		return nil, err
	}

	session := &Decision.DecisionStorageSession{
		Contract:     instance,
		CallOpts:     *client.GetCallOpts(),
		TransactOpts: *client.GetTransactOpts(),
	}

	// 调用合约获取决策
	count, err := session.GetDecisionCount()
	if err != nil {
		return nil, err
	}
	res := make([]string, count.Int64())
	for i := int64(0); i < count.Int64(); i++ {
		decision, err := session.GetDecision(big.NewInt(i))
		if err != nil {
			return nil, err
		}
		res[i] = decision.Evidence + "," + decision.NodeID + "," + decision.Timestamp.String() + "," + strconv.FormatBool(decision.Approved)
	}
	return res, nil
}

// 添加这些结构体用于解析返回数据
type EventResponse struct {
	Code       int         `json:"code"`
	Message    string      `json:"message"`
	Data       []EventData `json:"data"`
	TotalCount int         `json:"totalCount"`
}

type EventData struct {
	Log EventLog `json:"log"`
}

type EventLog struct {
	LogIndex         int      `json:"logIndex"`
	TransactionIndex int      `json:"transactionIndex"`
	TransactionHash  string   `json:"transactionHash"`
	BlockNumber      int      `json:"blockNumber"`
	Address          string   `json:"address"`
	Data             string   `json:"data"`
	Topics           []string `json:"topics"`
}

func GetEvent() (EventResponse, error) {
	url := "http://localhost:5000/mgr/WeBASE-Node-Manager/event/eventLogs/list"

	var requestBody = `{
		"groupId": "group0",
		"contractAbi": [
		  {
			"anonymous": false,
			"inputs": [
			  {
				"indexed": false,
				"internalType": "string",
				"name": "evidence",
				"type": "string"
			  },
			  {
				"indexed": false,
				"internalType": "string",
				"name": "nodeID",
				"type": "string"
			  },
			  {
				"indexed": false,
				"internalType": "uint256",
				"name": "timestamp",
				"type": "uint256"
			  },
			  {
				"indexed": false,
				"internalType": "bool",
				"name": "approved",
				"type": "bool"
			  }
			],
			"name": "DecisionRecorded",
			"type": "event"
		  },
		  {
			"inputs": [
			  {
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			  }
			],
			"name": "decisions",
			"outputs": [
			  {
				"internalType": "string",
				"name": "evidence",
				"type": "string"
			  },
			  {
				"internalType": "string",
				"name": "nodeID",
				"type": "string"
			  },
			  {
				"internalType": "uint256",
				"name": "timestamp",
				"type": "uint256"
			  },
			  {
				"internalType": "bool",
				"name": "approved",
				"type": "bool"
			  }
			],
			"stateMutability": "view",
			"type": "function"
		  },
		  {
			"inputs": [
			  {
				"internalType": "uint256",
				"name": "index",
				"type": "uint256"
			  }
			],
			"name": "getDecision",
			"outputs": [
			  {
				"internalType": "string",
				"name": "evidence",
				"type": "string"
			  },
			  {
				"internalType": "string",
				"name": "nodeID",
				"type": "string"
			  },
			  {
				"internalType": "uint256",
				"name": "timestamp",
				"type": "uint256"
			  },
			  {
				"internalType": "bool",
				"name": "approved",
				"type": "bool"
			  }
			],
			"stateMutability": "view",
			"type": "function"
		  },
		  {
			"inputs": [],
			"name": "getDecisionCount",
			"outputs": [
			  {
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			  }
			],
			"stateMutability": "view",
			"type": "function"
		  },
		  {
			"inputs": [
			  {
				"internalType": "string",
				"name": "evidence",
				"type": "string"
			  },
			  {
				"internalType": "string",
				"name": "nodeID",
				"type": "string"
			  },
			  {
				"internalType": "bool",
				"name": "approved",
				"type": "bool"
			  }
			],
			"name": "recordDecision",
			"outputs": [
			  {
				"internalType": "bool",
				"name": "",
				"type": "bool"
			  }
			],
			"stateMutability": "nonpayable",
			"type": "function"
		  },
		  {
			"inputs": [
			  {
				"internalType": "string",
				"name": "evidence",
				"type": "string"
			  }
			],
			"name": "verifyEvidence",
			"outputs": [
			  {
				"internalType": "bool",
				"name": "",
				"type": "bool"
			  }
			],
			"stateMutability": "view",
			"type": "function"
		  }
		],
		"contractAddress": "0x4721d1a77e0e76851d460073e64ea06d9c104194",
		"fromBlock": 1,
		"toBlock": -1,
		"topics": {
		  "eventName": "DecisionRecorded(string,string,uint256,bool)"
		}
	  }`

	var jsonStr = []byte(requestBody)
	// 创建请求
	req, err := http.NewRequest("POST", url, bytes.NewBuffer(jsonStr))
	if err != nil {
		fmt.Println("Error creating request:", err)
		return EventResponse{}, err
	}

	// 设置请求头
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorizationtoken", config.GlobalConfig.Fisco.Authorizationtoken)
	req.Header.Set("referer", "http://45.8.113.140:5000/")
	req.Header.Set("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")

	// 发送请求
	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		fmt.Println("Error sending request:", err)
		return EventResponse{}, err
	}
	defer resp.Body.Close()

	// 输出响应
	fmt.Println("Response status:", resp.Status)

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		fmt.Println("Error reading response body:", err)
		return EventResponse{}, err
	}

	var response EventResponse
	if err := json.Unmarshal(body, &response); err != nil {
		fmt.Printf("Error parsing response: %v\n", err)
		return EventResponse{}, err
	}

	for i := 0; i < response.TotalCount; i++ {
		data, _ := ParaseData(response.Data[i].Log.Data)
		response.Data[i].Log.Data = data
	}

	if response.Code != 0 {
		fmt.Printf("API returned error: %s\n", response.Message)
		return EventResponse{}, fmt.Errorf(response.Message)
	}

	return response, nil
}

func ParaseData(data string) (string, error) {
	const abiJSON = `[{"anonymous":false,"inputs":[{"indexed":false,"internalType":"string","name":"evidence","type":"string"},{"indexed":false,"internalType":"string","name":"nodeID","type":"string"},{"indexed":false,"internalType":"uint256","name":"timestamp","type":"uint256"},{"indexed":false,"internalType":"bool","name":"approved","type":"bool"}],"name":"DecisionRecorded","type":"event"},{"inputs":[{"internalType":"uint256","name":"","type":"uint256"}],"name":"decisions","outputs":[{"internalType":"string","name":"evidence","type":"string"},{"internalType":"string","name":"nodeID","type":"string"},{"internalType":"uint256","name":"timestamp","type":"uint256"},{"internalType":"bool","name":"approved","type":"bool"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"uint256","name":"index","type":"uint256"}],"name":"getDecision","outputs":[{"internalType":"string","name":"evidence","type":"string"},{"internalType":"string","name":"nodeID","type":"string"},{"internalType":"uint256","name":"timestamp","type":"uint256"},{"internalType":"bool","name":"approved","type":"bool"}],"stateMutability":"view","type":"function"},{"inputs":[],"name":"getDecisionCount","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"string","name":"evidence","type":"string"},{"internalType":"string","name":"nodeID","type":"string"},{"internalType":"bool","name":"approved","type":"bool"}],"name":"recordDecision","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"string","name":"evidence","type":"string"}],"name":"verifyEvidence","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"view","type":"function"}]`

	parsedABI, err := abi.JSON(strings.NewReader(abiJSON))
	if err != nil {
		log.Fatalf("Failed to parse ABI: %v", err)
	}

	dataBytes, err := hexutil.Decode(data)
	if err != nil {
		log.Fatalf("Failed to decode data: %v", err)
	}

	// 解码数据
	var Evidence string
	var NodeID string
	var Timestamp *big.Int
	var Approved bool
	err = parsedABI.UnpackIntoInterface(&[]interface{}{&Evidence, &NodeID, &Timestamp, &Approved}, "DecisionRecorded", dataBytes)
	if err != nil {
		log.Fatalf("Failed to unpack data: %v", err)
	}

	res := []string{Evidence, NodeID, Timestamp.String(), strconv.FormatBool(Approved)}

	return strings.Join(res, ","), nil
}
