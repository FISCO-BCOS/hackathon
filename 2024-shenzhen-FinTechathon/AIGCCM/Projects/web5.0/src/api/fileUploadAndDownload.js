import service from '@/utils/request'
// @Tags FileUploadAndDownload
// @Summary 分页文件列表
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body modelInterface.PageInfo true "分页获取文件户列表"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /fileUploadAndDownload/getFileList [post]
export const getFileList = (data) => {
  return service({
    url: '/fileUploadAndDownload/getFileList',
    method: 'post',
    data
  })
}

// @Tags FileUploadAndDownload
// @Summary 删除文件
// @Security ApiKeyAuth
// @Produce  application/json
// @Param data body dbModel.FileUploadAndDownload true "传入文件里面id即可"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"返回成功"}"
// @Router /fileUploadAndDownload/deleteFile [post]
export const deleteFile = (data) => {
  return service({
    url: '/fileUploadAndDownload/deleteFile',
    method: 'post',
    data
  })
}

/**
 * 编辑文件名或者备注
 * @param data
 * @returns {*}
 */
export const editFileName = (data) => {
  return service({
    url: '/fileUploadAndDownload/editFileName',
    method: 'post',
    data
  })
}

/*
用户版权查询
*/
export const SelectByUser = (data) => {
  return service({
    url: '/fileUploadAndDownload/getFileListofUser',
    method: 'post',
    data
  })
}

/*
用户详细信息查询
*/
export const getDetail = (url_data) => {
  return service({
    url: '/fileUploadAndDownload/findDetail?Url=' + url_data,
    method: 'get',
  })
}

export const doPurchase = (data) => {
  return service({
    url: '/fileUploadAndDownload/transfer',
    method: 'post',
    data
  })
}
