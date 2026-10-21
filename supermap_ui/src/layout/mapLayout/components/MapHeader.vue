<template>
  <div>
    <div class="mapHeader">
      <img class="mapLogo" src="@/assets/logo/qinghai_logo.png" alt="">
      <div class="mapTitle">
        <!--大屏页标题-->
        <div v-if="titleStyle" class="screenTitle">
          <p class="title_ch" :style="appCfg.headerPage.style_ch||style.ch">{{screenTitle.title}}</p>
          <p class="title_en" :style="appCfg.headerPage.style_en||style.en">{{screenTitle.title_en}}</p>
          <!-- 切换大屏 -->
          <div class="switchBtn">
            <switch-screens v-if="showSwitchScreen"/>
          </div>
        </div>
        <!--系统页标题-->
        <div v-else>
<!--          <p class="title_ch" :style="appCfg.headerPage.style_ch||style.ch">{{appCfg.title}}<span :style="appCfg.remarkPage.style">{{appCfg.remarkPage.title}}</span></p>
          <p class="title_en" :style="appCfg.headerPage.style_en||style.en">{{appCfg.title_en}}</p>-->
          <p class="title_ch" :style="appCfg.headerPage.style_ch||style.ch">{{screenTitle.title}}</p>
          <p class="title_en" :style="appCfg.headerPage.style_en||style.en">{{screenTitle.title_en}}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { mapState } from 'vuex'
  import switchScreens from "@/views/roleHome/components/switchScreens";
  export default {
    name: 'MapHeader',
    props:{
      titleStyle : String,
    },
    components: {
      switchScreens
    },
    data() {
      return {
        style: {
          ch: `margin: 0;
              font-size: 34px;
              font-weight: bolder;`,
          en: `margin: 7px 0 0 0;
              font-size: 10px;
              word-spacing: 2px;
              text-transform: uppercase;`
        },
        userRole: this.$store.state.user.roles,
        currentScreen: this.$store.state.user.screen,
        screenTitle: {},
        showSwitchScreen: false,
        switchFlag: false,
      }
    },
    mounted() {
      /* 判断初始化页面是否为超级管理员切换页面 */
      // 初始化切换
      if(this.currentScreen.name !== undefined){
        this.screenTitle = this.currentScreen;
        this.showSwitchScreen = true;
      }else{
        // 初始化不切换
        if(this.userRole[0].indexOf("admin")!==-1){
          this.screenTitle = this.appCfg.defaultTitle;
          this.showSwitchScreen = true;
        }else{
          /* 分权限换标题 */
          this.appCfg.screenTitles.filter((item,index) => {
            if(item.name.indexOf(this.userRole[0])!==-1){
              this.screenTitle = this.appCfg.screenTitles[index];
            }
          })
        }
      }

    },
    computed: {
      ...mapState({
        appCfg: state => state.app.appCfg
      })
    },
  }
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
    .mapHeader-border{
      position:absolute;
      border:0;
      height:.3px;
      top:56px;
      width:100%;
      background:rgba(255, 255, 255, .3);
    }
</style>
