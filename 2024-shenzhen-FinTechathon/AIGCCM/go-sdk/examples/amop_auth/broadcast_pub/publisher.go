package main

import (
	"crypto/ecdsa"
	"encoding/hex"
	"fmt"
	"os"
	"os/signal"
	"strconv"
	"time"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/ethereum/go-ethereum/common/hexutil"
	"github.com/ethereum/go-ethereum/crypto"
	"github.com/sirupsen/logrus"
)

func main() {
	var publicKeysHex = []string{
		"0x047cd0596006e3c0549482d010735303f25d6c308ef66643b63deef0a382a7620556eb49641cb3d45f4901a068a5e68475f8ba3b03a1bc785e84fe6490d66df365",
		"0x0419dece101df106ca4baf478f98911cdc525db5c6b58f2189af9f69ff314e9f0bcb816b41fb8bd49ae830dc1087bf51c71a21c3e3a332132262b5ecf0189817f4",
		"0x048b38138ea887220289276ca700e162647af79b4c61f33aefcfdaa2c3b714b2983084e519273208e8646b7f840e91b9053952df28a3bce1a6bca0132c26a36694",
	}
	publicKeys := make([]*ecdsa.PublicKey, 0)
	for _, k := range publicKeysHex {
		pubKeyBytes, err := hexutil.Decode(k)
		if err != nil {
			logrus.Fatalf("decode publicKey failed, err: %v\n", err)
		}
		pubKey, err := crypto.UnmarshalPubkey(pubKeyBytes)
		if err != nil {
			logrus.Fatalf("decompress pubkey failed, err: %v", err)
		}
		publicKeys = append(publicKeys, pubKey)
	}
	if len(os.Args) < 3 {
		logrus.Fatal("the number of arguments is not equal 3")
	} else if len(os.Args) > 3 {
		keys := 3
		publicKeys = make([]*ecdsa.PublicKey, 0)
		for ; keys < len(os.Args); keys++ {
			fmt.Printf("use user specified public key %s\n", os.Args[keys])
			keyBytes, _, err := conf.LoadECPublicKeyFromPEM(os.Args[keys])
			if err != nil {
				fmt.Printf("parse public key failed, err: %v\n", err)
				return
			}
			pubKey, err := crypto.UnmarshalPubkey(keyBytes)
			if err != nil {
				fmt.Printf("UnmarshalPubkey failed, err: %v\n", err)
				return
			}
			publicKeys = append(publicKeys, pubKey)
		}
	}
	endpoint := os.Args[1]
	topic := os.Args[2]
	privateKey, _ := hex.DecodeString("145e247e170ba3afd6ae97e88f00dbc976c2345d511b0f6713355d19d8b80b58")
	config := &conf.Config{IsHTTP: false, ChainID: 1, CAFile: "ca.crt", Key: "sdk.key", Cert: "sdk.crt",
		IsSMCrypto: false, GroupID: 1, PrivateKey: privateKey, NodeURL: endpoint}
	c, err := client.Dial(config)
	if err != nil {
		logrus.Fatalf("init publisher failed, err: %v\n", err)
	}

	err = c.PublishPrivateTopic(topic, publicKeys)
	if err != nil {
		logrus.Fatalf("publish topic failed, err: %v\n", err)
	}
	fmt.Println("publish topic success")
	time.Sleep(3 * time.Second)

	message := "Hi, FISCO BCOS!"
	go func() {
		for i := 0; i < 1000; i++ {
			logrus.Printf("publish message: %s ", message+" "+strconv.Itoa(i))
			err = c.BroadcastAMOPPrivateMsg(topic, []byte(message+" "+strconv.Itoa(i)))
			time.Sleep(2 * time.Second)
			if err != nil {
				logrus.Printf("PushTopicDataRandom failed, err: %v\n", err)
			}
		}
	}()

	killSignal := make(chan os.Signal, 1)
	signal.Notify(killSignal, os.Interrupt)
	<-killSignal
}
