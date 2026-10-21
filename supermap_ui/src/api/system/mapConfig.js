import request from '@/utils/request'

// 查询地图配置列表
export function listMapConfig(query) {
  return request({
    url: '/system/mapConfig/list',
    method: 'get',
    params: query
  })
}

// 查询地图配置详细
export function getMapConfig(configId) {
  return request({
    url: '/system/mapConfig/' + configId,
    method: 'get'
  })
}

// 新增地图配置
export function addMapConfig(data) {
  return request({
    url: '/system/mapConfig',
    method: 'post',
    data: data
  })
}

// 修改地图配置
export function updateMapConfig(data) {
  return request({
    url: '/system/mapConfig',
    method: 'put',
    data: data
  })
}

// 删除地图配置
export function delMapConfig(configId) {
  return request({
    url: '/system/mapConfig/' + configId,
    method: 'delete'
  })
}

// 导出地图配置
export function exportMapConfig(query) {
  return request({
    url: '/system/mapConfig/export',
    method: 'get',
    params: query
  })
}