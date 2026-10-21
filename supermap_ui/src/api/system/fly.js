import request from '@/utils/request'

// 查询飞行路线列表
export function listFly(query) {
  return request({
    url: '/system/fly/list',
    method: 'get',
    params: query
  })
}

// 查询飞行路线详细
export function getFly(id) {
  return request({
    url: '/system/fly/' + id,
    method: 'get'
  })
}

// 新增飞行路线
export function addFly(data) {
  return request({
    url: '/system/fly',
    method: 'post',
    data: data
  })
}

// 修改飞行路线
export function updateFly(data) {
  return request({
    url: '/system/fly',
    method: 'put',
    data: data,
  })
}

// 删除飞行路线
export function delFly(id) {
  return request({
    url: '/system/fly/' + id,
    method: 'delete'
  })
}

// 导出飞行路线
export function exportFly(query) {
  return request({
    url: '/system/fly/export',
    method: 'get',
    params: query
  })
}
