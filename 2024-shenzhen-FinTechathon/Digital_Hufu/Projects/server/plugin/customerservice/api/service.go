package api

import (
	"crypto/md5"
	"encoding/hex"
	"github.com/flipped-aurora/gin-vue-admin/server/global"
	"github.com/flipped-aurora/gin-vue-admin/server/model/common/response"
	"github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/model"
	"github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/service"
	"github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/tools"
	"github.com/gin-gonic/gin"
	"strconv"
	"time"
)

type AdminServiceApi struct{}

// GetServiceList
// @Tags      sysService
// @Summary   客服列表
// @Security  ApiKeyAuth
// @accept    application/json
// @Produce   application/json
// @Param     data  body      request.PageInfo                                        true  "页码, 每页大小"
// @Success   200   {object}  response.Response{data=response.PageResult,msg=string}  "分页客服列表,返回包括列表,总数,页码,每页数量"
// @Router    /service/get_service_list [post]
func (ad *AdminServiceApi) GetServiceList(c *gin.Context) {
	var pageInfo model.PageInfo
	if err := c.ShouldBindQuery(&pageInfo); err != nil {
		response.FailWithMessage("参数错误:"+err.Error(), c)
		return
	}
	limit := pageInfo.Limit
	offset := pageInfo.Limit * (pageInfo.Page - 1)
	db := global.GVA_DB.Model(&model.SysService{})
	var list []model.SysService
	var total int64
	db.Count(&total)
	err := db.Omit("password").Order("add_time desc").Limit(limit).Offset(offset).Find(&list).Error
	if err != nil {
		response.FailWithMessage("查询失败:"+err.Error(), c)
		return
	}
	response.OkWithDetailed(response.PageResult{
		List:     list,
		Total:    total,
		Page:     pageInfo.Page,
		PageSize: pageInfo.Limit,
	}, "获取成功", c)
}

// SaveService
// @Tags      sysService
// @Summary   添加/更新客服
// @Security  ApiKeyAuth
// @accept    application/json
// @Produce   application/json
// @Param     data  body      request                                       true  ""
// @Success   200   {object}  response.Response{data=response.PageResult,msg=string}  ""
// @Router    /service/save_service [post]
func (ad *AdminServiceApi) SaveService(c *gin.Context) {
	var serviceData model.SysService
	if err := c.ShouldBindJSON(&serviceData); err != nil {
		response.FailWithMessage("参数错误:"+err.Error(), c)
		return
	}
	//校验数据
	ser := service.ServiceGroupApp
	if err := ser.ValidateServiceData(&serviceData); err != nil {
		response.FailWithMessage("操作失败:"+err.Error(), c)
		return
	}
	var msg string
	if serviceData.Password != "" {
		hash := md5.Sum([]byte("gva-service" + serviceData.Password))
		serviceData.Password = hex.EncodeToString(hash[:])
	}

	if serviceData.Id == 0 {
		serviceData.AddTime = time.Now().Unix()
		if err := global.GVA_DB.Create(&serviceData).Error; err != nil {
			response.FailWithMessage("添加失败:"+err.Error(), c)
			return
		}
		msg = "添加成功"
	} else {
		if err := global.GVA_DB.Model(&model.SysService{}).Where("id = ?", serviceData.Id).Updates(serviceData).Error; err != nil {
			response.FailWithMessage("更新失败:"+err.Error(), c)
			return
		}
		msg = "更新成功"
	}
	response.OkWithMessage(msg, c)
}

// DeleteService
// @Tags      sysService
// @Summary   删除客服
// @Security  ApiKeyAuth
// @accept    application/json
// @Produce   application/json
// @Param     data  body      request.id                                        true  "id"
// @Success   200   {object}  response.Response{data=response.PageResult,msg=string}  "成功消息"
// @Router    /service/delete_service?id=xx [delete]
func (ad *AdminServiceApi) DeleteService(c *gin.Context) {
	idParam := c.Query("id")
	id, err := strconv.Atoi(idParam)
	if err != nil {
		response.FailWithMessage("参数错误:"+err.Error(), c)
		return
	}
	var ser model.SysService
	if err := global.GVA_DB.First(&ser, id).Error; err != nil {
		//if errors.Is(err, gorm.ErrRecordNotFound) {
		//
		//}
		response.FailWithMessage("用户不存在:"+err.Error(), c)
		return
	}
	// 删除用户
	if err := global.GVA_DB.Delete(&model.SysService{}, id).Error; err != nil {
		response.FailWithMessage("删除失败:"+err.Error(), c)
		return
	}
	response.OkWithMessage("删除成功", c)
}

