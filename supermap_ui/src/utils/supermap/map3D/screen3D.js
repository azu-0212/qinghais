import {Cesium} from "@/utils/supermap/consts";
import config from "@/utils/supermap/map3D/config";
import queryServer from "@/utils/supermap/map3D/queryServer";
import IPopup from "@/utils/supermap/map3D/popup";
import measure3D from "@/utils/supermap/map3D/measure3D";

export default {
  viewer: {},
  scene: {},
  widget: {},
  camera: {},
  rotateAnimate: {},
  measure3DUtil: measure3D,
  defaultS3mName: "",
  cfg: {},
  mvtMap: {},
  flyManager: undefined,
  S3MLayersNames: [],
  imageLayersMap: new Map(),
  scpS3MTilesLayer:new Map(),
  oldH: 99999,
  scp_show_limit: 99999,
  initScene() {
    // this.s_cfg = s_cfg
    this.viewer = new Cesium.Viewer("mapScreen3D", config.defaultCesiumOpt);
    this.widget = this.viewer.cesiumWidget;
    this.scene = this.viewer.scene;
    this.camera = this.scene.camera;
    this.popup = new IPopup(this.viewer, this.viewer.scene);
    config.init().then(res => {
      queryServer.queryConfig = res["query_config"];
      this.cfg = res["map_config"];
      this.scp_show_limit = res["map_config"]["mapInit"]["SCP_SHOW_LIMIT"];
      // 获取地图的多端口子域配置
      this.subdomainConfig = res["map_config"]["subdomainConfig"];
      // 为三维地图添加访问token
      this.registerCredential(this.cfg.server["map_token"]);
      // 为三维地图添加访问token
      this.registerCredential(this.cfg.server["map_token"]);
      // 关闭版权信息
      this.viewer._cesiumWidget._creditContainer.style.display = "none";
      this.flyToInitView(this.cfg["mapInit"]);
      this.openScene().then(res => {
        console.log(">>> 初始化场景成功！");
        // 添加地形图层
        this.addTerrainLayer()
          .then(e => {
            // 添加影像图层
            this.addImgProvider();
            // 添加MVT瓦片图形
            this.addMvtToMap();
          })
          .catch(e => {
            // 添加影像图层
            this.addImgProvider();
            // 添加MVT瓦片图形
            this.addMvtToMap();
          });
        //addAllS3MByScp
        // this.addAllS3MByScp()
        // 添加监听事件
        this.initSceneSuccess();
      });
      // 初始化测量控件
      this.measure3DUtil.initMeasure3DControl(Cesium, this.viewer);

    })
      .catch(e => {
      this.widget.showErrorPanel(
        "错误提示",
        e,
        "自定义方法initScene过程出错"
      );
    });
  },

  /**
   * 添加地图令牌
   * @param token {String} 地图令牌
   */
  registerCredential(token) {
    // console.log('>>> 添加地图令牌:token:', token)
    if (token) {
      Cesium.Credential.CREDENTIAL = new Cesium.Credential(token);
    }
  },

  /**
   * 初始化场景  矢量
   */
  openScene() {
    //console.log(">>> 正在初始化场景......");
    let subdomains = this.subdomainConfig;
    if (this.cfg.server && this.cfg.server.host) {
      if (this.cfg.server.s_realspace) {
        // const scene_url = `http://${this.cfg.server.host}/iserver/services/${this.cfg.server.s_realspace}`;
        const scene_url = `${this.cfg.server.s_realspace}`;
        //return this.scene.open(scene_url,undefined);
        return this.scene.open(scene_url,undefined,{subdomains:subdomains});
      } else {
        return new Promise((resolve, reject) => {
          resolve("");
          console.log(">>> 未检测到配置文件中有场景地址,直接跳过 <<<");
        });
      }
    }
    throw new Error(
      // `配置文件读取失败:server.host:${server.host},server.s_realspace:${server.s_realspace}`
      `配置文件读取失败:server.host:${server.host},server.s_realspace:${server.s_realspace}`
    );
  },

  /**
   * 前端缓存优化
   */
  CacheBrowser(layer){
    // 前端倾斜摄影优化
    layer.indexedDBSetting.isAttributesSave = true,
      layer.indexedDBSetting.isGeoTilesSave = true,
      layer.indexedDBSetting.isGeoTilesRootNodeSave = true,
      layer.indexedDBSetting.isOPen = true
  },

  /**
   * 添加地形
   */
  addTerrainLayer() {
    return new Promise((resolve, reject) => {
      if (this.cfg.server.terrainLayer) {
        // const terrain_url = `http://${this.cfg.server.host}/iserver/services/${this.cfg.server.terrainLayer}`;
        const terrain_url = `${this.cfg.server.terrainLayer}`;
        this.viewer.terrainProvider = new Cesium.CesiumTerrainProvider({
          // 默认立体地表
          url: terrain_url,
          // 多子域配置
          subdomains:this.subdomainConfig,
          //地形服务源自SuperMap iServer发布时需设置isSct为true
          isSct: true
          //请求批量
          // packingRequest: 1
        });
        setTimeout(() => {
          console.log(">>> 延时两秒后加载其他资源 <<<");
          resolve("");
        }, 2500);
      } else {
        console.log(">>> 未检测到配置文件中有地形图层地址,直接跳过 <<<");
        resolve("");
      }
    });
  },

  /**
   * 加载矢量瓦片
   */
  addMvtToMap() {
    console.log(">>> 正在加载矢量瓦片......");
    if (this.cfg.server.m3d_mvt) {
      // const mvt_url = `http://${this.cfg.server.host}/iserver/services/${this.cfg.server.m3d_mvt}`;
      const mvt_url = `${this.cfg.server.m3d_mvt}`;
      const subdomains = this.subdomainConfig
      this.mvtMap = this.scene.addVectorTilesMap({
        url: mvt_url,
        subdomains:subdomains,
        canvasWidth: 512,
        name: "testMVT",
        viewer: this.viewer
      });
      Cesium.when(this.mvtMap.readyPromise)
        .then(data => {
          console.log(">>> 加载矢量瓦片成功！");
        })
        .catch(e => {
          console.log(">>> 加载矢量瓦片失败！", e);
        });
    } else {
      console.log(">>> 未检测到配置文件中有矢量瓦片地址,直接跳过 <<<");
    }
  },

  /**
   * 添加影像提供者
   */
  addImgProvider() {
    if (this.cfg.server.imageryLayer) {
      // const imageryLayer_url = `http://${this.cfg.server.host}/iserver/services/${this.cfg.server.imageryLayer}`;
      const imageryLayer_url = `${this.cfg.server.imageryLayer}`;
      const subdomains = this.subdomainConfig
      this.viewer.imageryLayers.addImageryProvider(
        new Cesium.SuperMapImageryProvider({
          url: imageryLayer_url,
          subdomains:subdomains
          // 请求批量
          // packingRequest: 1
          // packingRequest: 1
        })
      );
    } else {
      console.log(">>> 未检测到配置文件中有影像图层地址,直接跳过 <<<");
    }
  },

  /**
   * 初始化成功后的回调
   */
  initSceneSuccess() {
    //一些绑定方法
    console.log(">>> setInputAction");
    let handler = new Cesium.ScreenSpaceEventHandler(this.viewer.canvas);
  },

  /**
   * 添加S3M 倾斜缓存
   * @returns {*}
   */
  addS3MTilesLayerByScp(data,position,dataName, url, flag, port) {
    // 区别左右分屏的scp
    dataName = dataName + port;
    if (!flag) {
      this.removeS3MTilesLayerByScp(dataName);
      this.popup.closeAllPopup();
      // 全副青海
      //this.entireView();
      return;
    }
    let pos = [
      parseFloat(position.lon),
      parseFloat(position.lat) - 0.01,
      4000
      //parseFloat(position.alt) + 300
    ]
    //清除历史数据
    this.removeScpLayersAll();
    const scp_url = `${url}`;
    const subdomain = this.subdomainConfig;
    if(subdomain){
      this.scene.addS3MTilesLayerByScp(scp_url, {subdomainConfig:{
          urlScheme:"http://{s}/iserver/services/"+ url.split("/")[5] +"/rest/realspace",
          subdomains:subdomain
        },
        name:dataName
      }).then(res => {
        const layer = this.scene.layers.find(dataName);
        // 设置倾斜摄影高程
        //layer.style3D.bottomAltitude = -(parseFloat(data.height));
        // 图层加lod
        layer.lodRangeScale = 0.1;
        /* let pos = [
           layer.lon,
           //layer.lat,
           layer.lat - 0.01,
           Math.round(layer._hypMaxHeight ? layer._hypMaxHeight + 2000 : 4000)
         ];*/
        this.flyToView(pos,this.cfg["mapInit"]["s3MTilesPos"]["orientation"],8);
        this.scpS3MTilesLayer.set(dataName, layer);
      }).otherwise(err => {
        console.log("加载倾斜摄影失败:err=>", err);
      });
      return;
    }

    this.scene.addS3MTilesLayerByScp(scp_url, {name: dataName},{subdomains:this.subdomainConfig}).then(res => {
      const layer = this.scene.layers.find(dataName);
      // 视口分屏
      layer.setVisibleInViewport(port, false);
      this.flyToView(pos);
    })
      .otherwise(err => {
        console.log("加载倾斜摄影失败:err=>", err);
      });
  },

  /**
   * 根据名称移除S3M缓存图层
   */
  removeS3MTilesLayerByScp(name) {
    if (this.scene.layers.find(name)) {
      this.scene.layers.remove(name, true);
    }
  },


  /**
   * 分屏初始化
   */
  initMultiViewPort(){
    this.scene.multiViewportMode = Cesium.MultiViewportMode["HORIZONTAL"];
  },

  /**
   * 添加marker点位
   */
  add3DMarkersToMap(data, icon, flag) {
    if(!flag){
      this.popup.closeAllPopup();
      // 全幅青海
      //this.entireView();
      return;
    }
    console.log('data',data);
    data.map(item => {
      let pos = JSON.parse(item.position);
      this.viewer.entities.add({
        position: Cesium.Cartesian3.fromDegrees(
          parseFloat(pos.lon),
          parseFloat(pos.lat),
          parseFloat(pos.alt)
        ),
        billboard: {
          image: icon,
          pixelOffset: new Cesium.Cartesian2(0, -10), //偏移量
          // heightReference:Cesium.HeightReference.CLAMP_TO_GROUND,
          clampToGround: true,
          disableDepthTestDistance: Number.POSITIVE_INFINITY,
          distanceDisplayCondition: new Cesium.DistanceDisplayCondition(0, 2700000),
        },
        label: {
          //文字标签
          text: item.nodeName || item.videoName,
          scale: 0.6,
          style: Cesium.LabelStyle.FILL,
          fillColor: Cesium.Color.WHITE,
          pixelOffset: new Cesium.Cartesian2(30, -15), //偏移量
          showBackground: true,
          backgroundColor: new Cesium.Color(0, 0, 0, 0.6),
          verticalOrigin: Cesium.VerticalOrigin.CENTER, //垂直位置
          horizontalOrigin: Cesium.HorizontalOrigin.LEFT, //水平位置
          //disableDepthTestDistance: Number.POSITIVE_INFINITY
          // heightReference:Cesium.HeightReference.CLAMP_TO_GROUND,
          distanceDisplayCondition: new Cesium.DistanceDisplayCondition(0, 120000),
          disableDepthTestDistance: 120000,
        },
        name: item.nodeName || item.videoName,
        description: item.nodeName || item.videoName
      });
      this.viewer.entities.entities.values[0].setVisibleInViewport(1, false);
    });
  },

  /**
   * 跳转给定视角
   */
  flyToView(pos, orientation, duration) {
    console.log("跳转给定视角", pos,orientation);
    if (orientation && orientation.pitch) {
      this.camera.flyTo({
        destination: Cesium.Cartesian3.fromDegrees(...pos),
        orientation: orientation,
        duration:duration
      });
    } else {
      this.camera.flyTo({
        destination: Cesium.Cartesian3.fromDegrees(...pos),
        duration:duration
      });
    }
  },

  /**
   * 根据名称移除栅格图层
   */
  removeImageLayersByName(name) {
    if (this.imageLayersMap.has(name)) {
      let rst = this.viewer.imageryLayers.remove(
        this.imageLayersMap.get(name),
        true
      );
      //console.log("移除图层:", rst ? "success" : "fuck");
      this.imageLayersMap.delete(name);
    }
  },

  /**
   * 添加影像地图
   * @param name
   * @param url
   * @param flag
   */
  addImageLayerByUrl(name, url, flag, port) {
    let _this = this;
    // 区别左右分屏的影像，分别清楚
    name = name + port;
    if (!flag) {
      this.removeImageLayersByName(name);
      // 全副青海
      //this.entireView();
      return;
    }
    // const imageryLayer_url = `http://${this.cfg.server.host}/iserver/services/${url}`;
    const imageryLayer_url = `${url}`;
    // const imageryLayer_url = "http://222.91.72.102:2686/iserver/services/map-zxytiles-ShanXiShengYingXiangShuJu/rest/maps/陕西省影像数据";  // 测试数据
    let layer = new Cesium.ImageryLayer(
      new Cesium.SuperMapImageryProvider({
        url: imageryLayer_url
        // name:name,
      })
    );
    // 前端倾斜摄影优化
    _this.CacheBrowser(layer);
    this.imageLayersMap.set(name, layer);
    this.viewer.imageryLayers.add(layer);
    console.log(this.viewer.imageryLayers);
    // 视口分屏
    layer.setVisibleInViewport(port, false);
  },

  /**
   * 添加MVT
   * @param name
   * @param url
   * @param flag
   */
  addMvtByUrl(name, url, flag, port) {
    name = name + port;
    if (!flag) {
      this.removeMVTLayerByName(name);
      return;
    }
    //const mvt_url = "http://222.91.72.102:2686/iserver/services/3D-3Dmvt-MapClipChina4326/rest/realspace/datas/MapClip@China_4326";   //测试数据
    const subdomains = this.subdomainConfig;
    // const mvt_url = `http://${this.cfg.server.host}/iserver/services/${url}`;
    const mvt_url = `${url}`;
    let mvtMap = this.scene.addVectorTilesMap({
      url: mvt_url,
      subdomains:subdomains,
      canvasWidth: 512,
      name: name,
      packingRequest: 1,
      viewer: this.viewer
    });
    Cesium.when(mvtMap.readyPromise)
      .then(data => {
        // let layer = this.scene.getVectorTilesMap(name);
        console.log(">>> 加载矢量瓦片成功！:");
        // const bounds = mvtMap.rectangle;
        // this.scene.camera.flyTo({
        //   destination: new Cesium.Cartesian3.fromRadians((bounds.east + bounds.west) * 0.5, (bounds.north + bounds.south) *
        //     0.5, 30000),
        //   orientation: {
        //     heading: 0,
        //     roll: 0
        //   }
        // });
        // let screenSpaceEventHandler = new Cesium.ScreenSpaceEventHandler(scene.canvas);
      })
      .otherwise(e => {
        console.log(">>> 加载矢量瓦片失败！", e);
      });
    // 视口分屏
    mvtMap.setVisibleInViewport(port, false);
  },

  /**
   * 根据名称移除MVT缓存图层
   * @param name
   */
  removeMVTLayerByName(name) {
    if (this.scene.getVectorTilesMap(name)) {
      const rst = this.scene.removeVectorTilesMap(name);
      console.log("移除图层:", rst ? "success" : "fuck");
    }
  },

  /**
   * 跳转默认视角
   */
  flyToInitView(mapInit = this.cfg["mapInit"]) {
    if (mapInit && mapInit.initPos) {
      this.viewer.camera.flyTo({
        destination: mapInit.initPos.position,
        orientation: mapInit.initPos.orientation
      });
    }
  },
  /**
   * 全幅青海
   */
  entireView() {
    console.log('ss',this.cfg["mapInit"]);
    this.camera.flyTo({
      destination: this.cfg["mapInit"]["entirePos"]["position"],
      orientation: this.cfg["mapInit"]["entirePos"]["orientation"]
    });
  },
  /**
   * 遍历清除所有scp倾斜摄影
   */
  removeScpLayersAll(){
    let _this = this;
    this.scpS3MTilesLayer.forEach(function(value,key){
      _this.removeS3MTilesLayerByScp(key);
    })
  },
  /**
   * 清除所有地图所添加的实体
   */
  clearEntity() {
    this.viewer.entities.removeAll();
  }
}
