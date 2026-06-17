import service from '@/utils/request'
// @Tags Invoices
// @Summary 创建发票信息
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.Invoices true "创建发票信息"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"创建成功"}"
// @Router /invoices/createInvoices [post]
export const createInvoices = (data) => {
  return service({
    url: '/invoices/createInvoices',
    method: 'post',
    data
  })
}

// @Tags Invoices
// @Summary 删除发票信息
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.Invoices true "删除发票信息"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /invoices/deleteInvoices [delete]
export const deleteInvoices = (params) => {
  return service({
    url: '/invoices/deleteInvoices',
    method: 'delete',
    params
  })
}

// @Tags Invoices
// @Summary 批量删除发票信息
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body request.IdsReq true "批量删除发票信息"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /invoices/deleteInvoices [delete]
export const deleteInvoicesByIds = (params) => {
  return service({
    url: '/invoices/deleteInvoicesByIds',
    method: 'delete',
    params
  })
}

// @Tags Invoices
// @Summary 更新发票信息
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.Invoices true "更新发票信息"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"更新成功"}"
// @Router /invoices/updateInvoices [put]
export const updateInvoices = (data) => {
  return service({
    url: '/invoices/updateInvoices',
    method: 'put',
    data
  })
}

// @Tags Invoices
// @Summary 用id查询发票信息
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query model.Invoices true "用id查询发票信息"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"查询成功"}"
// @Router /invoices/findInvoices [get]
export const findInvoices = (params) => {
  return service({
    url: '/invoices/findInvoices',
    method: 'get',
    params
  })
}

// @Tags Invoices
// @Summary 分页获取发票信息列表
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query request.PageInfo true "分页获取发票信息列表"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /invoices/getInvoicesList [get]
export const getInvoicesList = (params) => {
  return service({
    url: '/invoices/getInvoicesList',
    method: 'get',
    params
  })
}

// @Tags Invoices
// @Summary 不需要鉴权的发票信息接口
// @accept application/json
// @Produce application/json
// @Param data query hufuReq.InvoicesSearch true "分页获取发票信息列表"
// @Success 200 {object} response.Response{data=object,msg=string} "获取成功"
// @Router /invoices/getInvoicesPublic [get]
export const getInvoicesPublic = () => {
  return service({
    url: '/invoices/getInvoicesPublic',
    method: 'get',
  })
}
