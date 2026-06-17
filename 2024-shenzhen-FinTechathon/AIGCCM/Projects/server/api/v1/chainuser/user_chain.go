package chainuser

import (
	"github.com/flipped-aurora/gin-vue-admin/server/global"
	"github.com/flipped-aurora/gin-vue-admin/server/model/chainuser"
	chainuserReq "github.com/flipped-aurora/gin-vue-admin/server/model/chainuser/request"
	"github.com/flipped-aurora/gin-vue-admin/server/model/common/request"
	"github.com/flipped-aurora/gin-vue-admin/server/model/common/response"
	"github.com/flipped-aurora/gin-vue-admin/server/service"
	"github.com/flipped-aurora/gin-vue-admin/server/utils"
	"github.com/gin-gonic/gin"
	"go.uber.org/zap"
)

type UserchainApi struct {
}

var chainuseService = service.ServiceGroupApp.ChainuserServiceGroup.UserchainService

// CreateUserchain 创建区块链用户
// @Tags Userchain
// @Summary 创建区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body chainuser.Userchain true "创建区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"创建成功"}"
// @Router /chainuse/createUserchain [post]
func (chainuseApi *UserchainApi) CreateUserchain(c *gin.Context) {
	var chainuse chainuser.Userchain
	err := c.ShouldBindJSON(&chainuse)
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	chainuse.CreatedBy = utils.GetUserID(c)
	if err := chainuseService.CreateUserchain(&chainuse); err != nil {
		global.GVA_LOG.Error("创建失败!", zap.Error(err))
		response.FailWithMessage("创建失败", c)
	} else {
		response.OkWithMessage("创建成功", c)
	}
}

// DeleteUserchain 删除区块链用户
// @Tags Userchain
// @Summary 删除区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body chainuser.Userchain true "删除区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /chainuse/deleteUserchain [delete]
func (chainuseApi *UserchainApi) DeleteUserchain(c *gin.Context) {
	var chainuse chainuser.Userchain
	err := c.ShouldBindJSON(&chainuse)
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	chainuse.DeletedBy = utils.GetUserID(c)
	if err := chainuseService.DeleteUserchain(chainuse); err != nil {
		global.GVA_LOG.Error("删除失败!", zap.Error(err))
		response.FailWithMessage("删除失败", c)
	} else {
		response.OkWithMessage("删除成功", c)
	}
}

// DeleteUserchainByIds 批量删除区块链用户
// @Tags Userchain
// @Summary 批量删除区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body request.IdsReq true "批量删除区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"批量删除成功"}"
// @Router /chainuse/deleteUserchainByIds [delete]
func (chainuseApi *UserchainApi) DeleteUserchainByIds(c *gin.Context) {
	var IDS request.IdsReq
	err := c.ShouldBindJSON(&IDS)
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	deletedBy := utils.GetUserID(c)
	if err := chainuseService.DeleteUserchainByIds(IDS, deletedBy); err != nil {
		global.GVA_LOG.Error("批量删除失败!", zap.Error(err))
		response.FailWithMessage("批量删除失败", c)
	} else {
		response.OkWithMessage("批量删除成功", c)
	}
}

// UpdateUserchain 更新区块链用户
// @Tags Userchain
// @Summary 更新区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body chainuser.Userchain true "更新区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"更新成功"}"
// @Router /chainuse/updateUserchain [put]
func (chainuseApi *UserchainApi) UpdateUserchain(c *gin.Context) {
	var chainuse chainuser.Userchain
	err := c.ShouldBindJSON(&chainuse)
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	chainuse.UpdatedBy = utils.GetUserID(c)
	if err := chainuseService.UpdateUserchain(chainuse); err != nil {
		global.GVA_LOG.Error("更新失败!", zap.Error(err))
		response.FailWithMessage("更新失败", c)
	} else {
		response.OkWithMessage("更新成功", c)
	}
}

// FindUserchain 用id查询区块链用户
// @Tags Userchain
// @Summary 用id查询区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query chainuser.Userchain true "用id查询区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"查询成功"}"
// @Router /chainuse/findUserchain [get]
func (chainuseApi *UserchainApi) FindUserchain(c *gin.Context) {
	var chainuse chainuser.Userchain
	err := c.ShouldBindQuery(&chainuse)
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	if rechainuse, err := chainuseService.GetUserchain(chainuse.Name); err != nil {
		global.GVA_LOG.Error("查询失败!", zap.Error(err))
		response.FailWithMessage("查询失败", c)
	} else {
		response.OkWithData(gin.H{"rechainuse": rechainuse}, c)
	}
}

// GetUserchainList 分页获取区块链用户列表
// @Tags Userchain
// @Summary 分页获取区块链用户列表
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query chainuserReq.UserchainSearch true "分页获取区块链用户列表"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /chainuse/getUserchainList [get]
func (chainuseApi *UserchainApi) GetUserchainList(c *gin.Context) {
	var pageInfo chainuserReq.UserchainSearch
	err := c.ShouldBindQuery(&pageInfo)
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	if list, total, err := chainuseService.GetUserchainInfoList(pageInfo); err != nil {
		global.GVA_LOG.Error("获取失败!", zap.Error(err))
		response.FailWithMessage("获取失败", c)
	} else {
		response.OkWithDetailed(response.PageResult{
			List:     list,
			Total:    total,
			Page:     pageInfo.Page,
			PageSize: pageInfo.PageSize,
		}, "获取成功", c)
	}
}
