package knowledge

import (
	"github.com/flipped-aurora/gin-vue-admin/server/global"
    "github.com/flipped-aurora/gin-vue-admin/server/model/knowledge"
    "github.com/flipped-aurora/gin-vue-admin/server/model/common/request"
    knowledgeReq "github.com/flipped-aurora/gin-vue-admin/server/model/knowledge/request"
    "github.com/flipped-aurora/gin-vue-admin/server/model/common/response"
    "github.com/flipped-aurora/gin-vue-admin/server/service"
    "github.com/gin-gonic/gin"
    "go.uber.org/zap"
)

type Img2imgApi struct {
}

var img2imgService = service.ServiceGroupApp.KnowledgeServiceGroup.Img2imgService


// CreateImg2img 创建图生图
// @Tags Img2img
// @Summary 创建图生图
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body knowledge.Img2img true "创建图生图"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"创建成功"}"
// @Router /img2img/createImg2img [post]
func (img2imgApi *Img2imgApi) CreateImg2img(c *gin.Context) {
	var img2img knowledge.Img2img
	err := c.ShouldBindJSON(&img2img)
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	if err := img2imgService.CreateImg2img(&img2img); err != nil {
        global.GVA_LOG.Error("创建失败!", zap.Error(err))
		response.FailWithMessage("创建失败", c)
	} else {
		response.OkWithMessage("创建成功", c)
	}
}

// DeleteImg2img 删除图生图
// @Tags Img2img
// @Summary 删除图生图
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body knowledge.Img2img true "删除图生图"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /img2img/deleteImg2img [delete]
func (img2imgApi *Img2imgApi) DeleteImg2img(c *gin.Context) {
	var img2img knowledge.Img2img
	err := c.ShouldBindJSON(&img2img)
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	if err := img2imgService.DeleteImg2img(img2img); err != nil {
        global.GVA_LOG.Error("删除失败!", zap.Error(err))
		response.FailWithMessage("删除失败", c)
	} else {
		response.OkWithMessage("删除成功", c)
	}
}

// DeleteImg2imgByIds 批量删除图生图
// @Tags Img2img
// @Summary 批量删除图生图
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body request.IdsReq true "批量删除图生图"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"批量删除成功"}"
// @Router /img2img/deleteImg2imgByIds [delete]
func (img2imgApi *Img2imgApi) DeleteImg2imgByIds(c *gin.Context) {
	var IDS request.IdsReq
    err := c.ShouldBindJSON(&IDS)
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	if err := img2imgService.DeleteImg2imgByIds(IDS); err != nil {
        global.GVA_LOG.Error("批量删除失败!", zap.Error(err))
		response.FailWithMessage("批量删除失败", c)
	} else {
		response.OkWithMessage("批量删除成功", c)
	}
}

// UpdateImg2img 更新图生图
// @Tags Img2img
// @Summary 更新图生图
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body knowledge.Img2img true "更新图生图"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"更新成功"}"
// @Router /img2img/updateImg2img [put]
func (img2imgApi *Img2imgApi) UpdateImg2img(c *gin.Context) {
	var img2img knowledge.Img2img
	err := c.ShouldBindJSON(&img2img)
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	if err := img2imgService.UpdateImg2img(img2img); err != nil {
        global.GVA_LOG.Error("更新失败!", zap.Error(err))
		response.FailWithMessage("更新失败", c)
	} else {
		response.OkWithMessage("更新成功", c)
	}
}

// FindImg2img 用id查询图生图
// @Tags Img2img
// @Summary 用id查询图生图
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query knowledge.Img2img true "用id查询图生图"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"查询成功"}"
// @Router /img2img/findImg2img [get]
func (img2imgApi *Img2imgApi) FindImg2img(c *gin.Context) {
	var img2img knowledge.Img2img
	err := c.ShouldBindQuery(&img2img)
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	if reimg2img, err := img2imgService.GetImg2img(img2img.ID); err != nil {
        global.GVA_LOG.Error("查询失败!", zap.Error(err))
		response.FailWithMessage("查询失败", c)
	} else {
		response.OkWithData(gin.H{"reimg2img": reimg2img}, c)
	}
}

// GetImg2imgList 分页获取图生图列表
// @Tags Img2img
// @Summary 分页获取图生图列表
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query knowledgeReq.Img2imgSearch true "分页获取图生图列表"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /img2img/getImg2imgList [get]
func (img2imgApi *Img2imgApi) GetImg2imgList(c *gin.Context) {
	var pageInfo knowledgeReq.Img2imgSearch
	err := c.ShouldBindQuery(&pageInfo)
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	if list, total, err := img2imgService.GetImg2imgInfoList(pageInfo); err != nil {
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
