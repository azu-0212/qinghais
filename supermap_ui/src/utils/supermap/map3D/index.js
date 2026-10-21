import measure3D from "./measure3D";
import analysis3D from "./analysis3D";
import config from "./config";
import { Cesium } from "../consts";
import queryServer from "./queryServer";
import IPopup from "./popup";
import appConfig from "../../appConfig";

import { listInfo } from "@/api/system/info";
import MapUtil from "@/utils/supermap";
import $ from "jquery";
import { getOne, get, getSatellite } from "@/api/system/image";
import { getPictureAddress } from "@/api/system/resource"
import { listResource } from "@/api/system/resource";
import MapUtils from "@/utils/supermap";
import th from "element-ui/src/locale/lang/th";
import store from "@/store";

// const earthRotate = 0; const camera = {}

export default {
  viewer: {},
  scene: {},
  widget: {},
  camera: {},
  rotateAnimate: {},
  measure3DUtil: measure3D,
  analysis3DUtil: analysis3D,
  defaultS3mName: "",
  cameraPosition: {},
  entityId:"",
  cfg: {},
  mvtMap: {},
  scenePosition:{},
  flyManager: undefined,
  S3MLayersNames: [],
  imageLayersMap: new Map(),
  initImageLayersMap: new Map(),
  mvtLayersMap: new Map(),
  scpS3MTilesLayer: new Map(),
  geoJsonDataSources: new Map(),
  oldH: 99999,
  scp_show_limit: 99999,
  latStep: 0.0015, // 经纬度 相机位置 偏下
  altStep:1000, // 相机高程
  markersPanorama3D:require("@/assets/image/mapMarker/pos_1.png"),// 全景数据图标
  markersStreet:require("@/assets/image/mapMarker/pos_2.png"),// 街景数据图标
  markersVideo:require("@/assets/image/mapMarker/pos_4.png"),// 视频数据图标
  maskThume: require("@/assets/image/popupMarker/openImg.png"),// 放大照片图标
  maskersFullScreen: require("@/assets/image/popupMarker/fullScreenImg.png"),// 全屏图标
  // 照片相关库图片
  maskImgLeftArrow: require("@/assets/image/popupMarker/left-arrow.png"), // 左箭头
  maskImgRightArrow: require("@/assets/image/popupMarker/right-arrow.png"), // 右箭头
  maskImgGoMove: require("@/assets/image/popupMarker/go-ahead.png"), //前进
  maskImgBackMove: require("@/assets/image/popupMarker/back.png"), // 后退
  // 设置子域，设置服务器
  //this.subdomainConfig = ["localhost:8081","localhost:8082","localhost:8083"],
  interestPoints:[],//兴趣点信息
  position:{},
  getPositionType:"",// 点击对象类型
  imageType: "",//照片类型
  // 重点区域弹出框展示类型
  getAreaVideo:"",
  getAreaPanorama:"",
  initScene() {
    window.viewer = new Cesium.Viewer("map3D", config.defaultCesiumOpt);
    //this.viewer = new Cesium.Viewer("map3D", config.defaultCesiumOpt);
    this.viewer = window.viewer;
    // 用scene.open打开场景
    this.widget = this.viewer.cesiumWidget;
    this.scene = this.viewer.scene;
    this.camera = this.scene.camera;
    this.popup = new IPopup(this.viewer, this.viewer.scene);
    this.handler = new Cesium.ScreenSpaceEventHandler(this.scene.canvas);
    this.viewer.scene.terrainProvider.isCreateSkirt = false; //即可关闭网格。
    //this.scene.screenSpaceCameraController.enableIndoorColliDetection = true;  //开启碰撞检测
    config.init().then(res => {
        queryServer.queryConfig = res["query_config"];
        this.cfg = res["map_config"];
        this.scp_show_limit = res["map_config"]["mapInit"]["SCP_SHOW_LIMIT"];
        // 获取地图的多端口子域配置
        this.subdomainConfig = res["map_config"]["subdomainConfig"];
        //兴趣点信息
        this.interestServerUrl = res["map_config"]["interestPoint"]["serverUrl"];//服务地址
        this.interestDataSource = res["map_config"]["interestPoint"]["dataSource"];//数据源
        this.interestDataSet = res["map_config"]["interestPoint"]["dataSet"];//数据集
        this.streetHost = res["map_config"]["streetHost"]["host"];
        //查询兴趣点
        this.getInterestPoint();
        // 为三维地图添加访问token
        this.registerCredential(this.cfg.server["map_token"]);
        // 关闭版权信息
        this.viewer._cesiumWidget._creditContainer.style.display = "none";
        // 获取笛卡尔坐标及初始化
        this.getScenePosition();
        // 初始化全幅青海
        this.entireView();
        this.openScene().then(res => {
          console.log(">>> 初始化场景成功！");
          // 添加地形图层
          this.addTerrainLayer()
            .then(e => {
              // 添加影像图层
              this.addImgProvider();
              // 添加MVT瓦片图形
              this.addMvtToMap();
              // 初始化矢量数据
              this.initMapLayer();
            })
            .catch(e => {
            });
        });
        this.measure3DUtil.initMeasure3DControl(Cesium, this.viewer);
        this.analysis3DUtil.initAnalysis(this.viewer, this.scene);
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
   * 照片库相关
   */

  //初始化viewer  Cluser
  initViewer() {
    this.viewer.cesiumWidget.screenSpaceEventHandler.removeInputAction(Cesium.ScreenSpaceEventType.LEFT_DOUBLE_CLICK);
  },

  initCluser(flag, imgType) {
    let _this = this;
    if(!flag){
      this.destroyDynamicCluser();
      return;
    }

    let cluserPointJson = "";
    let cluserPointJson1 = "";
    let cluserPointJson2 = "";
    let cluserPointJson3 = "";
    let cluserPointJson4 = "";
    let cluserPointJson5 = "";
    let cluserPointJson6 = "";
    let cluserPointJson7 = "";
    let cluserPointJson8 = "";
    let cluserPointJson9 = "";
    let cluserPointJson10 = "";
    let cluserPointJson2020_1 = "";
    let cluserPointJson2020_2 = "";
    let cluserPointJson2020_3 = "";
    if(imgType.indexOf("地理国情-2015")!==-1){
      //cluserPointJson = appConfig.cluserPoint2015;
      _this.getPositionType = "image"; // 点击查询照片类（单张查询）
      cluserPointJson1 = appConfig.cluserPoint2015_1;
      cluserPointJson2 = appConfig.cluserPoint2015_2;
      cluserPointJson3 = appConfig.cluserPoint2015_3;
      cluserPointJson4 = appConfig.cluserPoint2015_4;
      cluserPointJson5 = appConfig.cluserPoint2015_5;
      cluserPointJson6 = appConfig.cluserPoint2015_6;
      cluserPointJson7 = appConfig.cluserPoint2015_7;
      cluserPointJson8 = appConfig.cluserPoint2015_8;
      cluserPointJson9 = appConfig.cluserPoint2015_9;
      cluserPointJson10 = appConfig.cluserPoint2015_10;
    }
    if(imgType.indexOf("地理国情-2017")!==-1){
      cluserPointJson = appConfig.cluserPoint2017;
      _this.getPositionType = "image"; // 点击查询照片类（单张查询）
    }
    if(imgType.indexOf("地理国情-2018")!==-1){
      cluserPointJson = appConfig.cluserPoint2018;
      _this.getPositionType = "image"; // 点击查询照片类（单张查询）
    }
    if(imgType.indexOf("地理国情-2019")!==-1){
      cluserPointJson = appConfig.cluserPoint2019;
      _this.getPositionType = "image"; // 点击查询照片类（单张查询）
    }
    if(imgType.indexOf("地理国情-2020")!==-1){
     // cluserPointJson = appConfig.cluserPoint2020;
      _this.getPositionType = "image"; // 点击查询照片类（单张查询）
      cluserPointJson2020_1 = appConfig.cluserPoint2020_1;
      cluserPointJson2020_2 = appConfig.cluserPoint2020_2;
      cluserPointJson2020_3 = appConfig.cluserPoint2020_3;
    }
    if(imgType.indexOf("采矿权")!==-1){
      cluserPointJson = appConfig.cluserPointMining;
      //cluserPointJson = appConfig.cluserPointMiningTest;
      _this.getPositionType = "images"; // 点击查询照片类（多张查询）
    }
    if(imgType.indexOf("工业企业")!==-1){
      cluserPointJson = appConfig.cluserPointIndustrialEnterprises;
      _this.getPositionType = "images"; // 点击查询照片类（多张查询）
    }
    if(imgType.indexOf("扰动生态")!==-1){
      cluserPointJson = appConfig.cluserPointDisturbedEcology;
      _this.getPositionType = "images"; // 点击查询照片类（多张查询）
    }
    if(imgType.indexOf("探矿权")!==-1){
      cluserPointJson = appConfig.cluserPointProspecting;
      _this.getPositionType = "images"; // 点击查询照片类（多张查询）
    }
    if(imgType.indexOf("渣山采坑")!==-1){
      cluserPointJson = appConfig.cluserPointSlagMountain;
      _this.getPositionType = "images"; // 点击查询照片类（多张查询）
    }

    if(imgType.indexOf("耕地“非农化”-2021年")!==-1){
      cluserPointJson = appConfig.cluserPointNonfarm;
      _this.getPositionType = "images"; // 点击查询照片类（多张查询）
    }

    // 卫片执法
    let cluserPointJsonWPPush2021_01 = '';
    let cluserPointJsonWPPush2021_02 = '';
    if(imgType.indexOf("2021年-部推送图斑")!==-1){
      cluserPointJsonWPPush2021_01 = appConfig.cluserPointPush1;
      cluserPointJsonWPPush2021_02 = appConfig.cluserPointPush2;
      _this.getPositionType = "images"; // 点击查询照片类（多张查询）
    }
    if(imgType.indexOf("2021年-省级补充图斑")!==-1){
      cluserPointJson = appConfig.cluserPointProvincial;
      _this.getPositionType = "images"; // 点击查询照片类（多张查询）
    }

    setTimeout(function(){
      _this.initViewer();
      _this.cluserImages = []; //缓存已经生成的图标
      _this.initCluseColors();
      if(imgType.indexOf("地理国情-2015")!==-1){
        _this.initEightDynamicCluser(cluserPointJson1,"地理国情-2015_1");
        _this.initEightDynamicCluser(cluserPointJson2,"地理国情-2015_2");
        _this.initEightDynamicCluser(cluserPointJson3,"地理国情-2015_3");
        _this.initEightDynamicCluser(cluserPointJson4,"地理国情-2015_4");
        _this.initEightDynamicCluser(cluserPointJson5,"地理国情-2015_5");
        _this.initEightDynamicCluser(cluserPointJson6,"地理国情-2015_6");
        _this.initEightDynamicCluser(cluserPointJson7,"地理国情-2015_7");
        _this.initEightDynamicCluser(cluserPointJson8,"地理国情-2015_8");
        _this.initEightDynamicCluser(cluserPointJson9,"地理国情-2015_9");
        _this.initEightDynamicCluser(cluserPointJson10,"地理国情-2015_10");
        return;
      }
      if(imgType.indexOf("地理国情-2020")!==-1){
        _this.initEightDynamicCluser(cluserPointJson2020_1,"地理国情-2020_1");
        _this.initEightDynamicCluser(cluserPointJson2020_2,"地理国情-2020_2");
        _this.initEightDynamicCluser(cluserPointJson2020_3,"地理国情-2020_3");
        return;
      }
      if(imgType.indexOf("2021年-部推送图斑")!==-1){
        _this.initEightDynamicCluser(cluserPointJsonWPPush2021_01,'2021_01');
        _this.initEightDynamicCluser(cluserPointJsonWPPush2021_02,'2021_02');
        return;
      }
      _this.initEightDynamicCluser(cluserPointJson,imgType);
    },100)
  },

  destroyDynamicCluser() {
    let _this = this;
    this.popup.closeAllPopup();
    this.removeListener = undefined;
    this.geoJsonDataSources.forEach(function(value,key){
      _this.viewer.dataSources.remove(value,false);
    })
  },
  /* 2017/2018聚合 */
  //初始化聚合
  initEightDynamicCluser(cluserPointJson,name) {
    let _this = this;
    this.clusteringlayer = new Cesium.GeoJsonDataSource().load(cluserPointJson).then(geoJsonDataSource => {
      this.geoJsonDataSource = geoJsonDataSource;
      this.geoJsonDataSources.set(name, this.geoJsonDataSource);
      this.viewer.dataSources.add(geoJsonDataSource);
      this.setEightClusterEvent(geoJsonDataSource);
      geoJsonDataSource.clustering.enabled = true;
      geoJsonDataSource.clustering.pixelRange = 50;
      geoJsonDataSource.clustering.minimumClusterSize = 10;

      //设置相机的图标
      geoJsonDataSource.entities.values.forEach(entity => {
        entity.billboard.image = require("@/assets/image/mapMarker/pos_5.png") //"static/images/camera.png";
        entity.type = "cluser";
        //如果有高度信息 需要重新赋值
        if(entity._properties._ALT!==undefined){
          //如果有高度信息 需要重新赋值
          entity.position = Cesium.Cartesian3.fromDegrees(Number(entity._properties._Center_X._value),Number(entity._properties._Center_Y._value),Number(entity._properties._ALT._value)+50);
        }else{
          entity.position = Cesium.Cartesian3.fromDegrees(Number(entity._properties._Center_X._value),Number(entity._properties._Center_Y._value),Number(4550));
           /*let cartographics= [Cesium.Cartographic.fromDegrees(entity._properties._Center_X._value,entity._properties._Center_Y._value)];
           Cesium.sampleTerrain(_this.viewer.terrainProvider, 11, cartographics).then((updatedPositions)=>{
             entity.position = Cesium.Cartesian3.fromDegrees(Number(entity._properties._Center_X._value),Number(entity._properties._Center_Y._value),Number(updatedPositions[0].height)+50);
           });*/
        }
      });
    })
  },
  //设置聚合事件
  setEightClusterEvent(geoJsonDataSource) {
    this.removeListener = geoJsonDataSource.clustering.clusterEvent.addEventListener(
      (clusteredEntities, cluster) => {
        cluster.billboard.show = true;
        cluster.label.show = false;
        cluster.billboard.id = cluster.label.id;
        cluster.billboard.verticalOrigin = Cesium.VerticalOrigin.BOTTOM;
        cluster.billboard.distanceDisplayCondition = new Cesium.DistanceDisplayCondition(0,2000000);
        cluster.billboard.image = this.getCluserImage(clusteredEntities.length);
      }
    );
  },

  //获取聚合图标
  getCluserImage(length) {
    if (this.cluserImages[length]) {
      return this.cluserImages[length];
    }
    var c = document.createElement("canvas");
    //一个数字大概12像素
    const d = (length + "").length * 12 + 50;
    c.width = c.height = d;
    var ctx = c.getContext("2d");
    //绘制大圆
    ctx.beginPath();
    ctx.globalAlpha = 0.5;
    ctx.fillStyle = this.getCluseColor(length); //绘制样式
    ctx.arc(d / 2, d / 2, d / 2 - 5, 0, 2 * Math.PI);
    ctx.fill();

    //绘制小圆
    ctx.beginPath();
    ctx.globalAlpha = 0.8;
    ctx.fillStyle = this.getCluseColor(length); //绘制样式
    ctx.arc(d / 2, d / 2, d / 2 - 10, 0, 2 * Math.PI);
    ctx.fill();

    //绘制文本
    ctx.font = "20px 微软雅黑";
    ctx.globalAlpha = 1;
    ctx.fillStyle = "rgb(255,255,255)";
    let fd = length.toString().length * 12; //文字的长度
    const x = d / 2 - fd / 2;
    ctx.fillText(length, x, d / 2 + 10);
    return c;
  },
  //图标颜色
  initCluseColors() {
    //颜色设置
    this.cluserColors = [{
      value: 100, //聚合数大于等于100 红色
      color: "rgb(255,0,0)"
    }, {
      value: 50, //聚合数大于等于50 黄
      color: "rgb(255,255,0)"
    }, {
      value: 10, //聚合数大于等于10 蓝色
      color: "rgb(51, 133, 255)"
    }, {
      value: 1, //聚合数大于等于1 绿
      color: "rgb(0,255,0)"
    }]
  },
  //获取聚合颜色
  getCluseColor(length) {
    for (let i = 0; i < this.cluserColors.length; i++) {
      const element = this.cluserColors[i];
      if (length >= element.value)
        return element.color;
    }
  },


  /**
   * 初始化场景  矢量
   */
  openScene() {
    //console.log(">>> 正在初始化场景......");

    if (this.cfg.server && this.cfg.server.host) {
      if (this.cfg.server.s_realspace) {
        // const scene_url = `http://${this.cfg.server.host}/iserver/services/${this.cfg.server.s_realspace}`;
        const scene_url = `${this.cfg.server.s_realspace}`;
        //return this.scene.open(scene_url,undefined);
        if(this.subdomainConfig.length>0){
          return this.scene.open(scene_url,undefined,{subdomains:this.subdomainConfig});
        }else{
          return this.scene.open(scene_url,undefined);
        }
      } else {
        return new Promise((resolve, reject) => {
          resolve("");
          console.log(">>> 未检测到配置文件中有场景地址,直接跳过 <<<");
        });
      }
    }
    throw new Error(
      // `配置文件读取失败:server.host:${server.host},server.s_realspace:${server.s_realspace}`
      `配置文件读取失败:server.s_realspace:${server.s_realspace}`
    );
  },

  /**
   * 初始化 行政边界
   */
  initMapLayer(){
    if (this.cfg.server) {
      if (this.cfg.server.mapLayer) {
        const imageryLayer_url = `${this.cfg.server.mapLayer}`;
        let layer;
       /* if (this.subdomainConfig) {
          layer = new Cesium.ImageryLayer(
            new Cesium.SuperMapImageryProvider({
              //url: imageryLayer_url,
              url:" http://{s}/iserver/services/"+ imageryLayer_url.split("/")[5] +"/rest/maps/"+imageryLayer_url.split("/")[8],
              subdomains: this.subdomainConfig,
              // name:name,
            })
          );
        } else {
          layer = new Cesium.ImageryLayer(
            new Cesium.SuperMapImageryProvider({
              url: imageryLayer_url,
              // name:name,
            })
          );
        }*/
        layer = new Cesium.ImageryLayer(
          new Cesium.SuperMapImageryProvider({
            url: imageryLayer_url,
            // name:name,
          })
        );
        layer._maximumTerrainLevel = 8;
        // 前端缓存优化
        this.CacheBrowser(layer);
        this.initImageLayersMap.set(imageryLayer_url.split("/")[5], layer);
        this.viewer.imageryLayers.add(layer);

      }
    }
  },

  /**
   *
   *  打开场景3D图层(数据包含两个图层)
   */
  add3DSceneLayerByScp(data,flag){
    let _this = this;
    // 清除初始化行政区划
    this.removeInitImageLayers();
    if(data && data.serverUrl.indexOf(",")!==-1){
      let server_arr = data.serverUrl.split(",");
      const scp_url = server_arr[0];
      let scpName = scp_url.split("/")[9];
      const text_url = server_arr[1];
      let textScpName = text_url.split("/")[9];
      if(!flag){
        if (this.scene.layers.find(scpName)) {
          this.scene.layers.remove(scpName, true);
        }
        if (this.scene.layers.find(textScpName)) {
          this.scene.layers.remove(textScpName, true);
        }
        return;
      }

      let promise = this.viewer.scene.addS3MTilesLayerByScp(scp_url, {name: scpName})
      let promiseText = this.viewer.scene.addS3MTilesLayerByScp(text_url, {name: textScpName}).then(res => {
        const layer = this.scene.layers.find(textScpName);
        // 设置可见高度
        layer.visibleDistanceMax = 8000000;
      })
      Cesium.when.all([promise, promiseText], function (layers) {
        _this.scpS3MTilesLayer.set(scpName, promise);
        _this.scpS3MTilesLayer.set(textScpName, promiseText);
      }, function (e) {
        if (this.widget._showRenderLoopErrors) {
          let title = '加载SCP失败，请检查网络连接状态或者url地址是否正确？';
          this.widget.showErrorPanel(title, undefined, e);
        }
      });
    }
    console.log('_this.scpS3MTilesLayer',_this.scpS3MTilesLayer);
  },

  /**
   * 兴趣点功能，根据sql查询兴趣点属性信息
   */
  getInterestPoint(name){
    //console.log('this.interestDataSource',this.interestDataSource,this.interestDataSet,this.interestServerUrl);
    let _this = this;
    var sqlParameter = {
      "datasetNames": [this.interestDataSource + `:` + this.interestDataSet], // [数据源:数据集]
      getFeatureMode: "SQL",
      queryParameter: {
        attributeFilter: ` NAME LIKE '%${name}%'`
      },
      maxFeatures: 20,
      toIndex: 20
    };
    /*let sqlParameter = {
      "datasetNames": ["layer:photo_layer"], // [数据源:数据集]
      getFeatureMode: "SQL",
      queryParameter: {}
    };*/
    //var url = "http://222.91.72.102:2686/iserver/services/data-photo_layer/rest/data/featureResults.rjson?returnContent=true";
    if (this.cfg.server && this.cfg.server.host) {
      // var url = `http://${this.cfg.server.host}/iserver/services/${this.interestServerUrl}.rjson?returnContent=true`;
      var url = `${this.interestServerUrl}.rjson?returnContent=true`;
    }
    let queryData = JSON.stringify(sqlParameter);
    $.ajax({
      type: "post",
      url: url,
      data: queryData,
      success: function (result) {
        let resultObj = JSON.parse(result);
        let selectedFeatures = resultObj.features;
        _this.interestPoints = selectedFeatures;
      },
      error:function(err){
        console.log("未找到兴趣点数据",err);
      }
    });
  },

  /**
   * 加载第三方WMTS服务图层  初始化WMTS服务  addWMTSRest
   */

  addWMTSRest(name, url, param, flag){
    if (!flag) {
      this.removeImageLayersByName(name);
      return;
    }
    // 先清除历史ImageLayers
    this.removeImageLayersAll();

    //const imageryLayer_url = `http://${this.cfg.server.host}/iserver/services/${url}`;
    //const imageryLayer_url = "http://10.63.1.221:8888/usogcrs?";
    const imageryLayer_url = `${url}`;
    const params = JSON.parse(param);
    if(params.layer === undefined ||
      params.style === undefined ||
      params.format === undefined ||
      params.tileMatrixSetID === undefined ||
      params.tileWidth === undefined ||
      params.ids === undefined){
      return;
    }
    //matrixIds 数量，追加数组中
    let ids=[];
    for(let i=0;i<params.ids;i++){
      ids.push(""+i);
    }
    let tilingScheme;
    if(params.tilingScheme === undefined){
      tilingScheme = null;
    }
    console.log('params',params);
    let layer = new Cesium.ImageryLayer(
      new Cesium.WebMapTileServiceImageryProvider({
        url : imageryLayer_url,
        layer:params.layer,
        style:params.style,
        format:params.format,
        tileMatrixSetID:params.tileMatrixSetID,
        tileWidth:params.tileWidth,
        tileHeight:params.tileHeight,
        matrixIds:ids,
        tilingScheme:new Cesium.GeographicTilingScheme({
          numberOfLevelZeroTilesX:2,
          numberOfLevelZeroTilesY:1
        })
      })
    );
    /*let layer = new Cesium.ImageryLayer(
      new Cesium.WebMapTileServiceImageryProvider({
        url : imageryLayer_url,
        layer:"GQ_2018_DEIMOS2",
        style:"raster",
        format:"image/png",
        tileMatrixSetID:"l",
        tileWidth:256,
        tileHeight:256,
        matrixIds:21,
        tilingScheme:new Cesium.GeographicTilingScheme({
          numberOfLevelZeroTilesX:2,
          numberOfLevelZeroTilesY:1
        })
      })
    );*/
    console.log('layer',layer);
    this.imageLayersMap.set(name, layer);
    this.viewer.imageryLayers.add(layer);
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
      if(subdomains){
        this.mvtMap = this.scene.addVectorTilesMap({
          url: mvt_url,
          subdomains:subdomains,
          canvasWidth: 512,
          name: "testMVT",
          viewer: this.viewer
        });
      }else{
        this.mvtMap = this.scene.addVectorTilesMap({
          url: mvt_url,
          canvasWidth: 512,
          name: "testMVT",
          viewer: this.viewer
        });
      }

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
   * 添加地形
   */
  addTerrainLayer() {
    return new Promise((resolve, reject) => {
      if (this.cfg.server.terrainLayer) {
        // const terrain_url = `http://${this.cfg.server.host}/iserver/services/${this.cfg.server.terrainLayer}`;
        const terrain_url = `${this.cfg.server.terrainLayer}`;
        const subdomains = this.subdomainConfig;
        if(subdomains){
          this.viewer.terrainProvider = new Cesium.CesiumTerrainProvider({
            // 默认立体地表
            url:"http://{s}/iserver/services/"+ terrain_url.split("/")[5] +"/rest/realspace/datas/"+terrain_url.split("/")[9],
            //url: terrain_url,
            // 多子域配置
            subdomains:subdomains,
            //地形服务源自SuperMap iServer发布时需设置isSct为true
            isSct: true,
            //请求批量
            packingRequest: 1
          });
        }else{
          this.viewer.terrainProvider = new Cesium.CesiumTerrainProvider({
            // 默认立体地表
            url: terrain_url,
            //地形服务源自SuperMap iServer发布时需设置isSct为true
            isSct: true,
            //请求批量
            packingRequest: 1
          });
        }

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
   * 添加影像提供者
   */
  addImgProvider() {
    if (this.cfg.server.imageryLayer) {
      // const imageryLayer_url = `http://${this.cfg.server.host}/iserver/services/${this.cfg.server.imageryLayer}`;
      const imageryLayer_url = `${this.cfg.server.imageryLayer}`;
      const subdomains = this.subdomainConfig;
      let layer;
      if(subdomains){
         layer = this.viewer.imageryLayers.addImageryProvider(
          new Cesium.SuperMapImageryProvider({
            url:"http://{s}/iserver/services/"+ this.cfg.server.imageryLayer.split("/")[5] +"/rest/realspace/datas/"+this.cfg.server.imageryLayer.split("/")[9],
            subdomains:subdomains
            // 请求批量
          })
        );
      }else{
        layer = this.viewer.imageryLayers.addImageryProvider(
          new Cesium.SuperMapImageryProvider({
            url: imageryLayer_url
            // packingRequest: 1
          })
        );
      }
      // 图层加lod
      layer.lodRangeScale = 0.1;
    } else {
      console.log(">>> 未检测到配置文件中有影像图层地址,直接跳过 <<<");
    }
  },

  /**
   * 获取点击位置笛卡尔坐标
   */
  getScenePosition(){
    let _this = this;
    this.viewer.camera.changed.addEventListener(function(percentage){
      _this.cameraPosition = _this.viewer.camera;
      //console.log('viewer.camera',_this.viewer.camera);
    })
    // 鼠标左击单击事件
    this.handler.setInputAction(function (e) {
      // 获取点击位置笛卡尔坐标
      _this.position = _this.scene.pickPosition(e.position);
      let cartographic = Cesium.Cartographic.fromCartesian(_this.position);
      let longitude = Cesium.Math.toDegrees(cartographic.longitude);
      let latitude = Cesium.Math.toDegrees(cartographic.latitude);
      //console.log('获取到的this.position：'+_this.position,'lat：'+latitude,'lon：',longitude,'height：'+cartographic.height);
      //console.log('{"lat":'+ latitude +',"lon":'+ longitude +',"alt":'+ cartographic.height +'}');

      //  照片库弹窗（单张照片）
      if(_this.viewer.selectedEntity && !_this.viewer.selectedEntity.collection && _this.getPositionType === "image"){
        _this.openSelectedEntity(_this.viewer.selectedEntity);
        return;
      }

      //  照片库弹窗（多张照片）
      if(_this.viewer.selectedEntity && !_this.viewer.selectedEntity.collection && _this.getPositionType === "images"){
        _this.openImagesDialoagEntity(_this.viewer.selectedEntity);
        return;
      }

      //栅格数据查询属性
      if(_this.getPositionType === "gridType"){
        // 栅格属性查询-根据点查询面
        _this.getGridLayerEntity(_this.toGeographical(_this.position),JSON.parse(window.localStorage.getItem("dataRestObj")));
        return;
      }

      /******场景点击： ******/
      // 全景数据，街景数据气泡弹出
      if(_this.viewer.selectedEntity && _this.viewer.selectedEntity.dataValue){
        let dataValue = _this.viewer.selectedEntity.dataValue;
        let marker;
        let html;
        // 在全景打开视频图标
        if(dataValue.videoSrc !== null){
          marker = _this.markersVideo;
        }

        // 重点区域点击弹出：全景数据
        if(dataValue.dataType === 3 && _this.getAreaPanorama.indexOf("areaPanoramaOpen")!==-1){
          marker = _this.markersPanorama3D;
          html = dataValue.serverUrl;
          MapUtils.openMarksPanorama3D(
            {
              position: JSON.parse(dataValue.position),
              //html: `<iframe src="${html}"></iframe>`,
              class:'panorama',
              html:`<div style="height:100%"><iframe src="${html}"></iframe><div id="fullScreen" class="fullScreen"><img src="`+_this.maskersFullScreen+`" alt=""></div></div>`,
              marker: marker,
              props: dataValue,
              checked:true,
            },
            'noFlyTo'
          )
          // 打开全景、街景
        }else if(dataValue.dataType !== null && (dataValue.dataType === 3 || dataValue.dataType === 5)){
          // 全景数据
          if(dataValue.dataType === 3){
            marker = _this.markersPanorama3D;
            html = dataValue.serverUrl;
            MapUtils.openMarksPanorama3DNoRemove(
              {
                position: JSON.parse(dataValue.position),
                class:'panorama',
                html:`<div style="height:100%"><iframe src="${html}"></iframe><div id="fullScreen" class="fullScreen"><img src="`+_this.maskersFullScreen+`" alt=""></div></div>`,
                marker: marker,
                props: dataValue,
                checked:true
              },
              _this.viewer.selectedEntity
            )
          }
          //街景数据
          if(dataValue.dataType === 5){
            marker = _this.markersStreet;
            html = `${_this.streetHost + dataValue.serverUrl}`
            MapUtils.openMarksPanorama3DNoRemove(
              {
                position: JSON.parse(dataValue.position),
                html: `<iframe src="${html}"></iframe>`,
                marker: marker,
                props: dataValue,
                checked:true
              },
              _this.viewer.selectedEntity
            )
          }
          //定位三维模型
        }else if(dataValue.dataType !== null && dataValue.dataType === 1){
          // 清除历史数据
          _this.removeScpLayersAll();
          // 三维模型定位
          let dataId = `${dataValue.nodeName}-${dataValue.resourceId}`;
          let position = JSON.parse(dataValue.position);
          _this.addS3MTilesLayerByScp(dataValue,position,dataId, dataValue.serverUrl, true);

          // 重点区域点击弹出：视频数据
        }else if(_this.getAreaVideo.indexOf("areaVideo")!==-1){// 重点区域+视频展示
          // 视频数据
          MapUtils.openMarksPanorama3D({
            position: JSON.parse(dataValue.position),
            html: `<iframe src="${dataValue.videoSrc}"></iframe>`,
            marker: marker,
            props: dataValue,
            checked:true
          }, 'noFlyTo');

        }else{
          // 视频数据
          MapUtils.openMarksPanorama3D({
            position: JSON.parse(dataValue.position),
            html: `<iframe src="${dataValue.videoSrc}"></iframe>`,
            marker: marker,
            props: dataValue,
            checked:true
          });
        }
        return;
      }
      return _this.position;
    }, Cesium.ScreenSpaceEventType.LEFT_CLICK);
  },
  /**
   * 将笛卡尔坐标转为地理坐标
   */
  toGeographical(position){
    let cartographic = Cesium.Cartographic.fromCartesian(position);
    let longitude = Cesium.Math.toDegrees(cartographic.longitude);
    let latitude = Cesium.Math.toDegrees(cartographic.latitude);
    let queryPoint = { // 查询点对象
      x: longitude,
      y: latitude
    };
    return queryPoint;
  },

  /**
   * 栅格瓦片弹出属性信息
   */
  getGridLayerEntity(queryPoint,dataRestObj){
    let _this = this;
    // 先关闭之前气泡
    this.popup.closeAllPopup();
    // 关闭之前所有高亮
    this.viewer.dataSources.removeAll();
    // 清除之前实体
    this.viewer.entities.removeAll();
    let dataSource = dataRestObj.dataSource;
    let dataSet = dataRestObj.dataSet;
    let dataServerUrl = dataRestObj.dataServerUrl;
    // 空间查询栅格数据的属性信息
    let queryObj = {
      getFeatureMode: "SPATIAL",
      spatialQueryMode: "INTERSECT",
      datasetNames: [`${dataSource}:${dataSet}`],
      //datasetNames: [`xzsj_gtdcdb:DLTB_2018`],
      geometry: {
       /* id: 0,
        parts: [1],*/
        points: [queryPoint],
        type: "POINT"
        //type: "polygon"
      }
    };
    let queryObjJSON = JSON.stringify(queryObj); // 转化为JSON字符串作为查询参数

    $.ajax({
      type: "post",
      //url: "http://10.63.1.32:8090/iserver/services/data-XZQ-3/rest/data/featureResults.rjson?returnContent=true",
      url: `${dataServerUrl}.rjson?returnContent=true`,
      data: queryObjJSON,
      success: function (result) {
        let resultObj = JSON.parse(result);
        if (resultObj.featureCount > 0) {
          let geometry = resultObj.features[0].geometry;
          // 显示属性弹出框
          _this.popup.show(
            {
              cartesian3: _this.position,
              width:334,
              height:280,
              class: "gridPopup",
              html: `<div class="contentBody">
                      <div class="header"><b></b><span id="header_title">【${resultObj.features[0].fieldValues[10]}】&nbsp;信息</span></div>
                      <div class="content_body" id="entitiesTable"></div>
                    </div>`,
              offsets: [170, 290],
            }
          );
          let entities = ``;
          let properties = resultObj.features[0].fieldNames;
          for(let i = 5; i< properties.length; i++){
            entities += `<p><span>` + properties[i] + `</span><span>` + resultObj.features[0].fieldValues[i] + `</span></p>`
          }
          $(document.getElementById("entitiesTable")).html(entities);
          // 高亮显示  clampToGround
           let geojson = L.Util.toGeoJSON(geometry);
          // _this.geojson = Cesium.GeoJsonDataSource.load(geojson,{clampToGround:true});
          // _this.viewer.dataSources.add(_this.geojson);
          let geo = geojson;
          let g = geojson.geometry.coordinates[0][0];
          geo.geometry.coordinates[0]=[];
          geo.geometry.coordinates[0].push(g);
          let color = new Cesium.Color(14/255, 101/255, 102/255, 0.4);
          _this.viewer.dataSources.add(Cesium.GeoJsonDataSource.load(geo,{clampToGround:true,
            stroke: Cesium.Color.RED,
            // fill: Cesium.Color.LIGHTSKYBLUE.withAlpha(0.4),
            fill: color,
            strokeWidth: 5,
            markerSymbol: '?'
          }));
          return;
        }
      },
      error: function (msg) {
        console.log("未找到数据信息",msg);
      }
    });
  },
  /**
   * 点击照片图标  弹出图片属性信息
   */
  openSelectedEntity(selectedEntity){
    let _this = this;
    let entityId = selectedEntity.id;
    let entityProperties = selectedEntity.properties;
    //let entityValue = selectedEntity.properties.CameraName._value;
    //console.log('entityValue',entityValue);
    // 点聚合显示弹出框
    if(entityId && entityId !== ""){
      // 先关闭之前气泡，关闭实体
      this.popup.closeAllPopup()
      this.clearEntity();

      $(document.getElementById("tab")).remove();
      //获取图片名称，返回base64信息
      // entityProperties._JCBH && entityProperties._JCBH._value!==null
      if(entityProperties._User_FILE && entityProperties._User_FILE._value!==null) {
        let imgName = entityProperties._User_FILE._value;
        let imgHtml = ``;
        getOne(imgName).then(res => {
        imgHtml +="<p class='showPhoto margin_p'><img style=\"width:100%;display:block;margin:0 auto;\" src="+"'"+"data:image/jpg;base64,"+res[0].base64+"'"+ "><img class='openImg' src="+this.maskThume+"></p>";

        // 表格信息
        //let imgNameStr = entityProperties._PHID._value;
        //let imgNameTr =  `<p>图片名称:</td><td style="width:75%">` + imgNameStr + `</p>`;

        // let imgLon = entityProperties._User_LONG._value;
        // let imgLat = entityProperties._LAT._value;
        // let imgAlt = entityProperties._ALT._value;
        //let imgPositionsTr = `<p>图片位置: </br>经度:` + imgAlt +`, 纬度:` + imgLon + `, 高程:` + imgLat +  `</p>`;
        let timeName = entityProperties._PHTM._value;
        let imgTime = ``;
        if(timeName.indexOf("T")!==-1){
          imgTime = timeName.split("T")[0]+' '+timeName.split("T")[1];
        }else{
          imgTime = timeName.split(" ")[0];
        }

        let codeId = entityProperties._CC._value;
        getPictureAddress({codeId: codeId}).then(res=>{
          let imgTitle = ``;
          let imgType = ``;
          if (res.rows[0] && res.rows[0].name!==null){
            imgType = res.rows[0].name;
            imgTitle = imgType
          }else{
            imgTitle = '点位照片信息'
            imgType = '暂未分类';
          }
          imgHtml += `<p>图片分类：` + imgType + `</p><p>拍摄时间：` + imgTime + `</p>`;
          if(entityProperties._NAME && entityProperties._NAME._value!==null){
            let imgAddStr = entityProperties._NAME._value;
            imgHtml +=  `<p>拍摄地点：` + imgAddStr + `</p>`;
          }
          let timer = setTimeout(function(){
            _this.popup.show(
              {
                cartesian3: _this.position,
                width:_this.popup.defaultPopup().width,
                height:_this.popup.defaultPopup().height,
                class: "imagePopup",
                html: _this.popup.defaultPopup().html,
                offsets: _this.popup.defaultPopup().offsets,
                onclose:()=>{
                  // 清除实体
                  _this.clearEntity();
                }
              }
            );
            $(document.getElementById("photo_title")).text(imgTitle)
            $(document.getElementById("tab")).html(imgHtml);
            $(document.querySelector("#tab p .openImg")).click(function(){
              let maskBackground = `<div class="maskPopup" style="background:#000;width:100%;height:100%;position:absolute;top:0;left:0;z-index:10000">
                        <div class="closeMask" style="position:absolute;right:20px;top:10px;font-size:32px;color:#fff;cursor: pointer">x</div>
                        <img class="maskImg"  src="${$(document.querySelector("#tab p img")).attr('src')}"/>
                    </div>`
              $(document.getElementById("popupContainer")).append(maskBackground);
              $(document.querySelector("#popupContainer .closeMask")).click(function(){
                $(document.querySelector(".maskPopup")).remove();
              })
            });
          },10);
        })
      });
      }
    }
  },

  /**
   * 点击照片图标（多张照片）  弹出图片属性信息
   */
  openImagesDialoagEntity(selectedEntity){
    let _this = this;
    let entityId = selectedEntity.id;
    let entityProperties = selectedEntity.properties;
    // 点聚合显示弹出框
    if(entityId && entityId !== "") {
      // 先关闭之前气泡，关闭实体
      this.popup.closeAllPopup()
      this.clearEntity();
      $(document.getElementById("tab")).remove();

      /** 耕地“非农”多照片查询  展示样式修改 **/
      if(entityProperties._patchNo && entityProperties._patchNo._value!==null) {
        let fileCode = entityProperties._patchNo._value;
        get(fileCode, 'null').then(res => {
          let popupTitle0 = res[0].imageType;
          let popupTitle1 = res[1].imageType;
          let patchNo =  fileCode;
          // 查询成功
          _this.popup.show(
            {
              cartesian3: _this.position,
              width: 625,
              height: 375,
              class: "imagePopup",
              html: _this.popup.imagesNonframPopup(popupTitle0, popupTitle1,  patchNo).html,
              offsets: [315, 370],
              onclose: () => {
                // 清除实体
                _this.clearEntity();
              }
            }
          );
           $(document.getElementById("fieldImg")).append('<img src="data:image/jpg;base64,' + res[0].base64 + '"/>')
           $(document.getElementById("imagePatch")).append('<img src="data:image/jpg;base64,' + res[1].base64 + '"/>')
        })
        return;
      }
      /** 卫片执法 多照片查询 展示样式修改 **/
      if(entityProperties._JCBH && entityProperties._JCBH._value!==null) {
        let fileCode = entityProperties._JCBH._value;
        getSatellite(fileCode).then(res=> {
          let title =  res[0].fileCode;
          let patchNo =  res[0].imageType;
          // 查询成功
          _this.popup.show(
            {
              cartesian3: _this.position,
              width: 625,
              height: 336,
              class: "imagePopup",
              html: _this.popup.imagesSatellitePopup(title, patchNo).html,
              offsets: [315, 380],
              onclose: () => {
                // 清除实体
                _this.clearEntity();
              }
            }
          );
          let html = ``;
          for(let i=0; i< res.length; i++) {
            html += `<img class="satellite-img" src="data:image/jpg;base64,${res[i].base64}"/>`
          }
          $(document.getElementById("imgSatelliteList")).append(html);
        })
        return;
      }

      /** 矿业核查 多照片查询 展示样式修改 **/
      /** 矿业核查 多照片查询 展示样式修改 **/
      if(entityProperties._TBH && entityProperties._TBH._value!==null) {
        // 获取图片所属区县名称
        let imgFilesName = entityProperties._area._value;
        //获取图片文件夹名称
        let fileCode = entityProperties._TBH._value;
        get(fileCode, imgFilesName).then(res => {
          // 查询成功
          let imageTitle = res[0].area;
          _this.popup.show(
            {
              cartesian3: _this.position,
              width: 625,
              height: 368,
              class: "imagePopup",
              html: _this.popup.imagesPopup(imageTitle).html,
              offsets: [315, 370],
              onclose: () => {
                // 清除实体
                _this.clearEntity();
              }
            }
          );

          // 按钮点击功能
          $(document.getElementById("leftArrow")).append('<img src="' + this.maskImgLeftArrow + '" alt=""/>'); // 左侧按钮
          let centerBox = document.getElementById("centerWrapper").children;
          let centerBoxIndex = 0;
          let step = [0, 0, 0];
          $("#leftArrow").click(function () {
            $('#centerWrapper').children().css('border', '1px solid transparent');
            if (centerBoxIndex < 1) {
              centerBoxIndex = 2;
              centerBox[centerBoxIndex].style.border = "1px solid #1de5e7";
            } else {
              centerBoxIndex--;
              centerBox[centerBoxIndex].style.border = "1px solid #1de5e7";
            }
          })
          $(document.getElementById("rightArrow")).append('<img src="' + this.maskImgRightArrow + '" alt=""/>'); // 右侧按钮
          $("#rightArrow").click(function () {
            $('#centerWrapper').children().css('border', '1px solid transparent');
            if (centerBoxIndex < centerBox.length - 1) {
              centerBoxIndex++;
              centerBox[centerBoxIndex].style.border = "1px solid #1de5e7";
            } else {
              centerBoxIndex = 0;
              centerBox[centerBoxIndex].style.border = "1px solid #1de5e7";
            }
          })
          $(document.getElementById("goMove")).append('<img src="' + this.maskImgGoMove + '" alt=""/>'); // 前进按钮
          $('#goMove').click(function () {
            let imgItemChildren = $('#centerWrapper').children().eq(centerBoxIndex).children().eq(0).children().length;
            // 找到当前选中的centerBox
            if (imgItemChildren > 0) {
              if (step[centerBoxIndex] < imgItemChildren - 1) {
                step[centerBoxIndex]++;
                document.getElementById("centerWrapper").children[centerBoxIndex].children[0].style.transition = "all 1s";
                document.getElementById("centerWrapper").children[centerBoxIndex].children[0].style.marginLeft = -195 * step[centerBoxIndex] + 'px';
              }
            }
          })
          $(document.getElementById("backMove")).append('<img src="' + this.maskImgBackMove + '" alt=""/>'); // 后退按钮
          $('#backMove').click(function () {
            let imgItemChildren = $('#centerWrapper').children().eq(centerBoxIndex).children().eq(0).children().length;
            // 找到当前选中的centerBox
            if (imgItemChildren > 0) {
              if (step[centerBoxIndex] > 0) {
                step[centerBoxIndex]--;
                document.getElementById("centerWrapper").children[centerBoxIndex].children[0].style.transition = "all 1s";
                document.getElementById("centerWrapper").children[centerBoxIndex].children[0].style.marginLeft = -195 * step[centerBoxIndex] + 'px';
              }
            }
          })


          /* 照片分类分组js */
          let list = res;
          let data = [];
          for (let i = 0; i < list.length; i++) {
            if (!data[list[i].imageType]) {
              let arr = [];
              arr.push(list[i]);
              data[list[i].imageType] = arr;
            } else {
              data[list[i].imageType].push(list[i])
            }
          }

          // 添加分类照片div
          $(document.getElementById("centerWrapper")).append('<div class="imgTypesBox" style="border:1px solid #1de5e7"><div id="earlyStageImg"></div><p class="typeTitle">前期</p></div>');
          $(document.getElementById("centerWrapper")).append('<div class="imgTypesBox"><div id="MidStageImg"></div><p class="typeTitle">中期</p></div>');
          $(document.getElementById("centerWrapper")).append('<div class="imgTypesBox"><div id="LateStageImg"></div><p class="typeTitle">后期</p></div>');
          // 判断前期有无照片
          if (data['前'] != null) {
            for (let j = 0; j < data['前'].length; j++) {
              $(document.getElementById("earlyStageImg")).append('<img class="imgItem" src="data:image/jpg;base64,' + data['前'][j].base64 + '"/>')
            }
          } else {
            $(document.getElementById("earlyStageImg")).append('暂无照片')
          }
          // 判断中期有无照片
          if (data['中'] != null) {
            for (let k = 0; k < data['中'].length; k++) {
              $(document.getElementById("MidStageImg")).append('<img class="imgItem" src="data:image/jpg;base64,' + data['中'][k].base64 + '"/>')
            }
          } else {
            $(document.getElementById("MidStageImg")).append('暂无照片')
          }
          // 判断后期有无照片
          if (data['后'] != null) {
            for (let l = 0; l < data['后'].length; l++) {
              $(document.getElementById("LateStageImg")).append('<img class="imgItem" src="data:image/jpg;base64,' + data['后'][l].base64 + '"/>')
            }
          } else {
            $(document.getElementById("LateStageImg")).append('暂无照片')
          }
        });
      }
    }
  },

  /**
   * 重点区域弹出展示详情的弹出框
   */
  showDialogPopup(data,cfg){
    let position = JSON.parse(data.position)
    let pos = {
      lon: parseFloat(position.lon),
      lat: parseFloat(position.lat),
      alt: 1000
    }
    let cartesian3 = Cesium.Cartesian3.fromDegrees(pos.lon,pos.lat,pos.alt);
    this.popup.show({
      cartesian3:cartesian3,
      width:cfg.width,
      height:cfg.height,
      class: "dialogPopup",
      offsets:cfg.offsets,
      html:cfg.html
    });
    $(document.getElementById("header_title")).text(data.imgName);
    $(document.getElementById("content")).html(data.description);
  },

  /**
   * 切换光照和阴影效果
   * @param flag
   */
  switchSunAndShadow(flag) {
    this.viewer.shadows = flag;
    this.scene.globe.enableLighting = flag;
  },
  /**
   * 初始化成功后的回调
   */
  initSceneSuccess() {
    //一些绑定方法
    let _this = this;
    console.log('鼠标滚动监听事件');
    console.log(">>> setInputAction");
    let handler = new Cesium.ScreenSpaceEventHandler(this.viewer.canvas);
    this.handler.setInputAction( ( scrollValue ) => {

    }, Cesium.ScreenSpaceEventType.WHEEL );
    // handler.setInputAction((e) => {
    //   let layer = this.scene.layers.find('拉西瓦-28');
    //   console.log('layer',layer)
    // }, Cesium.ScreenSpaceEventType.LEFT_CLICK)
    // handler['setInputAction']((movement)=> {
    //   //捕获椭球体，将笛卡尔二维平面坐标转为椭球体的笛卡尔三维坐标，返回球体表面的点
    //   let cartesian = this.viewer.camera.pickEllipsoid(movement.endPosition, this.viewer.scene.globe.ellipsoid)
    //   if (cartesian) {
    //     //将笛卡尔三维坐标转为地图坐标（弧度）
    //     let cartographic = this.viewer.scene.globe.ellipsoid.cartesianToCartographic(cartesian)
    //     //将地图坐标（弧度）转为十进制的度数
    //     let lat_String = Cesium.Math.toDegrees(cartographic.latitude).toFixed(4)
    //     let log_String = Cesium.Math.toDegrees(cartographic.longitude).toFixed(4)
    //     let alti_String = parseInt(this.viewer.scene.getHeight(parseFloat(log_String),parseFloat(lat_String)))
    //     if(alti_String){
    //       sessionStorage.setItem("posMsg",`{"lat":${lat_String},"lng":${log_String},"alt":${alti_String<0?0:alti_String}}`)
    //     }
    //   }
    // }, Cesium.ScreenSpaceEventType.MOUSE_MOVE)
    // selectedChanged
    // console.log('>>>>>> selectedChanged!')
    // this.viewer['selectedEntityChanged'].addEventListener((entity)=>{
    //
    // })
  },
  cameraHeight() {
    if (Cesium.viewer) {
      return Math.ceil(Cesium.viewer.camera.positionCartographic.height)
    }
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
   * 规避lod内存爆满问题
   */
  CacheSetMeMory(){
    Cesium.MemoryManager.setMaxMemory(800);
    Cesium.MemoryManager.setMinQuality(0.01);
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
   * 跳转给定视角
   */
  flyToView(pos, orientation, duration) {
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
   * 矢量数据的属性查询
   */
  openEventListenerEntity(cfg){
    this.popup.closeAllPopup();
    this.clearEntity();
    // 点击数据
    const _this = this;
    this.viewer.selectedEntityChanged.addEventListener(function (entity,val) {
      //console.log('_this.viewer.selectedEntity',_this.viewer.selectedEntity,entity);
      // 先关闭之前气泡，关闭实体
      _this.popup.closeAllPopup()
      _this.clearEntity();
      $(document.getElementById("tab")).remove();
      // 表格信息
      let layerIDs = ``;
      let layerEntitys = ``;
      let entities = ``;
      let timer = setTimeout(function(){
        // 获取属性信息
      let selectedEntity = _this.viewer.selectedEntity;
        //console.log('弹出框',_this.viewer.selectedEntity);
      let layerID = selectedEntity.layerID;
      let id = selectedEntity.id;
      let pickResult = selectedEntity.pickResult;
      let properties = pickResult[layerID][0].feature.properties;
      //console.log('properties',selectedEntity);
      for(let key in properties){
        entities += `<tr><td>` + key + `</td><td>` + properties[key] + `</td></tr>`
      }
      layerIDs = `<tr><td>图层</td><td>` + layerID +`</td></tr>
        <tr><td>ID</td><td>` + id +`</td></tr>`;

        _this.popup.show(
          {
            cartesian3: _this.position,
            width:cfg.width,
            height:cfg.height,
            html:cfg.html,
            offsets: cfg.offsets,
            onclose:()=>{
              // 清除实体
              _this.clearEntity();
            }
          }
        );

      // 添加图标
     /* _this.viewer.entities.add({
        position:_this.position,
        billboard: {
          image: cfg.marker,
          pixelOffset: new Cesium.Cartesian2(10, 20), //偏移量
          // heightReference:Cesium.HeightReference.CLAMP_TO_GROUND,
          clampToGround: true,
          disableDepthTestDistance: Number.POSITIVE_INFINITY
        }
      });*/

      $(document.getElementById("tab")).append(layerIDs);
      $(document.getElementById("tab")).append(layerEntitys);
      $(document.getElementById("tab")).append(entities);
      },200);
    });
    //this.viewer.selectedEntityChanged.addEventListener(_this.selectedEntityListener(cfg));
  },

  /**
   * 实体触发监听事件
   */
  selectedEntityListener(cfg){
    let _this = this;
    // 先关闭之前气泡，关闭实体
    _this.popup.closeAllPopup()
    _this.clearEntity();
    $(document.getElementById("tab")).remove();
    // 表格信息
    let layerIDs = ``;
    let layerEntitys = ``;
    let entities = ``;
    let timer = setTimeout(function(){
      // 获取属性信息
      let selectedEntity = _this.viewer.selectedEntity;
      let layerID = selectedEntity.layerID;
      let id = selectedEntity.id;
      let pickResult = selectedEntity.pickResult;
      let properties = pickResult[layerID][0].feature.properties;

      for(let key in properties){
        entities += `<tr><td>` + key + `</td><td>` + properties[key] + `</td></tr>`
      }
      layerIDs = `<tr><td>图层</td><td>` + layerID +`</td></tr>
        <tr><td>ID</td><td>` + id +`</td></tr>`;

      _this.popup.show(
        {
          cartesian3: _this.position,
          width:cfg.width,
          height:cfg.height,
          html:cfg.html,
          offsets: cfg.offsets,
          onclose:()=>{
            // 清除实体
            _this.clearEntity();
          }
        }
      );

      // 添加图标
      _this.viewer.entities.add({
        position:_this.position,
        billboard: {
          image: cfg.marker,
          pixelOffset: new Cesium.Cartesian2(10, 20), //偏移量
          // heightReference:Cesium.HeightReference.CLAMP_TO_GROUND,
          clampToGround: true,
          disableDepthTestDistance: Number.POSITIVE_INFINITY
        }
      });

      $(document.getElementById("tab")).append(layerIDs);
      $(document.getElementById("tab")).append(layerEntitys);
      $(document.getElementById("tab")).append(entities);
    },200);
  },

  /**
   * 全幅青海
   */
  entireView() {
    //console.log("全幅青海",this.cfg);  {x: -673860.1662008123, y: 5291161.596025125, z: 3494865.6510132113}
   /* this.camera.flyTo({
       destination: {
         "x": -636836.3548791159,
         "y": 7048369.6004195735,  // 值小  图大
         "z": 3684539.5559322587   // 值小  往前
       },
      orientation: {
        "roll": 6.253185307179586,  // 值大  逆时针
        "pitch": -1.183904024758136,  // 值小  往上
        "heading": 0.0764434439594541 // 值小  往右
      }
    });*/
    this.camera.flyTo({
       destination: this.cfg["mapInit"]["entirePos"]["position"],
       orientation: this.cfg["mapInit"]["entirePos"]["orientation"]
    });
  },
  /**
   * 地球自转特效
   */
  rotate(flag) {
    // console.log('flag', flag)
    if (flag) {
      this.camera.rotate(Cesium.Cartesian3.UNIT_Z, 0.0005);
      this.rotateAnimate = requestAnimationFrame(() => {
        this.rotate();
      });
    } else {
      cancelAnimationFrame(this.rotateAnimate);
    }
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
   * 添加点云数据缓存
   */
  addCloudLayerByScp(dataName, position, url, flag) {
    if (!flag) {
      this.removeS3MTilesLayerByScp(dataName);
      return;
    }
    //清除历史数据
    this.removeScpLayersAll();
    const scp_url = `${url}`;
    const subdomain = this.subdomainConfig;
    /*let pos = [
      parseFloat(position.lon),
      parseFloat(position.lat) - 0.01,
      //4000
      parseFloat(position.alt) + 300
    ]*/
    if(subdomain){
      this.scene.addS3MTilesLayerByScp(scp_url, {name: dataName},{subdomains:subdomain}).then(res => {
        const layer = this.scene.layers.find(dataName);
        // 设置点云高程
        // layer.style3D.bottomAltitude = 60;
        layer.style3D.bottomAltitude = 45;
        layer.refresh();
        // 设置大小
        layer.style3D.pointSize = 3;
        // 设置点云颜色
        this.setHypsometric(layer);
        // 前端缓存优化
        // this.CacheBrowser(layer);
        // layer.selectEnabled = true;
        // layer.selectedColor = new Cesium.Color(1, 1, 1, 1);
        // //图层加lod
        // layer.lodRangeScale = 0.1;
        // 点云去掉黑边
        layer.pointCloudShading.eyeDomeLighting=false;
        let pos = [
          layer.lon,
          layer.lat,
          Math.round(layer._hypMaxHeight ? layer._hypMaxHeight + 2000 : 5000)
        ];
        this.flyToView(pos,this.cfg["mapInit"]["s3MTilesPos"]["orientation"],8);
        this.scpS3MTilesLayer.set(dataName, layer);
      }).otherwise(err => {
        console.log("加载倾斜摄影失败:err=>", err);
      });
      return;
    }

    this.scene.addS3MTilesLayerByScp(scp_url, {name: dataName}).then(res => {
      const layer = this.scene.layers.find(dataName);
      // 设置点云高程
      layer.style3D.bottomAltitude = 45;
      layer.refresh();
      // 设置大小
      layer.style3D.pointSize = 3;
      // 设置点云颜色
      this.setHypsometric(layer);
      // 前端缓存优化
      // this.CacheBrowser(layer);
      // layer.selectEnabled = true;
      // layer.selectedColor = new Cesium.Color(1, 1, 1, 1);
      // //图层加lod
      layer.lodRangeScale = 3;
      // 点云去掉黑边
      layer.pointCloudShading.eyeDomeLighting=false;
      let pos = [
        layer.lon,
        layer.lat - 0.0023,
        //Math.round(layer._hypMaxHeight ? layer._hypMaxHeight + 2000 : 5000) - 900
        2980
      ];
      this.flyToView(pos,this.cfg["mapInit"]["s3MTilesPos"]["orientation"],8);
      this.scpS3MTilesLayer.set(dataName, layer);
    }).otherwise(err => {
      console.log("加载倾斜摄影失败:err=>", err);
    });
  },

  /**
   * 给点云设置颜色
   */
  setHypsometric(layer) {
    let hyp = new Cesium.HypsometricSetting();
    let minValue = layer.dataMinValue;
    let maxValue = layer.dataMaxValue;

    hyp.DisplayMode = Cesium.HypsometricSettingEnum.DisplayMode.FACE;
    hyp.LineColor = new Cesium.Color(1.0, 0.0, 0.0, 1.0);
    hyp.LineInterval = 100.0;
    hyp.ColorTableMinKey = minValue;
    hyp.ColorTableMaxKey = maxValue;

    var colorTable = new Cesium.ColorTable();
    colorTable.insert(minValue + 0 * (maxValue - minValue) / 15, new Cesium.Color(0 / 255, 0 / 255, 0 / 255));
    colorTable.insert(minValue + 1 * (maxValue - minValue) / 15, new Cesium.Color(16 / 255, 16 / 255, 16 / 255));
    colorTable.insert(minValue + 2 * (maxValue - minValue) / 15, new Cesium.Color(33 / 255, 33 / 255, 33 / 255));
    colorTable.insert(minValue + 3 * (maxValue - minValue) / 15, new Cesium.Color(25 / 255, 25 / 255, 25 / 255));
    colorTable.insert(minValue + 4 * (maxValue - minValue) / 15, new Cesium.Color(33 / 255, 33 / 255, 33 / 255));
    colorTable.insert(minValue + 5 * (maxValue - minValue) / 15, new Cesium.Color(49 / 255, 49 / 255, 49 / 255));
    colorTable.insert(minValue + 6 * (maxValue - minValue) / 15, new Cesium.Color(66 / 255, 66 / 255, 66 / 255));
    colorTable.insert(minValue + 7 * (maxValue - minValue) / 15, new Cesium.Color(82 / 255, 82 / 255, 82 / 255));
    colorTable.insert(minValue + 8 * (maxValue - minValue) / 15, new Cesium.Color(99 / 255, 99 / 255, 99 / 255));
    colorTable.insert(minValue + 9 * (maxValue - minValue) / 15, new Cesium.Color(115 / 255, 115 / 255, 115 / 255));
    colorTable.insert(minValue + 10 * (maxValue - minValue) / 15, new Cesium.Color(132 / 255, 132 / 255, 132 / 255));
    colorTable.insert(minValue + 11 * (maxValue - minValue) / 15, new Cesium.Color(148 / 255, 148 / 255, 148 / 255));
    colorTable.insert(minValue + 12 * (maxValue - minValue) / 15, new Cesium.Color(165 / 255, 165 / 255, 165 / 255));
    colorTable.insert(minValue + 13 * (maxValue - minValue) / 15, new Cesium.Color(181 / 255, 181 / 255, 181 / 255));
    colorTable.insert(minValue + 14 * (maxValue - minValue) / 15, new Cesium.Color(197 / 255, 197 / 255, 197 / 255));
    colorTable.insert(minValue + 15 * (maxValue - minValue) / 15, new Cesium.Color(214 / 255, 214 / 255, 214 / 255));

    hyp.ColorTable = colorTable;
    layer.hypsometricSetting = {
      hypsometricSetting: hyp,
      analysisMode: Cesium.HypsometricSettingEnum.AnalysisRegionMode.ARM_ALL
    }
  },

  /**
   * 添加S3M 倾斜缓存
   * @returns {*}
   */
  addS3MTilesLayerByScp(data,position,dataName, url, flag) {
    //"http://10.63.1.32:8090/iserver/services/3D-mongodb/rest/realspace/datas/西宁市中心城区/config";
    if (!flag) {
      this.removeS3MTilesLayerByScp(dataName);
      this.popup.closeAllPopup();
      return;
    }
    let pos = [
      parseFloat(position.lon),
      parseFloat(position.lat) - 0.01,
      //4000
      parseFloat(position.alt) + 300
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
        layer.style3D.bottomAltitude = -(parseFloat(data.height));
        // 前端缓存优化
        //this.CacheBrowser(layer);
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
    this.scene.addS3MTilesLayerByScp(scp_url, {name: dataName}).then(res => {
      const layer = this.scene.layers.find(dataName);
      //图层加lod
      //layer.lodRangeScale = 0.5;
      // 设置倾斜摄影高程
      layer.style3D.bottomAltitude = -(parseFloat(data.height));
      // 设置偏移量
      //layer.setPolygonoffset(-50,50);
      if(scp_url.indexOf("L")!==-1){
        // 空间索引
        layer.LoadingPriority = Cesium.LoadingPriorityMode.UsePagedLodInfo;
      }
      // 非线性索引
      //layer.LoadingPriority = Cesium.LoadingPriorityMode.Child_Priority_NonLinear;
      /* let pos = [
        layer.lon,
        layer.lat,
        Math.round(layer._hypMaxHeight ? layer._hypMaxHeight + 2000 : 5000)
      ];*/
        this.flyToView(pos,this.cfg["mapInit"]["s3MTilesPos"]["orientation"],8);
        this.scpS3MTilesLayer.set(dataName, layer);
      }).otherwise(err => {
          console.log("加载倾斜摄影失败:err=>", err);
        });

  },

  /**
   * 添加S3M 倾斜缓存---+飞行定位
   * @returns {*}
   */
  addS3MTilesLayerByScpAddFly(data,position,dataName, url, checked ,flyPath){
    let _this = this;
    const scp_url = `${url}`;
    let pos = [
      parseFloat(position.lon),
      parseFloat(position.lat) - 0.01,
      //4000
      parseFloat(position.alt) + 300
    ]
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
        layer.style3D.bottomAltitude = -(parseFloat(data.height));
        this.flyToView(pos,this.cfg["mapInit"]["s3MTilesPos"]["orientation"],7);
        setTimeout(function(){
          _this.initFlyManager(flyPath);
        },9000)
        this.scpS3MTilesLayer.set(dataName, layer);
      })
        .otherwise(err => {
          console.log("加载倾斜摄影失败:err=>", err);
        });
    }else{
      this.scene.addS3MTilesLayerByScp(scp_url, {name: dataName}).then(res => {
        const layer = this.scene.layers.find(dataName);
        // 设置倾斜摄影高程
        layer.style3D.bottomAltitude = -(parseFloat(data.height));
       /* let pos = [
          layer.lon,
          layer.lat,
          Math.round(layer._hypMaxHeight ? layer._hypMaxHeight + 2000 : 5000)
        ];*/
        this.flyToView(pos,null,9);
        setTimeout(function(){
          _this.initFlyManager(flyPath);
        },9000)
        this.scpS3MTilesLayer.set(dataName, layer);
      })
        .otherwise(err => {
          console.log("加载倾斜摄影失败:err=>", err);
        });
    }

  },

  /**
   * 添加MVT
   * @param name
   * @param url
   * @param flag
   */
  addMvtByUrl(name, url, flag, popup,type) {
    if (!flag) {
      // 先关闭之前气泡，关闭实体
      this.popup.closeAllPopup()
      this.clearEntity();
      this.removeMVTLayerByName(name);
      return;
    }
    this.getPositionType = type;
    const mvt_url = `${url}`;
    //const mvt_url = "http://222.91.72.102:2686/iserver/services/3D-3Dmvt-MapClipChina4326/rest/realspace/datas/MapClip@China_4326";  //三维瓦片测试数据
    let mvtMap;
    if(this.subdomainConfig){
      mvtMap = this.scene.addVectorTilesMap({
        //url: mvt_url,
        url:"http://{s}/services/"+ mvt_url.split("/")[5] +"/rest/realspace/datas/"+mvt_url.split("/")[9],
        subdomains:this.subdomainConfig,
        canvasWidth: 512,
        name: name,
        packingRequest: 1,
        viewer: this.viewer
      });
    }else{
      mvtMap = this.scene.addVectorTilesMap({
        url: mvt_url,
        canvasWidth: 512,
        name: name,
        packingRequest: 1,
        viewer: this.viewer
      });
    }
    Cesium.when(mvtMap.readyPromise)
      .then(data => {
        console.log(">>> 加载矢量瓦片成功！:");
        this.mvtLayersMap.set(name, mvtMap);
        //属性查询
        this.openEventListenerEntity(popup);
      })
      .otherwise(e => {
        console.log(">>> 加载矢量瓦片失败！", e);
      });
  },
  /**
   * 添加影像地图
   * @param name
   * @param url
   * @param flag
   */
  addImageLayerByUrl(name, url, flag) {
    // debugger
    if (!flag) {
      this.removeImageLayersByName(name);
      return;
    }
    // const imageryLayer_url = `http://${this.cfg.server.host}/iserver/services/${url}`;
    const imageryLayer_url = `${url}`;
    let layer;
    /*if(this.subdomainConfig && url!=null){
      layer = new Cesium.ImageryLayer(
        new Cesium.SuperMapImageryProvider({
          //url: imageryLayer_url,
          url:" http://{s}/iserver/services/"+ url.split("/")[5] +"/rest/maps/"+url.split("/")[8],
          subdomains:this.subdomainConfig,
          // name:name,
        })
      );
    }else{
      layer = new Cesium.ImageryLayer(
        new Cesium.SuperMapImageryProvider({
          url: imageryLayer_url,
          // name:name,
        })
      );
    }*/
    layer = new Cesium.ImageryLayer(
      new Cesium.SuperMapImageryProvider({
        url: imageryLayer_url,
        // name:name,
      })
    );

    // 图层加lod
    layer.lodRangeScale = 0.1;
    // 前端缓存优化
    this.CacheBrowser(layer);
    this.imageLayersMap.set(name, layer);
    this.viewer.imageryLayers.add(layer);
  },

  /**
   * 添加栅格图层
   *
   */
  addImageGridLayerByUrl(name, url, flag, data, type, popup) {
    if (!flag) {
      this.removeImageLayersByName(name);
      this.getPositionType = "";
      MapUtil.SceneUtil.clearEntity();
      this.clearHighLightAll();
      this.popup.closeAllPopup();
      return;
    }
    //清除选中的所有影像图层
    this.removeImageLayersAll();
    // 清除初始化行政区划
    this.removeInitImageLayers();
    // 设置点击类型
    this.getPositionType = type;
    let getDataRestObj = {
      // 设置查询数据源
      dataSource:data.dataSource,
      // 设置查询数据集
      dataSet:data.dataSet,
      // 设置查询数据服务
      dataServerUrl:data.dataServerUrl
    }
    window.localStorage.setItem("dataRestObj",JSON.stringify(getDataRestObj));
    const imageryLayer_url = `${url}`;
    let layer;
    /*if(this.subdomainConfig){
      layer = new Cesium.ImageryLayer(
        new Cesium.SuperMapImageryProvider({
          //url: imageryLayer_url,
          url:"http://{s}/iserver/services/"+ url.split("/")[5] +"/rest/maps/"+url.split("/")[8],
          subdomains:this.subdomainConfig,
          // name:name,
        })
      );
    }else{
      layer = new Cesium.ImageryLayer(
        new Cesium.SuperMapImageryProvider({
          url: imageryLayer_url,
          // name:name,
        })
      );
    }*/
    layer = new Cesium.ImageryLayer(
      new Cesium.SuperMapImageryProvider({
        url: imageryLayer_url,
        // name:name,
      })
    );

    // 前端缓存优化
    this.CacheBrowser(layer);
    layer.lodRangeScale = 0.1;
    this.imageLayersMap.set(name, layer);
    this.viewer.imageryLayers.add(layer);

    //属性查询
    this.openEventListenerEntity(popup);
  },

  /**
   * 添加三维模型倾斜摄影
   * @param data
   */
  addAllS3MByScp(data) {
    listInfo().then(res => {
      res.rows
        .filter(dt => {
          return dt.type === "0";
        })
        .map(dt => {
          // const scp_url = `http://${this.cfg.server.host}/iserver/services/${dt.url}`;
          const scp_url = `${dt.url}`;

          this.scene
            .addS3MTilesLayerByScp(scp_url, {
              name: dt.name + "-" + dt.id
            })
            .then(layer => {
              this.S3MLayersNames.push(layer.name);
              // console.log(layer.name)
              layer.visible = false;
            });
        });
    });
  },

  flyToS3MTilesLayer(name) {
    let layer = this.scene.layers.find(name);
    if (layer) {
      let pos = [
        layer.lon,
        layer.lat,
        Math.round(layer._hypMaxHeight ? layer._hypMaxHeight + 2000 : 5000)
      ];
      this.flyToView(pos);
    }
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
   * 根据名称移除栅格图层
   */
  removeImageLayersByName(name) {
    if (this.imageLayersMap.has(name)) {
      let rst = this.viewer.imageryLayers.remove(
        this.imageLayersMap.get(name),
        true
      );
      console.log("移除图层:", rst ? "success" : "fuck");
      this.imageLayersMap.delete(name);
    }
  },
  /**
   * 初始化飞行管理器
   * @param filePath
   */
  initFlyManager(filePath) {
    console.log('filePath',filePath);
    if (this.flyManager) {
      this.flyManager.stop();
    }
    let routes = new Cesium.RouteCollection();
    //添加fpf飞行文件，fpf由SuperMap iDesktop生成
    try {
      routes.fromFile(filePath);
    } catch (e) {
      console.error(e);
    }
    //初始化飞行管理
    this.flyManager = new Cesium.FlyManager({
      scene: this.scene,
      routes: routes
    });

    this.flyManager.readyPromise.then(res => {
      console.log('res',res);
      this.flyManager.play();
    });
  },
  /**
   * 兴趣点添加markers点位
   */
  addInterestMarkersToMap(data, icon, flag){
    this.viewer.entities.removeAll();

    let pos = data.position;
    this.viewer.entities.add({
      position: Cesium.Cartesian3.fromDegrees(
        parseFloat(pos.lon),
        parseFloat(pos.lat),
        // parseFloat(pos.alt)
        //parseFloat("0")
      ),
      billboard: {
        image: icon,
        width: 36,
        height: 36,
        pixelOffset: new Cesium.Cartesian2(14, -10), //偏移量
        //heightReference:Cesium.HeightReference.CLAMP_TO_GROUND,
        clampToGround: true,
        heightReference:100,
        disableDepthTestDistance: Number.POSITIVE_INFINITY,
        distanceDisplayCondition: new Cesium.DistanceDisplayCondition(0, 2700000),
      },
      label: {
        //文字标签
        text: data.text || data.name,
        scale: 0.6,
        style: Cesium.LabelStyle.FILL,
        fillColor: Cesium.Color.WHITE,
        pixelOffset: new Cesium.Cartesian2(30, -15), //偏移量
        showBackground: true,
        backgroundColor: new Cesium.Color(0, 0, 0, 0.6),
        verticalOrigin: Cesium.VerticalOrigin.CENTER, //垂直位置
        horizontalOrigin: Cesium.HorizontalOrigin.LEFT, //水平位置
        //disableDepthTestDistance: Number.POSITIVE_INFINITY,
        heightReference:100,
        // heightReference:Cesium.HeightReference.CLAMP_TO_GROUND,
        distanceDisplayCondition: new Cesium.DistanceDisplayCondition(0, 120000),
        disableDepthTestDistance: 120000,
      },
      name: data.name,
      description: data.name,
    });
    this.flyToView([pos.lon,pos.lat, 5000], undefined, 6)
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
    this.viewer.entities.removeAll();
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
          //disableDepthTestDistance: 200000,
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
          //disableDepthTestDistance: Number.POSITIVE_INFINITY,
           //heightReference:Cesium.HeightReference.CLAMP_TO_GROUND,
           distanceDisplayCondition: new Cesium.DistanceDisplayCondition(0, 120000),
           disableDepthTestDistance: 120000,
        },
        name: item.nodeName || item.videoName,
        description: item.nodeName || item.videoName,
        dataValue: item
      });

    });
  },

  /**
   * 添加重点区域的全景数据marker点位
   */
  addAreaMarkersToMap(data, icon){
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
          heightReference:Cesium.HeightReference.CLAMP_TO_GROUND,
          clampToGround: true,
          disableDepthTestDistance: Number.POSITIVE_INFINITY,
          distanceDisplayCondition: new Cesium.DistanceDisplayCondition(0, 2700000),
          //disableDepthTestDistance: 200000,
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
          //disableDepthTestDistance: Number.POSITIVE_INFINITY,
          heightReference:Cesium.HeightReference.CLAMP_TO_GROUND,
          distanceDisplayCondition: Number.POSITIVE_INFINITY,
          disableDepthTestDistance: 120000,
        },
        name: item.nodeName || item.videoName,
        description: item.nodeName || item.videoName,
        dataValue: item
      });

    });
  },
  /**
   * 根据名称查询行政区划
   */
  async queryArea(data) {
    let result = await queryServer.queryAreaInstance(data.trim());
    let features = result.originResult.features;
    let polygons = [];
    this.viewer.entities.removeAll();
    features.map((f, index) => {
      const geometry = f.geometry;
      f.props = {};
      f.fieldNames.map((name, index) => {
        f.props[name] = f.fieldValues[index];
      });
      if (geometry) {
        let pos = Cesium.Cartesian3.fromDegreesArray(
          geometry.points
            .map(item => {
              return [item.x, item.y];
            })
            .flat()
        );
        polygons.push(
          this.viewer.entities.add({
            polygon: {
              hierarchy: {
                positions: pos
              },
              clampToGround: true,
              material: Cesium.Color.fromCssColorString(
                this.cfg["draw"]["areaPolygonColor"]
              )
            },
            polyline: {
              positions: pos,
              clampToGround: true,
              width: parseFloat(this.cfg["draw"]["areaPolylineWidth"]),
              material: Cesium.Color.fromCssColorString(
                this.cfg["draw"]["areaPolylineColor"]
              )
            }
          })
        );
        this.viewer.entities.add({
          position: Cesium.Cartesian3.fromDegrees(
            f.geometry.center.x,
            f.geometry.center.y
          ),
          label: {
            //文字标签
            text: f.props["CNAME"],
            scale: 0.6,
            style: Cesium.LabelStyle.FILL,
            fillColor: Cesium.Color.WHITE,
            pixelOffset: new Cesium.Cartesian2(30, -15), //偏移量
            showBackground: true,
            backgroundColor: new Cesium.Color(0, 0, 0, 0.6),
            verticalOrigin: Cesium.VerticalOrigin.CENTER, //垂直位置
            horizontalOrigin: Cesium.HorizontalOrigin.CENTER, //水平位置
            disableDepthTestDistance: Number.POSITIVE_INFINITY,
            clampToGround: true,
            heightReference: Cesium.HeightReference.CLAMP_TO_GROUND
          }
        });
      }
    });
    this.viewer.flyTo(polygons);
  },

  /**
   * 遍历移除所有的矢量瓦片
   */
  removeMVTLayersAll(){
    let _this = this;
    this.mvtLayersMap.forEach(function(value,key){
      _this.removeMVTLayerByName(key);
      });
    },

  /**
   * 遍历清除所有的影像图层
   */
  removeImageLayersAll(){
    let _this = this;
    this.imageLayersMap.forEach(function(value,key){
      _this.removeImageLayersByName(key);
    })
  },

  /**
   * 遍历清除初始化行政区划地图
   */
  removeInitImageLayers(){
    let _this = this;
    this.initImageLayersMap.forEach(function(value,key){
      if (_this.initImageLayersMap.has(key)) {
        let rst = _this.viewer.imageryLayers.remove(
          _this.initImageLayersMap.get(key),
          true
        );
        console.log("移除图层:", rst ? "success" : "fuck");
        _this.initImageLayersMap.delete(key);
      }
    })
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
  },

  /**
   * 清除所有高亮
   */
  clearHighLightAll(){
    // 关闭之前所有高亮
    this.viewer.dataSources.removeAll();
  }
};


