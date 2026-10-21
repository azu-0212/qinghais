<template>
  <div class="photoGallery">
    <!-- 标题-->
    <modelTitle :titleInfo="titleInfo" :dataType="type"/>
    <div class="panel-total">
      <span class="name">{{ panelResult.className }}</span>
      <span class="class" v-if="panelResult.classNum > 10000"><b>{{ panelResult.classNum/10000 }}</b>万张</span>
      <span class="class" v-else><b>{{ panelResult.classNum }}</b>张</span>
      <span class="volumn" v-if="panelResult.classVolumn > 1024"><b>{{ (panelResult.classVolumn/1024).toFixed(2) }}</b>GB</span>
      <span class="volumn" v-else><b>{{ panelResult.classVolumn }}</b>MB</span>
      <strong/>
      <strong/>
      <strong/>
      <strong/>
    </div>
    <div class="panels-type">
      <div class="panels-item"
           v-for="(panelValue,index) in panelsType"
           v-show="parseInt(panelValue.imgNum) > 0">
        <p v-if="panelValue.imgNum > 10000"><b>{{ panelValue.imgNum/10000 }}</b> 万张</p>
        <p v-else><b>{{ panelValue.imgNum }}</b> 张</p>
        <span>{{ panelValue.nodeName }}</span>
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
import { getConfigKey } from "@/api/system/config";
import { getResourceByCode } from "@/api/system/resource";
export default {
  name: "PhotoGallery",
  data(){
    return {
      type:'image',
      typeCode:9,
      titleInfo: {
        title: '自然资源图片库',
        title_en: 'NATURAL RESOURCES PHOTO GALLERY'
      },
      panelResult:{
        className: '总量',
        classNum: '10219',
        classVolumn: '32'
      },
      panelsType:[
        {
          nodeName: '地理国情',
          imgNum: 0
        },
        {
          nodeName: '矿业核查',
          imgNum: 0
        },
        {
          nodeName: '耕地”非农化“',
          imgNum: 0
        },
        {
          nodeName: '卫片执法',
          imgNum: 0
        }
      ],
    }
  },
  components:{modelTitle},
  mounted() {
    // 获取照片总数
    getConfigKey("imgTotalSum").then(res => {
      this.panelResult.classNum = res.msg;
    });

    // 获取照片大小
    getConfigKey("imgTotalSize").then(res => {
      this.panelResult.classVolumn = res.msg;
    });
  },
  created() {
    //获取照片分类
    /*getResourceByCode(this.typeCode).then(res => {
      let _this = this;
      let data = this.handleTree(res.rows, "resourceId",'parentId','children',0);
      this.panelsType = data[0].children;
      this.panelsType.map((item,index) => {
        let panelObj = {};
        panelObj.nodeName = item.nodeName;
        panelObj.num = _this.panelNum[index];
        return panelObj;
      })
    })*/


    // 地理国情张数
    getConfigKey("imgDiliTotal").then(res => {
      this.panelsType[0].imgNum = res.msg;
    })
    // 矿业核查张数
    getConfigKey("imgKuangYeTotal").then(res => {
      this.panelsType[1].imgNum = res.msg;
    })

    // 耕地”非农化“张数
    getConfigKey("imgFeiNongTotal").then(res => {
      this.panelsType[2].imgNum = res.msg;
    })

    // 卫片执法张数
    getConfigKey("imgWeiPianTotal").then(res => {
      this.panelsType[3].imgNum = res.msg;
    })

    // 三调张数
    /* getConfigKey("imgSanDiaoTotal").then(res => {
       this.panelNum[2] = res.msg;
     })*/

  },
}
</script>

<style scoped>
.photoGallery{
  /*height:25%;*/
  position: absolute;
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
  left:80px;
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
  height:82px;
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
  width:80px;
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
</style>