// FindService
// @Tags      sysService
// @Summary   查找客服
// @Security  ApiKeyAuth
// @accept    application/json
// @Produce   application/json
// @Param     data  body      request.id                                        true  "id"
// @Success   200   {object}  response.Response{data=response.PageResult,msg=string}  "成功消息"
// @Router    /service/find_service?id=xx [get]
func (ad *AdminServiceApi) FindService(c *gin.Context) {
	idParam := c.Query("id")
	id, err := strconv.Atoi(idParam)
	if err != nil {
		response.FailWithMessage("参数错误:"+err.Error(), c)
		return
	}
	var ser model.SysService
	if err := global.GVA_DB.First(&ser, id).Error; err != nil {
		response.FailWithMessage("客服不存在:"+err.Error(), c)
		return
	}
	ser.Password = ""
	response.OkWithDetailed(ser, "success", c)
}

// AdminServiceLogin
// @Tags      sysService
// @Summary   进入工作台
// @Security  ApiKeyAuth
// @accept    application/json
// @Produce   application/json
// @Param     data  body      request.id                                        true  "id"
// @Success   200   {object}  response.Response{data=response.PageResult,msg=string}  "成功消息"
// @Router    /service/delete_reply/:id [delete]
func (ad *AdminServiceApi) AdminServiceLogin(c *gin.Context) {
	idParam := c.Query("id")
	var ser model.SysService
	if err := global.GVA_DB.First(&ser, idParam).Error; err != nil {
		response.FailWithMessage("客服不存在:"+err.Error(), c)
		return
	}
	data := map[string]interface{}{}

	expTime, token, err := tools.GenerateToken(ser.Id)
	if err != nil {
		response.FailWithMessage("登录失败:"+err.Error(), c)
		return
	}
	data["token"] = token
	data["exp_time"] = expTime

	response.OkWithDetailed(data, "success", c)
}

// AccountServiceLogin
// @Tags      sysService
// @Summary   账户密码登录
// @Security  ApiKeyAuth
// @accept    application/json
// @Produce   application/json
// @Param     data  body      request.id                                        true  "id"
// @Success   200   {object}  response.Response{data=response.PageResult,msg=string}  "成功消息"
// @Router    /service/admin_login?id=xx [get]
func (ad *AdminServiceApi) AccountServiceLogin(c *gin.Context) {
	var loginInfo model.LoginInfo
	if err := c.ShouldBindJSON(&loginInfo); err != nil {
		response.FailWithMessage("参数错误:"+err.Error(), c)
		return
	}
	if loginInfo.Account == "" || loginInfo.Password == "" {
		response.FailWithMessage("账户或密码为空", c)
		return
	}
	var serviceInfo model.SysService
	if err := global.GVA_DB.Limit(1).Where("account=?", loginInfo.Account).Find(&serviceInfo).Error; err != nil {
		response.FailWithMessage("客服不存在:"+err.Error(), c)
		return
	}
	hash := md5.Sum([]byte("gva-service" + loginInfo.Password))
	md5Pwd := hex.EncodeToString(hash[:])
	if md5Pwd != serviceInfo.Password {
		response.FailWithMessage("密码不正确", c)
		return
	}

	data := map[string]interface{}{}
	expTime, token, err := tools.GenerateToken(serviceInfo.Id)
	if err != nil {
		response.FailWithMessage("登录失败:"+err.Error(), c)
		return
	}
	data["token"] = token
	data["exp_time"] = expTime
	response.OkWithDetailed(data, "success", c)
}

// GetScriptList
// @Tags      sysService
// @Summary   客服话术列表
// @Security  ApiKeyAuth
// @accept    application/json
// @Produce   application/json
// @Param     data  body      request.PageInfo                                        true  "页码, 每页大小"
// @Success   200   {object}  response.Response{data=response.PageResult,msg=string}  "分页客服列表,返回包括列表,总数,页码,每页数量"
// @Router    /service/get_script_list [get]
func (ad *AdminServiceApi) GetScriptList(c *gin.Context) {
	var pageInfo model.PageInfo
	if err := c.ShouldBindQuery(&pageInfo); err != nil {
		response.FailWithMessage("参数错误:"+err.Error(), c)
		return
	}
	limit := pageInfo.Limit
	offset := pageInfo.Limit * (pageInfo.Page - 1)
	db := global.GVA_DB.Model(&model.SysServiceScript{})
	var list []model.SysServiceScript
	var total int64
	db.Count(&total)
	err := db.Order("sort desc,add_time desc").Limit(limit).Offset(offset).Find(&list).Error
	if err != nil {
		response.FailWithMessage("查询失败:"+err.Error(), c)
		return
	}
	for k, v := range list {
		t := time.Unix(v.AddTime, 0)
		v.AddTimeStr = t.Format("2006-01-02 15:04:05")
		list[k] = v
	}
	response.OkWithDetailed(response.PageResult{
		List:     list,
		Total:    total,
		Page:     pageInfo.Page,
		PageSize: pageInfo.Limit,
	}, "获取成功", c)
}

