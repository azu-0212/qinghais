import $ from "jquery";
import { Cesium } from "../consts";

/**
 * 自定义气泡组件api
 */
export default class IPopup {
  constructor(viewer, scene) {
    this.viewer = viewer;
    this.scene = scene;
    this.popups = [];
    this.fullScreenFlag = false;
    // 初始化全局容器
    this.container = $(document.getElementById("popupContainer"));
    // 初始化即绑定事件
    this.changePosition(scene);
  }

  /**
   * 显示气泡
   * @param {Object} obj 初始化气泡的参数
   */
  show(obj) {
    this.closeAllPopup();
    // 此处省略了许多参数的配置，如有其他参数，请配置于此，并在init中覆写
    const config = {
      cartesian3: obj.cartesian3,
      width: obj.width || 960,
      height: obj.height || 540,
      offsets: obj.offsets || [480, 560],
      html: obj.html || "hello world",
      class: obj.class || "myPopup",
      onclose: obj.onclose
    };
    this.init(config);
  }

  /**
   * 遮罩层
   */
  maskPopup(obj){
    const config = {
      cartesian3: obj.cartesian3,
      // offsets:[100,500],
      html: obj.html || "hello world",
      class: obj.class || "myPopup",
      onclose: obj.onclose
    };
    // 将笛卡尔坐标转换为屏幕坐标
    const position = Cesium.SceneTransforms.wgs84ToWindowCoordinates(
      this.scene,
      config.cartesian3,
      Cesium.Cartesian2()
    );
    const x = parseInt(position.x);
    const y = parseInt(position.y);
    // 初始化popup主体
    const popup = $(
      `<div style="width: 100%;height: 100%;left:0;top:0" class='${config.class}'><div class='dialogClose'>×</div>${config.html}</div>'`
    );
    // 保存至集合
    this.popups.push({
      popupInstance: popup,
      config: config
    });
    // 添加至html容器
    $(document.getElementById("popupContainer")).append(popup);
    // 初始化屏幕位置
    popup.css({
     /* left: x - config.offsets[0] + "px",
      top: y - config.offsets[1] + "px"*/


    });
    // 点击查看全景大屏
    $(".fullScreen").click(function(){
      console.log('查看全品');
    })
    // 绑定关闭事件
    $(".dialogClose").click(function () {
      $(this)
        .parent()
        .remove();
      if (config.onclose) {
        config.onclose();
      }
    });
    // 显示父容器
    this.container.show();
  }

  /**
   * 初始化气泡
   */
  init(config) {
    let _this = this;
    // 将笛卡尔坐标转换为屏幕坐标
    const position = Cesium.SceneTransforms.wgs84ToWindowCoordinates(
      this.scene,
      config.cartesian3,
      Cesium.Cartesian2()
    );
    const x = parseInt(position.x);
    const y = parseInt(position.y);
    // 初始化popup主体
    const popup = $(
       `<div style="width: ${config.width}px;height: ${config.height}px" class='${config.class}'><div class='popupClose'>关闭</div><div class='dialogClose'>×</div>${config.html}</div>'`
      //`<div style="width: ${config.width}px;" class='${config.class}'><div class='popupClose'>关闭</div><div class='dialogClose'>×</div>${config.html}</div>'`
    );
    // 保存至集合
    this.popups.push({
      popupInstance: popup,
      config: config
    });
    // 添加至html容器
    $(document.getElementById("popupContainer")).append(popup);
    // 初始化屏幕位置
    popup.css({
      left: x - config.offsets[0] + "px",
      top: y - config.offsets[1] + "px"
    });
    // 绑定关闭事件
    $(".popupClose").click(function () {
      $(this)
        .parent()
        .remove();
      if (config.onclose) {
        config.onclose();
      }
    });

    /* 全景全屏 */
    let panorama = $(document.querySelector(".panorama"))[0];
    let documentDiv = $(document)[0];
    $("#fullScreen").click(function(){
      this.fullScreenFlag = !this.fullScreenFlag;
      if(this.fullScreenFlag){
        if(panorama.requestFullscreen){
          panorama.requestFullscreen();
        }
        else if(panorama.mozRequestFullScreen) {
          panorama.mozRequestFullScreen();
        }
        else if(panorama.webkitRequestFullscreen) {
          panorama.webkitRequestFullscreen();
        }
        else if(panorama.msRequestFullscreen) {
          panorama.msRequestFullscreen();
        }
      }else{
        if(documentDiv.exitFullscreen) {
          documentDiv.exitFullscreen();
        } else if(documentDiv.mozCancelFullScreen) {
          documentDiv.mozCancelFullScreen();
        } else if(documentDiv.webkitExitFullscreen) {
          documentDiv.webkitExitFullscreen();
        }
      }

    })

    $(".dialogClose").click(function () {
      $(this)
        .parent()
        .remove();
      if (config.onclose) {
        config.onclose();
      }
    });
    // 显示父容器
    this.container.show();
  }

