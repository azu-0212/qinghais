import { Cesium } from '../consts'
import $ from 'jquery'

export default {
  handlerDis: {},
  handlerArea: {},
  handlerHeight: {},
  active: false,
  /**
     * 创建测量控件
     */
  initMeasure3DControl: function(Cesium, viewer) {
    // 注册测距、测高、测面积控件
    this.measureDistance(Cesium, viewer)
    this.measureHeight(Cesium, viewer)
    this.measureArea(Cesium, viewer)
  },
  /**
     * 测距离
     * @param {Object} cesium
     * @param {Object} viewer
     */
  measureDistance: function(cesium, viewer) {
    this.handlerDis = new Cesium.MeasureHandler(viewer, cesium.MeasureMode.Distance, 0)
    this.handlerDis.measureEvt.addEventListener((result) => {
      let distance = result.distance > 1000 ? (result.distance / 1000).toFixed(2) + 'km' : result.distance + 'm'
      this.handlerDis.disLabel.text = '距离:' + distance
    })
    this.handlerDis.activeEvt.addEventListener((isActive) => {
      if (isActive === true) {
        viewer.enableCursorStyle = false
        viewer._element.style.cursor = ''
        $('body').removeClass('measureCur').addClass('measureCur')
      } else {
        viewer.enableCursorStyle = true
        $('body').removeClass('measureCur')
      }
    })
  },
  /**
     * 测高度
     * @param {Object} cesium
     * @param {Object} viewer
     */
  measureHeight: function(cesium, viewer) {
    this.handlerHeight = new Cesium.MeasureHandler(viewer, cesium.MeasureMode.DVH)
    this.handlerHeight.measureEvt.addEventListener((result) => {
       let distance = result.distance > 1000 ? (result.distance / 1000).toFixed(2) + 'km' : (result.distance / 1).toFixed(2) + 'm'
       let vHeight = result.verticalHeight > 1000 ? (result.verticalHeight / 1000).toFixed(2) + 'km' : (result.verticalHeight / 1).toFixed(2) + 'm'
       let hDistance = result.horizontalDistance > 1000 ? (result.horizontalDistance / 1000).toFixed(2) + 'km' : (result.horizontalDistance / 1).toFixed(2) + 'm'
      // 单位为米
     /* let distance = (result.distance).toFixed(2) + 'm';
      let vHeight = (result.verticalHeight ).toFixed(2) + 'm';
      let hDistance = (result.horizontalDistance).toFixed(2) + 'm';*/
      this.handlerHeight.disLabel.text = '空间距离:' + distance
      this.handlerHeight.vLabel.text = '垂直高度:' + vHeight
      this.handlerHeight.hLabel.text = '水平距离:' + hDistance
    })
    this.handlerHeight.activeEvt.addEventListener((isActive) => {
      if (isActive == true) {
        viewer.enableCursorStyle = false
        viewer._element.style.cursor = ''
        $('body').removeClass('measureCur').addClass('measureCur')
      } else {
        viewer.enableCursorStyle = true
        $('body').removeClass('measureCur')
        this.active = false
      }
    })
  },
  /**
     * 测面积
     * @param {Object} cesium
     * @param {Object} viewer
     */
  measureArea: function(cesium, viewer) {
    this.handlerArea = new Cesium.MeasureHandler(viewer, cesium.MeasureMode.Area, 2)
    this.handlerArea.measureEvt.addEventListener((result) => {
      console.log('测面积',(result.area / 1).toFixed(2)+ 'km²');
      let area = result.area > 1000000 ? (result.area / 1000000).toFixed(2) + 'km²' : (result.area / 1).toFixed(2) + '㎡'
      this.handlerArea.areaLabel.text = '面积:' + area
    })
    this.handlerArea.activeEvt.addEventListener(function(isActive) {
      if (isActive == true) {
        viewer.enableCursorStyle = false
        viewer._element.style.cursor = ''
        $('body').removeClass('measureCur').addClass('measureCur')
      } else {
        viewer.enableCursorStyle = true
        $('body').removeClass('measureCur')
      }
    })
  },
  /**
     * 激活测量控件
     * @param {Object} type：控件类型（DIS--测距；AREA--面积；HEIGHT--高度）
     */
  activateMeasure3DControl: function(type) {
    this.deActiveAllMeasure3DControl()
    switch (type) {
      case 'DIS':
        this.handlerDis && this.handlerDis.activate()
        this.active = true
        break
      case 'AREA':
        this.handlerArea && this.handlerArea.activate()
        this.active = true
        break
      case 'HEIGHT':
        this.handlerHeight && this.handlerHeight.activate()
        this.active = true
        break
    }
  },
  /**
     * 销毁测量控件,并清除所有要素
     */
  deActiveAllMeasure3DControl: function() {
    this.active = false;
    this.handlerDis && this.handlerDis.deactivate()
    this.handlerArea && this.handlerArea.deactivate()
    this.handlerHeight && this.handlerHeight.deactivate()
    this.handlerDis && this.handlerDis.clear()
    this.handlerArea && this.handlerArea.clear()
    this.handlerHeight && this.handlerHeight.clear()
  },

  deActiveBtNtClear: function() {
    this.active = false
    this.handlerDis && this.handlerDis.deactivate()
    this.handlerArea && this.handlerArea.deactivate()
    this.handlerHeight && this.handlerHeight.deactivate()
  }
}
