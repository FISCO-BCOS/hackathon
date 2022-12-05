import { tradeApplyReq, myTradeReq, tradeListReq, bidReq, myPointReq, myPointLogsReq } from '@/api/point'
import { ObjTy } from '~/common'
import { defineStore } from 'pinia'

export const usePointStore = defineStore('point', {
  actions: {
    tradeApply(data: ObjTy) {
      return new Promise((resolve, reject) => {
        tradeApplyReq(data)
          .then((res: ObjTy) => {
            resolve(res)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    myTrade(data: ObjTy) {
      return new Promise((resolve, reject) => {
        myTradeReq(data)
          .then((res: ObjTy) => {
            resolve(res.data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    tradeList(data: ObjTy) {
      return new Promise((resolve, reject) => {
        tradeListReq(data)
          .then((res: ObjTy) => {
            resolve(res.data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    bid(data: ObjTy) {
      return new Promise((resolve, reject) => {
        bidReq(data)
          .then((res: ObjTy) => {
            resolve(res)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    myPoint() {
      return new Promise((resolve, reject) => {
        myPointReq()
          .then((res: ObjTy) => {
            resolve(res.data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    myPointLogs() {
      return new Promise((resolve, reject) => {
        myPointLogsReq()
          .then((res: ObjTy) => {
            resolve(res.data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    }
  }
})
