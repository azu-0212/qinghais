import request from '@/utils/request'

// 查询行政区划列表
export function listDivision(query) {
  return request({
    url: '/system/division/list',
    method: 'get',
    params: query
  })
}

// 查询行政区划详细
export function getDivision(id) {
  return request({
    url: '/system/division/' + id,
    method: 'get'
  })
}

// 查询行政区划子父级信息--关键字搜索
export function getDivisionSearch() {
  return request({
    url: '/system/division/search',
    method: 'get'
  })
}


// 新增行政区划
export function addDivision(data) {
  return request({
    url: '/system/division',
    method: 'post',
    data: data
  })
}

// 修改行政区划
export function updateDivision(data) {
  return request({
    url: '/system/division',
    method: 'put',
    data: data
  })
}

// 删除行政区划
export function delDivision(id) {
  return request({
    url: '/system/division/' + id,
    method: 'delete'
  })
}

// 导出行政区划
export function exportDivision(query) {
  return request({
    url: '/system/division/export',
    method: 'get',
    params: query
  })
}
