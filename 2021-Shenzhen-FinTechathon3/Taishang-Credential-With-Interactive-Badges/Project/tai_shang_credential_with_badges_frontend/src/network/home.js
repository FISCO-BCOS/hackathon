import {request} from './request'
export function getTokenURI(tokenid) {
  return request({
    url: '/tokenURI',
    params: {
      tokenid
    },
  })
}
