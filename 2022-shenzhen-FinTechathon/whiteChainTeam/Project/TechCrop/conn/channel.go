// Copyright FISCO-BCOS go-sdk
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package conn

import (
	"bytes"
	"context"
	"crypto/ecdsa"
	"crypto/rand"
	"encoding/binary"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"io/ioutil"
	"math/big"
	"net"
	"net/http"
	"strconv"
	"strings"
	"sync"
	"time"

	"github.com/FISCO-BCOS/crypto/tls"
	"github.com/FISCO-BCOS/go-sdk/core/types"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/crypto"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
	"golang.org/x/crypto/sha3"
)

const (
	maxTopicLength      = 254
	messageHeaderLength = 42
	protocolVersion     = 3
	clientType          = "Go-SDK"
	heartBeatInterval   = 30
	tlsConnReadDeadline = 10
)

type nodeInfo struct {
	blockNumber       int64
	Protocol          int32  `json:"protocol"`
	CompatibleVersion string `json:"nodeVersion"`
}

type eventInfo struct {
	params  *types.EventLogParams
	handler func(int, []types.Log)
}

type channelSession struct {
	// groupID   uint
	connMu    sync.Mutex
	c         *tls.Conn
	mu        sync.RWMutex
	responses map[string]*channelResponse
	// receiptsMutex sync.Mutex
	receiptResponses    map[string]*channelResponse
	topicMu             sync.RWMutex
	topicHandlers       map[string]func([]byte, *[]byte)
	eventLogMu          sync.RWMutex
	eventLogHandlers    map[string]eventInfo
	asyncMu             sync.RWMutex
	asyncHandlers       map[string]func(*types.Receipt, error)
	blockNotifyMu       sync.RWMutex
	blockNotifyHandlers map[uint64]func(int64)
	buf                 []byte
	nodeInfo            nodeInfo
	closeOnce           sync.Once
	closed              chan interface{}
	endpoint            string
	tlsConfig           *tls.Config
	heartBeat           *time.Ticker
}

const (
	// channel messages types
	rpcMessage             = 0x12   // channel rpc request
	clientHeartbeat        = 0x13   // Heartbeat for sdk
	clientHandshake        = 0x14   // type for hand shake
	clientRegisterEventLog = 0x15   // type for event log filter register request and response
	amopPushRandom         = 0x30   // type for request from sdk
	amopResponse           = 0x31   // type for response to sdk
	amopSubscribeTopics    = 0x32   // type for topic request
	amopMultiCast          = 0x35   // type for mult broadcast
	amopAuthTopic          = 0x37   // type for verified topic
	amopUpdateTopicStatus  = 0x38   // type for update status
	transactionNotify      = 0x1000 // type for  transaction notify
	blockNotify            = 0x1001 // type for  block notify
	eventLogPush           = 0x1002 // type for event log push

	// AMOP error code
	success                            = 0
	remotePeerUnavailable              = 100
	remoteClientPeerUnavailable        = 101
	timeout                            = 102
	rejectAmopReqForOverBandwidthLimit = 103
	sendChannelMessageFailed           = 104

	// authTopic prefix
	needVerifyPrefix  = "#!$TopicNeedVerify_"
	authChannelPrefix = "#!$VerifyChannel_"
	pushChannelPrefix = "#!$PushChannel_"

	blockNotifyPrefix = "_block_notify_"
)

type topicData struct {
	length uint8
	topic  string
	data   []byte
}

type channelMessage struct {
	length    uint32
	typeN     uint16
	uuid      string
	errorCode int32
	body      []byte
}

type handshakeRequest struct {
	MinimumSupport int32  `json:"minimumSupport"`
	MaximumSupport int32  `json:"maximumSupport"`
	ClientType     string `json:"clientType"`
}

type channelResponse struct {
	Message *channelMessage
	Err     error
	Notify  chan interface{}
}

type EventLog struct {
	LogIndex         string   `json:"logIndex"`
	TransactionIndex string   `json:"transactionIndex"`
	TransactionHash  string   `json:"transactionHash"`
	BlockHash        string   `json:"blockHash"`
	BlockNumber      string   `json:"blockNumber"`
	Address          string   `json:"address"`
	Data             string   `json:"data"`
	Topics           []string `json:"topics"`
}

type eventLogResponse struct {
	FilterID string     `json:"filterID"`
	Logs     []EventLog `json:"logs"`
	Result   int        `json:"result"`
}

type requestAuth struct {
	Topic        string `json:"topic"`
	TopicForCert string `json:"topicForCert"`
	NodeID       string `json:"nodeId"`
}

type updateAuthTopicStatus struct {
	CheckResult int    `json:"checkResult"`
	NodeID      string `json:"nodeId"`
	Topic       string `json:"topic"`
}

func newChannelMessage(msgType uint16, body []byte) (*channelMessage, error) {
	id, err := uuid.NewUUID()
	if err != nil {
		logrus.Warnf("newChannelMessage error: %v", err)
		return nil, err
	}
	idString := strings.ReplaceAll(id.String(), "-", "")
	// var idByte [32]byte
	// copy(idByte[:], idString[:32])
	msg := &channelMessage{length: uint32(messageHeaderLength + len(body)), typeN: msgType,
		errorCode: 0, uuid: idString, body: body}
	return msg, nil
}

func newTopicMessage(t string, data []byte, msgType uint16) (*channelMessage, error) {
	if len(t) > maxTopicLength {
		return nil, fmt.Errorf("topic length exceeds 254")
	}
	topic := &topicData{length: uint8(len(t)) + 1, topic: t, data: data}
	mesgData := topic.Encode()
	return newChannelMessage(msgType, mesgData)
}

