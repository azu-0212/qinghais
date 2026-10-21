<template>
  <div class="content flyWrapper">
    <div
      v-for="(fly,index) in flyRouteList"
      :key="index*1.2"
      class="flyBox"
      :class="{active:choose === index}"
      @mouseover="fly.active = true"
      @mouseout="fly.active = false"
      @click="flyClick(fly,index)"
    >
      <img
        :src="host+fly.imgPath"
        alt=""
      >
      <p>{{ fly.flyName }}</p>
    </div>
    <div class="flyManager">
      <div class="flyControl" @click="playFly">
        <i class="fa fa-play" aria-hidden="true"></i>
      </div>
      <div class="flyControl" @click="pauseFly">
        <i class="fa fa-pause-circle-o" aria-hidden="true"></i>
      </div>
      <!--      <div class="flyControl">-->
      <!--        <i class="fa fa-repeat" aria-hidden="true"></i>-->
      <!--      </div>-->
      <div class="flyControl" @click="stopFly">
        <i class="fa fa-stop-circle" aria-hidden="true"></i>
      </div>
    </div>
  </div>
</template>

<script>
  import { listFly } from "@/api/system/fly"
  import MapUtils from '../../../../utils/supermap'
  import MapUtil from "@/utils/supermap";
  import {getResource} from "@/api/system/resource";

  export default {
    name: 'FlyRouteList',
    // components:{FlyManager},
    data() {
      return {
        choose:null,
        flyRouteList: [],
        host:"",
        timer:null,
      }
    },
    created() {
      this.getConfigKey("sys.host").then(response => {
        this.host = response.msg;
      });
      listFly().then(res=>{
        this.flyRouteList = res.rows
      })
    },
    methods: {
      flyClick(fly,index) {
        let _this = this;
        this.choose = index;
        // 清除所有实体
        MapUtil.SceneUtil.clearEntity();
        //清除选中的所有倾斜摄影
        MapUtil.SceneUtil.removeScpLayersAll();
        if(fly.resourceId>0){
          getResource(fly.resourceId).then(response => {
            this.handleResource(response.data,`${_this.host}${fly.flyPath}`);
          });
        }else{
          MapUtils.SceneUtil.initFlyManager(`${_this.host}${fly.flyPath}`)
        }
      },
      handleResource(data,flyPath) {
        let dataId;
        if(data!== undefined){
          dataId = `${data.nodeName}-${data.resourceId}`;
          switch (data.dataType) {
            case 1:
              MapUtils.SceneUtil.addS3MTilesLayerByScpAddFly(
                data,
                JSON.parse(data.position),
                dataId,
                data.serverUrl,
                data.checked,
                flyPath
              );
              break;
            default:
              break;
              return;
          }
          return;
        }

      },
      playFly(){
        MapUtils.SceneUtil.flyManager.play()
      },
      pauseFly(){
        MapUtils.SceneUtil.flyManager.pause()
      },
      stopFly(){
        MapUtils.SceneUtil.flyManager.stop()
      }
    }
  }
</script>

<style scoped>
  .flyWrapper {
    display: flex;
    flex-wrap: wrap;
   /* position: relative;
    display: flex;
    flex-wrap: wrap;
    height: calc(100% - 40px);*/
    /*height: calc(100% - 288px);*/
  }

  .flyBox {
   /* position: relative;
    margin: 10px 0 0 38px;
    width: 100px;
    height: 100px;
    color: white;
    border: 1px solid transparent;
    border-radius: 5px;
    overflow: hidden;*/
    position: relative;
    margin: 10px 0 0 34px;
    width: 100px;
    height: 100px;
    color: white;
    border: 1px solid transparent;
    border-radius: 5px;
    overflow: hidden;
  }

  .flyBox img{
    width: 100%;
    height: 100%;
  }

  .flyBox p{
    position: absolute;
    text-align: center;
    height: 30px;
    width: 100px;
    bottom: -16px;
    line-height: 30px;
    background: rgba(0,0,0,0.8);
    pointer-events: none;
  }

  .flyBox.active,
  .flyBox:hover{
    background: rgb(231, 50 , 15);
    border: 1px solid rgb(24, 247, 248);
    color: rgb(24, 247, 248);
  }
  .flyBox img:hover {
    transform: scale(1.1)
  }

  .flyManager{
    display: flex;
    justify-content:center;
    position: absolute;
    bottom: 22px;
    /*width: 100%;*/
    width:75%;
    height: 45px;
    z-index: 99;
    opacity: 0.5;
    background: linear-gradient(to right,transparent ,rgba(1, 1, 20, 0.3),transparent);
  }
  .flyManager:hover{
    background: linear-gradient(to right,transparent ,rgba(1, 1, 20, 0.6),transparent);
    opacity: 1;
  }
  .flyControl{
    text-align: center;
    width: 100%;
    height: 100%;
    font-size: 25px;
    color: rgba(255,255,255,0.5);
    line-height: 45px;
  }
  .flyControl:hover{
    transform: scale(1.2);
    color: white;
  }
</style>
