import request from '@/utils/request'

// 查询在线用户列表
export function list(query) {
  return request({
    url: '/monitor/online/list',
    method: 'get',
    params: query
  })
}

// 强退用户
export function forceLogout(tokenId) {
  return request({
    url: '/monitor/online/' + tokenId,
    method: 'delete'
  })
}

// 查询当前在线用户 getLoginOnline
export function getLoginOnline(query) {
  return request({
    // url: '/monitor/online/time/start/' + startTime + '/current/' + currentTime,
    url: '/monitor/online/time',
    method: 'get',
  })
}