func (t *topicData) Encode() []byte {
	var raw []byte
	buf := bytes.NewBuffer(raw)
	err := binary.Write(buf, binary.LittleEndian, t.length)
	if err != nil {
		logrus.Fatal("encode length error:", err)
	}
	err = binary.Write(buf, binary.LittleEndian, []byte(t.topic))
	if err != nil {
		logrus.Fatal("encode type error:", err)
	}

	err = binary.Write(buf, binary.LittleEndian, t.data)
	if err != nil {
		logrus.Fatal("encode data error:", err)
	}
	return buf.Bytes()
}

func (msg *channelMessage) Encode() []byte {
	var raw []byte
	buf := bytes.NewBuffer(raw)
	err := binary.Write(buf, binary.BigEndian, msg.length)
	if err != nil {
		logrus.Fatal("encode length error:", err)
	}
	err = binary.Write(buf, binary.BigEndian, msg.typeN)
	if err != nil {
		logrus.Fatal("encode type error:", err)
	}
	err = binary.Write(buf, binary.LittleEndian, []byte(msg.uuid))
	if err != nil {
		logrus.Fatal("encode uuid error:", err)
	}
	err = binary.Write(buf, binary.BigEndian, msg.errorCode)
	if err != nil {
		logrus.Fatal("encode ErrorCode error:", err)
	}
	err = binary.Write(buf, binary.LittleEndian, msg.body)
	if err != nil {
		logrus.Fatal("encode Body error:", err)
	}
	if uint32(buf.Len()) != msg.length {
		logrus.Warnf("%d != %d\n, buf is %v", buf.Len(), msg.length, buf.String())
		logrus.Fatal("encode error length error:", err)
	}
	return buf.Bytes()
}

func decodeChannelMessage(raw []byte) (*channelMessage, error) {
	buf := bytes.NewReader(raw)
	result := new(channelMessage)
	err := binary.Read(buf, binary.BigEndian, &result.length)
	if err != nil {
		logrus.Println("binary.Read failed:", err)
	}
	if uint32(len(raw)) < result.length {
		return nil, errors.New("uncomplete message")
	}
	err = binary.Read(buf, binary.BigEndian, &result.typeN)
	if err != nil {
		logrus.Println("binary.Read failed:", err)
	}
	var uuid [32]byte
	err = binary.Read(buf, binary.LittleEndian, &uuid)
	if err != nil {
		// logrus.Fatal("encode error:", err)
		logrus.Println("binary.Read failed:", err)
	}
	result.uuid = string(uuid[:])

	err = binary.Read(buf, binary.BigEndian, &result.errorCode)
	if err != nil {
		logrus.Println("binary.Read failed:", err)
	}
	dataLength := result.length - messageHeaderLength
	result.body = make([]byte, dataLength)
	err = binary.Read(buf, binary.BigEndian, &result.body)
	if err != nil {
		logrus.Println("binary.Read failed:", err)
	}
	return result, nil
}

func decodeTopic(raw []byte) (*topicData, error) {
	buf := bytes.NewReader(raw)
	result := new(topicData)
	err := binary.Read(buf, binary.LittleEndian, &result.length)
	if err != nil {
		logrus.Println("binary.Read failed:", err)
	}
	topic := make([]byte, result.length-1)
	err = binary.Read(buf, binary.LittleEndian, &topic)
	if err != nil {
		logrus.Println("binary.Read failed:", err)
	}
	result.topic = string(topic)
	dataLength := len(raw) - int(result.length)
	result.data = make([]byte, dataLength)
	err = binary.Read(buf, binary.LittleEndian, &result.data)
	if err != nil {
		logrus.Println("binary.Read failed:", err)
	}
	return result, nil
}

// channelCon n is treated specially by Connection.
func (hc *channelSession) Write(context.Context, interface{}) error {
	panic("Write called on channelSession")
}

func (hc *channelSession) RemoteAddr() string {
	return hc.c.RemoteAddr().String()
}

func (hc *channelSession) Read() ([]*jsonrpcMessage, bool, error) {
	<-hc.closed
	return nil, false, io.EOF
}

func (hc *channelSession) Close() {
	hc.closeOnce.Do(func() { close(hc.closed) })
}

func (hc *channelSession) Closed() <-chan interface{} {
	return hc.closed
}

// ChannelTimeouts represents the configuration params for the Channel RPC server.
type ChannelTimeouts struct {
	// ReadTimeout is the maximum duration for reading the entire
	// request, including the body.
	//
	// Because ReadTimeout does not let Handlers make per-request
	// decisions on each request body's acceptable deadline or
	// upload rate, most users will prefer to use
	// ReadHeaderTimeout. It is valid to use them both.
	ReadTimeout time.Duration

	// WriteTimeout is the maximum duration before timing out
	// writes of the response. It is reset whenever a new
	// request's header is read. Like ReadTimeout, it does not
	// let Handlers make decisions on a per-request basis.
	WriteTimeout time.Duration

	// IdleTimeout is the maximum amount of time to wait for the
	// next request when keep-alives are enabled. If IdleTimeout
	// is zero, the value of ReadTimeout is used. If both are
	// zero, ReadHeaderTimeout is used.
	IdleTimeout time.Duration
}

// DefaultChannelTimeouts represents the default timeout values used if further
// configuration is not provided.
var DefaultChannelTimeouts = ChannelTimeouts{
	ReadTimeout:  30 * time.Second,
	WriteTimeout: 30 * time.Second,
	IdleTimeout:  120 * time.Second,
}

