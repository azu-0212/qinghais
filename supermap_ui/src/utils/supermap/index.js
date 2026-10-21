import sceneUtil from './map3D/index';
import SceneScreenUtil from './map3D/screen3D'
import bus from '../bus'
import { Cesium, } from './consts'
import $ from "jquery";
export default {
  SceneUtil: sceneUtil,
  SceneScreenUtil: SceneScreenUtil,
  // mapUitl: false,
  measure3D:sceneUtil.measure3DUtil,
  // 经纬度 相机位置 偏下
  latStep:sceneUtil.latStep,
  altStep:sceneUtil.altStep,
  /**
   * 查询行政区划面
   */
  openPopup3D(cfg){
    // sceneUtil.popup.closeAllPopup()
    // console.log('index cfg',cfg)
    // sceneUtil.flyToView([cfg.position.lon,cfg.position.lat,cfg.position.alt],undefined,()=>{
    //   sceneUtil.popup.show({
    //     cartesian3:Cesium.Cartesian3.fromDegrees(cfg.position.lon,cfg.position.lat,cfg.position.alt),
    //     html:cfg.html
    //   })
    // })
  },



  /**
   * 全景Marks点标注 定位
   */
  openMarksPanorama3D(cfg,flyParam){
   /* if(!cfg.checked){
      console.log('关');
      sceneUtil.popup.closeAllPopup();
      //清楚所有照片库实体图标等信息
      //sceneUtil.clearEntity();
      sceneUtil.entireView();
      return;
    }
    console.log('开');
    sceneUtil.add3DMarkersToMap([cfg.props],cfg.marker,cfg.checked);
    let position = {
        lon: parseFloat(cfg.position.lon),
        lat: parseFloat(cfg.position.lat),
        alt: parseFloat(cfg.position.alt)
      }
      console.log('position',position);
    sceneUtil.popup.closeAllPopup();
    sceneUtil.flyToView([position.lon,position.lat+sceneUtil.latStep,position.alt+sceneUtil.altStep],undefined);
    setTimeout(function(){
      sceneUtil.popup.show({
        // 源位置 [经纬=>Cartesian3]
        // 请注意fromDegrees里面经纬度的顺序
        cartesian3: sceneUtil.viewer.entities.values[0].position._value,
        // 初始化出的popup绑定的class,方便在css里面更改
        // 默认css配置[@/assets/css/popupStyle.css],引入位置[main.js]
        class: 'myPopup',
        // popup框内容
        html: cfg.html,
        onclose:()=>{
          // 清除实体，并全幅青海
          //sceneUtil.clearEntity();
          sceneUtil.entireView();
          // 关闭弹框 取消全景、街景选中
          bus.$emit('clearChecked')
        }
      })
    },3000)*/
    if(!cfg.checked){
      sceneUtil.popup.closeAllPopup();
      sceneUtil.clearEntity();
      return;
    }
    setTimeout(function(){
      let position = {
        lon: parseFloat(cfg.position.lon),
        lat: parseFloat(cfg.position.lat),
        alt: parseFloat(cfg.position.alt)
      }
      sceneUtil.popup.closeAllPopup();
      // 是否需要定位  需要飞行定位
      if(!flyParam){
        sceneUtil.add3DMarkersToMap([cfg.props],cfg.marker,cfg.checked);
        sceneUtil.flyToView([position.lon,position.lat+sceneUtil.latStep,position.alt+sceneUtil.altStep],undefined);
        setTimeout(function(){
          sceneUtil.popup.show({
            // 源位置 [经纬=>Cartesian3]
            // 请注意fromDegrees里面经纬度的顺序
            cartesian3: sceneUtil.viewer.entities.values[0].position._value,
            // 初始化出的popup绑定的class,方便在css里面更改
            // 默认css配置[@/assets/css/popupStyle.css],引入位置[main.js]
            class: cfg.class || 'myPopup',
            // popup框内容
            html: cfg.html,
            onclose:()=>{
              // 清除实体，并全幅青海
              //sceneUtil.clearEntity();
              sceneUtil.entireView();
              // 关闭弹框 取消全景、街景选中
              bus.$emit('clearChecked')
            }
          })
        },3000)
        return;
      }
      // 不需要飞行定位 弹出框坐标为点击场景坐标
      setTimeout(function(){
        let cartographic = Cesium.Cartographic.fromCartesian(sceneUtil.position);
        let longitude = Cesium.Math.toDegrees(cartographic.longitude);
        let latitude = Cesium.Math.toDegrees(cartographic.latitude);
        sceneUtil.flyToView([longitude,latitude+0.15,95000],undefined);
        sceneUtil.popup.show({
          // 源位置 [经纬=>Cartesian3]
          // 请注意fromDegrees里面经纬度的顺序
          cartesian3: sceneUtil.position,
          // 初始化出的popup绑定的class,方便在css里面更改
          // 默认css配置[@/assets/css/popupStyle.css],引入位置[main.js]
          class: cfg.class || 'myPopup',
          // popup框内容
          html: cfg.html,
          onclose:()=>{
            // 清除实体，并全幅青海
            //sceneUtil.clearEntity();
          }
        })
      },500)
    },10)

  },

  /**
   * 全景Marks定位  不去除其它图标
   */
  openMarksPanorama3DNoRemove(cfg,selectedEntity){
    setTimeout(function(){
      let position = {
        lon: parseFloat(cfg.position.lon),
        lat: parseFloat(cfg.position.lat),
        alt: parseFloat(cfg.position.alt)
      }
      sceneUtil.popup.closeAllPopup();
      // 是否需要定位  需要飞行定位
      console.log('sceneUtil.viewer.entities',selectedEntity.name);
      sceneUtil.viewer.entities.values.map(item =>{
        if(item.name === selectedEntity.name){
          console.log('item',item.position._value);
          sceneUtil.flyToView([position.lon,position.lat+0.0065,10000],undefined);
          setTimeout(function(){
            sceneUtil.popup.show({
              // 源位置 [经纬=>Cartesian3]
              // 请注意fromDegrees里面经纬度的顺序
              cartesian3: item.position._value,
              // 初始化出的popup绑定的class,方便在css里面更改
              // 默认css配置[@/assets/css/popupStyle.css],引入位置[main.js]
              class: cfg.class || 'myPopup',
              // popup框内容
              html: cfg.html,
              onclose:()=>{
                // 全幅青海
                sceneUtil.entireView();
              }
            })
          },3000)
        }
      })

    },10)
  }
}
