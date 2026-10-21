import request from '@/utils/request'

// 登录方法
export function keepAlive () {
  return request({
    url: '/keepAlvie/send',
    method: 'get',
  })
}



export function resourceDelete(data) {
  return request({
    url: `/common/resourceDelete`,
    method: 'post',
    data:data
  })
}