// DialChannelWithClient creates a new RPC client that connects to an RPC server over Channel
// using the provided Channel Client.
func DialChannelWithClient(endpoint string, config *tls.Config, groupID int) (*Connection, error) {
	initctx := context.Background()
	return newClient(initctx, func(context.Context) (ServerCodec, error) {
		conn, err := tls.Dial("tcp", endpoint, config)
		if err != nil {
			return nil, err
		}
		ch := &channelSession{c: conn, responses: make(map[string]*channelResponse),
			receiptResponses: make(map[string]*channelResponse), topicHandlers: make(map[string]func([]byte, *[]byte)),
			eventLogHandlers:    make(map[string]eventInfo),
			asyncHandlers:       make(map[string]func(*types.Receipt, error)),
			blockNotifyHandlers: make(map[uint64]func(int64)),
			nodeInfo:            nodeInfo{blockNumber: 0, Protocol: 1}, closed: make(chan interface{}), endpoint: endpoint,
			tlsConfig: config,
			heartBeat: time.NewTicker(heartBeatInterval * time.Second)}
		go ch.processMessages()
		if err = ch.handshakeChannel(); err != nil {
			logrus.Errorf("handshake channel protocol failed, use default protocol version")
		}
		ch.topicHandlers[blockNotifyPrefix+strconv.Itoa(groupID)] = nil
		if err = ch.sendSubscribedTopics(); err != nil {
			return nil, fmt.Errorf("subscriber block nofity failed")
		}
		return ch, nil
	})
}

func (c *Connection) sendRPCRequest(ctx context.Context, op *requestOp, msg interface{}) error {
	hc := c.writeConn.(*channelSession)
	rpcMsg := msg.(*jsonrpcMessage)
	if rpcMsg.Method == "sendRawTransaction" {
		respBody, err := hc.sendTransaction(ctx, msg)
		if err != nil {
			return fmt.Errorf("sendTransaction failed, %v", err)
		}
		rpcResp := new(jsonrpcMessage)
		rpcResp.Result = respBody
		op.resp <- rpcResp
	} else {
		respBody, err := hc.doRPCRequest(ctx, msg)
		if respBody != nil {
			defer respBody.Close()
		}

		if err != nil {
			if respBody != nil {
				buf := new(bytes.Buffer)
				if _, err2 := buf.ReadFrom(respBody); err2 == nil {
					return fmt.Errorf("%v %v", err, buf.String())
				}
			}
			return err
		}
		var respmsg jsonrpcMessage
		if err := json.NewDecoder(respBody).Decode(&respmsg); err != nil {
			return err
		}
		op.resp <- &respmsg
	}
	return nil
}

func (c *Connection) sendBatchChannel(ctx context.Context, op *requestOp, msgs []*jsonrpcMessage) error {
	hc := c.writeConn.(*channelSession)
	respBody, err := hc.doRPCRequest(ctx, msgs)
	if err != nil {
		return err
	}
	defer respBody.Close()
	var respmsgs []jsonrpcMessage
	if err := json.NewDecoder(respBody).Decode(&respmsgs); err != nil {
		return err
	}
	for i := 0; i < len(respmsgs); i++ {
		op.resp <- &respmsgs[i]
	}
	return nil
}

func (hc *channelSession) doRPCRequest(ctx context.Context, msg interface{}) (io.ReadCloser, error) {
	body, err := json.Marshal(msg)
	if err != nil {
		return nil, err
	}
	var rpcMsg *channelMessage
	rpcMsg, err = newChannelMessage(rpcMessage, body)
	if err != nil {
		return nil, err
	}
	msgBytes := rpcMsg.Encode()
	if hc.c == nil {
		return nil, errors.New("connection unavailable")
	}
	_, err = hc.c.Write(msgBytes)
	if err != nil {
		return nil, err
	}
	response := &channelResponse{Message: nil, Notify: make(chan interface{})}
	hc.mu.Lock()
	hc.responses[rpcMsg.uuid] = response
	hc.mu.Unlock()
	<-response.Notify
	hc.mu.Lock()
	response = hc.responses[rpcMsg.uuid]
	delete(hc.responses, rpcMsg.uuid)
	hc.mu.Unlock()
	if response.Err != nil {
		return nil, response.Err
	}
	if response.Message.errorCode != 0 {
		return nil, errors.New("response error:" + string(response.Message.errorCode))
	}
	return ioutil.NopCloser(bytes.NewReader(response.Message.body)), nil
}

func (hc *channelSession) sendTransaction(ctx context.Context, msg interface{}) ([]byte, error) {
	body, err := json.Marshal(msg)
	if err != nil {
		return nil, err
	}
	var rpcMsg *channelMessage
	rpcMsg, err = newChannelMessage(rpcMessage, body)
	if err != nil {
		return nil, err
	}
	response := &channelResponse{Message: nil, Notify: make(chan interface{})}
	receiptResponse := &channelResponse{Message: nil, Notify: make(chan interface{})}
	hc.mu.Lock()
	hc.responses[rpcMsg.uuid] = response
	hc.receiptResponses[rpcMsg.uuid] = receiptResponse
	hc.mu.Unlock()
	defer func() {
		hc.mu.Lock()
		delete(hc.responses, rpcMsg.uuid)
		delete(hc.receiptResponses, rpcMsg.uuid)
		hc.mu.Unlock()
	}()
	msgBytes := rpcMsg.Encode()
	if hc.c == nil {
		return nil, errors.New("connection unavailable")
	}
	_, err = hc.c.Write(msgBytes)
	if err != nil {
		return nil, err
	}
	<-response.Notify
	hc.mu.Lock()
	response = hc.responses[rpcMsg.uuid]
	delete(hc.responses, rpcMsg.uuid)
	hc.mu.Unlock()
	if response.Err != nil {
		return nil, response.Err
	}
	if response.Message.errorCode != 0 {
		return nil, errors.New("response error:" + string(response.Message.errorCode))
	}
	var respmsg jsonrpcMessage
	if err := json.NewDecoder(bytes.NewReader(response.Message.body)).Decode(&respmsg); err != nil {
		return nil, err
	}
	if respmsg.Error != nil {
		return nil, fmt.Errorf("send transaction error, code=%d, message=%s", respmsg.Error.Code, respmsg.Error.Message)
	}
	// logrus.Warnf("sendTransaction reveived response,seq:%s message:%s\n ", rpcMsg.uuid, respmsg.Result)
	<-receiptResponse.Notify
	hc.mu.RLock()
	receiptResponse = hc.receiptResponses[rpcMsg.uuid]
	hc.mu.RUnlock()
	if receiptResponse.Err != nil {
		return nil, response.Err
	}
	if receiptResponse.Message.errorCode != 0 {
		return nil, errors.New("response error:" + string(receiptResponse.Message.errorCode))
	}
	return receiptResponse.Message.body, nil
}

