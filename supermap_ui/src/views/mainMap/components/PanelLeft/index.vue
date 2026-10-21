<template>
  <div class="panelWrapper">
    <div class="buttonsWrapper">
      <div
        v-for="(btn, index) in buttons"
        :key="index * 1.1"
        class="buttons"
        :class="{ active: btn.choose }"
        @mouseover="btn.active = true"
        @mouseout="btn.active = false"
        @click="btnClick(btn, index)"
      >
        <img v-if="btn.choose || btn.active" :src="btn.icon_active" />
        <img v-if="!(btn.choose || btn.active)" :src="btn.icon" />
        <p>{{ btn.name }}</p>
      </div>
    </div>
    <transition name="el-fade-in-linear">
      <div class="panelContent" v-show="panelShow">
        <hr class="regionSplit" />
        <div class="pTitle">
          {{button.name}}
<!--          {{
            buttons.filter((item) => {
              return item.choose;
            })[0].title
          }}-->
          <span
            class="closeBtn el-icon-close"
            @click="panelShow = false"
          ></span>
        </div>
        <!--场景数据弹框-->
        <sceneDataTree v-show="buttons[0].choose"/>
        <!--三维模型弹框-->
        <Model3DTree v-show="buttons[1].choose" v-if="dataFlag" :Model3DList="Model3DList"/>
        <!--全景数据弹框-->
        <PanoramaDataTree v-show="buttons[2].choose" v-if="dataFlag" :panoramaList="panoramaList"/>
        <!--街景数据弹框-->
        <StreetDataTree v-show="buttons[3].choose" v-if="dataFlag" :streetList="streetList"/>
        <!--点云数据弹框-->
        <CloudDataTree v-show="buttons[4].choose"/>
        <!--视频-->
        <VideoPanel v-show="buttons[5].choose"/>
        <!--图片-->
        <ImageData v-show="buttons[6].choose" :type="buttons[6].typeCode"/>
        <div
          class="trianglePointer"
          :style="`transform:translateY(${selBtnIndex * 80}px)`"
        ></div>
      </div>
    </transition>
  </div>
</template>

