import request from '@/utils/request'

// 查询数据资源列表
export function listResource(query) {
  return request({
    url: '/system/resource/list',
    method: 'get',
    params: query
  })
}

// 查询数据资源列表树结构(只显示转态>0的)
export function listResourceByTree() {
  return request({
    url: '/system/resource/tree',
    method: 'get'
  })
}

// 查询数据资源列表叶子节点
export function listResourceByLeaf() {
  return request({
    url: '/system/resource/leaf',
    method: 'get'
  })
}

// 按类查询自然资源列表
export function listResourceByType() {
  return request({
    url: '/system/resource/list/type',
    method: 'get'
  })
}

// 查询数据资源详细
export function getResource(resourceId) {
  return request({
    url: '/system/resource/' + resourceId,
    method: 'get'
  })
}

// 新增数据资源
export function addResource(data) {
  return request({
    url: '/system/resource',
    method: 'post',
    data: data
  })
}

// 修改数据资源
export function updateResource(data) {
  return request({
    url: '/system/resource',
    method: 'put',
    data: data
  })
}

// 删除数据资源
export function delResource(resourceId) {
  return request({
    url: '/system/resource/' + resourceId,
    method: 'delete'
  })
}

// 导出数据资源
export function exportResource(query) {
  return request({
    url: '/system/resource/export',
    method: 'get',
    params: query
  })
}


// 根据数据类型查找数据列表
export function getResourceByCode(typeCode) {
  return request({
    url: '/system/resource/code/' + typeCode,
    method: 'get'
  })
}

// 根据数据多级树节点类型查找数据列表
export function getResourceByCodeTree(typeCode) {
  return request({
    url: '/system/resource/tree/' + typeCode,
    method: 'get'
  })
}

// 根据数据类型查找数据列表
export function getResourceByDataType(dataType){
  return request({
    url: '/system/resource/type/' + dataType,
    method: 'get'
  })
}

// 根据数据类型年份列表
export function getResourceByDataYear(dataType){
  return request({
    url: '/system/resource/year/' + dataType,
    method: 'get'
  })
}

// 分组查询数据扇形统计
export function getResourceRegionByGroup(dataType){
  return request({
    url: '/system/resource/region/' + dataType,
    method: 'get'
  })
}

// 按年份，类型分组查询数据扇形统计
export function getResourceYearByGroup(query){
  return request({
    url: '/system/resource/yearRegion/',
    method: 'get',
    params: query
  })
}



// 根据数据类型  年份 查询数据列表
export function getResourceByDataYearList(dataType,year){
  return request({
    url: '/system/resource/type/' + dataType + '/year/' + year,
    method: 'get'
  })
}

// 修改资源数据状态
export function updateResourceStatus(resourceId,status){
  return request({
    url: '/system/resource/id/' + resourceId +'/status/' + status,
    method: 'put'
  })
}

// 查询照片拍摄地点
export function getPictureAddress(query) {
  return request({
    url: '/system/resource/address',
    method: 'get',
    params: query
  })
}

// 清空历史分配
export function deleteAssignRoles(roleType) {
  return request({
    url: '/system/resource/delRole/' + roleType,
    method: 'get'
  })
}

// 为 资源 数据分配角色
export function resourceAssignRoles(query) {
  return request({
    url: '/system/resource/role/' + query.roleType + '/resourceIds/' + query.resourceAutoIds,
    method: 'get'
  })
}

// 前端展示-分角色权限展示树结构 自然资源、新型基础测绘、三维立体时空数据库
export function getRolesResourceList(role) {
  return request({
    url: '/system/resource/webRole/' + role,
    method: 'get'
  })
}
