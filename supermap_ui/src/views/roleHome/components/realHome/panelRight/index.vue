<template>
  <div class="panelWrapper">
    <div class="panelContent">
      <!-- 自然资源照片库 -->
      <image-resource/>
      <!--物联网感知数据-->
      <perceptual-data/>
    </div>
  </div>
</template>

<script>
import ImageResource from "./NaturalImage";
import PerceptualData from "./PerceptualData";
export default {
  name: "index",
  components: {
    ImageResource,
    PerceptualData
  }
}
</script>

<style scoped>
.panelWrapper {
  position: absolute;
  top: 85px;
 /* top: 357px;*/
  right:20px;
  width: 320px;
  height: 56%;
  /*height: 27%;*/
  cursor: pointer;
  pointer-events: none;
}
.panelContent {
  position: relative;
  float: left;
  width: 100%;
  color:#FFF;
  height: 100%;
  background: rgba(8, 46, 83, 0.9);
  box-shadow: 0 0 5px rgba(8, 46, 83, 1);
  pointer-events: auto;
  padding:22px 16px;
  box-sizing: border-box;
  overflow-y: auto;
  overflow-x: hidden;
}
</style>
