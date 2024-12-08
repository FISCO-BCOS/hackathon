package {{.Package}}

import (
	"github.com/flipped-aurora/gin-vue-admin/server/global"
    "github.com/flipped-aurora/gin-vue-admin/server/model/{{.Package}}"
    "github.com/flipped-aurora/gin-vue-admin/server/model/common/request"
    {{.Package}}Req "github.com/flipped-aurora/gin-vue-admin/server/model/{{.Package}}/request"
    "github.com/flipped-aurora/gin-vue-admin/server/model/common/response"
    "github.com/flipped-aurora/gin-vue-admin/server/service"
    "github.com/gin-gonic/gin"
    "go.uber.org/zap"
    {{- if .NeedValid }}
    "github.com/flipped-aurora/gin-vue-admin/server/utils"
    {{- else if .AutoCreateResource}}
    "github.com/flipped-aurora/gin-vue-admin/server/utils"
    {{- end }}
)

type {{.StructName}}Api struct {
}

var {{.Abbreviation}}Service = service.ServiceGroupApp.{{.PackageT}}ServiceGroup.{{.StructName}}Service


// Create{{.StructName}} 创建{{.Description}}
// @Tags {{.StructName}}
// @Summary 创建{{.Description}}
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body {{.Package}}.{{.StructName}} true "创建{{.Description}}"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"创建成功"}"
// @Router /{{.Abbreviation}}/create{{.StructName}} [post]
func ({{.Abbreviation}}Api *{{.StructName}}Api) Create{{.StructName}}(c *gin.Context) {
	var {{.Abbreviation}} {{.Package}}.{{.StructName}}
	err := c.ShouldBindJSON(&{{.Abbreviation}})
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	{{- if .AutoCreateResource }}
    {{.Abbreviation}}.CreatedBy = utils.GetUserID(c)
	{{- end }}
    {{- if .NeedValid }}
    verify := utils.Rules{
    {{- range $index,$element := .Fields }}
       {{- if $element.Require }}
        "{{$element.FieldName}}":{utils.NotEmpty()},
        {{- end }}
    {{- end }}
    }
	if err := utils.Verify({{.Abbreviation}}, verify); err != nil {
    		response.FailWithMessage(err.Error(), c)
    		return
    	}
    {{- end }}
	if err := {{.Abbreviation}}Service.Create{{.StructName}}(&{{.Abbreviation}}); err != nil {
        global.GVA_LOG.Error("创建失败!", zap.Error(err))
		response.FailWithMessage("创建失败", c)
	} else {
		response.OkWithMessage("创建成功", c)
	}
}

// Delete{{.StructName}} 删除{{.Description}}
// @Tags {{.StructName}}
// @Summary 删除{{.Description}}
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body {{.Package}}.{{.StructName}} true "删除{{.Description}}"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /{{.Abbreviation}}/delete{{.StructName}} [delete]
func ({{.Abbreviation}}Api *{{.StructName}}Api) Delete{{.StructName}}(c *gin.Context) {
	var {{.Abbreviation}} {{.Package}}.{{.StructName}}
	err := c.ShouldBindJSON(&{{.Abbreviation}})
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
		{{- if .AutoCreateResource }}
    {{.Abbreviation}}.DeletedBy = utils.GetUserID(c)
        {{- end }}
	if err := {{.Abbreviation}}Service.Delete{{.StructName}}({{.Abbreviation}}); err != nil {
        global.GVA_LOG.Error("删除失败!", zap.Error(err))
		response.FailWithMessage("删除失败", c)
	} else {
		response.OkWithMessage("删除成功", c)
	}
}