<script>
  import sceneDataTree from "@/views/mainMap/components/PanelLeft/SceneDataTree";
  import Model3DTree from "@/views/mainMap/components/PanelLeft/Model3DTree"
  import PanoramaDataTree from "@/views/mainMap/components/PanelLeft/PanoramaDataTree";
  import StreetDataTree from "@/views/mainMap/components/PanelLeft/StreetDataTree";
  import CloudDataTree from "@/views/mainMap/components/PanelLeft/CloudDataTree";
  import VideoPanel from "@/views/mainMap/components/PanelLeft/VideoPanel";
  import sceneUtil from "@/utils/supermap/map3D";
  import MapUtil from "@/utils/supermap";
  import ImageData from "@/views/mainMap/components/PanelLeft/imageDataTree";
  import {getResourceByDataType} from "@/api/system/resource";
  import {listVideo} from "@/api/system/video";
  import bus from "@/utils/bus";

  export default {
    name: "PanelLeft",
    components: {
      PanoramaDataTree,
      sceneDataTree,
      Model3DTree,
      StreetDataTree,
      CloudDataTree,
      VideoPanel,
      ImageData
    },
    data() {
      return {
        dataFlag: false,
        panelShow: false,
        flag:false,
        flagPhoto:false,
        flagStreet:false,
        flagVideo: false,
        button:{
          name:"[无内容]"
        },
        buttons: [
          {
            name: "地形级三维",
            typeCode:6,
            icon: require("@/assets/image/dataCatalog/3Dscene.png"),
            icon_active: require("@/assets/image/dataCatalog/3Dscene_active.png"),
            active: false,
            choose: false,
            title: "[地形级三维]",
            dataType:[]
          },
          {
            name: "城市级三维",
            typeCode:1,
            icon: require("@/assets/image/dataCatalog/3Dmodule.png"),
            icon_active: require("@/assets/image/dataCatalog/3Dmodule_active.png"),
            active: false,
            choose: false,
            title: "[城市级三维]",
            dataType:1
          },
          {
            name: "全景数据",
            typeCode:2,
            icon: require("@/assets/image/dataCatalog/viewIcon.png"),
            icon_active: require("@/assets/image/dataCatalog/viewIcon_active.png"),
            active: false,
            choose: false,
            title: "[全景]",
            dataType:3
          },
          {
            name: "街景数据",
            typeCode:5,
            icon: require("@/assets/image/dataCatalog/streetIcon.png"),
            icon_active: require("@/assets/image/dataCatalog/streetIcon_active.png"),
            active: false,
            choose: false,
            title: "[街景数据]",
            dataType:5
          },
          {
            name: "精细三维",
            typeCode: 7,
            icon: require("@/assets/image/dataCatalog/btnIcon.png"),
            icon_active: require("@/assets/image/dataCatalog/btnIcon_active.png"),
            active: false,
            choose: false,
            title: "[精细三维]",
          },
          {
            name: "视频",
            icon: require("@/assets/image/dataCatalog/videosIcon.png"),
            icon_active: require("@/assets/image/dataCatalog/videosIcon_active.png"),
            active: false,
            choose: false,
            title: "[视频]",
          },
          {
            name: "照片库",
            typeCode: 9,
            icon: require("@/assets/image/dataCatalog/phototsIcon.png"),
            icon_active: require("@/assets/image/dataCatalog/phototsIcon_active.png"),
            active: false,
            choose: false,
            title: "[照片库]",
          },
          {
            name: "物联网数据",
            icon: require("@/assets/image/dataCatalog/InternetsIcon.png"),
            icon_active: require("@/assets/image/dataCatalog/InternetsIcon_active.png"),
            active: false,
            choose: false,
            title: "[物联网数据]",
          },
          /*{
            name: "不动产",
            // name: "不动产专题",
            icon: require("@/assets/image/analysis/bdc.png"),
            icon_active: require("@/assets/image/analysis/bdc_active.png"),
            active: false,
            choose: false,
            title: "不动产专题",
          },*/
        ],
        selBtnIndex: 0,
        // 获取数据类型
        dataTypeOptions:[],
        panoramaList:[],
        Model3DList:[],
        streetList:[],
        videoList: []
      };
    },
    created(){
      this.getDicts('sys_layer_type').then(response => {
        this.dataTypeOptions = response.data;
      });
      getResourceByDataType(this.buttons[1].dataType).then(response => {
        this.Model3DList = response.rows;
        this.dataFlag = true;
      });
      getResourceByDataType(this.buttons[2].dataType).then(response => {
        this.panoramaList = response.rows;
        this.dataFlag = true;
      });
      getResourceByDataType(this.buttons[3].dataType).then(response => {
        this.streetList = response.rows;
        this.dataFlag = true;
      });
      listVideo().then(response => {
        this.videoList = response.rows;
      })
      this.getQuery();

    },
    mounted() {
      bus.$on('closeLeftPanel', this.closeLeftPanel)
    },
    methods: {
      btnClick(btn, index) {
        this.selBtnIndex = index;
        this.buttons.map((item) => {
          item.choose = false;
          item.active = false;
        });
        this.button.name = btn.title;
        btn.choose = true;
        this.panelShow = true;

        //清楚所有照片库实体图标等信息
        MapUtil.SceneUtil.destroyDynamicCluser();
        //清楚所有图标实体
        MapUtil.SceneUtil.clearEntity();
        // 清楚选中的所有MVT矢量瓦片
        MapUtil.SceneUtil.removeMVTLayersAll();
        //清除选中的所有影像图层
        MapUtil.SceneUtil.removeImageLayersAll();
        //清楚测量工具
         MapUtil.SceneUtil.measure3DUtil.deActiveAllMeasure3DControl();
        //清除选中的所有倾斜摄影
         MapUtil.SceneUtil.removeScpLayersAll();
        // 清除高亮
        MapUtil.SceneUtil.clearHighLightAll();
        //清除照片类型坐标
        MapUtil.SceneUtil.getPositionType = "";
        //清楚所有照片库实体图标等信息
        MapUtil.SceneUtil.destroyDynamicCluser();
        //清楚弹出框
        sceneUtil.popup.closeAllPopup();
        // 全幅青海
        //sceneUtil.entireView();
        // 隐藏右侧面板
        bus.$emit('closeRightPanel')
        // 取消已选中的结构树
        bus.$emit('clearChecked')
        // 清除兴趣点输入框内容
        bus.$emit('removeRegionSearch')

        if (btn.name !=null && btn.name.indexOf('全景数据') !== -1) {
          this.flag = !this.flag;
          let markerIcon = require("@/assets/image/mapMarker/pos_1.png");
          if (this.panoramaList && this.panoramaList.length > 0) {
            // MapUtil.SceneUtil.add3DMarkersToMap(this.panoramaList, markerIcon, this.flag); //没有勾选项
            return;
          } else {
            //item.active = false
            this.$alert("暂无点位,请通过后台导入!", '提示信息')
          }
        }

        if ( btn.name !=null && btn.name.indexOf('三维模型') !== -1) {
          this.flagPhoto = !this.flagPhoto;
          let markerIcon = require("@/assets/image/mapMarker/pos_0.png");
          if (this.Model3DList && this.Model3DList.length > 0) {
            //MapUtil.SceneUtil.add3DMarkersToMap(this.Model3DList, markerIcon, this.flagPhoto); //没有勾选项
            return;
          } else {
            this.$alert("暂无点位,请通过后台导入!", '提示信息')
          }
        }

        if ( btn.name !=null && btn.name.indexOf('街景数据') !== -1) {
          this.flagStreet = !this.flagStreet;
          let markerIcon = require("@/assets/image/mapMarker/pos_2.png");
          if (this.streetList && this.streetList.length > 0) {
            //MapUtil.SceneUtil.add3DMarkersToMap(this.streetList, markerIcon, this.flagStreet); //没有勾选项
            return;
          } else {
            this.$alert("暂无点位,请通过后台导入!", '提示信息')
          }
        }

        if ( btn.name !=null && btn.name.indexOf('视频') !== -1) {
          this.flagVideo = !this.flagVideo;
          let markerIcon = require("@/assets/image/mapMarker/pos_4.png");
          if (this.videoList && this.videoList.length > 0) {
            MapUtil.SceneUtil.add3DMarkersToMap(this.videoList, markerIcon, this.flagVideo); //没有勾选项
            return;
          } else {
            this.$alert("暂无点位,请通过后台导入!", '提示信息')
          }
        }
        if ( btn.name !=null && btn.name.indexOf('照片库') !== -1) {
          // 全幅青海
          sceneUtil.entireView();
        }
      },
      getQuery(){
        this.buttons.map((item) => {
          item.choose = false;
          item.active = false;
        });

        if(this.$route.query.type && this.$route.query.type.indexOf('video') !==-1){
          this.buttons[5].choose = true;
          this.buttons[5].active = true;
          this.panelShow = true;
          this.button.name = "视频";
          return;
        }

        if(this.$route.query.type  && this.$route.query.type.indexOf('panorama') !==-1){
          this.buttons[2].choose = true;
          this.buttons[2].active = true;
          this.panelShow = true;
          this.button.name = "全景";
          return;
        }

         if(this.$route.query.type  && this.$route.query.type.indexOf('photography') !==-1){
              this.buttons[1].choose = true;
              this.buttons[1].active = true;
              this.button.name = "三维模型";
              this.panelShow = true;
              return;
          }

         if(this.$route.query.type && this.$route.query.type.indexOf('street') !==-1){
          this.buttons[3].choose = true;
          this.buttons[3].active = true;
          this.panelShow = true;
          this.button.name = "街景数据";
          return;
        }

        if(this.$route.query.type && this.$route.query.type.indexOf('image') !==-1){
          this.buttons[6].choose = true;
          this.buttons[6].active = true;
          this.panelShow = true;
          this.button.name = "照片库";
          return;
        }
        else{
          this.panelShow = false;
        }
      },
      closeLeftPanel(){
        this.panelShow = false;
        this.buttons.map((item) => {
          item.choose = false;
          item.active = false;
        });
      }
    },
  };
