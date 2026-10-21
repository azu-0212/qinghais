import { Cesium } from "../consts";
import $ from "jquery";
let viewer = null,
  scene = null,
  handlerPoint = null,
  sightline = null,
  viewPosition = null,
  // 可视域相关变量
  viewshed3D = null,
  handler = null,
  pointHandler = null,
  viewModel = {
    direction: 1.0,
    pitch: 1.0,
    distance: 1.0,
    verticalFov: 1.0,
    horizontalFov: 1.0,
    visibleAreaColor: "#ffffffff",
    invisibleAreaColor: "#ffffffff"
  },
  // BOX裁剪相关
  handlerBox = null,
  editorBox = null,
  layers = null,
  boxEntity = null,
  boxMode = "",
  // 坡度坡向
  handlerPolygon = null,
  isPolygonActive = true,
  slope = null,
  wide = null,
  colorTable = null,
  ribbon = [
    {
      colors: [
        {
          slope: 0,
          color: '9, 9, 255'
        },
        {
          slope: 20,
          color: '0, 161, 1'
        },
        {
          slope: 30,
          color: '20, 187, 18'
        },
        {
          slope: 50,
          color: '221, 224, 7'
        },
        {
          slope: 80,
          color: '255, 0, 0'
        }
      ]
    },
    {
      colors: [
        {
          slope: 0,
          color: '162, 251, 194'
        },
        {
          slope: 20,
          color: '180, 200, 170'
        },
        {
          slope: 30,
          color: '200, 160, 130'
        },
        {
          slope: 50,
          color: '225, 130, 130'
        },
        {
          slope: 80,
          color: '1, 103, 103'
        }
      ]
    },
    {
      colors: [
        {
          slope: 0,
          color: '230, 198, 1'
        },
        {
          slope: 20,
          color: '210, 150, 1'
        },
        {
          slope: 30,
          color: '190, 100, 1'
        },
        {
          slope: 50,
          color: '165, 50, 1'
        },
        {
          slope: 80,
          color: '157, 0, 1'
        }
      ]
    },
    {
      colors: [
        {
          slope: 0,
          color: '200, 245, 245'
        },
        {
          slope: 20,
          color: '149, 232, 249'
        },
        {
          slope: 30,
          color: '70, 116, 200'
        },
        {
          slope: 50,
          color: '20, 50, 180'
        },
        {
          slope: 80,
          color: '0, 39, 148'
        }
      ]
    }
  ],
  tooltip = null
;

export default {
  ribbon: ribbon,
  handlerBox:handlerBox,
  pointHandler:pointHandler,
  editorBox:editorBox,
  handlerPolygon:handlerPolygon,
  // 初始化绘制对象
  initAnalysis: function (viewerO, sceneO) {
    viewer = viewerO;
    scene = sceneO;
    layers = scene.layers._layerQueue;
    sightlineAnalysis(); // 通视分析
    viewshedAnalysis(); // 可视域分许
    boxCrop3D(); // BOX裁剪（交互）
  },
  // 选取观察位置
  chooseView: function () {
    if (handlerPoint.active) {
      return;
    }
    scene.viewFlag1 = true;
    viewer.entities.removeAll();
    if (sightline.couldRemove) {
      sightline.removeAllTargetPoint();
    }
    handlerPoint.activate();
  },
  // 添加目标点
  addTarget: function () {
    scene.viewFlag1 = false;
    handlerPoint.activate();
  },
  // 激活绘制可视域
  chooseViewPoint: function () {
    if (pointHandler.active) {
      return;
    }
    //先清除之前的可视域分析
    viewer.entities.removeAll();
    //viewshed3D.distance = 0.1;
    scene.viewFlag2 = true;
    //激活绘制点类
    pointHandler.activate();
  },
  //   激活BOX裁剪功能
  activeBoxClip: function (val) {
    if (handlerBox.active) {
      return;
    }
    viewer.entities.removeAll();
    boxMode = val;
    handlerBox.activate();
  },

  // 切换裁剪模式
  changeClipMode: function (mode) {
    boxMode = mode;
    setClipBox(mode);
  },

  // 绘制坡向分析多边形
  beginDraw: function () {
    if (handlerPolygon.active) {
      return;
    }
    viewer.entities.removeAll();
    handlerPolygon.activate();
  },
  slopeAnalysis() {
    slopeAnalysis();
  },
  endDraw(){
    isPolygonActive = false;
  },
  // 修改坡度坡向分析选择项
  changeSlope: function (slopeOptions) {
    changeSlope(slopeOptions);
  },
  // 清除各种实体
  clear: function (type) {
    switch (type) {
      case "通视":
        handlerPoint.clear();
        viewer.entities.removeAll();
        if (sightline.couldRemove) {
          sightline.removeAllTargetPoint();
          sightline.couldRemove = false;
        }
        break;
      case "可视域":
        pointHandler.clear();
        viewer.entities.removeAll();
        viewshed3D.distance = 0.01;
        scene.viewFlag2 = true;

      case "BOX裁剪":
        for (let i = 0, j = layers.length; i < j; i++) {
          layers[i].clearCustomClipBox();
        }
        editorBox.deactivate();
        viewer.entities.removeAll();
        handlerBox.clear();
        handlerBox.deactivate();
      case "坡度坡向":
        viewer.scene.globe.SlopeSetting = {
          slopeSetting: slope,
          analysisMode:
          Cesium.HypsometricSettingEnum.AnalysisRegionMode.ARM_NONE
        };
        handlerPolygon.polygon.show = false;
        handlerPolygon.polyline.show = false;
      default:
        break;
    }
  },

  removeAll: function () {
    handlerPoint.clear();
    pointHandler.clear();
    // editorBox.deactivate();
    handlerBox.clear();
    handlerBox.deactivate();
    // handlerPolygon.polygon.show = false;
    // handlerPolygon.polyline.show = false;
    viewer.entities.removeAll();

  }
};

