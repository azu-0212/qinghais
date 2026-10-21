<template>
  <div class="mapLayout">
    <!-- <MapHeader /> -->
    <Map3D />
    <!-- <MapTools></MapTools> -->
    <Popup></Popup>
    <router-view />
  </div>
</template>

<script>
import MapHeader from './components/MapHeader'
import Map3D from './components/SuperMap/Map3D'
import MapTools from './components/MapTools'
import Popup from './components/Popup'

export default {
  name: 'MapLayout',
  components: { MapTools, MapHeader, Map3D ,Popup},
  created() {
    this.getDicts('sys_title_name').then(res=>{
      let obj = {};
      if(res.code === 200 && res.data){
        res.data.map(dt=>{
          obj[dt.dictLabel] = dt.dictValue;
        });
        this.$store.commit('app/SET_TITLE',obj);
        // document.title = obj.s_name;
        // console.log(obj);
      }
    })
  },
  computed: {
    flyRouteShow() {
      return this.$store.getters.flyRoute_show
    }
  },
}
</script>
<style scoped>
.mapLayout {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 0;
}
</style>
