package ws

import (
	"encoding/json"
	"fmt"
	"github.com/flipped-aurora/gin-vue-admin/server/global"
	"github.com/flipped-aurora/gin-vue-admin/server/model/common/response"
	sysModel "github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/model"
	"github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/tools"
	"github.com/gin-gonic/gin"
	"github.com/gorilla/websocket"
	"net/http"
	"strconv"
	"time"
)

type Message struct {
	Sender    string `json:"sender"`
	Receiver  string `json:"receiver"`
	Content   string `json:"content"`
	MsgType   string `json:"msg_type"` //对应msg表的msg_type
	Role      string `json:"role"`
	Timestamp int64  `json:"timestamp"`
	Nickname  string `json:"nickname"`
	AvatarUrl string `json:"avatar_url"`
	IsKf      int64  `json:"is_kf"`
}

type TypeMsg struct {
	Type string      `json:"type"`
	Data interface{} `json:"data,omitempty"`
}

type Client struct {
	UserID       string
	Role         string
	Socket       *websocket.Conn
	Send         chan []byte
	LastPingTime time.Time
}

type ClientManager struct {
	Clients    map[string]*Client
	Broadcast  chan TypeMsg
	Register   chan *Client
	Unregister chan *Client
}

var Manager = ClientManager{
	Clients:    make(map[string]*Client),
	Broadcast:  make(chan TypeMsg),
	Register:   make(chan *Client),
	Unregister: make(chan *Client),
}

// 定时检查连接的活动状态
func (manager *ClientManager) CheckClientActivity() {
	ticker := time.NewTicker(5 * time.Second)
	defer ticker.Stop()

	for {
		<-ticker.C
		now := time.Now()

		for ck, client := range manager.Clients {
			// 如果超过一定时间没有收到ping，则断开连接
			fmt.Println(ck)
			fmt.Println(now.Sub(client.LastPingTime))
			if now.Sub(client.LastPingTime) > 120*time.Second {
				client.Socket.Close()
				delete(manager.Clients, ck)
				//设置离线
				if client.Role == "user" {
					setUserOnline("offline", client.UserID)
				}
			}
		}
	}
}

func (manager *ClientManager) Start() {
	for {
		select {
		case conn := <-manager.Register:
			key := conn.Role + conn.UserID
			if existingConn, ok := manager.Clients[key]; ok {
				existingConn.Socket.Close()
				delete(manager.Clients, key)
			}
			fmt.Println(key)
			manager.Clients[key] = conn
		case conn := <-manager.Unregister:
			key := conn.Role + conn.UserID
			if existingConn, ok := manager.Clients[key]; ok && existingConn == conn {
				delete(manager.Clients, key)
			}
		case message := <-manager.Broadcast:
			data := message.Data.(map[string]interface{})
			receiver := data["receiver"].(string)
			receiverKey := "user" + receiver
			if data["role"].(string) == "user" {
				receiverKey = "kf" + receiver
			}
			if client, ok := manager.Clients[receiverKey]; ok {
				str, _ := json.Marshal(message)
				client.Send <- str
			} else {
				fmt.Println(receiverKey + "链接不存在")
			}
		}
	}
}

func (c *Client) Read() {
	defer func() {
		Manager.Unregister <- c
		c.Socket.Close()
	}()
	c.Socket.SetReadLimit(512)

	for {
		_, message, err := c.Socket.ReadMessage()
		if err != nil {
			break
		}
		var msg TypeMsg
		if err := json.Unmarshal(message, &msg); err != nil {
			continue
		}
		switch msg.Type {
		case "ping":
			// 更新最后一次收到ping消息的时间
			c.LastPingTime = time.Now()

			// 回复pong消息
			pongMsg := TypeMsg{
				Type: "pong",
				Data: time.Now().Unix(),
			}
			pongStr, _ := json.Marshal(pongMsg)
			c.Send <- pongStr

		case "message":
			//发送消息走的后台接口去触发广播，改成前端发送消息走这里
			Manager.Broadcast <- msg
		}
	}
}

func (c *Client) Write() {
	defer func() {
		c.Socket.Close()
	}()
	for {
		select {
		case message, ok := <-c.Send:
			c.Socket.SetWriteDeadline(time.Now().Add(10 * time.Second))
			if !ok {
				c.Socket.WriteMessage(websocket.CloseMessage, []byte{})
				return
			}
			if err := c.Socket.WriteMessage(websocket.TextMessage, message); err != nil {
				return
			}
		}
	}
}

var Upgrader = websocket.Upgrader{
	ReadBufferSize:  1024,
	WriteBufferSize: 1024,
	CheckOrigin: func(r *http.Request) bool {
		return true
	},
}

func WsServe(ctx *gin.Context) {
	//token := ctx.Query("token")
	userID := ctx.Query("user_id")

	conn, err := Upgrader.Upgrade(ctx.Writer, ctx.Request, nil)
	if err != nil {
		http.NotFound(ctx.Writer, ctx.Request)
		return
	}
	client := &Client{
		UserID:       userID,
		Role:         "user",
		Socket:       conn,
		Send:         make(chan []byte),
		LastPingTime: time.Now(),
	}

	Manager.Register <- client
	setUserOnline("online", userID)
	go client.Read()
	go client.Write()
}

func ServeWsForKefu(ctx *gin.Context) {
	token := ctx.Query("token")
	claims, err := tools.ValidateToken(token)
	if err != nil {
		response.FailWithMessage("token已失效", ctx)
		return
	}
	kfId := claims.ServiceId
	db := global.GVA_DB.Model(&sysModel.SysService{})
	var info sysModel.SysService
	err = db.Find(&info).Error
	if err != nil {
		response.FailWithMessage("客服不存在", ctx)
		return
	}
	conn, err2 := Upgrader.Upgrade(ctx.Writer, ctx.Request, nil)
	if err2 != nil {
		http.NotFound(ctx.Writer, ctx.Request)
		return
	}
	client := &Client{
		UserID:       fmt.Sprintf("%v", kfId),
		Role:         "kf",
		Socket:       conn,
		Send:         make(chan []byte),
		LastPingTime: time.Now(),
	}
	Manager.Register <- client

	go client.Read()
	go client.Write()
}

func setUserOnline(cType string, Id string) {
	//给用户在record表里的客服广播此用户离线
	var list []sysModel.SysServiceRecord
	err := global.GVA_DB.Where("uid=?", Id).Find(&list).Error
	if err == nil && len(list) > 0 {
		for _, rec := range list {
			strSerId := strconv.FormatInt(rec.ServiceId, 10)
			roleKey := "kf" + strSerId
			fmt.Println(roleKey)
			serviceClient, ok := Manager.Clients[roleKey]
			if serviceClient != nil && ok {
				dataMsg := Message{
					MsgType:  "1",
					Sender:   Id,
					Receiver: strSerId,
					Role:     "user",
				}
				sendMsg := TypeMsg{
					Type: cType,
					Data: dataMsg,
				}
				str, _ := json.Marshal(sendMsg)
				serviceClient.Send <- str
			}
		}
	}
}
