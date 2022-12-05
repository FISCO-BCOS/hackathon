import { applyReq, accessResultReq, entListReq, passReq, rejectReq, downloadReq } from '@/api/entAccess'
import { ObjTy } from '~/common'
import { defineStore } from 'pinia'

export const useEntAccessStore = defineStore('entAccess', {
  actions: {
    apply(data: ObjTy) {
      return new Promise((resolve, reject) => {
        applyReq(data)
          .then(() => {
            resolve(null)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    accessResult() {
      return new Promise((resolve, reject) => {
        accessResultReq()
          .then((res: ObjTy) => {
            resolve(res.data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    entList(data: ObjTy) {
      return new Promise((resolve, reject) => {
        entListReq(data)
          .then((res: ObjTy) => {
            resolve(res.data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    pass(data: ObjTy) {
      return new Promise((resolve, reject) => {
        passReq(data)
          .then((res: ObjTy) => {
            resolve(res.data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    reject(data: ObjTy) {
      return new Promise((resolve, reject) => {
        rejectReq(data)
          .then((res: ObjTy) => {
            resolve(res.data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    download(userName) {
      return new Promise((resolve, reject) => {
        downloadReq(userName)
          .then((res: ObjTy) => {
            resolve(res)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    }
  }
})
