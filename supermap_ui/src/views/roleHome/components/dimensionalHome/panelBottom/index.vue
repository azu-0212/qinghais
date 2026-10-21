<template>
  <div class="panelWrapper">
    <div class="panelContent">
      <!-- 草原资源 -->
      <grassland-resources/>
      <!-- 自然监测数据 -->
      <natural-monitoring-data/>
      <!-- 地下资源 -->
      <under-ground-resources/>
      <!-- 湿地资源 -->
      <wetland-resources/>
    </div>
  </div>
</template>

<script>
import grasslandResources from "./GrasslandResources"
import underGroundResources from "./UnderGroundResources"
import naturalMonitoringData from "./NaturalMonitoringData"
import wetlandResources from "./WetlandResources"
export default {
  name: "index",
  components: {
    grasslandResources,
    underGroundResources,
    naturalMonitoringData,
    wetlandResources
  }
}
</script>

<style scoped>
.panelWrapper {
  position: absolute;
  bottom: 22px;
  left: 20px;
  right:20px;
  height: 29.5%;
  /*height: 300px;*/
  cursor: pointer;
  pointer-events: none;
  z-index: 99;
}
.panelContent {
  position: relative;
  float: left;
  width: 100%;
  color:#FFF;
  height: 100%;
  pointer-events: auto;
  box-sizing: border-box;
  overflow-y: hidden;
  overflow-x: auto;
}
</style>