  // 默认气泡弹出框
  defaultPopup(){
    const popup = {
      width:410,
      height: 362,
      html: `<div id="bubble" class="contentBody">
                    <div class="header"><b></b><span id="photo_title"></span></div>
                    <div style="overflow-y:auto;" id="tableContainer">
                        <div id="tab" style="width:100%"></div>
                    </div>
                </div>`,
      offsets: [200, 360]
    }
    return popup;
  }

  /**
   *  多张照片轮播相关
   *  */
  // 多张照片气泡弹出框
  imagesPopup(imageTitle){
    const popup = {
      html: `<div id="bubble" class="contentBody">
                    <div class="header"><b></b><span id="photo_title">${imageTitle}</span></div>
                    <div style="overflow-y:auto;height:305px" id="tableContainer" >
                        <div class="imgList">
                            <div class="centerWrapper" id="centerWrapper">

                            </div>
                            <!--箭头-->
                           <div class="leftButton arrow" id="leftArrow"></div>
                           <div class="rightButton arrow" id="rightArrow"></div>
                           <!--前进后退按钮-->
                           <div class="backMove arrow" id="backMove"></div>
                           <div class="goMove arrow" id="goMove"></div>
                        </div>
                    </div>
               </div>`,
    }

    return popup;
  }

  // 耕地“非农” 多张照片气泡弹出框  res == result
  imagesNonframPopup(title0, title1, patchNo) {
    const popup = {
      html: `<div id="bubble" class="contentBody">
                    <div class="header"><b></b><span id="photo_title">耕地“非农化”照片</span></div>
                    <div style="overflow-y:auto;" id="tableContainer" >
                    <p style="margin: 10px 0 0 0;font-size: 18px;">图斑号： ${patchNo}</p>
                        <div class="nonFarmImageList" id="FarmImageContent">
                           <div class="imgBorderItem">
                                <p>${title0}</p>
                                <div id="fieldImg"></div>
                            </div>
                             <div class="imgBorderItem">
                                <p>${title1}</p>
                                <div id="imagePatch"></div>
                            </div>
                        </div>
                    </div>
               </div>`,
    }
    return popup;
  }

  // 卫片执法 多张照片气泡弹出框
  imagesSatellitePopup(title, patchNo) {
    const popup = {
      html: `<div id="bubble" class="contentBody">
                    <div class="header"><b></b><span id="photo_title">${title}</span></div>
                    <div style="overflow-y:auto;" id="tableContainer" >
                    <p style="margin: 10px 0 0 0;font-size: 18px;">图斑号： ${patchNo}</p>
                    <div class="images-satellite-list" id="imgSatelliteList">

                    </div>
                    </div>
               </div>`,
    }
    return popup;
  }

  /**
   * 拖动场景时，改变气泡位置随场景一起移动
   */
  changePosition(scene) {
    scene.postRender.addEventListener(() => {
      // var cartesian=viewer.camera.pickEllipsoid(movement.endPosition, ellipsoid);
      // if(cartesian){
      //   var cartographic=viewer.scene.globe.ellipsoid.cartesianToCartographic(cartesian);
      //   //将地图坐标（弧度）转为十进制的度数
      //   var lat_String=Cesium.Math.toDegrees(cartographic.latitude).toFixed(4); //经
      //   var log_String=Cesium.Math.toDegrees(cartographic.longitude).toFixed(4);//纬
      //   var alti_String=(viewer.camera.positionCartographic.height/1000).toFixed(2);//高
      //
      // }
      // 遍历更新位置
      this.popups.map(p => {
        // debugger
        // var canvasHeight = scene.canvas.height;
        // var windowPosition = new Cesium.Cartesian2();
        // Cesium.SceneTransforms.wgs84ToWindowCoordinates(scene, p.config.cartesian3, windowPosition);
        // const bottom = (canvasHeight - windowPosition.y - 120) + 'px';;
        // const left = (windowPosition.x - 70) + 'px';
        // console.log(left, bottom)
        // p.popupInstance.css({
        //   bottom: `${bottom}px`,
        //   left: `${left}px`,
        //   display: "block"
        // });

        const position = Cesium.SceneTransforms.wgs84ToWindowCoordinates(
          scene,
          p.config.cartesian3
        );
        if (position && position.x) {
          const left = Math.round(position.x) - p.config.offsets[0];
          const top = Math.round(position.y) - p.config.offsets[1];
          p.popupInstance.css({
            left: `${left}px`,
            top: `${top}px`,
            display: "block"
          });
        }
      });
    });
  }

  /**
   * 关闭气泡的方法
   */
  closeAllPopup() {
    // 遍历移除
    this.popups.map(p => {
      p.popupInstance.remove();
    });
    // 清空集合
    this.popups = [];
    this.container.hide();
  }
}