func (hc *channelSession) asyncSendTransaction(msg interface{}, handler func(*types.Receipt, error)) error {
	body, err := json.Marshal(msg)
	if err != nil {
		return err
	}
	var rpcMsg *channelMessage
	rpcMsg, err = newChannelMessage(rpcMessage, body)
	if err != nil {
		return err
	}

	response := &channelResponse{Message: nil, Notify: make(chan interface{})}
	hc.mu.Lock()
	hc.responses[rpcMsg.uuid] = response
	hc.mu.Unlock()

	hc.asyncMu.Lock()
	hc.asyncHandlers[rpcMsg.uuid] = handler
	hc.asyncMu.Unlock()
	defer func() {
		hc.mu.Lock()
		delete(hc.responses, rpcMsg.uuid)
		hc.mu.Unlock()
	}()
	msgBytes := rpcMsg.Encode()
	if hc.c == nil {
		return errors.New("connection unavailable")
	}
	_, err = hc.c.Write(msgBytes)
	if err != nil {
		return err
	}
	<-response.Notify

	hc.mu.Lock()
	response = hc.responses[rpcMsg.uuid]
	hc.mu.Unlock()
	if response.Err != nil {
		return response.Err
	}
	if response.Message.errorCode != 0 {
		return errors.New("response error:" + string(response.Message.errorCode))
	}
	var responseMsg jsonrpcMessage
	if err := json.NewDecoder(bytes.NewReader(response.Message.body)).Decode(&responseMsg); err != nil {
		return err
	}
	if responseMsg.Error != nil {
		return fmt.Errorf("async-send transaction error, code=%d, message=%s", responseMsg.Error.Code, responseMsg.Error.Message)
	}
	return nil
}

func (hc *channelSession) sendMessageNoResponse(msg *channelMessage) error {
	msgBytes := msg.Encode()
	if hc.c == nil {
		return errors.New("connection unavailable")
	}
	_, err := hc.c.Write(msgBytes)
	if err != nil {
		return err
	}
	return nil
}

func (hc *channelSession) sendMessage(msg *channelMessage) (*channelMessage, error) {
	msgBytes := msg.Encode()
	response := &channelResponse{Message: nil, Notify: make(chan interface{})}
	hc.mu.Lock()
	hc.responses[msg.uuid] = response
	hc.mu.Unlock()
	if hc.c == nil {
		return nil, errors.New("connection unavailable")
	}
	hc.connMu.Lock()
	_, err := hc.c.Write(msgBytes)
	hc.connMu.Unlock()
	if err != nil {
		return nil, err
	}
	defer func() {
		hc.mu.Lock()
		delete(hc.responses, msg.uuid)
		hc.mu.Unlock()
	}()
	<-response.Notify
	hc.mu.Lock()
	response = hc.responses[msg.uuid]
	hc.mu.Unlock()
	if response.Err != nil {
		return nil, response.Err
	}
	switch response.Message.errorCode {
	case success:
		_ = struct{}{}
	case remotePeerUnavailable:
		return nil, fmt.Errorf("error code %v, remote peer unavailable", remotePeerUnavailable)
	case remoteClientPeerUnavailable:
		return nil, fmt.Errorf("error code %v, remote client peer unavailable", remoteClientPeerUnavailable)
	case timeout:
		return nil, fmt.Errorf("error code %v, timeout", timeout)
	case rejectAmopReqForOverBandwidthLimit:
		return nil, fmt.Errorf("error code %v, reject amop reqeust or over bandwidth limit", rejectAmopReqForOverBandwidthLimit)
	case sendChannelMessageFailed:
		return nil, fmt.Errorf("error code %v, send channel message failed", sendChannelMessageFailed)
	default:
		return nil, fmt.Errorf("response error: %v", response.Message.errorCode)
	}
	return response.Message, nil
}

func (hc *channelSession) handshakeChannel() error {
	handshakeBody := handshakeRequest{MinimumSupport: 1, MaximumSupport: protocolVersion, ClientType: clientType}
	body, err := json.Marshal(handshakeBody)
	if err != nil {
		return fmt.Errorf("encode handshake request failed %w", err)
	}
	var msg, response *channelMessage
	msg, err = newChannelMessage(clientHandshake, body)
	if err != nil {
		return err
	}
	response, err = hc.sendMessage(msg)
	if err != nil {
		return err
	}
	var info nodeInfo
	if err = json.Unmarshal(response.body, &info); err != nil {
		return fmt.Errorf("parse handshake channel protocol response failed %w", err)
	}
	hc.nodeInfo = info
	// logrus.Warnf("node info:%+v", info)
	return nil
}

func (hc *channelSession) sendSubscribedEvent(eventLogParams *types.EventLogParams) error {
	data, err := json.Marshal(*eventLogParams)
	if err != nil {
		return errors.New("marshal eventLogParams failed")
	}
	msg, err := newTopicMessage("", data, clientRegisterEventLog)
	if err != nil {
		return fmt.Errorf("new topic message failed, err: %v", err)
	}
	response, err := hc.sendMessage(msg)
	if err != nil {
		return err
	}
	result := &struct {
		Result int `json:"result"`
	}{}
	json.Unmarshal(response.body, result)
	if result.Result != 0 {
		return fmt.Errorf("send subscribed event failed, result: %v", result.Result)
	}
	return nil
}

