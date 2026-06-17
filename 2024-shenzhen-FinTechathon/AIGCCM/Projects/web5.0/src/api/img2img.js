import service from '@/utils/request'
import bservice from '@/utils/brequest'


// @Tags Img2img
// @Summary 创建图生图
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.Img2img true "创建图生图"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"创建成功"}"
// @Router /img2img/createImg2img [post]
export const createImg2img = (data) => {
  return service({
    url: '/img2img/createImg2img',
    method: 'post',
    data
  })
}

// @Tags Img2img
// @Summary 删除图生图
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.Img2img true "删除图生图"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /img2img/deleteImg2img [delete]
export const deleteImg2img = (data) => {
  return service({
    url: '/img2img/deleteImg2img',
    method: 'delete',
    data
  })
}

// @Tags Img2img
// @Summary 批量删除图生图
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body request.IdsReq true "批量删除图生图"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /img2img/deleteImg2img [delete]
export const deleteImg2imgByIds = (data) => {
  return service({
    url: '/img2img/deleteImg2imgByIds',
    method: 'delete',
    data
  })
}

// @Tags Img2img
// @Summary 更新图生图
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.Img2img true "更新图生图"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"更新成功"}"
// @Router /img2img/updateImg2img [put]
export const updateImg2img = (data) => {
  return service({
    url: '/img2img/updateImg2img',
    method: 'put',
    data
  })
}

// @Tags Img2img
// @Summary 用id查询图生图
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query model.Img2img true "用id查询图生图"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"查询成功"}"
// @Router /img2img/findImg2img [get]
export const findImg2img = (params) => {
  return service({
    url: '/img2img/findImg2img',
    method: 'get',
    params
  })
}

// @Tags Img2img
// @Summary 分页获取图生图列表
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data query request.PageInfo true "分页获取图生图列表"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /img2img/getImg2imgList [get]
export const getImg2imgList = (params) => {
  return service({
    url: '/img2img/getImg2imgList',
    method: 'get',
    params
  })
}
// @Tags txt2img
// @Summary 创建文生图
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.Img2img true "创建文生图"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"创建成功"}"
// @Router /img2img/createImg2img [post]
export const txt2img = (data) => {
  console.log("bservice")
    return bservice({
      url: '/aaaa/v1/txt2img', // 接口地址
      method: 'post', // 请求方法
      data: data, // 参数数据
  })
}

// export function aigc_API(url,data) {
//   return aigc_service({
//       url: url, // 接口地址
//       method: 'post', // 请求方法
//       data: data, // 参数数据
//   })
// }