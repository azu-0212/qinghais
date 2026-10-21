import request from '@/utils/request'
import axios from "axios";

// 查询重点区域定位列表
export function listAreas(query) {
  return request({
    url: '/system/areas/list',
    method: 'get',
    params: query
  })
}

// 查询重点区域定位详细
export function getAreas(id) {
  return request({
    url: '/system/areas/' + id,
    method: 'get'
  })
}

// 新增重点区域定位
export function addAreas(data) {
  return request({
    url: '/system/areas',
    method: 'post',
    data: data
  })
}


// 修改重点区域定位
export function updateAreas(data) {
  return request({
    url: '/system/areas',
    method: 'put',
    data: data
  })
}

// 删除重点区域定位
export function delAreas(id) {
  return request({
    url: '/system/areas/' + id,
    method: 'delete'
  })
}

// 导出重点区域定位
export function exportAreas(query) {
  return request({
    url: '/system/areas/export',
    method: 'get',
    params: query
  })

}
// 重点区域图片上传
export function uploadAvatar(data) {
  return request({
    url: '/system/areas/addImgSave',
    method: 'post',
    data: data
  })
}
