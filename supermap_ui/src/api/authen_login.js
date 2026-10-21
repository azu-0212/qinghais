import request from '@/utils/request'
// 获取账户认证登录信息
export function authenticationLogin(username, password) {
  console.log('data',username, password);
  return request({
    url: '/authenlogin/param/'+ username + '/' + password,
    method: 'get'
  })
}
