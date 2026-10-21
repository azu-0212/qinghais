const state = {
  panoramaShow: false,
  panoramaUrl: "",
  flyRouteShow: false,
  mapShowBool: true,

  buildIndex: null,
  houseIndex: null
}

const mutations = {
  SET_PANORAMA_SHOW: (state, val) => {
    state.panoramaShow = val
  },
  SET_FLY_ROUTE_SHOW: (state, val) => {
    state.flyRouteShow = val
  },
  SET_PANORAMA_URL: (state, val) => {
    state.panoramaUrl = val
  },
  SET_MAP_SHOW: (state, val) => {
    state.mapShowBool = val
  },

  SET_BUILD_INDEX: (state, val) => {
    state.buildIndex = val
  },
  SET_HOUSE_INDEX: (state, val) => {
    state.houseIndex = val
  }
}

const actions = {
  CHANGE_PANORAMA_SHOW({ commit }, data) {
    commit('SET_PANORAMA_SHOW', data)
  },
  CHANGE_FLY_ROUTE_SHOW({ commit }, data) {
    commit('SET_FLY_ROUTE_SHOW', data)
  },
  CHANGE_PANORAMA_URL({ commit }, data) {
    commit('SET_PANORAMA_URL', data)
  },
  CHANGE_MAP_SHOW({ commit }, data) {
    commit('SET_MAP_SHOW', data)
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
