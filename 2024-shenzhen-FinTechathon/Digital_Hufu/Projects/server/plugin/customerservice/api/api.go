package api

import (
	"crypto/md5"
	"encoding/base64"
	"encoding/hex"
	"encoding/json"
	"errors"
	"fmt"
	"github.com/flipped-aurora/gin-vue-admin/server/global"
	"github.com/flipped-aurora/gin-vue-admin/server/model/common/response"
	sysModel "github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/model"
	"github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/service"
	"github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/service/ws"
	"github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/tools"
	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
	"go.uber.org/zap"
	"gorm.io/gorm"
	"net/http"
	"path/filepath"
	"sort"
	"strconv"
	"time"
)

type CustomerServiceApi struct{}

func (cus *CustomerServiceApi) ServeWs(ctx *gin.Context) {
	ws.WsServe(ctx)
}

func (cus *CustomerServiceApi) ServeWsForKefu(ctx *gin.Context) {
	ws.ServeWsForKefu(ctx)
}

func (cus *CustomerServiceApi) HandleTransfer(c *gin.Context) {
	var transferReq struct {
		FromAgent string `json:"from_agent"`
		ToAgent   string `json:"to_agent"`
		UserID    string `json:"user_id"`
	}
	if err := c.ShouldBindJSON(&transferReq); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	// 更新用户与客服的映射关系
	// 例如：userAgentMap[transferReq.UserID] = transferReq.ToAgent

	c.JSON(http.StatusOK, gin.H{"status": "success"})
}

func (cus *CustomerServiceApi) GetKefuInfo(c *gin.Context) {
	serviceIdAny, ok := c.Get("service_id")
	uidStr := c.Query("uid")
	var serviceId int64
	if !ok && uidStr != "" {
		var recordData sysModel.SysServiceRecord
		result := global.GVA_DB.Where("uid=?", uidStr).Order("update_time DESC").Limit(1).Find(&recordData)
		if result.RowsAffected == 0 || result.Error != nil {
			//直接查询service表
			result2 := global.GVA_DB.Model(&sysModel.SysService{}).Select("id").Where("`status`=?", 1).Order("add_time DESC").Limit(1).Scan(&serviceId)
			fmt.Println(result2)
			fmt.Println(serviceId)
			if result2.Error != nil || result2.RowsAffected == 0 {
				response.FailWithMessage("获取客服信息失败-1", c)
				return
			}
		} else {
			serviceId = recordData.ServiceId
		}

	} else {
		serviceId, _ = serviceIdAny.(int64)
	}
	var serviceData sysModel.SysService
	result3 := global.GVA_DB.Select("id,uid,online,avatar,nickname,add_time,status").Where("id=?", serviceId).Where("`status`=?", 1).Order("add_time DESC").Limit(1).Find(&serviceData)

	if result3.Error != nil || result3.RowsAffected == 0 {
		response.FailWithMessage("获取客服信息失败-2", c)
		return
	}

	response.OkWithDetailed(serviceData, "获取成功", c)
}

