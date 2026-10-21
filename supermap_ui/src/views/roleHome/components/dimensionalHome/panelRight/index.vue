<template>
  <div class="panelWrapper">
    <div class="panelContent">
      <!-- 水资源 -->
      <water-resource/>
      <!-- 地表基质 -->
      <ground/>
    </div>
  </div>
</template>

<script>
import waterResource from "./WaterResources"
import ground from "./Ground"
export default {
  name: "index",
  components: {
    waterResource,
    ground
  }
}
</script>

<style scoped>
.panelWrapper {
  position: absolute;
  top: 85px;
  right: 20px;
  width: 370px;
  height: 622px;
  cursor: pointer;
  pointer-events: none;
}
.panelContent {
  position: relative;
  float: right;
  width: 100%;
  color:#FFF;
  height: 100%;
  pointer-events: auto;
  box-sizing: border-box;
  overflow-y: auto;
  overflow-x: hidden;
}
</style>