func (hc *channelSession) sendSubscribedTopics() error {
	keys := make([]string, 0, len(hc.topicHandlers))
	for k := range hc.topicHandlers {
		keys = append(keys, k)
	}
	data, err := json.Marshal(keys)
	if err != nil {
		return errors.New("marshal topics failed")
	}
	msg, err := newChannelMessage(amopSubscribeTopics, data)
	if err != nil {
		return fmt.Errorf("newChannelMessage failed, err: %v", err)
	}
	return hc.sendMessageNoResponse(msg)
}

func (hc *channelSession) subscribeEvent(eventLogParams types.EventLogParams, handler func(int, []types.Log)) error {
	if handler == nil {
		return errors.New("handler is nil")
	}
	id, err := uuid.NewUUID()
	if err != nil {
		return errors.New("new UUID failed")
	}
	eventLogParams.FilterID = strings.ReplaceAll(id.String(), "-", "")
	hc.eventLogMu.RLock()
	_, ok := hc.eventLogHandlers[eventLogParams.FilterID]
	hc.eventLogMu.RUnlock()
	if ok {
		return errors.New("already subscribed to event " + eventLogParams.FilterID)
	}
	if err := hc.sendSubscribedEvent(&eventLogParams); err != nil {
		return err
	}
	hc.eventLogMu.Lock()
	hc.eventLogHandlers[eventLogParams.FilterID] = eventInfo{&eventLogParams, handler}
	hc.eventLogMu.Unlock()
	return nil
}

func (hc *channelSession) subscribeTopic(topic string, handler func([]byte, *[]byte)) error {
	if len(topic) > maxTopicLength {
		return errors.New("topic length exceeds 254")
	}
	if handler == nil {
		return errors.New("handler is nil")
	}
	if _, ok := hc.topicHandlers[topic]; ok {
		return errors.New("already subscribed to topic " + topic)
	}
	hc.topicMu.Lock()
	hc.topicHandlers[topic] = handler
	hc.topicMu.Unlock()

	return hc.sendSubscribedTopics()
}

func (hc *channelSession) unsubscribeTopic(topic string) error {
	if _, ok := hc.topicHandlers[topic]; !ok {
		return fmt.Errorf("topic \"%v\" has't been subscribed", topic)
	}
	hc.topicMu.Lock()
	delete(hc.topicHandlers, topic)
	hc.topicMu.Unlock()

	return hc.sendSubscribedTopics()
}

func (hc *channelSession) sendAMOPMsg(topic string, data []byte) ([]byte, error) {
	msg, err := newTopicMessage(topic, data, amopPushRandom)
	if err != nil {
		return nil, fmt.Errorf("new topic message failed, err: %v", err)
	}
	message, err := hc.sendMessage(msg)
	if err != nil {
		return nil, fmt.Errorf("sendMessage failed, err: %v", err)
	}
	responseTopicData, err := decodeTopic(message.body)
	if err != nil {
		return nil, err
	}
	return responseTopicData.data, nil
}

func (hc *channelSession) sendHeartbeatMsg() error {
	msg, err := newChannelMessage(clientHeartbeat, nil)
	if err != nil {
		return fmt.Errorf("new topic message failed, err: %v", err)
	}
	// ignore response, because if wait response will block the process message
	err = hc.sendMessageNoResponse(msg)
	if err != nil {
		return fmt.Errorf("sendMessage failed, err: %v", err)
	}
	return nil
}

func (hc *channelSession) broadcastAMOPMsg(topic string, data []byte) error {
	msg, err := newTopicMessage(topic, data, amopMultiCast)
	if err != nil {
		return err
	}
	message, err := hc.sendMessage(msg)
	if err != nil {
		return fmt.Errorf("pushTopicDataToALL, sendMessage failed, err: %v", err)
	}
	_, err = decodeTopic(message.body)
	if err != nil {
		return fmt.Errorf("pushTopicDataToALL, decodeTopic failed, err: %v", err)
	}
	return nil
}

func (hc *channelSession) publishPrivateTopic(topic string, publicKeys []*ecdsa.PublicKey) error {
	var authTopicName = needVerifyPrefix + topic
	if _, ok := hc.topicHandlers[authTopicName]; ok {
		return errors.New("already subscribed to topic " + topic)
	}
	if len(authChannelPrefix+authTopicName) > maxTopicLength-33 {
		return fmt.Errorf("the length of real topic %s exceeds 254, because of prefix \"#!$VerifyChannel_\", \"#!$TopicNeedVerify_\" and \"_{uuid}\"", authChannelPrefix+authTopicName+"_92be6ce4dbd311eaae5a983b8fda4e0e")
	}
	verfiyHandler := func(data []byte, _ *[]byte) {
		// generate random number and send it to node
		var authInfo requestAuth
		err := json.Unmarshal(data, &authInfo)
		if err != nil {
			logrus.Warnf("unmarshal authInfo failed, err: %v", err)
			return
		}
		randomData := generateRandomNum()
		signature, err := hc.sendAMOPMsg(authInfo.TopicForCert, randomData)
		if err != nil {
			logrus.Warnf("send message failed, err: %v", err)
			return
		}
		if len(signature) == 0 {
			logrus.Println("signature is empty")
			return
		}
		var checkResult = 1 // 1 is false
		hw := sha3.NewLegacyKeccak256()
		if _, err = hw.Write(randomData); err != nil {
			logrus.Warnf("keccak256 failed, err: %v\n", err)
			return
		}
		digest := hw.Sum(nil)
		for i := 0; i < len(publicKeys); i++ {
			publicKeyBytes := crypto.FromECDSAPub(publicKeys[i])
			if crypto.VerifySignature(publicKeyBytes, digest, signature[:len(signature)-1]) {
				checkResult = 0
				// logrus.Printf("verify NodeID %v success", authInfo.NodeID)
				break
			}
		}
		var updateNodeTopicStatus = new(updateAuthTopicStatus)
		updateNodeTopicStatus.CheckResult = checkResult
		updateNodeTopicStatus.Topic = authInfo.Topic
		updateNodeTopicStatus.NodeID = authInfo.NodeID
		jsonBytes, err := json.Marshal(updateNodeTopicStatus)
		if err != nil {
			logrus.Warnf("nodeUpdateTopicStatus marshal failed, err: %v", err)
		}
		newMessage, err := newChannelMessage(amopUpdateTopicStatus, jsonBytes)
		if err != nil {
			logrus.Warnf("new topic message failed, err: %v", err)
		}
		err = hc.sendMessageNoResponse(newMessage)
		if err != nil {
			logrus.Warnf("send message no response failed, err: %v", err)
		}
	}
	hc.topicMu.Lock()
	hc.topicHandlers[pushChannelPrefix+authTopicName] = verfiyHandler
	hc.topicMu.Unlock()

	return hc.sendSubscribedTopics()
}

