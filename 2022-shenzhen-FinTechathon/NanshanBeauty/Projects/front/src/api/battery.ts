import request from '@/utils/axiosReq'
import { ObjTy } from '~/common'

export function addReq(data: ObjTy) {
  return request({
    url: '/battery/add',
    data,
    method: 'post'
  })
}

export function listReq(data: ObjTy) {
  return request({
    url: '/battery/list',
    data,
    method: 'post'
  })
}

export function traceInfoReq(data: ObjTy) {
  return request({
    url: '/battery/traceInfo',
    data,
    method: 'post'
  })
}

export function downloadTemplateReq() {
  return request({
    url: '/download/batchBatteryNo',
    method: 'post',
    isDownLoadFile: true
  })
}

export function firstRecycleApplyReq(data: ObjTy) {
  return request({
    url: '/secondUsed/apply',
    data,
    method: 'post',
    isUploadFile: true
  })
}

export function firstRecycleMyTradeReq(data: ObjTy) {
  return request({
    url: '/secondUsed/myList',
    data,
    method: 'post',
  })
}

export function firstRecycleTradeReq(data: ObjTy) {
  return request({
    url: '/secondUsed/list',
    data,
    method: 'post',
  })
}

export function firstRecycleBidReq(data: ObjTy) {
  return request({
    url: '/secondUsed/bid',
    data,
    method: 'post'
  })
}

export function secondRecycleApplyReq(data: ObjTy) {
  return request({
    url: '/recycle/apply',
    data,
    method: 'post',
    isUploadFile: true
  })
}

export function secondRecycleMyTradeReq(data: ObjTy) {
  return request({
    url: '/recycle/myList',
    data,
    method: 'post',
  })
}

export function secondRecycleTradeReq(data: ObjTy) {
  return request({
    url: '/recycle/list',
    data,
    method: 'post',
  })
}

export function secondRecycleBidReq(data: ObjTy) {
  return request({
    url: '/recycle/bid',
    data,
    method: 'post'
  })
}
