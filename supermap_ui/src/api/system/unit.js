import request from '@/utils/request'

// 查询地理单元列表
export function listUnit(query) {
  return request({
    url: '/system/unit/list',
    method: 'get',
    params: query
  })
}

// 查询地理单元详细
export function getUnit(id) {
  return request({
    url: '/system/unit/' + id,
    method: 'get'
  })
}

// 新增地理单元
export function addUnit(data) {
  return request({
    url: '/system/unit',
    method: 'post',
    data: data
  })
}

// 修改地理单元
export function updateUnit(data) {
  return request({
    url: '/system/unit',
    method: 'put',
    data: data
  })
}

// 删除地理单元
export function delUnit(id) {
  return request({
    url: '/system/unit/' + id,
    method: 'delete'
  })
}

// 导出地理单元
export function exportUnit(query) {
  return request({
    url: '/system/unit/export',
    method: 'get',
    params: query
  })
}
