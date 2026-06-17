import service from '@/utils/request'
// @Tags Wallet
// @Summary 创建钱包
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.Wallet true "创建钱包"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"创建成功"}"
// @Router /wallet/createWallet [post]
export const createWallet = (data) => {
  return service({
    url: '/wallet/createWallet',
    method: 'post',
    data
  })
}

// @Tags Wallet
// @Summary 删除钱包
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.Wallet true "删除钱包"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /wallet/deleteWallet [delete]
export const deleteWallet = (params) => {
  return service({
    url: '/wallet/deleteWallet',
    method: 'delete',
    params
  })
}

// @Tags Wallet
// @Summary 批量删除钱包
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body request.IdsReq true "批量删除钱包"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /wallet/deleteWallet [delete]
export const deleteWalletByIds = (params) => {
  return service({
    url: '/wallet/deleteWalletByIds',
    method: 'delete',
    params
  })
}

// @Tags Wallet
// @Summary 更新钱包
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.Wallet true "更新钱包"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"更新成功"}"
// @Router /wallet/updateWallet [put]
export const updateWallet = (data) => {
  return service({
    url: '/wallet/updateWallet',
    method: 'put',
    data
  })
}

// @Tags Wallet
// @Summary 用id查询钱包
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query model.Wallet true "用id查询钱包"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"查询成功"}"
// @Router /wallet/findWallet [get]
export const findWallet = (params) => {
  return service({
    url: '/wallet/findWallet',
    method: 'get',
    params
  })
}

// @Tags Wallet
// @Summary 分页获取钱包列表
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query request.PageInfo true "分页获取钱包列表"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /wallet/getWalletList [get]
export const getWalletList = (params) => {
  return service({
    url: '/wallet/getWalletList',
    method: 'get',
    params
  })
}

// @Tags Wallet
// @Summary 不需要鉴权的钱包接口
// @accept application/json
// @Produce application/json
// @Param data query hufuReq.WalletSearch true "分页获取钱包列表"
// @Success 200 {object} response.Response{data=object,msg=string} "获取成功"
// @Router /wallet/getWalletPublic [get]
export const getWalletPublic = () => {
  return service({
    url: '/wallet/getWalletPublic',
    method: 'get',
  })
}