func (hc *channelSession) subscribePrivateTopic(topic string, privateKey *ecdsa.PrivateKey, handler func([]byte, *[]byte)) error {
	if handler == nil {
		return errors.New("handler is nil")
	}
	topic = needVerifyPrefix + topic
	if _, ok := hc.topicHandlers[topic]; ok {
		return errors.New("already subscribed to topic " + topic)
	}

	id, err := uuid.NewUUID()
	if err != nil {
		return errors.New("new UUID failed")
	}
	idString := strings.ReplaceAll(id.String(), "-", "")
	authChannelTopic := authChannelPrefix + topic + "_" + idString
	if len(authChannelTopic) > maxTopicLength {
		return fmt.Errorf("the length of real topic %s exceeds 254, because of prefix \"#!$VerifyChannel_\", \"#!$TopicNeedVerify_\" and \"_{uuid}\"", authChannelTopic)
	}
	hc.topicMu.Lock()
	hc.topicHandlers[topic] = handler
	hc.topicHandlers[authChannelTopic] = func(data []byte, response *[]byte) {
		// sign random number and send back
		hw := sha3.NewLegacyKeccak256()
		if _, err = hw.Write(data); err != nil {
			logrus.Warnf("keccak256 failed, err: %v\n", err)
			return
		}
		digest := hw.Sum(nil)
		signature, err := crypto.Sign(digest, privateKey)
		if err != nil {
			logrus.Warnf("sign random number failed, err: %v\n", err)
			return
		}
		*response = signature
	}
	hc.topicMu.Unlock()

	return hc.sendSubscribedTopics()
}

func (hc *channelSession) unsubscribePrivateTopic(topic string) error {
	var authTopicName = needVerifyPrefix + topic
	if _, ok := hc.topicHandlers[authTopicName]; !ok {
		return fmt.Errorf("topic \"%v\" has't been subscribed", topic)
	}
	var authChannelTopicName = authChannelPrefix + authTopicName // real authChannelTopicName has the UUID suffix
	for k := range hc.topicHandlers {
		if strings.Contains(k, authChannelTopicName) {
			authChannelTopicName = k
			break
		}
	}
	hc.topicMu.Lock()
	delete(hc.topicHandlers, authTopicName)
	delete(hc.topicHandlers, authChannelTopicName)
	delete(hc.topicHandlers, pushChannelPrefix+authTopicName)
	hc.topicMu.Unlock()

	return hc.sendSubscribedTopics()
}

func (hc *channelSession) sendAMOPPrivateMsg(topic string, data []byte) ([]byte, error) {
	return hc.sendAMOPMsg(needVerifyPrefix+topic, data)
}

func (hc *channelSession) broadcastAMOPPrivateMsg(topic string, data []byte) error {
	return hc.broadcastAMOPMsg(needVerifyPrefix+topic, data)
}

func (hc *channelSession) processTopicMessage(msg *channelMessage) {
	topic, err := decodeTopic(msg.body)
	if err != nil {
		// logrus.Warnf("decode topic failed: %+v\n", msg)
		return
	}
	hc.topicMu.RLock()
	handler, ok := hc.topicHandlers[topic.topic]
	hc.topicMu.RUnlock()
	responseData := new([]byte)
	if ok {
		handler(topic.data, responseData)
	}
	responseMessage, err := newTopicMessage(topic.topic, *responseData, amopResponse)
	if err != nil {
		logrus.Warnf("newTopicMessage failed, err: %v\n", err)
		return
	}
	responseMessage.uuid = msg.uuid
	err = hc.sendMessageNoResponse(responseMessage)
	if err != nil {
		logrus.Warnf("response message failed, uuid: %v, err: %v\n", msg.uuid, err)
		return
	}
}

func (hc *channelSession) processAuthTopicMessage(msg *channelMessage) {
	var requestAuthInfo requestAuth
	err := json.Unmarshal(msg.body, &requestAuthInfo)
	if err != nil {
		// logrus.Warnf("unmarshal authInfo failed, err: %v\n", err)
		return
	}

	responseMessage, err := newTopicMessage(authChannelPrefix+requestAuthInfo.TopicForCert, nil, amopResponse)
	if err != nil {
		// logrus.Warnf("err: %v\n", err)
		return
	}
	responseMessage.uuid = msg.uuid
	err = hc.sendMessageNoResponse(responseMessage)
	if err != nil {
		// logrus.Println("response message failed")
		return
	}
	// logrus.Warnf("unsubscribed topic %s\n", requestAuthInfo.Topic)
	hc.topicMu.RLock()
	handler, ok := hc.topicHandlers[pushChannelPrefix+requestAuthInfo.Topic]
	hc.topicMu.RUnlock()

	if ok {
		go handler(msg.body, nil)
		// return
	}
}

