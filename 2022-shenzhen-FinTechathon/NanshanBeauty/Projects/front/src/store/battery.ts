import { addReq, listReq, traceInfoReq, downloadTemplateReq, firstRecycleApplyReq, firstRecycleMyTradeReq, firstRecycleTradeReq, firstRecycleBidReq, secondRecycleApplyReq, secondRecycleMyTradeReq, secondRecycleTradeReq, secondRecycleBidReq } from '@/api/battery'
import { ObjTy } from '~/common'
import { defineStore } from 'pinia'

export const useBatteryStore = defineStore('battery', {
  actions: {
    add(data: ObjTy) {
      return new Promise((resolve, reject) => {
        addReq(data)
          .then(() => {
            resolve(null)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    list(data: ObjTy) {
      return new Promise((resolve, reject) => {
        listReq(data)
          .then((res: ObjTy) => {
            resolve(res.data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    traceInfo(data: ObjTy) {
      return new Promise((resolve, reject) => {
        traceInfoReq(data)
          .then((res: ObjTy) => {
            resolve(res.data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    downloadTemplate() {
      return new Promise((resolve, reject) => {
        downloadTemplateReq()
          .then((res: ObjTy) => {
            resolve(res)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    firstRecycleApply(data: ObjTy) {
      return new Promise((resolve, reject) => {
        firstRecycleApplyReq(data)
          .then((res: ObjTy) => {
            resolve(res)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    firstRecycleMyTrade(data: ObjTy) {
      return new Promise((resolve, reject) => {
        firstRecycleMyTradeReq(data)
          .then((res: ObjTy) => {
            resolve(res.data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    firstRecycleTrade(data: ObjTy) {
      return new Promise((resolve, reject) => {
        firstRecycleTradeReq(data)
          .then((res: ObjTy) => {
            resolve(res.data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    firstRecycleBid(data: ObjTy) {
      return new Promise((resolve, reject) => {
        firstRecycleBidReq(data)
          .then((res: ObjTy) => {
            resolve(res)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    secondRecycleApply(data: ObjTy) {
      return new Promise((resolve, reject) => {
        secondRecycleApplyReq(data)
          .then((res: ObjTy) => {
            resolve(res)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    secondRecycleMyTrade(data: ObjTy) {
      return new Promise((resolve, reject) => {
        secondRecycleMyTradeReq(data)
          .then((res: ObjTy) => {
            resolve(res.data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    secondRecycleTrade(data: ObjTy) {
      return new Promise((resolve, reject) => {
        secondRecycleTradeReq(data)
          .then((res: ObjTy) => {
            resolve(res.data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    secondRecycleBid(data: ObjTy) {
      return new Promise((resolve, reject) => {
        secondRecycleBidReq(data)
          .then((res: ObjTy) => {
            resolve(res)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
  }
})