/**
 * 通视分析相关
 */
function sightlineAnalysis() {
  handlerPoint = new Cesium.DrawHandler(viewer, Cesium.DrawMode.Point);
  sightline = new Cesium.Sightline(scene);
  sightline.couldRemove = false;
  sightline.build();
  handlerPoint.drawEvt.addEventListener(function (result) {
    let point = result.object;
    point.show = false;
    let position = result.object.position;
    //将获取的点的位置转化成经纬度
    let cartographic = Cesium.Cartographic.fromCartesian(position);
    let longitude = Cesium.Math.toDegrees(cartographic.longitude);
    let latitude = Cesium.Math.toDegrees(cartographic.latitude);
    let height = cartographic.height;
    if (scene.viewFlag1) {
      //设置观察点
      sightline.viewPosition = [longitude, latitude, height];
      viewer.entities.add({
        position: Cesium.Cartesian3.fromDegrees(
          longitude,
          latitude,
          height + 1
        ),
        billboard: {
          image: require('../../../assets/image/analysis/location.png'),
          width: 20,
          height: 30
        },
        name: "观察点"
      })
      scene.viewFlag1 = false;
    } else {
      //添加目标点
      sightline.addTargetPoint({
        position: [longitude, latitude, height],
        name: "point" + new Date()
      });
      viewer.entities.add({
        position: Cesium.Cartesian3.fromDegrees(
          longitude,
          latitude,
          height + 1
        ),
        billboard: {
          image: require('../../../assets/image/analysis/location.png'),
          width: 20,
          height: 30
        },
        name: "目标点"
      })
      sightline.couldRemove = true;
    }
  });
}
/**
 * 可视域分析相关
 */