func hexToUint64(s string) (uint64, error) {
	cleaned := strings.Replace(s, "0x", "", -1)
	return strconv.ParseUint(cleaned, 16, 64)
}

func (hc *channelSession) processEventLogMessage(msg *channelMessage) {
	var eventLogResponse eventLogResponse
	err := json.Unmarshal(msg.body, &eventLogResponse)
	if err != nil {
		logrus.Warnf("unmarshal eventLogResponse failed, err: %v\n", err)
		return
	}
	logs := []types.Log{}
	var nextBlock uint64
	for _, log := range eventLogResponse.Logs {
		number, _ := strconv.Atoi(log.BlockNumber)
		logIndex, err := hexToUint64(log.LogIndex)
		if err != nil {
			logrus.Warnf("unmarshal logIndex failed, err: %v\n", err)
			return
		}
		txIndex, err := hexToUint64(log.TransactionIndex)
		if err != nil {
			logrus.Warnf("unmarshal TransactionIndex failed, err: %v\n", err)
			return
		}
		topics := []common.Hash{}
		for _, topic := range log.Topics {
			topics = append(topics, common.HexToHash(topic))
		}
		data := common.FromHex(log.Data)
		logs = append(logs, types.Log{
			Address:     common.HexToAddress(log.Address),
			Topics:      topics,
			Data:        data,
			BlockNumber: uint64(number),
			TxHash:      common.HexToHash(log.TransactionHash),
			TxIndex:     uint(txIndex),
			BlockHash:   common.HexToHash(log.BlockHash),
			Index:       uint(logIndex),
			Removed:     false,
		})
		nextBlock = uint64(number) + 1
	}

	hc.eventLogMu.RLock()
	eventLogInfo, ok := hc.eventLogHandlers[eventLogResponse.FilterID]
	if ok {
		eventLogInfo.params.FromBlock = strconv.FormatUint(nextBlock, 10)
	}
	hc.eventLogMu.RUnlock()
	if ok {
		go eventLogInfo.handler(eventLogResponse.Result, logs)
	}
}

// processMessages process incoming messages from the node
func (hc *channelSession) processMessages() {
	for {
		select {
		case <-hc.closed:
			// delete old network
			_ = hc.c.Close()
			hc.c = nil
			// return err for responses and receiptResponses
			hc.mu.Lock()
			for _, response := range hc.responses {
				response.Err = errors.New("connection lost, reconnecting")
				response.Notify <- struct{}{}
			}
			for _, receiptResponse := range hc.receiptResponses {
				receiptResponse.Err = errors.New("connection lost, reconnecting")
				receiptResponse.Notify <- struct{}{}
			}
			hc.mu.Unlock()
			// delete asyncHandler
			hc.asyncMu.Lock()
			for key, handler := range hc.asyncHandlers {
				handler(nil, errors.New("connection lost, reconnecting"))
				delete(hc.asyncHandlers, key)
			}
			hc.asyncMu.Unlock()
			// re-connect network
			for {
				con, err := tls.Dial("tcp", hc.endpoint, hc.tlsConfig)
				if err != nil {
					logrus.Warnf("tls.Dial %v failed, err: %v\n", hc.endpoint, err)
					time.Sleep(5 * time.Second)
					continue
				}
				hc.c = con
				hc.closed = make(chan interface{})
				hc.closeOnce = sync.Once{}
				hc.nodeInfo.Protocol = 1
				go hc.processMessages()
				if err = hc.handshakeChannel(); err != nil {
					logrus.Warnf("handshake channel protocol failed, use default protocol version")
				}
				err = hc.sendSubscribedTopics() // re-subscribe topic
				if err != nil {
					logrus.Errorf("re-subscriber topic failed, err: %v\n", err)
				}
				// resubscribe contract event
				for _, eventLogInfo := range hc.eventLogHandlers {
					err = hc.sendSubscribedEvent(eventLogInfo.params)
					if err != nil {
						logrus.Errorf("re-subscriber event failed, event: %+v, err: %v\n", *eventLogInfo.params, err)
					}
				}
				return
			}
		case <-hc.heartBeat.C:
			hc.sendHeartbeatMsg()
		default:
			receiveBuf := make([]byte, 4096)
			hc.c.SetReadDeadline(time.Now().Add(tlsConnReadDeadline * time.Second))
			b, err := hc.c.Read(receiveBuf)
			if err != nil {
				nerr, ok := err.(net.Error)
				if !ok || !nerr.Timeout() {
					logrus.Warnf("channel Read error:%v\n", err)
					hc.Close()
				}
				continue
			}
			hc.buf = append(hc.buf, receiveBuf[:b]...)
			msg, err := decodeChannelMessage(hc.buf)
			if err != nil {
				logrus.Debugf("decodeChannelMessage error:%v", err)
				continue
			}
			hc.buf = hc.buf[msg.length:]
			// TODO: move notify into switch
			hc.mu.Lock()
			if response, ok := hc.responses[msg.uuid]; ok {
				response.Message = msg
				if response.Notify != nil {
					response.Notify <- struct{}{}
					close(response.Notify)
				}
				response.Notify = nil
			}
			hc.mu.Unlock()
			switch msg.typeN {
			case rpcMessage, amopResponse, clientHandshake, clientRegisterEventLog, clientHeartbeat:
				logrus.Debugf("response type:%d seq:%s, msg:%s", msg.typeN, msg.uuid, string(msg.body))
			case transactionNotify:
				hc.mu.Lock()
				if receipt, ok := hc.receiptResponses[msg.uuid]; ok {
					receipt.Message = msg
					receipt.Notify <- struct{}{}
				}
				hc.mu.Unlock()
				hc.asyncMu.Lock()
				if handler, ok := hc.asyncHandlers[msg.uuid]; ok {
					if msg.errorCode != 0 {
						go handler(nil, errors.New("response error:"+string(msg.errorCode)))
					} else {
						var receipt = new(types.Receipt)
						var anonymityReceipt = &struct {
							types.Receipt
							Status string `json:"status"`
						}{}
						err := json.Unmarshal(msg.body, anonymityReceipt)
						if err != nil {
							go handler(nil, fmt.Errorf("json.Unmarshal failed, err%v", err))
							continue
						}
						status, err := strconv.ParseInt(anonymityReceipt.Status[2:], 16, 32)
						if err != nil {
							go handler(nil, fmt.Errorf("strconv.ParseInt failed: "+fmt.Sprint(err)))
						}
						receipt = &anonymityReceipt.Receipt
						receipt.Status = int(status)
						go handler(receipt, nil)
					}
					delete(hc.asyncHandlers, msg.uuid)
				}
				hc.asyncMu.Unlock()
			case blockNotify:
				go hc.updateBlockNumber(msg)
			case amopPushRandom, amopMultiCast:
				go hc.processTopicMessage(msg)
			case amopAuthTopic:
				go hc.processAuthTopicMessage(msg)
				// logrus.Printf("response type:%d seq:%s, msg:%s, err:%v", msg.typeN, msg.uuid, string(msg.body), err)
			case eventLogPush:
				go hc.processEventLogMessage(msg)
			default:
				logrus.Errorf("unknown message type:%d, msg:%+v", msg.typeN, msg)
			}
		}
	}
}

