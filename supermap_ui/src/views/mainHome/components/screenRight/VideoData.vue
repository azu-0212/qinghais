<template>
  <div class="videoData">
    <!-- 标题-->
    <modelTitle :titleInfo="titleInfo" :dataType="type"/>

    <div class="panel-total">
      <span class="name">{{ panelResult.className }}</span>
      <span class="class"><b>{{ panelResult.classNum }}</b>个</span>
      <span class="volumn"><b>{{ panelResult.classVolumn }}</b>GB</span>
      <strong/>
      <strong/>
      <strong/>
      <strong/>
    </div>
    <div class="panels-type">
      <div class="panels-item" v-for="item in panelsType" @click="handlePanel(item)">
        <img :src="host.concat(item.imgPath)" :title="item.videoName" alt="">
        <span><b>{{ item.videoName }}</b></span>
        <strong/>
        <strong/>
        <strong/>
        <strong/>
      </div>
    </div>
  </div>
</template>

<script>
import modelTitle from "@/views/mainHome/components/modelTitle";
import {listVideo} from "@/api/system/video";
import MapUtils from "@/utils/supermap";
export default {
  name: "videoData",
  data(){
    return {
      type: 'video',
      titleInfo: {
        title: '视频数据',
        title_en: 'VIDEO DATA'
      },
      panelResult:{
        className: '现有视频',
        classNum: 0,
        classVolumn: 0
      },
      panelsType:[
        {
          videoName:'三调',
          imgPath:require("@/assets/image/videoData/qilianshan.png"),
        },
        {
          videoName:'地理国情',
          imgPath:require("@/assets/image/videoData/qinghaihu.png"),
        },
        {
          videoName:'矿业核查',
          imgPath:require("@/assets/image/videoData/yanhu.png"),
        },
        {
          videoName:'三调',
          imgPath:require("@/assets/image/videoData/qilianshan.png"),
        },
      ],
      host:"",
      markers: [
        require("@/assets/image/mapMarker/pos_4.png"),
      ],
    }
  },
  components:{
    modelTitle
  },
  created() {
    this.getConfigKey("sys.host").then(response => {
      this.host = response.msg;
    });
    listVideo().then(response => {
      this.panelsType = response.rows;
      this.dataTotal();
      this.volumeTotal();
    })
  },
  methods:{
    dataTotal(){
      this.panelResult.classNum = this.panelsType.length;
    },
    volumeTotal(){
      let total = 0;
      this.panelsType.forEach((item) => {
        total += parseFloat(item.dataVolume);
      });
      this.panelResult.classVolumn = total.toFixed(2);
    },
    handlePanel(data){
      let pos = JSON.parse(data.position);
      MapUtils.openMarksPanorama3D({
        position: pos,
        html: `<iframe src="${data.videoSrc}"></iframe>`,
        marker: this.markers[0],
        props: data,
        checked:true
      });
    }
  }
}
</script>

<style scoped>
.videoData{
  /*margin-top:16%;
  height:20%;*/
  position: absolute;
  bottom:16px;
  width:89%;
}
.titleHeader{
  border-left:3px solid #1cd6d7;
  padding-left:5px;
  margin-bottom:20px;
}
.title{
  font-weight:700;
  margin-bottom:4px;
}
.title_en{
  font-size:12px;
}
.title_en span{
  font-style: italic;
  display:inline-block;
  width:85%;
  overflow: hidden;
  text-overflow:ellipsis;
  white-space: nowrap;
}
.details-button{
  float:right;
  color: #1cd6d7;
}
p{
  margin:0;
}
.panel-total{
  height:32px;
  width:100%;
  background:rgba(28, 214, 215, .1);
  position:relative;
  color:#FFF;
  line-height:32px;
  font-size:12px;
  margin-bottom:20px;
}
.volumn, .name, .class{
  position:absolute;
}
.volumn{
  right:10px;
}
.name{
  left:10px;
}
.class{
  left:105px;
  margin:auto;
}
.panel-total span b{
  font-size:18px;
  color:#1cd6d7;
  margin-right:3px;
  font-weight:normal;
}
strong{
  position: absolute;
  border-width: 1px;
  border-style: solid;
}
.panel-total strong{
  width:6px;
  height:5px;
}
.panels-item strong{
  width:4px;
  height:4px;
}
.panel-total strong:nth-of-type(1){
  border-color: #1cd6d7 transparent transparent #1cd6d7;
  top:-4px;
  left:-3px;
}
.panel-total strong:nth-of-type(2){
  border-color: #1cd6d7 #1cd6d7 transparent transparent;
  top:-4px;
  right:-3px;
}
.panel-total strong:nth-of-type(3){
  border-color: transparent transparent #1cd6d7 #1cd6d7;
  bottom:-4px;
  left:-3px;
}
.panel-total strong:nth-of-type(4){
  border-color: transparent #1cd6d7 #1cd6d7 transparent;
  bottom:-4px;
  right:-3px;
}
.panels-item strong:nth-of-type(1){
  border-color: #1cd6d7 transparent transparent #1cd6d7;
  top:0;
  left:0;
}
.panels-item strong:nth-of-type(2){
  border-color: #1cd6d7 #1cd6d7 transparent transparent;
  top:0;
  right:0;
}
.panels-item strong:nth-of-type(3){
  border-color: transparent transparent #1cd6d7 #1cd6d7;
  bottom:0;
  left:0;
}
.panels-item strong:nth-of-type(4){
  border-color: transparent #1cd6d7 #1cd6d7 transparent;
  bottom:0;
  right:0;
}
.panels-type{
  width:100%;
  height:90px;
  pointer-events: auto;
  overflow-x: scroll;
  overflow-y: hidden;
  white-space: nowrap;
}
.panels-type::-webkit-scrollbar {
  height:7px;

}
.panels-item:last-child{
  margin:0;
}
.panels-item img{
  width:100%;
  height:100%;
}

/* 滚动槽 */
.panels-type::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 6px rgba(22, 223, 226, 0.7);
  border-radius: 10px;
  margin:0 100px;
}

/* 滚动条滑块 */
.panels-type::-webkit-scrollbar-thumb {
  border-radius: 5px;
  background: rgba(24, 247, 248, 0.8);
  -webkit-box-shadow: inset 0 0 6px rgba(22, 223, 226, 0.9);
}
.panels-item{
  width:100px;
  height:62px;
  position:relative;
  text-align: center;
  display: inline-block;
  margin-right:10px;
  background:rgba(28, 217, 218, .1);
  font-size:12px;
}
.panels-item p{
  margin:10px 0 4px;
  color:#1cd9da;
}
.panels-item > p > b{
  font-size:22px;
  font-family:"黑体";
}
.panels-item span{
  position:absolute;
  height:20px;
  width:100%;
  bottom:0;
  left:0;
  line-height:20px;
  background:rgba(0, 0, 0, .5);
  z-index:2;
}
.panels-item span b{
  display: inline-block;
  width:80%;
  margin:0 auto;
  color:#FFF;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
</style>
