import request from '@/utils/request';

// 根据数据类型查询各类年份信息
export function listResourceByYear(pid) {
  return request({
    url: '/system/resource/year/' + pid,
    method: 'get'
  })
}
