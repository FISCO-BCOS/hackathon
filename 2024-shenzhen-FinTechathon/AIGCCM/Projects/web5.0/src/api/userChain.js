import service from '@/utils/request'

// @Tags Userchain
// @Summary 创建区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.Userchain true "创建区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"创建成功"}"
// @Router /chainuse/createUserchain [post]
export const createUserchain = (data) => {
  return service({
    url: '/chainuse/createUserchain',
    method: 'post',
    data
  })
}

// @Tags Userchain
// @Summary 删除区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.Userchain true "删除区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /chainuse/deleteUserchain [delete]
export const deleteUserchain = (data) => {
  return service({
    url: '/chainuse/deleteUserchain',
    method: 'delete',
    data
  })
}

// @Tags Userchain
// @Summary 批量删除区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body request.IdsReq true "批量删除区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /chainuse/deleteUserchain [delete]
export const deleteUserchainByIds = (data) => {
  return service({
    url: '/chainuse/deleteUserchainByIds',
    method: 'delete',
    data
  })
}

// @Tags Userchain
// @Summary 更新区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.Userchain true "更新区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"更新成功"}"
// @Router /chainuse/updateUserchain [put]
export const updateUserchain = (data) => {
  return service({
    url: '/chainuse/updateUserchain',
    method: 'put',
    data
  })
}

// @Tags Userchain
// @Summary 用id查询区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query model.Userchain true "用id查询区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"查询成功"}"
// @Router /chainuse/findUserchain [get]
export const findUserchain = (params) => {
  return service({
    url: '/chainuse/findUserchain',
    method: 'get',
    params
  })
}

// @Tags Userchain
// @Summary 分页获取区块链用户列表
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query request.PageInfo true "分页获取区块链用户列表"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /chainuse/getUserchainList [get]
export const getUserchainList = (params) => {
  return service({
    url: '/chainuse/getUserchainList',
    method: 'get',
    params
  })
}