function viewshedAnalysis() {
  scene.viewFlag2 = true;
  pointHandler = new Cesium.DrawHandler(viewer, Cesium.DrawMode.Point);
  viewshed3D = new Cesium.ViewShed3D(scene);
  //Cesium.knockout.track(viewModel);
  handler = new Cesium.ScreenSpaceEventHandler(scene.canvas);
  // 鼠标移动时间回调
  handler.setInputAction(function (e) {
    // 若此标记为false，则激活对可视域分析对象的操作
    if (!scene.viewFlag2) {
      //获取鼠标屏幕坐标,并将其转化成笛卡尔坐标
      let position = e.endPosition;
      let last = scene.pickPosition(position);
      //计算该点与视口位置点坐标的距离
      let distance = Cesium.Cartesian3.distance(viewPosition, last);
      if (distance > 0) {
        // 将鼠标当前点坐标转化成经纬度
        let cartographic = Cesium.Cartographic.fromCartesian(last);
        let longitude = Cesium.Math.toDegrees(cartographic.longitude);
        let latitude = Cesium.Math.toDegrees(cartographic.latitude);
        let height = cartographic.height;
        // 通过该点设置可视域分析对象的距离及方向
        viewshed3D.setDistDirByPoint([longitude, latitude, height]);
      }
    }
  }, Cesium.ScreenSpaceEventType.MOUSE_MOVE);
  handler.setInputAction(function (e) {
    //鼠标右键事件回调，不再执行鼠标移动事件中对可视域的操作
    scene.viewFlag2 = true;
    //   viewModel.direction = viewshed3D.direction;
    //   viewModel.pitch = viewshed3D.pitch;
    //   viewModel.distance = viewshed3D.distance;
    //   viewModel.horizontalFov = viewshed3D.horizontalFov;
    //   viewModel.verticalFov = viewshed3D.verticalFov;
  }, Cesium.ScreenSpaceEventType.RIGHT_CLICK);
  pointHandler.drawEvt.addEventListener(function (result) {
    console.log('result',result);
    let point = result.object;
    let position = point.position;
    viewPosition = position;
    // 将获取的点的位置转化成经纬度
    let cartographic = Cesium.Cartographic.fromCartesian(position);
    let longitude = Cesium.Math.toDegrees(cartographic.longitude);
    let latitude = Cesium.Math.toDegrees(cartographic.latitude);
    let height = cartographic.height + 1.8;
    point.position = Cesium.Cartesian3.fromDegrees(longitude, latitude, height);
    console.log('point',point);
    if (scene.viewFlag2) {
      // 设置视口位置
      viewshed3D.viewPosition = [longitude, latitude, height];
      viewshed3D.build();
      // 将标记置为false以激活鼠标移动回调里面的设置可视域操作
      scene.viewFlag2 = false;
    }
  });
}

/**
 * BOX裁剪相关
 */
function boxCrop3D() {
  // 设置裁剪线颜色
  setAllLayersClipColor();
  boxEntity = undefined;
  //交互绘制box
  handlerBox = new Cesium.DrawHandler(viewer, Cesium.DrawMode.Box);
  handlerBox.movingEvt.addEventListener(function (windowPosition) { });
  handlerBox.drawEvt.addEventListener(function (e) {
    boxEntity = e.object;
    let newDim = boxEntity.box.dimensions.getValue();
    let position = boxEntity.position.getValue(0);
    let boxOption = {
      dimensions: newDim,
      position: position,
      clipMode: boxMode,
      heading: 0
    };
    //box编辑
    editorBox = new Cesium.BoxEditor(viewer, boxEntity);
    editorBox.editEvt.addEventListener(function (e) {
      boxEntity.box.dimensions = e.dimensions;
      boxEntity.position = e.position;
      boxEntity.orientation = e.orientation;
      setClipBox(boxMode);
    });
    editorBox.distanceDisplayCondition = new Cesium.DistanceDisplayCondition(
      0,
      950
    );
    editorBox.activate();
    setAllLayersClipOptions(boxOption);
    handlerBox.clear();
    handlerBox.deactivate();
  });
}
function setClipBox(val) {
  let clipMode = val;
  if (typeof boxEntity == "undefined") {
    return;
  }
  let newDim = boxEntity.box.dimensions.getValue();
  let position = boxEntity.position.getValue(0);
  let heading = 0;
  if (typeof boxEntity.orientation != "undefined") {
    let rotationM3 = Cesium.Matrix3.fromQuaternion(
      boxEntity.orientation._value,
      new Cesium.Matrix3()
    );
    let localFrame = Cesium.Matrix4.fromRotationTranslation(
      rotationM3,
      Cesium.Cartesian3.ZERO,
      new Cesium.Matrix4()
    );
    let inverse = Cesium.Matrix4.inverse(
      Cesium.Transforms.eastNorthUpToFixedFrame(position),
      new Cesium.Matrix4()
    );
    let hprm = Cesium.Matrix4.multiply(
      inverse,
      localFrame,
      new Cesium.Matrix4()
    );
    let rotation = Cesium.Matrix4.getMatrix3(hprm, new Cesium.Matrix3());
    let hpr = Cesium.HeadingPitchRoll.fromQuaternion(
      Cesium.Quaternion.fromRotationMatrix(rotation)
    );
    heading = hpr.heading;
  }
  let boxOptions = {
    dimensions: newDim,
    position: position,
    clipMode: boxMode,
    heading: heading
  };
  setAllLayersClipOptions(boxOptions);
}
function setAllLayersClipColor() {
  for (let i = 0, j = layers.length; i < j; i++) {
    layers[i].clipLineColor = new Cesium.Color(1, 1, 1, 0);
  }
}
function setAllLayersClipOptions(boxOptions) {
  for (let i = 0, j = layers.length; i < j; i++) {
    layers[i].setCustomClipBox(boxOptions);
  }
}

