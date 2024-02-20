package main

import (
	"encoding/hex"
	"fmt"
	"os"
	"os/signal"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/ethereum/go-ethereum/crypto"
	"github.com/sirupsen/logrus"
)

const (
	privateKey1 = "b8ac1f6be271cd4ad5644615dea54823f7fa5d860fb8d3ae8b24141d9f1b9486"
	privateKey2 = "0fe5e3ce06d6d48ec806ea17d13ce3d80e74b85f23c32c38f2c8e4180f539a7e"
	privateKey3 = "13e3531ac291bcf5674acd1c8c7c77b725dc9bf56242b02ef76bf970190412aa"
)

func onPush(data []byte, response *[]byte) {
	logrus.Printf("received: %s\n", string(data))
}

var (
	c *client.Client
)

func main() {
	privateKey, _ := crypto.HexToECDSA(privateKey1)
	if len(os.Args) == 4 {
		fmt.Printf("use user specified private key %s\n", os.Args[3])
		keyBytes, _, err := conf.LoadECPrivateKeyFromPEM(os.Args[3])
		if err != nil {
			fmt.Printf("parse private key failed, err: %v\n", err)
			return
		}
		privateKey, err = crypto.ToECDSA(keyBytes)
		if err != nil {
			fmt.Printf("HexToECDSA failed, err: %v\n", err)
			return
		}
	} else if len(os.Args) < 3 {
		logrus.Fatal("the number of arguments less than 3")
	}

	endpoint := os.Args[1]
	topic := os.Args[2]
	signKey, _ := hex.DecodeString("145e247e170ba3afd6ae97e88f00dbc976c2345d511b0f6713355d19d8b80b58")
	config := &conf.Config{IsHTTP: false, ChainID: 1, CAFile: "ca.crt", Key: "sdk.key", Cert: "sdk.crt",
		IsSMCrypto: false, GroupID: 1, PrivateKey: signKey, NodeURL: endpoint}
	c, err := client.Dial(config)
	if err != nil {
		logrus.Fatalf("init client failed, err: %v\n", err)
	}

	err = c.SubscribePrivateTopic(topic, privateKey, onPush)
	if err != nil {
		logrus.Fatalf("SubscribeAuthTopic failed, err: %v\n", err)
	}
	fmt.Println("Subscriber success")

	killSignal := make(chan os.Signal, 1)
	signal.Notify(killSignal, os.Interrupt)
	<-killSignal
}