func (cus *CustomerServiceApi) SendMsg(c *gin.Context) {
	var msgJson ws.Message
	if jsErr := c.ShouldBindJSON(&msgJson); jsErr != nil {
		fmt.Println(jsErr)
		response.FailWithMessage("参数有误-1", c)
		return
	}
	fromIdStr := msgJson.Sender
	toIdStr := msgJson.Receiver
	content := msgJson.Content
	cType := msgJson.Role
	msgTypeStr := msgJson.MsgType
	if content == "" || fromIdStr == "" || toIdStr == "" || msgTypeStr == "" {
		response.FailWithMessage("参数有误-2", c)
		return
	}
	toId, err_1 := strconv.ParseInt(toIdStr, 10, 64)
	fromId, err_2 := strconv.ParseInt(fromIdStr, 10, 64)
	msgType, err_3 := strconv.ParseInt(msgTypeStr, 10, 64)
	if err_1 != nil || err_2 != nil || err_3 != nil {
		response.FailWithMessage("参数有误", c)
		return
	}
	//限流
	if !tools.LimitFreqSingle("send_message:"+c.ClientIP(), 1, 2) {
		response.FailWithMessage("发送频率过快", c)
		return
	}
	var kfInfo sysModel.SysService
	var userInfo sysModel.SysTestUser
	var err, err2 error
	isKf := "0"
	if cType == "kf" {
		err = global.GVA_DB.Where("id = ?", fromId).First(&kfInfo).Error
		err2 = global.GVA_DB.Where("id = ?", toId).First(&userInfo).Error
		isKf = "1"

	} else if cType == "user" {
		err = global.GVA_DB.Where("id = ?", toId).First(&kfInfo).Error
		err2 = global.GVA_DB.Where("id = ?", fromId).First(&userInfo).Error
	}
	if err != nil || err2 != nil {
		response.FailWithMessage("获取失败-1", c)
		return
	} else if errors.Is(err, gorm.ErrRecordNotFound) || errors.Is(err2, gorm.ErrRecordNotFound) {
		response.FailWithMessage("获取失败-2", c)
		return
	}

	ser := service.ServiceGroupApp
	cErr := ser.CreateMsg(kfInfo, userInfo, msgType, content, isKf)
	if cErr != nil {
		response.FailWithMessage("发送失败", c)
		return
	}
	message := ws.Message{
		Sender:    fromIdStr,
		Receiver:  toIdStr,
		Content:   content,
		MsgType:   msgTypeStr,
		Role:      "kf",
		Timestamp: time.Now().Unix(),
	}
	var key string
	if cType == "kf" {
		//查找指定用户广播消息
		key = "user" + toIdStr
		message.AvatarUrl = kfInfo.Avatar
		message.Nickname = kfInfo.Nickname
	} else if cType == "user" {
		//查找指定客服广播消息
		key = "kf" + toIdStr
		message.Role = "user"
		message.AvatarUrl = userInfo.Avatar
		message.Nickname = userInfo.Nickname
	}
	conn, ok := ws.Manager.Clients[key]
	if conn != nil && ok {
		sendMsg := ws.TypeMsg{
			Type: "message",
			Data: message,
		}
		str, _ := json.Marshal(sendMsg)
		conn.Send <- str

		if cType == "user" {
			//客服给用户发送自动回复消息
			var autoReply sysModel.SysServiceReply
			autoContent := ""
			var autoMsgType int64
			aErr := global.GVA_DB.Where("is_complete = ? AND `status` = ? AND keyword = ?", 1, 1, content).First(&autoReply).Error
			fmt.Println(aErr)
			if aErr == nil {
				fmt.Println(autoReply)
				autoContent = autoReply.Content
				autoMsgType = autoReply.ReplyType
			} else {
				aErr = global.GVA_DB.Where("is_complete = ? AND `status` = ? AND keyword LIKE ?", 0, 1, "%"+content+"%").First(&autoReply).Error
				if aErr == nil {
					autoContent = autoReply.Content
					autoMsgType = autoReply.ReplyType
				}
			}
			if autoContent != "" {
				if autoMsgType == 2 {
					autoMsgType = 3 //图片
				}
				aErr = ser.CreateMsg(kfInfo, userInfo, autoMsgType, autoContent, "1")
				if aErr == nil {
					autoUidStr := strconv.FormatInt(userInfo.Id, 10)
					message.Sender = strconv.FormatInt(kfInfo.Id, 10)
					message.Receiver = autoUidStr
					message.MsgType = strconv.FormatInt(autoMsgType, 10)
					message.Content = autoContent
					message.IsKf = 1
					message.Role = "kf"
					message.AvatarUrl = kfInfo.Avatar
					message.Nickname = kfInfo.Nickname
					sendMsg.Data = message
					autoStr, _ := json.Marshal(sendMsg)
					kfConn, isOk := ws.Manager.Clients["user"+autoUidStr]
					if kfConn != nil && isOk {
						kfConn.Send <- autoStr
					}
				}
			}
		}
	}

	response.OkWithDetailed(nil, "发送成功", c)
}

func (cus *CustomerServiceApi) GetMsgList(c *gin.Context) {
	uid := c.Query("uid")
	serviceId, ok := c.Get("service_id") //jwt里解出的
	if !ok {
		//前端测试用户连接请求消息列表
		serviceId = c.Query("kf_id")
	}
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("page_size", "10"))
	if pageSize > 20 {
		pageSize = 20
	}
	offset := pageSize * (page - 1)
	var total int64
	var list []sysModel.SysServiceMsg
	global.GVA_DB.Model(&sysModel.SysServiceMsg{}).Where("uid=?", uid).Where("service_id=?", serviceId).Count(&total)
	err := global.GVA_DB.Where("uid=?", uid).Where("service_id=?", serviceId).Limit(pageSize).Offset(offset).Order("add_time desc").Find(&list).Error
	if err != nil {
		global.GVA_LOG.Error("获取失败!", zap.Error(err))
		response.FailWithMessage("获取失败", c)
		return
	}
	if len(list) > 0 {
		sort.Slice(list, func(i, j int) bool {
			return list[i].AddTime < list[j].AddTime
		})
		for k, v := range list {
			decoded, _ := base64.StdEncoding.DecodeString(v.Content)
			v.Content = string(decoded)
			list[k] = v
		}
	}
	response.OkWithDetailed(response.PageResult{
		List:     list,
		Total:    total,
		Page:     page,
		PageSize: pageSize,
	}, "获取成功", c)
}