// SaveScript
// @Tags      sysService
// @Summary   添加/更新客服话术
// @Security  ApiKeyAuth
// @accept    application/json
// @Produce   application/json
// @Param     data  body      request                                       true  ""
// @Success   200   {object}  response.Response{data=response.PageResult,msg=string}  ""
// @Router    /service/save_script [post]
func (ad *AdminServiceApi) SaveScript(c *gin.Context) {
	var scriptData model.SysServiceScript
	if err := c.ShouldBindJSON(&scriptData); err != nil {
		response.FailWithMessage("参数错误:"+err.Error(), c)
		return
	}
	//校验数据
	ser := service.ServiceGroupApp
	if err := ser.ValidateScriptData(&scriptData); err != nil {
		response.FailWithMessage("操作失败:"+err.Error(), c)
		return
	}
	var msg string
	if scriptData.Id == 0 {
		scriptData.AddTime = time.Now().Unix()
		if err := global.GVA_DB.Create(&scriptData).Error; err != nil {
			response.FailWithMessage("添加失败:"+err.Error(), c)
			return
		}
		msg = "添加成功"
	} else {
		if err := global.GVA_DB.Model(&model.SysServiceScript{}).Where("id = ?", scriptData.Id).Updates(scriptData).Error; err != nil {
			response.FailWithMessage("更新失败:"+err.Error(), c)
			return
		}
		msg = "更新成功"
	}
	response.OkWithMessage(msg, c)
}

// DeleteScript
// @Tags      sysService
// @Summary   删除客服话术
// @Security  ApiKeyAuth
// @accept    application/json
// @Produce   application/json
// @Param     data  body      request.id                                        true  "id"
// @Success   200   {object}  response.Response{data=response.PageResult,msg=string}  "成功消息"
// @Router    /service/delete_script?id=xxx [delete]
func (ad *AdminServiceApi) DeleteScript(c *gin.Context) {
	idParam := c.Query("id")
	id, err := strconv.Atoi(idParam)
	if err != nil {
		response.FailWithMessage("参数错误:"+err.Error(), c)
		return
	}
	var ser model.SysServiceScript
	if err := global.GVA_DB.First(&ser, id).Error; err != nil {
		response.FailWithMessage("话术不存在或已删除:"+err.Error(), c)
		return
	}
	// 删除
	if err := global.GVA_DB.Delete(&model.SysServiceScript{}, id).Error; err != nil {
		response.FailWithMessage("删除失败:"+err.Error(), c)
		return
	}
	response.OkWithMessage("删除成功", c)
}

// FindScript
// @Tags      sysService
// @Summary   查找话术
// @Security  ApiKeyAuth
// @accept    application/json
// @Produce   application/json
// @Param     data  body      request.id                                        true  "id"
// @Success   200   {object}  response.Response{data=response.PageResult,msg=string}  "成功消息"
// @Router    /service/find_script?id=xx [get]
func (ad *AdminServiceApi) FindScript(c *gin.Context) {
	idParam := c.Query("id")
	id, err := strconv.Atoi(idParam)
	if err != nil {
		response.FailWithMessage("参数错误:"+err.Error(), c)
		return
	}
	var ser model.SysServiceScript
	if err := global.GVA_DB.First(&ser, id).Error; err != nil {
		response.FailWithMessage("话术不存在:"+err.Error(), c)
		return
	}
	response.OkWithDetailed(ser, "success", c)
}

