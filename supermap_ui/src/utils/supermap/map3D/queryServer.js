import { Cesium, SuperMap } from "../consts";
import { resolveBlob } from "../../zipdownload";
import sceneUtil from "@/utils/supermap/map3D/index";
import $ from "jquery";
import store from "@/store/index.js"

let id,
  lastHouseEntity = null,
  // 单体化气泡内容
  buildPopup = {
    title: "地块信息",
    buildInfo: [
      {
        name: "承包方名称",
        prop: "CONTRACTOR"
      },
      {
        name: "小地名",
        prop: "PLACENAME"
      },
      {
        name: "实测面积",
        prop: "HOUSEAREA"
      },
      {
        name: "土地用途",
        prop: "LANDUSE"
      },
      {
        name: "地类",
        prop: "TYPE"
      },
      {
        name: "地理等级",
        prop: "GEOLEVEL"
      }
    ],
    curSelected: {}
  },
  // 分户气泡内容
  housePopup = {
    title: "房屋信息",
    houseInfo: [
      {
        name: "门牌号",
        prop: "HOUSENUMBER"
      },
      {
        name: "户主姓名",
        prop: "HOUSEHOLDER"
      },
      {
        name: "家庭人数",
        prop: "FAMILY"
      },
      {
        name: "房屋面积",
        prop: "HOUSINGAREA"
      }
    ],
    curSelected: {}

  },
  // 当前点击的点位置
  currectPoint = {},
  flag = ''  // 点击的是场景还是列表（场景不加飞行功能）
export default {
  queryConfig: {},
  buildPopup: buildPopup,
  housePopup: housePopup,
  queryFeaturesBySQLParameters(url, attribute, datasetNames) {
    console.log(">>>attribute", attribute);
    return new Promise((resolve, reject) => {
      let getFeatureParam, getFeatureBySQLService, getFeatureBySQLParams;
      getFeatureParam = new SuperMap.REST.FilterParameter({
        attributeFilter: attribute
      });
      getFeatureBySQLParams = new SuperMap.REST.GetFeaturesBySQLParameters({
        queryParameter: getFeatureParam,
        toIndex: -1,
        datasetNames: datasetNames
      });
      getFeatureBySQLService = new SuperMap.REST.GetFeaturesBySQLService(url, {
        eventListeners: {
          processCompleted: res => {
            resolve(res);
          },
          processFailed: e => {
            throw new Error(e);
          }
        }
      });
      getFeatureBySQLService.processAsync(getFeatureBySQLParams);
    });
  },
  queryAreaInstance(data) {
    let cfg = this.queryConfig["area"];
    let url = `http://${this.queryConfig["host"]}/iserver/services/${cfg["areaRest"]}`,
      attribute = `${cfg["attributeParam"]} LIKE "${data}"`,
      datasetNames = cfg["datasetNames"];
    return new Promise((res, rej) => {
      this.queryFeaturesBySQLParameters(url, attribute, datasetNames)
        .then(rst => {
          res(rst);
        })
        .catch(e => {
          rej(e);
        });
    });
  },
  queryAreaInstanceBuildings() {
    let cfg = this.queryConfig["buildings"];
    let url = `http://${this.queryConfig["host"]}/iserver/services/${cfg["areaRest"]}`,
      attribute = "SMID >=1",
      datasetNames = cfg["datasetNames"];
    return new Promise((res, rej) => {
      this.queryFeaturesBySQLParameters(url, attribute, datasetNames)
        .then(rst => {
          res(rst);
        })
        .catch(e => {
          rej(e);
        });
    });
  },
  queryAreaInstanceHouseholders() {
    let cfg = this.queryConfig["householders"];
    let url = `http://${this.queryConfig["host"]}/iserver/services/${cfg["areaRest"]}`,
      attribute = "SMID >=1",
      datasetNames = cfg["datasetNames"];
    return new Promise((res, rej) => {
      this.queryFeaturesBySQLParameters(url, attribute, datasetNames)
        .then(rst => {
          res(rst);
        })
        .catch(e => {
          rej(e);
        });
    });
  },
  // 初始化场景点击对象
  initBuildHandler() {
    let handler = new Cesium.ScreenSpaceEventHandler(sceneUtil.scene.canvas);
    const that = this
    handler.setInputAction(function (e) {
      // 首先移除之前添加标识实体
      sceneUtil.viewer.entities.removeById(id);
      removeIdentify();
      // 获取点击位置笛卡尔坐标
      let position = sceneUtil.scene.pickPosition(e.position);
      // 从笛卡尔坐标获取经纬度
      let cartographic = Cesium.Cartographic.fromCartesian(position);
      let longitude = Cesium.Math.toDegrees(cartographic.longitude);
      let latitude = Cesium.Math.toDegrees(cartographic.latitude);
      let height = cartographic.height
      currectPoint = {
        lng: longitude,
        lat: latitude,
        height: height
      }
      flag = "scene"
      let params = {
        lng: longitude,
        lat: latitude,
        height: height,
      };
      doQuery(params)
    }, Cesium.ScreenSpaceEventType.LEFT_CLICK);
    sceneUtil.popup.changePosition(sceneUtil.scene)
  },

  // 单体化
  queryByPoint,

  // 分层分户查询
  queryHouse(params) {
    currectPoint = {
      lng: params.lng,
      lat: params.lat,
      height: params.height
    }
    doQuery(params);
  },
};

