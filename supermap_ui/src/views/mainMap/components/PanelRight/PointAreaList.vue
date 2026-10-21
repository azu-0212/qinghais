<template>
  <div class="content pointWrapper">
    <div
      v-for="(point, index) in pointArea"
      :key="index * 1.3"
      class="pointBox"
      :class="{ active:choose === index }"
      @mouseover="point.active = true"
      @mouseout="point.active = false"
      @click="pointClick(point,index)"
    >
      <img :src="host + point.imgPath" alt="" />
      <p>{{ point.imgName }}</p>
    </div>
  </div>
</template>

<script>
import { listAreas } from "@/api/system/areas";
import MapUtils from "../../../../utils/supermap";
import MapUtil from "@/utils/supermap";
import { getResource } from "@/api/system/resource"
import {getVideo} from "@/api/system/video";

export default {
  name: "PointAreaList",
  data() {
    return {
      pointArea: [],
      choose:null,
      host: "",
      marker: [
        require("../../../../assets/image/mapMarker/pos_1.png"),
        require("../../../../assets/image/mapMarker/pos_4.png"),
      ]
    };
  },
  mounted() {
    listAreas().then((res) => {
      this.pointArea = res.rows;
    });
  },
  created() {
    this.getConfigKey("sys.host").then((response) => {
      this.host = response.msg;
    });

  },
  methods: {
    pointClick(p,index) {
      let _this = this;
      this.choose = index;
      let pos = JSON.parse(p.position);
      // 清除所有实体
      MapUtil.SceneUtil.clearEntity();
      //清除选中的所有影像图层
      MapUtil.SceneUtil.removeImageLayersAll();
      // 关闭所有弹窗
      MapUtil.SceneUtil.popup.closeAllPopup();
      // 清除点击场景类型：重点区域+全景展示
      MapUtil.SceneUtil.getPositionType = "";
      // 定位区域
      MapUtils.SceneUtil.flyToView([
        parseFloat(pos.lon),
        parseFloat(pos.lat),
        parseFloat(pos.alt),
      ]);
      // 加载矢量数据
      MapUtils.SceneUtil.addImageLayerByUrl(
        p.serverUrl,
        p.serverUrl,
        true
      );
      setTimeout(function(){
        // 加载弹出框
        MapUtils.SceneUtil.showDialogPopup(p,{
          width:796,
          height:560,
          class: "dialogPopup",
          offsets:[450, 260],
          html:`<div class="contentBody">
                <div class="header"><b></b><span id="header_title"></span></div>
                <div class="content_text">
                     <div id="content"></div>
                </div>
              </div>`
        });
      },3000)
      // 查询全景数据
      let panorama = p.resourceId;
      if(panorama!==null && panorama.length>0){
        // 点击场景类型：重点区域+全景展示
        MapUtil.SceneUtil.getAreaPanorama = "areaPanoramaOpen";
        let panoramasArr = panorama.split(",");
        let panoramasList = [];
        setTimeout(function(){
          for(let i=0;i<panoramasArr.length;i++){
            if(panoramasArr[i]!=='NaN'){
              getResource(parseInt(panoramasArr[i])).then(response=>{
                panoramasList.push(response.data);
                MapUtil.SceneUtil.clearEntity();
                MapUtil.SceneUtil.addAreaMarkersToMap(panoramasList, _this.marker[0])
              })
            }
          }
        },3000)
      }

      // 查询视频数据
      let video = p.videoId;
      if(video!==null){
        // 点击场景类型：重点区域+视频展示
        MapUtil.SceneUtil.getAreaVideo = "areaVideo";
        let videoList = [];
        setTimeout(function(){
          getVideo(video).then(response=>{
            videoList.push(response.data);
            MapUtil.SceneUtil.addAreaMarkersToMap(videoList, _this.marker[1])
          })
        },3200)
      }
    },
  },
};
</script>

<style scoped>
.pointWrapper {
  display: flex;
  flex-wrap: wrap;
}

.pointBox {
  position: relative;
  margin: 10px 0 0 34px;
  width: 100px;
  height: 100px;
  color: white;
  border: 1px solid transparent;
  border-radius: 5px;
  overflow: hidden;
}

.pointBox img {
  width: 100%;
  height: 100%;
}

.pointBox p {
  position: absolute;
  text-align: center;
  height: 30px;
  width: 100px;
  bottom: -16px;
  line-height: 30px;
  background: rgba(0, 0, 0, 0.8);
  pointer-events: none;
}

.pointBox.active,
.pointBox:hover{
  border: 1px solid rgb(24, 247, 248);
  color: rgb(24, 247, 248);
}
.pointBox img:hover {
  transform: scale(1.1);
}
</style>
