import request from '@/utils/request'

// 批量存储文件下的所有图片
export function saveImgs(data) {
  return request({
    url: '/img/saveImages',
    method: 'post',
    data: data
  })
}

// 批量存储文件夹下的所有图片
export function saveFileImgs(data) {
  return request({
    url: '/img/saveFileImgsMore',
    method: 'post',
    data: data
  })
}

// 查询文件夹图片
export function getOne(filename) {
  return request({
    url: '/img/getOne/'+ filename,
    method: 'get'
  })
}

// 查询文件夹图片
export function get(fileCode, filename) {
  return request({
    url: '/img/get/'+ fileCode + "/" + filename,
    method: 'get'
  })
}

// 卫片执法查询图片接口
export function getSatellite(fileCode) {
  return request({
    url: '/img/getSatellite/'+ fileCode,
    method: 'get'
  })
}


