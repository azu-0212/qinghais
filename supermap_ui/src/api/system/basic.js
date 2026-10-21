import request from '@/utils/request'
import axios from "axios";

// 查询在线人数
export function listOnline() {
  return request({
    url: '/monitor/online/list',
    method: 'get'
  })
}

// 查询访问量
export function listLoginInfor() {
  return request({
    url: '/monitor/logininfor/list',
    method: 'get'
  })
}

