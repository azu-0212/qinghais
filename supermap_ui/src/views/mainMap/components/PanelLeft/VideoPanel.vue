<template>
  <div class="videoPanel">
    <el-collapse v-model="activeName" accordion @change="handleChange">
      <el-collapse-item
        v-for="(item,index) in videoTypeOptions"
        :title="item.dictLabel"
        :name="item.dictValue"
        :key="index">
        <div v-if="!videoList.length">
          <p class="warningMessage">{{ message }}</p>
        </div>
          <div
               v-for="(video, index) in videoList"
               :key="index * 1.3"
               class="pointBox"
               @click="pointClick(video)"
          >
            <img :src="host + video.imgPath" alt="" />
            <p>{{ video.videoName }}</p>
          </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script>
  import { listVideoByType } from "@/api/system/video";
  import MapUtils from "@/utils/supermap";

  export default {
    name: "VideoPanel",
    data(){
      return {
        host:"",
        message:'',
        activeName: '',
        videoTypeOptions:[],
        videoList:[],
        markers: [
          require("../../../../assets/image/mapMarker/pos_4.png"),
        ],
      }
    },
    created() {
      this.getConfigKey("sys.host").then((response) => {
        this.host = response.msg;
      });
      this.getDicts('sys_video_type').then(response => {
        this.videoTypeOptions = response.data;
      });
    },
    methods:{
      handleChange(typeValue) {
        this.message = '';
        this.videoList = [];
        if(typeValue && typeValue !== ''){
          let type = parseInt(typeValue);
          listVideoByType(type).then((res) => {
            if(res.length){
              this.videoList = res;
            }else{
              this.message = '未找到数据资源！';
            }
          });
        }else{

        }
      },
      pointClick(p){
        this.videoList.map((pItem) => {
          pItem.active = false;
        });
        let pos = JSON.parse(p.position);
        MapUtils.openMarksPanorama3D({
          position: pos,
          html: `<iframe src="${p.videoSrc}"></iframe>`,
          marker: this.markers[0],
          props: p,
          checked:true
        });
        p.active = true;
      },
    }
  }
</script>

<style scoped>
  .videoPanel {
    height: calc(100% - 55px);
    width: 100%;
    padding: 0 15px 0 10px;
    overflow-x: hidden;
    overflow-y: auto;
  }

  .videoPanel::-webkit-scrollbar {
    /*滚动条整体样式*/
    width: 5px;
    height: 1px;
  }

  .videoPanel::-webkit-scrollbar-thumb {
    /*滚动条里面小方块*/
    border-radius: 5px;
    background: rgba(1, 1, 20, 0.9);
  }

  .videoPanel::-webkit-scrollbar-track {
    /*滚动条里面轨道*/
    border-radius: 5px;
    background: transparent;
  }
  .el-collapse{
    border:0;
  }
  .el-collapse-item{
    background: transparent;
    color: white;
    height: calc(100% - 250px);
  }
  .el-collapse-item >>> .el-collapse-item__header{
    color:#fff;
    background: transparent;
    height:36px;
    line-height:36px;
    border:0;
  }
  .el-collapse-item >>> .el-collapse-item__header:hover{
    background: rgba(24, 247, 248, 0.2);
    color: rgb(24, 247, 248);
  }
  .pointBox {
    position: relative;
    margin: 10px 0 0 0;
    width: 100%;
    height: 120px;
    color: white;
    border: 1px solid transparent;
    border-radius: 5px;
    overflow: hidden;
  }

  .pointBox img {
    width: 100%;
    height: 100%;
  }

  .pointBox p {
    position: absolute;
    text-align: center;
    height: 35px;
    width: 100%;
    overflow: hidden;
    text-overflow:ellipsis;
    white-space: nowrap;
    bottom: -16px;
    line-height: 35px;
    background: rgba(0, 0, 0, 0.8);
    pointer-events: none;
  }

  .pointBox:hover {
    border: 1px solid rgb(24, 247, 248);
    color: rgb(24, 247, 248);
  }
  .pointBox img:hover {
    transform: scale(1.1);
  }

  .el-collapse-item >>> .el-collapse-item__header{
    color:#fff;
    font-size:16px;
  }
  .el-collapse-item >>> .el-collapse-item__wrap{
      background-color:transparent;
      border:0;
   }
  .el-collapse-item >>> .el-collapse-item__wrap .el-collapse-item__content{
    display: flex;
    flex-wrap: wrap;
  }
  .warningMessage{
    color:#c3c3c3;
    text-align: center;
    font-size:14px;
  }

</style>