func (cus *CustomerServiceApi) GetMsgUser(c *gin.Context) {
	kfId, _ := c.Get("service_id")
	var list []sysModel.SysServiceRecord
	err := global.GVA_DB.Where("service_id=?", kfId).Find(&list).Error
	if err != nil {
		response.FailWithMessage("获取失败", c)
		return
	}
	if len(list) > 0 {
		//判断用户在线状况
		for k, v := range list {
			userKey := "user" + strconv.FormatInt(v.Uid, 10)
			isClent, ok := ws.Manager.Clients[userKey]
			if ok && isClent != nil {
				v.Online = 1
			} else {
				v.Online = 0
			}
			decoded, _ := base64.StdEncoding.DecodeString(v.Message)
			v.Message = string(decoded)
			//查找未读消息数
			var noCount int64
			global.GVA_DB.Model(&sysModel.SysServiceMsg{}).Where("is_view=?", 0).Where("is_kf=?", 0).Where("service_id=?", kfId).Where("uid=?", v.Uid).Count(&noCount)
			v.NoRead = noCount
			v.AddTimeStr = tools.FormatTimestamp(v.UpdateTime)
			if v.MessageType == 3 {
				v.Message = "[图片]"
			}
			list[k] = v
		}
		sort.Slice(list, func(i, j int) bool {
			if list[i].Online != list[j].Online {
				return list[i].Online > list[j].Online
			}
			return list[i].AddTime > list[j].AddTime
		})
	}
	response.OkWithDetailed(list, "获取成功", c)
}

func (cus *CustomerServiceApi) SetMsgView(c *gin.Context) {
	kfId, _ := c.Get("service_id")
	uid := c.Query("uid")
	global.GVA_DB.Model(&sysModel.SysServiceMsg{}).Where(map[string]interface{}{"is_kf": 0, "service_id": kfId, "is_view": 0, "uid": uid}).Update("is_view", 1)
	response.Ok(c)
}

func (cus *CustomerServiceApi) UploadFile(c *gin.Context) {
	file, err := c.FormFile("file")
	if err != nil {
		response.FailWithMessage("上传失败", c)
		return
	}
	extension := filepath.Ext(file.Filename)
	newUUID := uuid.New().String()
	hash := md5.Sum([]byte("gva-service" + newUUID))
	md5Pwd := hex.EncodeToString(hash[:])
	filename := md5Pwd + extension
	if err := c.SaveUploadedFile(file, "./uploads/file/"+filename); err != nil {
		response.FailWithMessage("上传失败-2", c)
		return

	}
	ser := service.ServiceGroupApp
	url := ser.GetUrlHost(c)
	response.OkWithDetailed(url+"api/uploads/file/"+filename, "获取成功", c)
	return
}

func (cus *CustomerServiceApi) GetTestMsgList(c *gin.Context) {
	uid := c.Query("uid")
	serviceId := c.Query("service_id")
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("page_size", "10"))
	if pageSize > 20 {
		pageSize = 20
	}
	offset := pageSize * (page - 1)
	var total int64
	var list []sysModel.SysServiceMsg
	global.GVA_DB.Model(&sysModel.SysServiceMsg{}).Where("uid=?", uid).Where("service_id=?", serviceId).Count(&total)
	err := global.GVA_DB.Where("uid=?", uid).Where("service_id=?", serviceId).Limit(pageSize).Offset(offset).Order("add_time desc").Find(&list).Error
	if err != nil {
		global.GVA_LOG.Error("获取失败!", zap.Error(err))
		response.FailWithMessage("获取失败", c)
		return
	}
	if len(list) > 0 {
		sort.Slice(list, func(i, j int) bool {
			return list[i].AddTime < list[j].AddTime
		})
		for k, v := range list {
			decoded, _ := base64.StdEncoding.DecodeString(v.Content)
			v.Content = string(decoded)
			list[k] = v
		}
	}
	response.OkWithDetailed(response.PageResult{
		List:     list,
		Total:    total,
		Page:     page,
		PageSize: pageSize,
	}, "获取成功", c)
}

func (cus *CustomerServiceApi) GetUserInfo(c *gin.Context) {
	uidStr := c.Query("uid")
	var test sysModel.SysTestUser
	result := global.GVA_DB.Where("id=?", uidStr).Limit(1).Find(&test)

	if result.Error != nil || result.RowsAffected == 0 {
		response.FailWithMessage("获取用户信息失败", c)
		return
	}

	response.OkWithDetailed(test, "获取成功", c)
}

func (cus *CustomerServiceApi) GetServiceScript(c *gin.Context) {
	rType := c.Query("type")
	db := global.GVA_DB.Model(&sysModel.SysServiceScript{})
	if rType == "1" {
		serviceId, ok := c.Get("service_id")
		if serviceId != "" && ok {
			db = db.Where("service_id=?", serviceId)
		}
	} else {
		db = db.Where("service_id=?", 0)
	}
	var list []sysModel.SysServiceScript
	err := db.Order("add_time desc").Limit(20).Offset(0).Find(&list).Error
	if err != nil {
		response.FailWithMessage("查询失败:"+err.Error(), c)
		return
	}
	response.OkWithDetailed(list, "获取成功", c)
}
