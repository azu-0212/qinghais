<template>
  <div class="mapTools">
    <transition name="el-zoom-in-bottom">
      <div v-show="menuButton.active" class="menuTools">
        <div :class="['toolsBtn',{'active':btn.active}]"
             v-for="(btn,index) in toolsBtn"
             :key="index*1.4"
             @mouseover="btn.hover=true"
             @mouseout="btn.hover=false"
             @click="handlerBtnClick(btn)"
        >
          <img v-if="btn.hover||btn.active" :src="btn.icon_active" alt="">
          <img v-else :src="btn.icon" alt="">
          <span>{{btn.name}}</span>
        </div>
      </div>
    </transition>
    <div :class="['menuBtn',{'active':menuButton.active}]"
         @click="menuButton.active=!menuButton.active"
         @mouseover="menuButton.hover=true"
         @mouseout="menuButton.hover=false"
    >
      <img v-if="menuButton.active||menuButton.hover" :src="menuButton.icon_active" alt="">
      <img v-else :src="menuButton.icon" alt="">
      <span>{{menuButton.name}}</span>
    </div>
    <transition name="el-fade-in-linear">
      <div v-show="menuButton.active" class="sysButtons">
        <el-button v-hasRole="['admin']" @click="toSys"><i class="el-icon-s-tools"></i>后台</el-button>
        <el-button v-show="false" @click="earthRotate"><i class="el-icon-refresh"></i>{{rotate?'停止':'自转'}}</el-button>
      </div>
    </transition>
<!--    <StatusBar></StatusBar>-->
  </div>
</template>

<script>
  import MapUtil from '../../../utils/supermap'
  import StatusBar from './tools/statusBar'
  export default {
    name: 'MapTools',
    // components: {StatusBar},
    data() {
      return {
        menuButton: {
          name: '工具箱',
          icon: require('../../../assets/image/mapTools/menu.png'),
          icon_active: require('../../../assets/image/mapTools/menu_active.png'),
          active: false,
          hover: false
        },
        toolsBtn: [
          {
            name: '测距',
            icon: require('../../../assets/image/mapTools/mDis.png'),
            icon_active: require('../../../assets/image/mapTools/mDis_active.png'),
            active: false,
            hover: false
          },
          {
            name: '测面积',
            icon: require('../../../assets/image/mapTools/mArea.png'),
            icon_active: require('../../../assets/image/mapTools/mArea_active.png'),
            active: false,
            hover: false
          },
          {
            name: '测高',
            icon: require('../../../assets/image/mapTools/mHigh.png'),
            icon_active: require('../../../assets/image/mapTools/mHigh_active.png'),
            active: false,
            hover: false
          },
          {
            name: '清除',
            icon: require('../../../assets/image/mapTools/mClear.png'),
            icon_active: require('../../../assets/image/mapTools/mClear_active.png'),
            active: false,
            hover: false
          },
          {
            name: '全幅',
            icon: require('../../../assets/image/mapTools/mEntire.png'),
            icon_active: require('../../../assets/image/mapTools/mEntire_active.png'),
            active: false,
            hover: false
          }
        ],
        rotate: false
      }
    },
    methods: {
      handlerBtnClick(btn) {
        this.toolsBtn.filter(item=>{
          return item.name.indexOf('时间轴')===-1
        }).map(item => {
          item.active = false
        })
        switch (btn.name) {
          case '测距':
            MapUtil.SceneUtil.measure3DUtil.activateMeasure3DControl("DIS")
            btn.active = true
            break
          case '测面积':
            MapUtil.SceneUtil.measure3DUtil.activateMeasure3DControl("AREA")
            btn.active = true
            break
          case '测高':
            MapUtil.SceneUtil.measure3DUtil.activateMeasure3DControl("HEIGHT")
            btn.active = true
            break
          case '清除':
            MapUtil.SceneUtil.measure3DUtil.deActiveAllMeasure3DControl()
            this.menuButton.active = false
            break
          case '全幅':
            this.menuButton.active = false
            MapUtil.SceneUtil.flyToInitView()
            break
          default:
            break
        }
        // MapUtil.SceneUtil.rotate(false)
        this.rotate = false
      },
      earthRotate(){
        if(this.rotate){
          MapUtil.SceneUtil.rotate(false)
        }else {
          MapUtil.SceneUtil.rotate(true)
        }
        this.rotate = !this.rotate
      },
      toSys(){
        this.$router.push({ path: '/sys/index' })
      }
    }
  }
</script>

<style scoped>
  .mapTools {
    position: absolute;
    width: 100px;
    height: 225px;
    left: 20px;
    bottom: 20px;
    z-index: 999;
  }

  .menuTools {
    margin-bottom: 15px;
    width: 100px;
    height: 180px;
    border-radius: 5px;
    background: rgba(1, 1, 20, 0.6);
    box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
    pointer-events: auto;
    cursor: pointer;
  }

  .toolsBtn {
    display: flex;
    height: 35px;
    color: white;
    line-height: 35px;
    font-size: 14px;
  }

  .menuBtn {
    display: flex;
    color: white;
    position: absolute;
    bottom: 0;
    width: 100px;
    height: 35px;
    background: rgba(1, 1, 20, 0.6);
    box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
    border-radius: 5px;
    pointer-events: auto;
    cursor: pointer;
  }

  .toolsBtn:hover, .menuBtn:hover,
  .toolsBtn.active, .menuBtn.active {
    color: #1ce5e6;
  }

  .toolsBtn img, .menuBtn img {
    margin: 10px;
  }

  .menuBtn span {
    line-height: 35px;
    font-size: 14px;
    font-weight: bolder;
  }

  .sysButtons{
    position: absolute;
    bottom:0;
    left: 120px;
    width: 200px;
    height: 35px;
    /*background: red;*/
  }

  .sysButtons >>> .el-button{
    pointer-events: auto;
    color: white;
    background: rgba(1, 1, 20, 0.6);
    box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
    border: 0;
  }

  .sysButtons >>> .el-button:hover{
    color: #1ae3e3;
  }

  .sysButtons >>> .el-button i{
    margin: 0 5px 0 0;
  }
</style>
