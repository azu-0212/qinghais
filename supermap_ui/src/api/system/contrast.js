import request from '@/utils/request'

// 查询影像对比列表
export function listContrast(query) {
  return request({
    url: '/system/contrast/list',
    method: 'get',
    params: query
  })
}

// 根据数据类型查询影像对比列表
export function listContrastByType(pid,dataType){
  return request({
    url: '/system/contrast/list/' + pid + '/data/' + dataType,
    method: 'get',
  })
}

// 查询影像对比详细
export function getContrast(id) {
  return request({
    url: '/system/contrast/' + id,
    method: 'get'
  })
}

// 新增影像对比
export function addContrast(data) {
  return request({
    url: '/system/contrast',
    method: 'post',
    data: data
  })
}

// 修改影像对比
export function updateContrast(data) {
  return request({
    url: '/system/contrast',
    method: 'put',
    data: data
  })
}

// 删除影像对比
export function delContrast(id) {
  return request({
    url: '/system/contrast/' + id,
    method: 'delete'
  })
}

// 导出影像对比
export function exportContrast(query) {
  return request({
    url: '/system/contrast/export',
    method: 'get',
    params: query
  })
}
