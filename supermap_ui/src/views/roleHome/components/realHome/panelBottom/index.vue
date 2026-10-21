<template>
  <div class="panelWrapper">
    <div class="panelContent">
      <!-- 标题-->
      <div class="titleHeader">
        <p class="title">{{ titleInfo.title }}</p>
      </div>
      <!--底部模块-->
      <!--地形级-->
      <terrain/>
      <!--城市级-->
      <city/>
      <!--部件级-->
      <parts/>
      <!-- 其它场景 -->
      <scene/>
    </div>
  </div>
</template>

<script>
import terrain from "./Terrain";
import parts from "./Parts";
import city from "./City";
import scene from "./Other";
export default {
  name: "index",
  data() {
    return {
      titleInfo: {
        title: '地理场景',
        title_en: 'GEOGRAPHICAL ENTITY'
      },
    }
  },
  components: {
    terrain,
    parts,
    city,
    scene
  }
}
</script>

<style scoped>
.panelWrapper {
  position: absolute;
  bottom: 22px;
  left: 20px;
  right:20px;
  height: 32%;
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
  background: rgba(8, 46, 83, 0.9);
  box-shadow: 0 0 5px rgba(8, 46, 83, 1);
  pointer-events: auto;
  padding:20px;
  box-sizing: border-box;
  overflow-y: hidden;
  overflow-x: auto;
}

.titleHeader{
  border-left:3px solid #1cd6d7;
  padding-left:5px;
  margin-bottom:14px;
  font-size:16px;
}
.title{
  font-weight:700;
  margin-bottom:3px;
}
p{
  margin:0;
}
</style>
