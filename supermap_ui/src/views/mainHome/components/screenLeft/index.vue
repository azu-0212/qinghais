<template>
  <div class="panelWrapper">
    <div class="panelContent">
      <!--自然资源-->
      <natural :typeCode="panelModel[0].typeCode" :dataType="panelModel[0].dataType"/>
      <!--倾斜摄影-->
      <Photography :dataType="panelModel[1].dataType"/>
      <!--街景数据-->
      <Streetscape :dataType="panelModel[2].dataType"/>
    </div>
  </div>
</template>

<script>
import natural from "@/views/mainHome/components/screenLeft/Natural";
import Photography from "@/views/mainHome/components/screenLeft/Photography";
import Streetscape from "@/views/mainHome/components/screenRight/Streetscape";
export default {
  name: "index",
  data(){
    return{
      panelModel:[
        {
          typeCode:3,
          dataType:2
        },
        {
          dataType:1
        },
        {
          dataType:5
        }
      ]
    }
  },
  components:{
    natural,
    Photography,
    Streetscape
  }
}
</script>

<style scoped>
.panelWrapper {
  position: absolute;
  top: 0;
  left: 10px;
  width: 320px;
  height: 100%;
  cursor: pointer;
  pointer-events: none;
}
.panelContent {
  position: relative;
  float: left;
  width: 280px;
  color:#FFF;
  margin-top: 85px;
  height: calc(100% - 85px);
  background: rgba(1, 1, 20, 0.6);
  box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
  pointer-events: auto;
  padding:16px;
  box-sizing: border-box;
  overflow-y: auto;
  overflow-x: hidden;
}
.panelContent::-webkit-scrollbar {
  width: 4px;
}

</style>
