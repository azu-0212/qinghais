<template>
  <!-- 切换大屏按钮 -->
  <div>
    <div class="screenBtn"
         @click="dropScreenClick">
      <img
        v-if="screenBtn.hover || screenBtn.active"
        :class="['panelsBtn',{'img-active':screenBtn.active}]"
        :src="screenBtn.icon" alt="">
      <img class="panelsBtn" v-else :src="screenBtn.icon" alt="">
    </div>
    <transition name="el-fade-in-linear">
      <div v-show="showScreenContent" class="dropContent dropScreen">
          <div class="regionScreen" v-for="(screen,index) in switchScreenTitles" :key="index" @click="handleSwitchScreen(screen)">
            <div class="screenTitle">{{ screen.title }}</div>
            <div class="screenTitleEn">{{ screen.title_en }}</div>
          </div>
      </div>
    </transition>
  </div>
</template>

<script>
import {mapState} from "vuex";
import bus from "@/utils/bus";

export default {
  name: "switchScreens",
  data() {
    return {
      screenBtn: {
        active: false,
        hover: false,
        icon: require("@/assets/image/popupMarker/right-arrow.png"),
      },
      switchScreenTitles: [],
      showScreenContent: false
    }
  },
  methods: {
    dropScreenClick() {
      this.showScreenContent = !this.showScreenContent;
      this.screenBtn.active = !this.screenBtn.active;
    },
    handleSwitchScreen(screen) {
      /* 切换页面，切换信息保存至 store 库 */
      this.$store
        .dispatch('Screen', screen)
        .then((res) => {
          if(res.msg === 200){
            this.$router.push({ path: '/' + screen.name + '/home/index' })
            this.showScreenContent = false;
          }
        }).catch(() => {
        this.$message.error('切换大屏信息失败！！!')
      })
    }
  },
  mounted() {
    this.switchScreenTitles = this.appCfg.screenTitles;
  },
  computed: {
    ...mapState({
      appCfg: state => state.app.appCfg
    })
  },
}
</script>

<style scoped>
.screenBtn{
  position: absolute;
  top:0;
  right:-30px;
  width: 30px;
  height: 30px;
  pointer-events: auto;
  cursor: pointer;
}
.dropScreen{
  padding: 10px 0;
}
.dropContent {
  position: absolute;
  top: 40px;
  left: 166px;
  width: 350px;
  height: 195px;
  color: white;
  background: rgba(1, 1, 20, 0.6);
  box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
  border: 0;
  border-radius: 5px;
  pointer-events: auto;
  overflow-y: auto;
  overflow-x: hidden;
}
.panelsBtn{
  transition: all 0.5s;
}
.img-active{
  transform: rotate(90deg);
  transition: all 0.5s;
}
.regionScreen {
  display: block;
  text-align: center;
  padding: 5px 0;
  margin: 4px 0;
  cursor: pointer;
  color: white;
}
.regionScreen:hover {
  background: #1cdadb;
}
.screenTitle {
  font-size: 18px;
  font-weight: 600;
  background: transparent;
  border: 0;
}
.screenTitleEn {
  margin: 2px 0;
  background: transparent;
  border: 0;
  font-size:12px;
}
</style>