/**
 * 破向坡度分析
 */
function slopeAnalysis() {
  // scene.globe.enableLighting = true;
  slope = new Cesium.SlopeSetting();
  slope.DisplayMode = Cesium.SlopeSettingEnum.DisplayMode.FACE_AND_ARROW;
  slope.MaxVisibleValue = "90";
  slope.MinVisibleValue = "0";
  colorTable = new Cesium.ColorTable();
  colorTable.insert(0, new Cesium.Color(9 / 255, 9 / 255, 255 / 255));
  colorTable.insert(20, new Cesium.Color(0, 161 / 255, 1));
  colorTable.insert(30, new Cesium.Color(20 / 255, 187 / 255, 18 / 255));
  colorTable.insert(50, new Cesium.Color(221 / 255, 224 / 255, 7 / 255));
  colorTable.insert(80, new Cesium.Color(255 / 255, 0 / 255, 0 / 255));
  wide = Cesium.HypsometricSettingEnum.AnalysisRegionMode.ARM_REGION;
  slope.ColorTable = colorTable;
  slope.Opacity = 0.5;
  tooltip = createTooltip(document.body)
  //绘制多边形
  handlerPolygon = new Cesium.DrawHandler(viewer, Cesium.DrawMode.Polygon, 0);
  handlerPolygon.activeEvt.addEventListener(function (isActive) {
    if (isActive === true) {
      viewer.enableCursorStyle = false;
      viewer._element.style.cursor = "";
      $('.slopeCloseBtn').css('display','none');
    }else{
      viewer.enableCursorStyle = true;
      $('.slopeCloseBtn').css('display','block');
    }
  });
  handlerPolygon.movingEvt.addEventListener(function (windowPosition) {
    if (windowPosition.x < 200 && windowPosition.y < 150) {
      tooltip.setVisible(false);
      return;
    }
    if (handlerPolygon.isDrawing) {
      tooltip.showAt(windowPosition, '<p>点击确定查询区域中间点</p><p>右键单击结束绘制</p>');
    } else {
      tooltip.showAt(windowPosition, '<p>点击绘制查询区域第一个点</p>');
    }
  });
  handlerPolygon.drawEvt.addEventListener(function (result) {
    tooltip.setVisible(false);
    if (!result.object.positions) {
      handlerPolygon.polygon.show = false;
      handlerPolygon.polyline.show = false;
      handlerPolygon.deactivate();
      return;
    }
    let array = [].concat(result.object.positions);
    let positions = [];
    for (let i = 0, len = array.length; i < len; i++) {
      let cartographic = Cesium.Cartographic.fromCartesian(array[i]);
      let longitude = Cesium.Math.toDegrees(cartographic.longitude);
      let latitude = Cesium.Math.toDegrees(cartographic.latitude);
      let h = cartographic.height;
      if (
        positions.indexOf(longitude) == -1 &&
        positions.indexOf(latitude) == -1
      ) {
        positions.push(longitude);
        positions.push(latitude);
        positions.push(h);
      }
    }
    slope.CoverageArea = positions;
    viewer.scene.globe.SlopeSetting = {
      slopeSetting: slope,
      analysisMode: wide
    };
    handlerPolygon.polygon.show = false;
    handlerPolygon.polyline.show = true;
    handlerPolygon.deactivate();
  });
  // viewer._cesiumWidget._creditContainer.style.display = "none";
  if (!scene.pickPositionSupported) {
    alert(
      "不支持深度纹理,无法绘制多边形，根据多边形显示分析区域功能无法使用！"
    );
  }
}
// 修改坡度坡向分析选择项
function changeSlope(slopeOptions) {
  wide =
    Cesium.HypsometricSettingEnum.AnalysisRegionMode[slopeOptions.computeMode];
  slope.MinVisibleValue = slopeOptions.slope.start;
  slope.MaxVisibleValue = slopeOptions.slope.end;
  slope.DisplayMode =
    Cesium.SlopeSettingEnum.DisplayMode[slopeOptions.fillMode];
  // 颜色设置
  console.log(slopeOptions.colorTable)
  slopeOptions.colorTable.forEach(item => {
    colorTable.remove(item.slope);
    colorTable.insert(item.slope, new Cesium.Color(item.color[0], item.color[1], item.color[2]));
  })
  slope.ColorTable = colorTable;
  slope.Opacity = slopeOptions.opacity;
  viewer.scene.globe.SlopeSetting = {
    slopeSetting: slope,
    analysisMode: wide
  };
}