func (hc *channelSession) updateBlockNumber(msg *channelMessage) {
	var blockNumber int64
	var groupID uint64
	topic, err := decodeTopic(msg.body)
	if err != nil {
		logrus.Warnf("decodeTopic msg.body failed, err: %v\n", err)
		return
	}
	if hc.nodeInfo.Protocol == 1 {
		response := strings.Split(string(topic.data), ",")
		blockNumber, err = strconv.ParseInt(response[1], 10, 32)
		if err != nil {
			logrus.Warnf("v1 block notify parse blockNumber failed, %v\n", string(topic.data))
			return
		}
		groupID, err = strconv.ParseUint(response[0], 10, 32)
		if err != nil {
			logrus.Warnf("v1 block notify parse GroupID failed, %v\n", string(topic.data))
			return
		}
	} else {
		var notify struct {
			GroupID     uint64 `json:"groupID"`
			BlockNumber int64  `json:"blockNumber"`
		}
		err = json.Unmarshal(topic.data, &notify)
		if err != nil {
			logrus.Warnf("block notify parse blockNumber failed, %v\n", string(topic.data))
			return
		}
		blockNumber = notify.BlockNumber
		groupID = notify.GroupID
	}
	// logrus.Printf("blockNumber updated %d -> %d", hc.nodeInfo.blockNumber, blockNumber)
	hc.nodeInfo.blockNumber = blockNumber

	if handler, ok := hc.blockNotifyHandlers[groupID]; ok {
		go handler(blockNumber)
	}
}

func (hc *channelSession) subscribeBlockNumberNotify(groupID uint64, handler func(int64)) error {
	if _, ok := hc.blockNotifyHandlers[groupID]; ok {
		return errors.New("the group's blockchain notification has been subscribed")
	}
	hc.blockNotifyMu.Lock()
	hc.blockNotifyHandlers[groupID] = handler
	hc.blockNotifyMu.Unlock()
	if err := hc.subscribeTopic(blockNotifyPrefix+strconv.Itoa(int(groupID)), func(_ []byte, _ *[]byte) {}); err != nil {
		return fmt.Errorf("subscriber group block nofity failed")
	}

	return nil
}

func (hc *channelSession) unSubscribeBlockNumberNotify(groupID uint64) error {
	if _, ok := hc.blockNotifyHandlers[groupID]; !ok {
		return errors.New("group on-chain block notification does not exist")
	}
	if err := hc.unsubscribeTopic(blockNotifyPrefix + strconv.Itoa(int(groupID))); err != nil {
		return err
	}
	hc.blockNotifyMu.Lock()
	delete(hc.blockNotifyHandlers, groupID)
	hc.blockNotifyMu.Unlock()
	return nil
}

// channelServerConn turns a Channel connection into a Conn.
type channelServerConn struct {
	io.Reader
	io.Writer
	r *http.Request
}

func newChannelServerConn(r *http.Request, w http.ResponseWriter) ServerCodec {
	body := io.LimitReader(r.Body, maxRequestContentLength)
	conn := &channelServerConn{Reader: body, Writer: w, r: r}
	return NewJSONCodec(conn)
}

// Close does nothing and always returns nil.
func (t *channelServerConn) Close() error { return nil }

// RemoteAddr returns the peer address of the underlying connection.
func (t *channelServerConn) RemoteAddr() string {
	return t.r.RemoteAddr
}

// SetWriteDeadline does nothing and always returns nil.
func (t *channelServerConn) SetWriteDeadline(time.Time) error { return nil }

func generateRandomNum() []byte {
	// Max random value, a 130-bits integer, i.e 2^130 - 1
	max := new(big.Int)
	max.Exp(big.NewInt(2), big.NewInt(130), nil).Sub(max, big.NewInt(1))
	// Generate cryptographically strong pseudo-random between 0 - max
	n, err := rand.Int(rand.Reader, max)
	if err != nil {
		panic("generate random number failed")
	}
	return n.Bytes()
}
