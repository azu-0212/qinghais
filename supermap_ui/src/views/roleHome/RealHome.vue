<template>
  <div @contextmenu.prevent="removeDefault">
    <!-- 实景三位青海首页 -->
    <div class="mainPage" v-if="mapShowBool">
      <MapHeader :titleStyle="style"/>
      <!--进入系统按钮-->
      <div :class="['sysButton',{'active':sysButton.active}]"
           @click="toMap"
           @mouseover="sysButton.hover=true"
           @mouseout="sysButton.hover=false"
      >
        <img v-if="sysButton.active||sysButton.hover" :src="sysButton.icon_active" alt="">
        <img v-else :src="sysButton.icon" alt="">
        <span>{{sysButton.name}}</span>
      </div>
      <!--   左侧面板   -->
      <screenLeft/>
      <!--   右侧面板   -->
      <screen-right/>
      <!--   底部面板   -->
      <screen-bottom/>
      <!--基本信息-->
      <basicInfo/>
      <!--主办信息-->
      <hostInfo/>
    </div>
  </div>
</template>

<script>
import MapHeader from "../../layout/mapLayout/components/MapHeader";
import screenLeft from "./components/realHome/panelLeft";
import screenBottom from "./components/realHome/panelBottom";
import screenRight from "./components/realHome/panelRight";
import MapUtil from "@/utils/supermap";
import hostInfo from "@/views/mainMap/components/hostInfo";
import basicInfo from "@/views/mainHome/components/basicInfo";
import {forceLogout, list} from "@/api/monitor/online";
export default {
  name: "",
  data(){
    return{
      style: 'screen',
      sysButton: {
        name: '数据中心',
        icon: require('@/assets/image/mapBtns/sysToggleBtn.png'),
        icon_active: require('@/assets/image/mapBtns/sysToggleBtn_active.png'),
        active: false,
        hover: false
      },
      onlineList: []
    }
  },
  components: {
    MapHeader,
    screenLeft,
    screenBottom,
    screenRight,
    hostInfo,
    basicInfo
  },
  methods:{
    getOnlineList(){
      list().then(response => {
        this.onlineList = response.rows
      })
    },
    // 去除右击默认事件
    removeDefault(){},
    toMap(){
      this.$router.push({ path: '/home/index' });
      MapUtil.SceneUtil.popup.closeAllPopup();
      MapUtil.SceneUtil.clearEntity();
    }
  },
  created() {
    let _this = this;
    this.getOnlineList();
    let expireTime = 1000 * 60; // 1分钟
    // 定时任务，清空缓存用户
    setTimeout(function(){
      _this.onlineList.filter(((loginUser,index)=>{
        if(index>0){
          forceLogout(loginUser.tokenId);
        }
      }))
    },expireTime)
  },
  computed: {
    mapShowBool() {
      return this.$store.getters.mapShowBool;
    },
  },
}
</script>

<style scoped>
.mainPage {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 999;
  user-select: none;
}
.sysButton{
  display: flex;
  color: white;
  position: absolute;
  top:15px;
  right:10px;
  width: 120px;
  height: 35px;
  background: rgba(1, 1, 20, 0.6);
  box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
  border-radius: 5px;
  pointer-events: auto;
  cursor: pointer;
}
.sysButton:hover,
.sysButton.active {
  color: #1ce5e6;
}
.toolsBtn img, .sysButton img ,.screenButton img {
  margin: 10px 10px 10px 15px;
}
.sysButton span {
  line-height: 35px;
  font-size: 14px;
  font-weight: bolder;
}
</style>
