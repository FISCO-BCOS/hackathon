package consensus

import (
	"context"
	"encoding/hex"
	"os"
	"regexp"
	"testing"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
)

const (
	standardOutput = 1
)

var (
	nodeID  = ""
	service *Service
)

func getClient(t *testing.T) *client.Client {
	privateKey, _ := hex.DecodeString("b89d42f12290070f235fb8fb61dcf96e3b11516c5d4f6333f26e49bb955f8b62")
	config := &conf.Config{IsHTTP: true, ChainID: 1, IsSMCrypto: false, GroupID: 1,
		PrivateKey: privateKey, NodeURL: "http://localhost:8545"}
	c, err := client.Dial(config)
	if err != nil {
		t.Fatalf("Dial to %s failed of %v", config.NodeURL, err)
	}
	return c
}

func getService(t *testing.T) {
	c := getClient(t)
	newService, err := NewConsensusService(c)
	if err != nil {
		t.Fatalf("init ConsensusService failed: %+v", err)
	}
	service = newService
}

func getNodeID(t *testing.T) {
	c := getClient(t)
	sealerList, err := c.GetNodeIDList(context.Background())
	if err != nil {
		t.Fatalf("sealer list not found: %v", err)
	}
	reg := regexp.MustCompile(`[\w]+`)
	nodeList := reg.FindAllString(string(sealerList), -1)
	if len(nodeList) < 2 {
		t.Fatalf("the number of nodes does not exceed 2")
	}
	nodeID = nodeList[1]
}

func TestMain(m *testing.M) {
	getService(&testing.T{})
	getNodeID(&testing.T{})
	exitCode := m.Run()
	os.Exit(exitCode)
}

func TestAddObserver(t *testing.T) {
	observer, err := service.client.GetObserverList(context.Background())
	if err != nil {
		t.Fatalf("ConsensusService GetObserverList failed: %+v\n", err)
	}
	t.Logf("Observer list before excute AddObserver: %s\n", observer)

	result, err := service.AddObserver(nodeID)
	if err != nil {
		t.Fatalf("ConsensusService AddObserver failed: %+v\n", err)
	}
	if result != standardOutput {
		t.Fatalf("TestAddObserver failed, the result \"%v\" is inconsistent with \"%v\"", result, standardOutput)
	}

	observer, err = service.client.GetObserverList(context.Background())
	if err != nil {
		t.Fatalf("ConsensusService invoke GetObserverList second time failed: %+v\n", err)
	}
	t.Logf("Observer list after excute AddObserver: %s\n", observer)
}

func TestAddSealer(t *testing.T) {
	observer, err := service.client.GetSealerList(context.Background())
	if err != nil {
		t.Fatalf("ConsensusService GetSealerList failed: %+v\n", err)
	}
	t.Logf("Sealer list before excute AddSealer: %s\n", observer)

	result, err := service.AddSealer(nodeID)
	if err != nil {
		t.Fatalf("ConsensusService AddSealer failed: %+v\n", err)
	}
	if result != standardOutput {
		t.Fatalf("TestAddSealer failed, the result \"%v\" is inconsistent with \"%v\"", result, standardOutput)
	}

	observer, err = service.client.GetSealerList(context.Background())
	if err != nil {
		t.Fatalf("ConsensusService invoke GetSealerList second time failed: %+v\n", err)
	}
	t.Logf("Sealer list after excute AddSealer: %s\n", observer)
}

func TestRemove(t *testing.T) {
	observer, err := service.client.GetSealerList(context.Background())
	if err != nil {
		t.Fatalf("ConsensusService GetSealerList failed: %+v\n", err)
	}
	t.Logf("Sealer list before excute RemoveNode: %s\n", observer)

	result, err := service.RemoveNode(nodeID)
	if err != nil {
		t.Fatalf("ConsensusService Remove failed: %+v\n", err)
	}
	if result != standardOutput {
		t.Fatalf("TestRemove failed, the result \"%v\" is inconsistent with \"%v\"", result, standardOutput)
	}

	observer, err = service.client.GetSealerList(context.Background())
	if err != nil {
		t.Fatalf("ConsensusService invoke GetSealerList second time failed: %+v\n", err)
	}
	t.Logf("Sealer list after excute RemoveNode: %s\n", observer)
}
