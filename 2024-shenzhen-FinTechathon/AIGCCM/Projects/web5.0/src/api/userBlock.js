import service from '@/utils/request'

// @Tags UserBlock
// @Summary 创建区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.UserBlock true "创建区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"创建成功"}"
// @Router /blockuser/createUserBlock [post]
export const createUserBlock = (data) => {
  return service({
    url: '/blockuser/createUserBlock',
    method: 'post',
    data
  })
}

// @Tags UserBlock
// @Summary 删除区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.UserBlock true "删除区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /blockuser/deleteUserBlock [delete]
export const deleteUserBlock = (data) => {
  return service({
    url: '/blockuser/deleteUserBlock',
    method: 'delete',
    data
  })
}

// @Tags UserBlock
// @Summary 批量删除区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body request.IdsReq true "批量删除区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /blockuser/deleteUserBlock [delete]
export const deleteUserBlockByIds = (data) => {
  return service({
    url: '/blockuser/deleteUserBlockByIds',
    method: 'delete',
    data
  })
}

// @Tags UserBlock
// @Summary 更新区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.UserBlock true "更新区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"更新成功"}"
// @Router /blockuser/updateUserBlock [put]
export const updateUserBlock = (data) => {
  return service({
    url: '/blockuser/updateUserBlock',
    method: 'put',
    data
  })
}

// @Tags UserBlock
// @Summary 用id查询区块链用户
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query model.UserBlock true "用id查询区块链用户"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"查询成功"}"
// @Router /blockuser/findUserBlock [get]
export const findUserBlock = (params) => {
  return service({
    url: '/blockuser/findUserBlock',
    method: 'get',
    params
  })
}

// @Tags UserBlock
// @Summary 分页获取区块链用户列表
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query request.PageInfo true "分页获取区块链用户列表"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /blockuser/getUserBlockList [get]
export const getUserBlockList = (params) => {
  return service({
    url: '/blockuser/getUserBlockList',
    method: 'get',
    params
  })
}
