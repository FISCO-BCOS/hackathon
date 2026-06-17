import service from '@/utils/request'
// @Tags AbnormalTransactions
// @Summary 创建异常交易
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.AbnormalTransactions true "创建异常交易"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"创建成功"}"
// @Router /abnormalTransactions/createAbnormalTransactions [post]
export const createAbnormalTransactions = (data) => {
  return service({
    url: '/abnormalTransactions/createAbnormalTransactions',
    method: 'post',
    data
  })
}

// @Tags AbnormalTransactions
// @Summary 删除异常交易
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.AbnormalTransactions true "删除异常交易"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /abnormalTransactions/deleteAbnormalTransactions [delete]
export const deleteAbnormalTransactions = (params) => {
  return service({
    url: '/abnormalTransactions/deleteAbnormalTransactions',
    method: 'delete',
    params
  })
}

// @Tags AbnormalTransactions
// @Summary 批量删除异常交易
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body request.IdsReq true "批量删除异常交易"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /abnormalTransactions/deleteAbnormalTransactions [delete]
export const deleteAbnormalTransactionsByIds = (params) => {
  return service({
    url: '/abnormalTransactions/deleteAbnormalTransactionsByIds',
    method: 'delete',
    params
  })
}

// @Tags AbnormalTransactions
// @Summary 更新异常交易
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.AbnormalTransactions true "更新异常交易"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"更新成功"}"
// @Router /abnormalTransactions/updateAbnormalTransactions [put]
export const updateAbnormalTransactions = (data) => {
  return service({
    url: '/abnormalTransactions/updateAbnormalTransactions',
    method: 'put',
    data
  })
}

// @Tags AbnormalTransactions
// @Summary 用id查询异常交易
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query model.AbnormalTransactions true "用id查询异常交易"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"查询成功"}"
// @Router /abnormalTransactions/findAbnormalTransactions [get]
export const findAbnormalTransactions = (params) => {
  return service({
    url: '/abnormalTransactions/findAbnormalTransactions',
    method: 'get',
    params
  })
}

// @Tags AbnormalTransactions
// @Summary 分页获取异常交易列表
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query request.PageInfo true "分页获取异常交易列表"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /abnormalTransactions/getAbnormalTransactionsList [get]
export const getAbnormalTransactionsList = (params) => {
  return service({
    url: '/abnormalTransactions/getAbnormalTransactionsList',
    method: 'get',
    params
  })
}

// @Tags AbnormalTransactions
// @Summary 不需要鉴权的异常交易接口
// @accept application/json
// @Produce application/json
// @Param data query hufuReq.AbnormalTransactionsSearch true "分页获取异常交易列表"
// @Success 200 {object} response.Response{data=object,msg=string} "获取成功"
// @Router /abnormalTransactions/getAbnormalTransactionsPublic [get]
export const getAbnormalTransactionsPublic = () => {
  return service({
    url: '/abnormalTransactions/getAbnormalTransactionsPublic',
    method: 'get',
  })
}
