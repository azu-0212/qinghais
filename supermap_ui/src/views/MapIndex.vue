<template>
  <div @contextmenu.prevent="removeDefault">
    <!-- 系统首页 -->
    <div class="mainPage" v-if="mapShowBool">
      <!-- 大屏标题 -->
      <div class="mapHeader">
        <img class="mapLogo" src="@/assets/logo/qinghai_logo.png" alt="">
        <div class="mapTitle">
          <!--大屏页标题-->
          <div class="screenTitle">
            <p class="title_ch" :style="appCfg.headerPage.style_ch||style.ch">{{screenTitle.title}}</p>
            <p class="title_en" :style="appCfg.headerPage.style_en||style.en">{{screenTitle.title_en}}</p>
          </div>
        </div>
      </div>
      <div class="bgPng" />
      <!--左侧菜单-->
      <PanelLeft/>
      <!--区域选择-->
      <RegionSelect />
      <!-- 地区搜索 -->
      <RegionSearch/>
      <!--右侧菜单-->
      <PanelRight />
      <!--主办信息-->
      <hostInfo/>
      <!--后台切换按钮-->
      <div v-if="sysButton.show" :class="['sysButton',{'active':sysButton.active}]"
           @click="toSys"
           @mouseover="sysButton.hover=true"
           @mouseout="sysButton.hover=false"
      >
        <img v-if="sysButton.active||sysButton.hover" :src="sysButton.icon_active" alt="">
        <img v-else :src="sysButton.icon" alt="">
        <span>{{sysButton.name}}</span>
      </div>
      <!--返回大屏按钮-->
      <div v-if="screenButton.show" :class="['screenButton',{'active':screenButton.active}]"
           @click="toScreen"
           @mouseover="screenButton.hover=true"
           @mouseout="screenButton.hover=false"
           ref="screenButton"
      >
        <img v-if="screenButton.active||screenButton.hover" :src="screenButton.icon_active" alt="">
        <img v-else :src="screenButton.icon" alt="">
        <span>{{screenButton.name}}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { keepAlive } from "@/api/tool/utils";
import PanelRight from "./mainMap/components/PanelRight/index";
import PanelLeft from "./mainMap/components/PanelLeft/index";
import DataStatistics from "./mainMap/components/DataStatistics";
import RegionSelect from "./mainMap/components/RegionSelect";
import RegionSearch from "./mainMap/components/RegionSearch";
import Panorama from "./mainMap/components/Panorama";
import MapHeader from "../layout/mapLayout/components/MapHeader";
import MapTools from "../layout/mapLayout/components/MapTools";
import hostInfo from "./mainMap/components/hostInfo";
import MapUtils from "@/utils/supermap";
import MapUtil from "@/utils/supermap";
import {mapState} from "vuex";

