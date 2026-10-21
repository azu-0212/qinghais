<template>
  <div class="panelWrapper">
    <div class="panelContent">
      <!--自然资源图片库-->
      <PhotoGallery/>
      <!--全景数据-->
      <PanoramaData :dataType="panelModel[1].dataType"/>
      <!--视频数据-->
      <VideoData/>
    </div>
  </div>
</template>

<script>
  import PhotoGallery from "@/views/mainHome/components/screenRight/PhotoGallery";
  import PanoramaData from "@/views/mainHome/components/screenRight/PanoramaData";
  import VideoData from "@/views/mainHome/components/screenRight/VideoData";
  export default {
    name: "index",
    data(){
      return{
        panelModel:[
          {
            typeCode:3
          },
          {
            dataType:3
          }
        ]
      }
    },
    components: {
      PhotoGallery,
      PanoramaData,
      VideoData,
    }
  }
</script>

<style scoped>
  .panelWrapper {
    position: absolute;
    top: 0;
    right: 10px;
    width: 320px;
    height: 100%;
    cursor: pointer;
    pointer-events: none;
  }
  .panelContent {
    position: relative;
    float: right;
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
