import { MockMethod } from 'vite-plugin-mock'
export default [
  {
    url: '/getMapInfo',
    method: 'get',
    response: () => {
      return {
        code: 0,
        title: 'mock请求测试'
      }
    }
  }
] as MockMethod[]