export default {
  name: "MainMap",
  components: {
    Panorama,
    RegionSelect,
    PanelRight,
    DataStatistics,
    MapHeader,
    MapTools,
    PanelLeft,
    hostInfo,
    RegionSearch
  },
  data() {
    return {
      sysButton: {
        name: '进入后台',
        icon: require('@/assets/image/mapBtns/sysToggleBtn.png'),
        icon_active: require('@/assets/image/mapBtns/sysToggleBtn_active.png'),
        active: false,
        hover: false,
        show: false
      },
      screenButton: {
        name: '返回大屏',
        icon: require('@/assets/image/mapBtns/screenBtn.png'),
        icon_active: require('@/assets/image/mapBtns/screenBtn_active.png'),
        active: false,
        hover: false,
        show: true
      },
      liveInterval: 10 * 60 * 1000,
      userRole:this.$store.state.user.roles,
      screenTitle: {},
    };
  },
  mounted() {
    /* 管理员权限 */
    if(this.userRole[0].indexOf("admin")!==-1){
      this.sysButton.show = true;
      this.screenTitle = this.appCfg.screenTitles[0];
      return;
    }else{
      this.$refs['screenButton'].style.right = '28px';
      /* 分权限换标题 */
      this.appCfg.screenTitles.filter((item,index) => {
        if(item.name.indexOf(this.userRole[0])!==-1){
          this.screenTitle = this.appCfg.screenTitles[index];
        }
      })
    }
    this.keepAliveCircle();
  },
  methods: {
    // 去除右击默认事件
    removeDefault(){},
    keepAliveCircle() {
      keepAlive()
        .then()
        .catch((e) => {
          console.log("后台访问失败！msg=>", e);
        });
      setTimeout(() => {
        this.keepAliveCircle();
      }, this.liveInterval);
    },
    toSys(){
      this.$router.push({ path: '/sys/index' });
      MapUtil.SceneUtil.popup.closeAllPopup();
      MapUtil.SceneUtil.clearEntity();
    },
    toScreen(){
      // 根据不同权限展示不同按钮
      // 判断当前用户是否已拉取完user_info信息
      /* 自然资源权限 */
      if(this.userRole[0].indexOf("natural")!==-1){
        this.$router.push({ path: '/natural/home/index' });
      }else
      if(this.userRole[0].indexOf("real")!==-1){/* 实景三维权限 */
        this.$router.push({ path: '/real/home/index' });
      }else
      if(this.userRole[0].indexOf("dimensionalSpace")!==-1){ /* 三维立体时空数据库权限 */
        this.$router.push({ path: '/dimensionalSpace/home/index' });
      }else{
        this.$router.push({ path: '/index' });
      }

      //this.$router.push({ path: '/index' });
      MapUtil.SceneUtil.popup.closeAllPopup();
      MapUtil.SceneUtil.clearEntity();
      //全幅青海
      MapUtil.SceneUtil.entireView();
      //清楚所有照片库实体图标等信息
      MapUtil.SceneUtil.destroyDynamicCluser();
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
    },
  },
  computed: {
    ...mapState({
      appCfg: state => state.app.appCfg
    }),
    panoramaShow() {
      return this.$store.getters.panorama_show;
    },
    mapShowBool() {
      return this.$store.getters.mapShowBool;
    },
  },
};
</script>

<style scoped>
.mapHeader {
  position: absolute;
  display: inline;
  top: 18px;
  left: 24px;
  text-align: left;
  color: white;
  border-radius: 3px;
  z-index: 100;
  /* pointer-events: none;*/
}
.mapLogo{
  width:47px;
}
.mapLogo, .mapTitle{
  float:left;
  margin-right:5px;
}
.title_ch {
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}
.title_en {
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}
.mainPage {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 999;
  user-select: none;
}
.bgPng {
  width: 100%;
  height: 100%;
  background: url(../assets/image/mainPage/mainBg.png);
  opacity: 0.5;
}
.sysButton, .screenButton {
  display: flex;
  color: white;
  position: absolute;
  right:26px;
  top:34px;
  width: 120px;
  height: 35px;
  background: rgba(1, 1, 20, 0.6);
  box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
  border-radius: 5px;
  pointer-events: auto;
  cursor: pointer;
}
.screenButton{
  right:158px;
}

.toolsBtn:hover, .sysButton:hover, .screenButton:hover,
.toolsBtn.active, .sysButton.active {
  color: #1ce5e6;
}
.toolsBtn img, .sysButton img ,.screenButton img {
  margin: 10px 10px 10px 15px;
}

.sysButton span ,.screenButton span {
  line-height: 35px;
  font-size: 14px;
  font-weight: bolder;
}
.changeModel {
  position: absolute;
  width: 2.5%;
  height: 2.5%;
  top: 5%;
  left: 27%;
  pointer-events: auto;
  cursor: pointer;
  z-index: 999;
}
.changeModelData {
  background: url(../assets/image/mainPage/arrows_w_active.png);
  background-size: 100% 100%;
}
.changeModelScreen {
  background: url(../assets/image/mainPage/arrows_e_active.png);
  background-size: 100% 100%;
}
.jumpToSys {
  position: absolute;
  left: 500px;
  top: 60px;
}

.MapTools {
  position: absolute;
  bottom: 20px;
  left: 20px;
  z-index: 999;
  pointer-events: auto;
}
</style>
