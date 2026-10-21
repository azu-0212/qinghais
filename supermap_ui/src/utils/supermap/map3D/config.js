import { Cesium } from '../consts'
import { listMapConfig } from '@/api/system/mapConfig'

export default {
  init() {
    return new Promise((resolve, reject) => {
      listMapConfig().then(res => {
        if (res && res.rows && res.rows.length > 0) {
          resolve(this.handleResult(res.rows))
        } else {
          reject('无在线配置文件，请在后台添加！')
        }
      }).catch(e => {
        reject('请求配置文件失败!')
      })
    })
  },
  handleResult(data) {
    this.remoteConfig = {}
    data.map(item => {
      this.remoteConfig[item.configName] = JSON.parse(item.configJson)
    })
    return this.remoteConfig
  },
  remoteConfig: {},
  // cesium 初始化参数
  defaultCesiumOpt: {
    // 是否显示导航控件
    navigation: false,
    // 是否显示信息框
    infoBox: false,
    // 是否显示选取指示器组件
    selectionIndicator: true
    // 是否创建动画小器件，左下角仪表
    // animation: false,
    // 是否显示图层选择器
    // baseLayerPicker: false,
    // 是否显示全屏按钮
    // fullscreenButton: false,
    // 是否显示geocoder小器件，右上角查询按钮
    // geocoder: false,
    // 是否显示Home按钮
    // homeButton: false,
    // 是否显示3D/2D选择器
    // sceneModePicker: false,
    // 是否显示时间轴
    // timeline: false,
    // 是否显示右上角的帮助按钮
    // navigationHelpButton: false,
    // 如果设置为true，则所有几何图形以3D模式绘制以节约GPU资源
    // scene3DOnly: true,
    // 用于控制当前时间的时钟对象
    // clock: new Cesium.Clock(),
    // 当前图像图层的显示模型，仅baseLayerPicker设为true有意义
    // selectedImageryProviderViewModel: undefined,
    // 可供BaseLayerPicker选择的图像图层ProviderViewModel数组
    // imageryProviderViewModels: Cesium.createDefaultImageryProviderViewModels(),
    // 当前地形图层的显示模型，仅baseLayerPicker设为true有意义
    // selectedTerrainProviderViewModel: undefined,
    // 可供BaseLayerPicker选择的地形图层ProviderViewModel数组
    // terrainProviderViewModels: Cesium.createDefaultTerrainProviderViewModels(),
    // 图像图层提供者，仅baseLayerPicker设为false有意义
    // imageryProvider: new Cesium.SuperMapImageryProvider({
    //   credit: '',
    //   url: 'http://www.supermapol.com/realspace/services/3D-dixingyingxiang/rest/realspace/datas/MosaicResult'
    // }),
    // 地形图层提供者，仅baseLayerPicker设为false有意义
    // terrainProvider:new Cesium.CesiumTerrainProvider({
    //   url : 'http://www.supermapol.com/realspace/services/3D-dixingyingxiang/rest/realspace/datas/DatasetDEM',
    //   isSct : true//地形服务源自SuperMap iServer发布时需设置isSct为true
    // }),
    // 用于渲染星空的SkyBox对象
    // skyBox: new Cesium.SkyBox({
    //   sources: {
    //     positiveX: 'Cesium-1.7.1/Skybox/px.jpg',
    //     negativeX: 'Cesium-1.7.1/Skybox/mx.jpg',
    //     positiveY: 'Cesium-1.7.1/Skybox/py.jpg',
    //     negativeY: 'Cesium-1.7.1/Skybox/my.jpg',
    //     positiveZ: 'Cesium-1.7.1/Skybox/pz.jpg',
    //     negativeZ: 'Cesium-1.7.1/Skybox/mz.jpg'
    //   }
    // }),
    // fullscreenElement: document.body,//全屏时渲染的HTML元素,
    // useDefaultRenderLoop: true,//如果需要控制渲染循环，则设为true
    // targetFrameRate: undefined,//使用默认render loop时的帧率
    // showRenderLoopErrors: false,//如果设为true，将在一个HTML面板中显示错误信息
    // automaticallyTrackDataSourceClocks: true,//自动追踪最近添加的数据源的时钟设置
    // contextOptions: undefined,//传递给Scene对象的上下文参数（scene.options）
    // sceneMode: Cesium.SceneMode.SCENE3D,//初始场景模式
  }
}
