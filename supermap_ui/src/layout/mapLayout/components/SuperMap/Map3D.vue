<template>
  <div id="map3D" />
</template>

<script>
import MapUtil from '../../../../utils/supermap/index'
export default {
  name: 'Map3D',
  mounted() {
    MapUtil.SceneUtil.initScene()
  }
}
</script>

<style scoped>
#map3D {
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 9;
}
/* 去除supermap字样信息 */
/*#map3D >>> .cesium-widget-credits {*/
/*  display: none;*/
/*}*/
</style>
