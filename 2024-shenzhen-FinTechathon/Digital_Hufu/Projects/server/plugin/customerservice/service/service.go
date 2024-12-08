package service

import (
	"encoding/base64"
	"errors"
	"github.com/flipped-aurora/gin-vue-admin/server/global"
	"github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/model"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"time"
)

type CustomerServiceService struct{}

func (e *CustomerServiceService) PlugService() (err error) {
	// 写你的业务逻辑
	return nil
}

func (e *CustomerServiceService) ValidateServiceData(sys *model.SysService) error {
	if sys.Uid == 0 {
		return errors.New("客服关联的用户id不能为空")
	} else {
		db := global.GVA_DB.Model(&model.SysService{})
		if sys.Id > 0 {
			db = db.Where("uid=?", sys.Uid).Where("id<>?", sys.Id)
		} else {
			db = db.Where("uid=?", sys.Uid)
		}
		var dCount int64
		db.Count(&dCount)
		if dCount > 0 {
			return errors.New("用户id已关联其他客服，请重新输入")
		}
	}
	db := global.GVA_DB.Model(&model.SysService{})
	if sys.Id == 0 {
		if sys.Password == "" {
			return errors.New("客服密码必须填写")
		}
		db = db.Where("account=?", sys.Account)
	} else {
		db = db.Where("account=?", sys.Account).Where("id<>?", sys.Id)
		var dCount int64
		db.Count(&dCount)
		if dCount > 0 {
			return errors.New("账户已存在，请重新输入")
		}
	}
	if sys.Account == "" {
		return errors.New("客服账户必须填写")
	}
	if sys.Nickname == "" {
		return errors.New("客服名称必须填写")
	}
	if sys.Avatar == "" {
		return errors.New("客服头像必须选择")
	}

	return nil
}

func (e *CustomerServiceService) ValidateScriptData(sys *model.SysServiceScript) error {
	if sys.Title == "" {
		return errors.New("话术标题必须填写")
	}
	if sys.Content == "" {
		return errors.New("话术内容必须填写")
	}
	return nil
}

func (e *CustomerServiceService) ValidateReplyData(sys *model.SysServiceReply) error {
	if sys.Keyword == "" {
		return errors.New("关键字必须填写")
	}
	if sys.Content == "" {
		return errors.New("回复内容必须填写")
	}
	return nil
}

func (e *CustomerServiceService) GetUrlHost(c *gin.Context) string {
	host := c.Request.Host
	scheme := "http"
	if c.Request.TLS != nil {
		scheme = "https"
	}
	referer := c.Request.Referer()
	if referer != "" {
		return referer
	}
	return scheme + "://" + host + "/"
}

func (e *CustomerServiceService) CreateMsg(kfInfo model.SysService, userInfo model.SysTestUser, msgType int64, content string, isKf string) (err error) {
	msgRecord := &model.SysServiceRecord{
		ServiceId:   kfInfo.Id,
		Uid:         userInfo.Id,
		Message:     base64.StdEncoding.EncodeToString([]byte(content)),
		MessageType: msgType,
		UpdateTime:  time.Now().Unix(),
		Avatar:      userInfo.Avatar,
		Nickname:    userInfo.Nickname,
		Online:      1,
	}
	var record model.SysServiceRecord

	eErr := global.GVA_DB.Where("service_id = ?", kfInfo.Id).Where("uid = ?", userInfo.Id).First(&record).Error
	if errors.Is(eErr, gorm.ErrRecordNotFound) {
		msgRecord.AddTime = time.Now().Unix()
		global.GVA_DB.Create(msgRecord)
	} else {
		global.GVA_DB.Model(&model.SysServiceRecord{}).Where("id = ?", record.Id).Updates(msgRecord)
	}

	//插入消息记录
	msg := map[string]interface{}{
		"service_id": kfInfo.Id,
		"uid":        userInfo.Id,
		"content":    base64.StdEncoding.EncodeToString([]byte(content)),
		"msg_type":   msgType,
		"is_view":    0,
		"add_time":   time.Now().Unix(),
		"is_kf":      isKf,
	}
	err = global.GVA_DB.Model(&model.SysServiceMsg{}).Create(msg).Error
	return err
}
