import request from '@/utils/axiosReq'
import { ObjTy } from '~/common'

export function loginReq(data: ObjTy) {
  return request({
    url: '/user/login',
    data,
    method: 'post',
    bfLoading: false,
    isParams: false,
    isAlertErrorMsg: false
  })
}

export function getInfoReq() {
  return request({
    url: '/user/getInfo',
    bfLoading: false,
    method: 'post',
    isAlertErrorMsg: false
  })
}

export function logoutReq() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

export function customerRegistReq(data: ObjTy) {
  return request({
    url: '/user/customerRegist',
    data,
    method: 'post'
  })
}

export function entRegistReq(data: ObjTy) {
  return request({
    url: '/user/entRegist',
    data,
    method: 'post'
  })
}