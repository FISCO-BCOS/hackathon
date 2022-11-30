package main

import (
	"encoding/hex"
	"os"
	"os/signal"
	"strings"
	"time"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/sirupsen/logrus"
)

func main() {
	if len(os.Args) < 3 {
		logrus.Fatalf("parameters are not enough, example \n%s 127.0.0.1:20202 hello", os.Args[0])
	}
	endpoint := os.Args[1]
	topic := os.Args[2]
	privateKey, _ := hex.DecodeString("145e247e170ba3afd6ae97e88f00dbc976c2345d511b0f6713355d19d8b80b58")
	config := &conf.Config{IsHTTP: false, ChainID: 1, CAFile: "ca.crt", Key: "sdk.key", Cert: "sdk.crt",
		IsSMCrypto: false, GroupID: 1, PrivateKey: privateKey, NodeURL: endpoint}
	var c *client.Client
	var err error
	for i := 0; i < 3; i++ {
		logrus.Printf("%d try to connect\n", i)
		c, err = client.Dial(config)
		if err != nil {
			logrus.Printf("init subscriber failed, err: %v, retrying\n", err)
			continue
		}
		break
	}
	if err != nil {
		logrus.Fatalf("init subscriber failed, err: %v\n", err)
	}
	timeout := 10 * time.Second
	queryTicker := time.NewTicker(timeout)
	defer queryTicker.Stop()
	done := make(chan bool)
	err = c.SubscribeTopic(topic, func(data []byte, response *[]byte) {
		logrus.Printf("received: %s\n", string(data))
		queryTicker.Stop()
		if strings.Contains(string(data), "Done") {
			done <- true
			return
		}
		queryTicker = time.NewTicker(timeout)
	})
	if err != nil {
		logrus.Printf("SubscribeAuthTopic failed, err: %v\n", err)
		return
	}
	logrus.Printf("Subscriber %s success %s\n", topic, time.Now().String())

	killSignal := make(chan os.Signal, 1)
	signal.Notify(killSignal, os.Interrupt)
	for {
		select {
		case <-done:
			logrus.Println("Done!")
			os.Exit(0)
		case <-queryTicker.C:
			logrus.Printf("can't receive message after 10s, %s\n", time.Now().String())
			os.Exit(1)
		case <-killSignal:
			logrus.Println("user exit")
			os.Exit(0)
		}
	}
}
