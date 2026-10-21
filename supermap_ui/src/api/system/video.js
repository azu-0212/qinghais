import request from '@/utils/request'

// 查询视频管理列表
export function listVideo(query) {
  return request({
    url: '/system/video/list',
    method: 'get',
    params: query
  })
}

// 根据类型查询视频管理列表
export function listVideoByType(type) {
  return request({
    url: '/system/video/type/' + type,
    method: 'get'
  })
}

// 查询视频管理详细
export function getVideo(id) {
  return request({
    url: '/system/video/' + id,
    method: 'get'
  })
}

// 新增视频管理
export function addVideo(data) {
  return request({
    url: '/system/video',
    method: 'post',
    data: data
  })
}

// 修改视频管理
export function updateVideo(data) {
  return request({
    url: '/system/video',
    method: 'put',
    data: data
  })
}

// 删除视频管理
export function delVideo(id) {
  return request({
    url: '/system/video/' + id,
    method: 'delete'
  })
}

// 导出视频管理
export function exportVideo(query) {
  return request({
    url: '/system/video/export',
    method: 'get',
    params: query
  })
}
