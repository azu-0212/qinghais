import supermap from './modules/supermap'

const getters = {
  sidebar: state => state.app.sidebar,
  size: state => state.app.size,
  device: state => state.app.device,
  title: state => state.app.title,
  appCfg: state => state.app.appCfg,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  introduction: state => state.user.introduction,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,
  permission_routes: state => state.permission.routes,
  panorama_show: state => state.supermap.panoramaShow,
  flyRoute_show: state => state.supermap.flyRouteShow,
  panorama_url: state => state.supermap.panoramaUrl,
  mapShowBool: state => state.supermap.mapShowBool,
  buildIndex: state => state.supermap.buildIndex,
  houseIndex: state => state.supermap.houseIndex
}
export default getters
