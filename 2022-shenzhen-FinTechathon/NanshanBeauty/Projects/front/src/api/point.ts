import request from '@/utils/axiosReq'
import { ObjTy } from '~/common'

export function tradeApplyReq(data: ObjTy) {
  return request({
    url: '/point/apply',
    data,
    method: 'post'
  })
}

export function myTradeReq(data: ObjTy) {
  return request({
    url: '/point/myList',
    data,
    method: 'post',
  })
}

export function tradeListReq(data: ObjTy) {
  return request({
    url: '/point/list',
    data,
    method: 'post',
  })
}

export function bidReq(data: ObjTy) {
  return request({
    url: '/point/bid',
    data,
    method: 'post'
  })
}

export function myPointReq() {
  return request({
    url: '/point/my',
    method: 'post'
  })
}

export function myPointLogsReq() {
  return request({
    url: '/point/logs',
    method: 'post'
  })
}