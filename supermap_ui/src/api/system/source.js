import request from '@/utils/request'

// 查询来源类型列表
export function listSource(query) {
  return request({
    url: '/system/source/list',
    method: 'get',
    params: query
  })
}

// 查询来源类型所有列表
export function listSourceAll() {
  return request({
    url: '/system/source/all',
    method: 'get'
  })
}

// 查询来源类型详细
export function getSource(id) {
  return request({
    url: '/system/source/' + id,
    method: 'get'
  })
}

// 新增来源类型
export function addSource(data) {
  return request({
    url: '/system/source',
    method: 'post',
    data: data
  })
}

// 修改来源类型
export function updateSource(data) {
  return request({
    url: '/system/source',
    method: 'put',
    data: data
  })
}

// 删除来源类型
export function delSource(id) {
  return request({
    url: '/system/source/' + id,
    method: 'delete'
  })
}

// 导出来源类型
export function exportSource(query) {
  return request({
    url: '/system/source/export',
    method: 'get',
    params: query
  })
}
