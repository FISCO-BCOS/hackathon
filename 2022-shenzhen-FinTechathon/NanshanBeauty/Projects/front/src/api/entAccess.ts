import request from '@/utils/axiosReq'
import { ObjTy } from '~/common'

export function applyReq(data: ObjTy) {
  return request({
    url: '/ent/apply',
    data,
    method: 'post',
    isUploadFile: true
  })
}

export function accessResultReq() {
  return request({
    url: '/ent/access/curInfo',
    method: 'post'
  })
}

export function entListReq(data: ObjTy) {
  return request({
    url: '/ent/list',
    data,
    method: 'post',
  })
}

export function passReq(data: ObjTy) {
  return request({
    url: '/ent/access/pass',
    data,
    method: 'post',
  })
}

export function rejectReq(data: ObjTy) {
  return request({
    url: '/ent/access/reject',
    data,
    method: 'post',
  })
}

export function downloadReq(data: ObjTy) {
  return request({
    url: '/ent/download' + data,
    data,
    method: 'post',
    isDownLoadFile: true
  })
}