</script>

<style scoped>
  .panelWrapper {
    position: absolute;
    top: 0;
    left: 20px;
    width: 396px;
    height: 100%;
    cursor: pointer;
    pointer-events: none;
  }

  @keyframes moveIn {
    from {
      right: -300px;
    }
    to {
      right: 0;
    }
  }

  @keyframes moveOut /* Safari 与 Chrome */ {
    from {
      right: 0;
    }
    to {
      right: -300px;
    }
  }

  .panelIn {
    animation: moveIn 0.4s ease forwards;
  }

  .panelOut {
    animation: moveOut 0.4s ease forwards;
  }

  .buttonsWrapper {
    margin-top: 70px;
    float: left;
    width: 76px;
    height: 100%;
  }

  .panelContent {
    float: left;
    width: 300px;
    margin-top: 100px;
    height: calc(100% - 110px);
    background: rgba(1, 1, 20, 0.6);
    box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
    pointer-events: auto;
  }
  .pTitle {
    position: relative;
    height: 40px;
    color: white;
    font-size: 16px;
    text-align: center;
    line-height: 40px;
    font-weight: bolder;
    letter-spacing: 2px;
  }

  .regionSplit {
    margin: 0;
    padding: 0;
    background: rgb(24, 247, 248);
    border: 0;
    height: 3px;
  }

  /*.pTitle:hover .closeBtn {
    opacity: 1;
  }*/

  .closeBtn {
    position: absolute;
    right: 0;
    top: 0;
    display: block;
    width: 40px;
    height: 40px;
    text-align: center;
    line-height: 40px;
    font-size: 26px;
    color: #18f7f8;
    opacity: 1;
  }

  .buttons {
    margin: 12px 0 0 2px;
    width: 60px;
    height: 60px;
    color: white;
    border-radius: 10px;
    background: rgba(1, 1, 20, 0.6);
    box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
    pointer-events: auto;
  }

  .buttons:nth-child(1) {
    margin-top: 30px;
  }
  .buttons:hover,
  .buttons.active,
  .buttons.choose {
    color: rgb(24, 247, 248);
  }

  .buttons img {
    margin: 7px 18px 5px 18px;
    width: 24px;
  }

  .buttons p {
    margin: 0;
    text-align: center;
    font-size: 10px;
  }

  /*.trianglePointer {
    position: absolute;
    right: 80px;
    top: 45px;
    transform: translate(50, 0);
    border-width: 10px;
    border-style: solid;
    border-color: transparent transparent transparent rgba(1, 1, 20, 0.6);
  }*/
</style>