// AutoReplyList
// @Tags      sysService
// @Summary   自动回复列表
// @Security  ApiKeyAuth
// @accept    application/json
// @Produce   application/json
// @Param     data  body      request.PageInfo                                        true  "页码, 每页大小"
// @Success   200   {object}  response.Response{data=response.PageResult,msg=string}  "分页客服列表,返回包括列表,总数,页码,每页数量"
// @Router    /service/auto_reply_list [get]
func (ad *AdminServiceApi) AutoReplyList(c *gin.Context) {
	var pageInfo model.AutoPageInfo
	if err := c.ShouldBindQuery(&pageInfo); err != nil {
		response.FailWithMessage("参数错误:"+err.Error(), c)
		return
	}
	limit := pageInfo.Limit
	offset := pageInfo.Limit * (pageInfo.Page - 1)
	db := global.GVA_DB.Model(&model.SysServiceReply{})
	var list []model.SysServiceReply
	var total int64
	db.Count(&total)
	err := db.Order("add_time desc").Limit(limit).Offset(offset).Find(&list).Error
	if err != nil {
		response.FailWithMessage("查询失败:"+err.Error(), c)
		return
	}
	for k, v := range list {
		t := time.Unix(v.AddTime, 0)
		v.AddTimeStr = t.Format("2006-01-02 15:04:05")
		list[k] = v
	}
	response.OkWithDetailed(response.PageResult{
		List:     list,
		Total:    total,
		Page:     pageInfo.Page,
		PageSize: pageInfo.Limit,
	}, "获取成功", c)
}

// SaveReply
// @Tags      sysService
// @Summary   添加/更新自动回复
// @Security  ApiKeyAuth
// @accept    application/json
// @Produce   application/json
// @Param     data  body      request                                       true  ""
// @Success   200   {object}  response.Response{data=response.PageResult,msg=string}  ""
// @Router    /service/save_reply [post]
func (ad *AdminServiceApi) SaveReply(c *gin.Context) {
	var replyData model.SysServiceReply
	if err := c.ShouldBindJSON(&replyData); err != nil {
		response.FailWithMessage("参数错误:"+err.Error(), c)
		return
	}
	//校验数据
	ser := service.ServiceGroupApp
	if err := ser.ValidateReplyData(&replyData); err != nil {
		response.FailWithMessage("操作失败:"+err.Error(), c)
		return
	}
	var msg string
	if replyData.Id == 0 {
		replyData.AddTime = time.Now().Unix()
		if err := global.GVA_DB.Create(&replyData).Error; err != nil {
			response.FailWithMessage("添加失败:"+err.Error(), c)
			return
		}
		msg = "添加成功"
	} else {
		if err := global.GVA_DB.Model(&model.SysServiceReply{}).Where("id = ?", replyData.Id).Updates(replyData).Error; err != nil {
			response.FailWithMessage("更新失败:"+err.Error(), c)
			return
		}
		msg = "更新成功"
	}
	response.OkWithMessage(msg, c)
}

// DeleteReply
// @Tags      sysService
// @Summary   删除自动回复
// @Security  ApiKeyAuth
// @accept    application/json
// @Produce   application/json
// @Param     data  body      request.id                                        true  "id"
// @Success   200   {object}  response.Response{data=response.PageResult,msg=string}  "成功消息"
// @Router    /service/delete_reply?id=xx [delete]
func (ad *AdminServiceApi) DeleteReply(c *gin.Context) {
	idParam := c.Query("id")
	id, err := strconv.Atoi(idParam)
	if err != nil {
		response.FailWithMessage("参数错误:"+err.Error(), c)
		return
	}
	var ser model.SysServiceReply
	if err := global.GVA_DB.First(&ser, id).Error; err != nil {
		response.FailWithMessage("内容不存在或已删除:"+err.Error(), c)
		return
	}
	// 删除数据
	if err := global.GVA_DB.Delete(&model.SysServiceReply{}, id).Error; err != nil {
		response.FailWithMessage("删除失败:"+err.Error(), c)
		return
	}
	response.OkWithMessage("删除成功", c)
}

// FindReply
// @Tags      sysService
// @Summary   查找自动回复详情
// @Security  ApiKeyAuth
// @accept    application/json
// @Produce   application/json
// @Param     data  body      request.id                                        true  "id"
// @Success   200   {object}  response.Response{data=response.PageResult,msg=string}  "成功消息"
// @Router    /service/find_reply?id=xxx [get]
func (ad *AdminServiceApi) FindReply(c *gin.Context) {
	idParam := c.Query("id")
	id, err := strconv.Atoi(idParam)
	if err != nil {
		response.FailWithMessage("参数错误:"+err.Error(), c)
		return
	}
	var ser model.SysServiceReply
	if err := global.GVA_DB.First(&ser, id).Error; err != nil {
		response.FailWithMessage("自动回复内容不存在:"+err.Error(), c)
		return
	}
	response.OkWithDetailed(ser, "success", c)
}
