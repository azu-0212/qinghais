<template>
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
</template>

<script>
  import MapUtils from '../../../../utils/supermap'

  export default {
    name: 'FlyManager',
    methods:{
      playFly(){
        MapUtils.SceneUtil.flyManager.play()
      },
      pauseFly(){
        MapUtils.SceneUtil.flyManager.pause()
      },
      stopFly(){
        MapUtils.SceneUtil.flyManager.stop()
      }
    },
    beforeDestroy() {
      if(MapUtils.SceneUtil.flyManager){
        this.stopFly()
      }
    }
  }
</script>

<style scoped>
  .flyManager{
    display: flex;
    justify-content:center;
    position: absolute;
    bottom: 0;
    left: calc(50% - 600px);
    width: 1200px;
    height: 100px;
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
    width: 100px;
    height: 100px;
    font-size: 40px;
    color: rgba(255,255,255,0.5);
    line-height: 100px;
  }
  .flyControl:hover{
    transform: scale(1.2);
    color: white;
  }
</style>