// Delete{{.StructName}}ByIds 批量删除{{.Description}}
// @Tags {{.StructName}}
// @Summary 批量删除{{.Description}}
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body request.IdsReq true "批量删除{{.Description}}"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"批量删除成功"}"
// @Router /{{.Abbreviation}}/delete{{.StructName}}ByIds [delete]
func ({{.Abbreviation}}Api *{{.StructName}}Api) Delete{{.StructName}}ByIds(c *gin.Context) {
	var IDS request.IdsReq
    err := c.ShouldBindJSON(&IDS)
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
    	{{- if .AutoCreateResource }}
    deletedBy := utils.GetUserID(c)
        {{- end }}
	if err := {{.Abbreviation}}Service.Delete{{.StructName}}ByIds(IDS{{- if .AutoCreateResource }},deletedBy{{- end }}); err != nil {
        global.GVA_LOG.Error("批量删除失败!", zap.Error(err))
		response.FailWithMessage("批量删除失败", c)
	} else {
		response.OkWithMessage("批量删除成功", c)
	}
}

// Update{{.StructName}} 更新{{.Description}}
// @Tags {{.StructName}}
// @Summary 更新{{.Description}}
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body {{.Package}}.{{.StructName}} true "更新{{.Description}}"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"更新成功"}"
// @Router /{{.Abbreviation}}/update{{.StructName}} [put]
func ({{.Abbreviation}}Api *{{.StructName}}Api) Update{{.StructName}}(c *gin.Context) {
	var {{.Abbreviation}} {{.Package}}.{{.StructName}}
	err := c.ShouldBindJSON(&{{.Abbreviation}})
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	    {{- if .AutoCreateResource }}
    {{.Abbreviation}}.UpdatedBy = utils.GetUserID(c)
        {{- end }}
	{{- if .NeedValid }}
      verify := utils.Rules{
      {{- range $index,$element := .Fields }}
         {{- if $element.Require }}
          "{{$element.FieldName}}":{utils.NotEmpty()},
          {{- end }}
      {{- end }}
      }
    if err := utils.Verify({{.Abbreviation}}, verify); err != nil {
      	response.FailWithMessage(err.Error(), c)
      	return
     }
    {{- end }}
	if err := {{.Abbreviation}}Service.Update{{.StructName}}({{.Abbreviation}}); err != nil {
        global.GVA_LOG.Error("更新失败!", zap.Error(err))
		response.FailWithMessage("更新失败", c)
	} else {
		response.OkWithMessage("更新成功", c)
	}
}

// Find{{.StructName}} 用id查询{{.Description}}
// @Tags {{.StructName}}
// @Summary 用id查询{{.Description}}
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query {{.Package}}.{{.StructName}} true "用id查询{{.Description}}"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"查询成功"}"
// @Router /{{.Abbreviation}}/find{{.StructName}} [get]
func ({{.Abbreviation}}Api *{{.StructName}}Api) Find{{.StructName}}(c *gin.Context) {
	var {{.Abbreviation}} {{.Package}}.{{.StructName}}
	err := c.ShouldBindQuery(&{{.Abbreviation}})
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	if re{{.Abbreviation}}, err := {{.Abbreviation}}Service.Get{{.StructName}}({{.Abbreviation}}.ID); err != nil {
        global.GVA_LOG.Error("查询失败!", zap.Error(err))
		response.FailWithMessage("查询失败", c)
	} else {
		response.OkWithData(gin.H{"re{{.Abbreviation}}": re{{.Abbreviation}}}, c)
	}
}

// Get{{.StructName}}List 分页获取{{.Description}}列表
// @Tags {{.StructName}}
// @Summary 分页获取{{.Description}}列表
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query {{.Package}}Req.{{.StructName}}Search true "分页获取{{.Description}}列表"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /{{.Abbreviation}}/get{{.StructName}}List [get]
func ({{.Abbreviation}}Api *{{.StructName}}Api) Get{{.StructName}}List(c *gin.Context) {
	var pageInfo {{.Package}}Req.{{.StructName}}Search
	err := c.ShouldBindQuery(&pageInfo)
	if err != nil {
		response.FailWithMessage(err.Error(), c)
		return
	}
	if list, total, err := {{.Abbreviation}}Service.Get{{.StructName}}InfoList(pageInfo); err != nil {
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
