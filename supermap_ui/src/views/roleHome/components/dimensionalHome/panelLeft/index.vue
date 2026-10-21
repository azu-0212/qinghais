<template>
  <div class="panelWrapper">
    <div class="panelContent">
      <!-- 土地资源 -->
      <land-resources/>
      <!-- 森林资源 -->
      <forest-resources/>
    </div>
  </div>
</template>

<script>
import landResources from "./LandResources"
import forestResources from "./ForestResources"
export default {
  name: "index",
  components: {
    landResources,
    forestResources
  }
}
</script>

<style scoped>
.panelWrapper {
  position: absolute;
  top: 85px;
  left: 20px;
  width: 370px;
  height: 622px;
  cursor: pointer;
  pointer-events: none;
}
.panelContent {
  position: relative;
  float: left;
  width: 100%;
  color:#FFF;
  height: 100%;
  pointer-events: auto;
  box-sizing: border-box;
  overflow-y: auto;
  overflow-x: hidden;
}
</style>