// 通过点查询面
function queryByPoint(queryPoint, type, center) {
  flag = type ? type : "scene"
  if (type === "list") {
    let height = sceneUtil.scene.getHeight(center.lng, center.lat)
    currectPoint = {
      lng: queryPoint.lng,
      lat: queryPoint.lat,
      height: height
    }
  }
  let queryObj = {
    getFeatureMode: "SPATIAL",
    spatialQueryMode: "INTERSECT",
    datasetNames: ["buildings_1:Buildings"],
    geometry: {
      id: 0,
      parts: [1],
      points: [queryPoint],
      type: "POINT"
    }
  };
  let queryObjJSON = JSON.stringify(queryObj); // 转化为JSON字符串作为查询参数
  removeIdentify();
  sceneUtil.popup.closeAllPopup();
  store.commit('supermap/SET_HOUSE_INDEX', null)
  queryBuilding(queryObjJSON);
}
// 查询建筑信息进行单体化
function queryBuilding(query) {
  // 移除分层分户标识体
  if (lastHouseEntity) {
    sceneUtil.viewer.entities.remove(lastHouseEntity);
    lastHouseEntity = null;
  }
  // 使用axios存在跨域问题（chrome浏览器下），建议使用下方 ajax方式
  $.ajax({
    type: "post",
    url:
      "http://222.91.72.102:2686/iserver/services/data-model/rest/data/featureResults.rjson?returnContent=true",
    data: query,
    success: function (result) {
      let resultObj = JSON.parse(result);
      if (resultObj.featureCount > 0) {
        createPopup("build", resultObj.features[0])
        addClapFeature(resultObj.features[0]);
        store.commit('supermap/SET_BUILD_INDEX', resultObj.features[0].geometry.id - 1)
      } else {
        store.commit('supermap/SET_BUILD_INDEX', null)
      }
    },
    error: function (msg) {
      store.commit('supermap/SET_BUILD_INDEX', null)
    }
  });
}
// 将数据服务查询到的要素添加到场景中高亮显示，表示选中效果。
function addClapFeature(feature) {
  let lonLatArr = getLonLatArray(feature.geometry.points);
  // 在分层分户查询的时候设置过，所以在单体化查询的时候设置回去
  Cesium.GroundPrimitive.bottomAltitude = 0; //矢量面贴对象的底部高程
  Cesium.GroundPrimitive.extrudeHeight = 500; //矢量面贴对象的拉伸高度
  let i = 0;
  i++;
  id = "identify" + i;
  sceneUtil.viewer.entities.add({
    id: id,
    name: "单体化标识面",
    polygon: {
      hierarchy: Cesium.Cartesian3.fromDegreesArray(lonLatArr),
      material: new Cesium.Color(1.0, 0.0, 0.0, 0.3)
    },
    classificationType: Cesium.ClassificationType.S3M_TILE // 贴在S3M模型表面
  });
}
// 得到[经度,纬度,经度,纬度...]形式的数组，用于构造面。
function getLonLatArray(points) {
  let point3D = [];
  points.forEach(function (point) {
    point3D.push(point.x);
    point3D.push(point.y);
  });
  return point3D;
}
// 分层分户查询
function doQuery(params) {
  store.commit('supermap/SET_BUILD_INDEX', null)
  removeIdentify()
  sceneUtil.popup.closeAllPopup();
  let SQL = `BOTTOM < ${params.height} and ${params.height} < (BOTTOM + LSG) and ${params.lng} > SMSDRIW and ${params.lng} < SMSDRIE and ${params.lat} > SMSDRIS and ${params.lat} < SMSDRIN`;
  let getFeatureParam, getFeatureBySQLService, getFeatureBySQLParams;
  getFeatureParam = new SuperMap.REST.FilterParameter({
    attributeFilter: SQL
  });
  getFeatureBySQLParams = new SuperMap.REST.GetFeaturesBySQLParameters({
    queryParameter: getFeatureParam,
    toIndex: -1,
    datasetNames: ["buildings_1:" + "households"] // 本例中“户型面”为数据源名称，“专题户型面2D”为楼层面相应的数据集名称
  });
  let url = "http://222.91.72.102:2686/iserver/services/data-model/rest/data"; // 数据服务地址
  getFeatureBySQLService = new SuperMap.REST.GetFeaturesBySQLService(url, {
    eventListeners: {
      processCompleted: onQueryComplete, // 查询成功时的回调函数
      processFailed: processFailed // 查询失败时的回调函数
    }
  });
  getFeatureBySQLService.processAsync(getFeatureBySQLParams);
}
// 分层分户查询成功
function onQueryComplete(queryEventArgs) {
  // 首先移除之前添加标识实体
  if (lastHouseEntity) {
    sceneUtil.viewer.entities.remove(lastHouseEntity);
    lastHouseEntity = null;
  }
  if (queryEventArgs.originResult.featureCount > 0) {
    let selectedFeature = queryEventArgs.originResult.features[0]; // 选中楼层的楼层面信息对象
    store.commit('supermap/SET_HOUSE_INDEX', selectedFeature.ID - 1)
    if (!selectedFeature) {
      // 关闭气泡
      return;
    }
    if (!selectedFeature.geometry.points) {
      return;
    }
    let bottomHeight = Number(
      selectedFeature.fieldValues[selectedFeature.fieldNames.indexOf("BOTTOM")]
    ); // 底部高程
    let extrudeHeight = Number(
      selectedFeature.fieldValues[selectedFeature.fieldNames.indexOf("LSG")]
    ); // 层高（拉伸高度）
    Cesium.GroundPrimitive.bottomAltitude = bottomHeight; // 矢量面贴对象的底部高程
    Cesium.GroundPrimitive.extrudeHeight = extrudeHeight; // 矢量面贴对象的拉伸高度
    let points3D = []; // [经度, 纬度, 经度, 纬度, ...]的形式，存放楼层面上的点坐标
    for (let pt of selectedFeature.geometry.points) {
      points3D.push(pt.x, pt.y);
    }
    lastHouseEntity = sceneUtil.viewer.entities.add({
      polygon: {
        hierarchy: Cesium.Cartesian3.fromDegreesArray(points3D),
        material: new Cesium.Color(223 / 255, 199 / 255, 0 / 255, 0.4)
      },
      classificationType: Cesium.ClassificationType.S3M_TILE // 贴在S3M模型表面
    });
    // 添加气泡
    createPopup("house", queryEventArgs.result.features[0].data)
  } else {
    let queryPoint = { // 查询点对象
      x: currectPoint.lng,
      y: currectPoint.lat
    };
    queryByPoint(queryPoint);
  }

}
// 分层分户查询失败
function processFailed(queryEventArgs) {
  store.commit('supermap/SET_HOUSE_INDEX', null)
  alert("查询失败！");
  console.log(queryEventArgs);
}
// 移除单体化标识体
function removeIdentify() {
  sceneUtil.viewer.entities.removeById(id);
}
// 气泡内容
function createPopup(type, params) {
  let obj = {}
  if (type === "build") {
    for (let i = 0; i < params.fieldNames.length; i++) {
      buildPopup.curSelected[params.fieldNames[i]] = params.fieldValues[i]
    }
    let html = `<div class="popup-content"><p class="title">${buildPopup.title}</p><div class="buildInfo baseInfo">`;
    buildPopup.buildInfo.forEach(val => {
      html += `
              <p class="info-item"><span class="item-name">${val.name}</span>
              <span class="item-value">${val.prop === "HOUSEAREA"
          ? parseFloat(buildPopup.curSelected[val.prop]).toFixed(2)
          : buildPopup.curSelected[val.prop]
        }</span></p>
          `;
    });
    html += `</div></div>`;
    obj = {
      x: parseFloat(buildPopup.curSelected.LNG),
      y: parseFloat(buildPopup.curSelected.LAT),
      z: currectPoint.height,
      html: html
    };
    if (flag === "list") {
      flyTo(obj);
    }
  } else if (type === "house") {
    let html = `<div class="popup-content"><p class="title">${housePopup.title}</p><div class="houseInfo baseInfo">`;
    housePopup.houseInfo.forEach(val => {
      html += `
              <p class="info-item"><span class="item-name">${val.name}</span>
              <span class="item-value">${val.prop === "HOUSINGAREA"
          ? parseFloat(params[val.prop]).toFixed(2)
          : params[val.prop]
        }</span></p>
        `;
    });
    html += `</div></div>`;
    obj = {
      x: parseFloat(params.LNG),
      y: parseFloat(params.LAT),
      z: currectPoint.height,
      html: html
    };
  } else {
    return;
  }
  initPopup(obj);
}
// 飞行到选中地方
function flyTo(position) {
  // let orientation = {
  //   heading: 0,
  //   pitch: -1,
  //   roll: 0
  // }
  sceneUtil.flyToView([
    position.x,
    position.y,
    700
  ]);
}
// 显示气泡
function initPopup(obj) {
  sceneUtil.popup.show({
    // 源位置 [经纬=>Cartesian3]
    // 请注意fromDegrees里面经纬度的顺序
    cartesian3: Cesium.Cartesian3.fromDegrees(obj.x, obj.y, obj.z),
    width: 360,
    height: 240,
    offsets: [180, 260],
    // 初始化出的popup绑定的class,方便在css里面更改
    // 默认css配置[@/assets/css/popupStyle.css],引入位置[main.js]
    class: "myPopup popup",
    // popup框内容
    html: obj.html,
    onclose: () => {
      // sceneUtil.clearEntity();
    }
  });
}
