<template>
  <div class="panorama">
    <!-- 标题-->
    <modelTitle :titleInfo="titleInfo"  :dataType="type"/>

    <div class="panel-total">
      <span class="name">{{ panelResult.className }}总量</span>
      <span class="class"><b>{{ panelResult.classNum }}</b>个</span>
      <!--      <span class="volumn"><b>{{(panelResult.classVolumn)}}</b>GB</span>-->
      <span class="volumn" v-if="panelResult.classVolumn > 1024"><b>{{(panelResult.classVolumn/1024).toFixed(2)}}</b>TB</span>
      <span class="volumn" v-else><b>{{(panelResult.classVolumn)}}</b>GB</span>
      <strong/>
      <strong/>
      <strong/>
      <strong/>
    </div>
    <div class="year-toggle">
      <a href="#" v-for="(btn, index) in years"
         :key="index * 1.1"
         :class="{ active: btn.choose || btn.active }"
         @mouseover="btn.active = true"
         @mouseout="btn.active = false"
         @click="yearClick(btn, index)"
      >{{ btn.yearName }}
      </a>
    </div>
    <div class="panel-total" @click="openPanorama">
      <span class="name">{{ result.className }}</span>
      <span class="class"><b>{{ result.classNum }}</b>个</span>
      <span class="volumn"><b>{{ result.classVolumn }}</b>GB</span>
      <strong/>
      <strong/>
      <strong/>
      <strong/>
    </div>
    <table class="resource-table" cellspacing="0">
      <tr class="tr-header">
        <th>{{ Name }}</th>
        <th>{{ Addr }}</th>
        <!--        <th>{{ Volumn }}</th>-->
      </tr>
      <tr class="tr-body" v-for="item in yearsTable">
        <td>{{ item.nodeName }}</td>
        <td v-if="item.dataRegion !== null">{{ dataRegionList[item.dataRegion].label }}</td>
        <td v-else></td>
      </tr>
    </table>
  </div>
</template>

<script>
import modelTitle from "@/views/mainHome/components/modelTitle";
import {getResourceByDataType, getResourceByDataYear, getResourceByDataYearList} from "@/api/system/resource";
import MapUtil from "@/utils/supermap";
export default {
  name: "PanoramaData",
  props:{
    dataType:Number
  },
  data(){
    return{
      dataRegionList:[
        {
          label:"西宁市"
        },
        {
          label:"海东市"
        },
        {
          label:"海北州"
        },
        {
          label:"海南州"
        },
        {
          label:"黄南州"
        },
        {
          label:"果洛州"
        },
        {
          label:"玉树州"
        },
        {
          label:"海西州"
        }
      ],
      type:'panorama',
      flag:false,
      titleInfo: {
        title: '全景数据',
        title_en: 'A PANORAMIC VIEW OF THE DATA'
      },
      panelResult:{
        className: '全景数据',
        classNum: '0',
        classVolumn: '0'
      },
      result:{
        className: '全景数据',
        classNum: 0,
        classVolumn: 0
      },
      years:[
        {
          yearName: '2017',
          active:true,
          choose:true
        },
        {
          yearName: '2018',
          active:false,
          choose:false
        },
        {
          yearName: '2019',
          active:false,
          choose:false
        }
      ],
      Name: '名称',
      Addr: '地点',
      Volumn: '体量',
      yearsTable:[],
      panoramaList:[],

    }
  },
  components:{modelTitle},
  created(){
    let dataType = this.dataType;
    // 查询全景数据列表
    getResourceByDataType(dataType).then(response => {
      this.panoramaList = response.rows;
      this.dataTotal();
      this.volumeTotal();
    });
    // 查询全景年份
    getResourceByDataYear(dataType).then(res => {
      let yearsArr = [];
      res.forEach((item) => {
        let str = `{"yearName":`+item+`,"active":false,"choose":false}`;
        var strObj = JSON.parse(str);
        yearsArr.push(strObj);
      })
      this.years = yearsArr;
      this.years[0].active = true;
      this.years[0].choose = true;
      this.initGetResourceList(dataType);
    });
  },
  methods:{
    initGetResourceList(dataType){
      let total = 0;
      //根据年份 类型查询数据
      getResourceByDataYearList(dataType,this.years[0].yearName).then(res => {
        this.yearsTable = res.rows;
        this.result.classNum = this.yearsTable.length;
        this.yearsTable.forEach((item) => {
          total += parseFloat(item.dataVolume);
        });
        this.result.classVolumn = total.toFixed(2);
      })
    },
    yearClick(btn,index){
      this.years.map((item) => {
        item.choose = false;
        item.active = false;
      });
      btn.choose = true;

      //根据年份 类型查询数据
      let dataType = this.dataType;
      let total = 0;
      getResourceByDataYearList(dataType,btn.yearName).then(res => {
        this.yearsTable = res.rows;
        this.result.classNum = this.yearsTable.length;
        this.yearsTable.forEach((item) => {
          total += parseFloat(item.dataVolume);
        });
        this.result.classVolumn = total.toFixed(2);
      })
      this.flag = !this.flag;
      MapUtil.SceneUtil.popup.closeAllPopup();
      MapUtil.SceneUtil.clearEntity();
      MapUtil.SceneUtil.entireView();
    },
    dataTotal(){
      this.panelResult.classNum = this.panoramaList.length;
    },
    volumeTotal(){
      let total = 0;
      this.panoramaList.forEach((item) => {
        total += parseFloat(item.dataVolume);
      });
      this.panelResult.classVolumn = total.toFixed(2);
    },
    openPanorama(){
      this.flag = !this.flag;
      let markerIcon = require("@/assets/image/mapMarker/pos_1.png");
      if(this.yearsTable && this.yearsTable.length>0){
        MapUtil.SceneUtil.add3DMarkersToMap(this.yearsTable,markerIcon,this.flag); //没有勾选项
        MapUtil.SceneUtil.entireView();//跳转全幅青海
        return;
      }else {
        //item.active = false
        this.$alert("暂无点位,请通过后台导入!", '提示信息')
      }
    }
  }
}
</script>

<style scoped>
.panorama{
  /*height:49%;*/
  position: absolute;
  width:89%;
  top:20.3%;
}
.titleHeader{
  border-left:3px solid #1cd6d7;
  padding-left:5px;
  margin-bottom:18px;
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
.year-toggle{
  pointer-events: auto;
  overflow-x: scroll;
  overflow-y: hidden;
  white-space: nowrap;
  margin-bottom:10px;
  padding-bottom:10px;
  width:100%;
}
.year-toggle::-webkit-scrollbar {
  height: 5px;
}

/* 滚动槽 */
.year-toggle::-webkit-scrollbar-track {
  /* -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);*/
  border-radius: 10px;
}

/* 滚动条滑块 */
.year-toggle::-webkit-scrollbar-thumb {
  border-radius: 2px;
  background: rgba(24, 247, 248, 0.3);
  -webkit-box-shadow: inset 0 0 6px rgba(22, 223, 226, 0.5);
}
.year-toggle a{
  display: inline-block;
  margin-left:35px;
  width:46px;
  height:26px;
  line-height:26px;
  text-align: center;
  border-radius: 2px;
  font-size: 14px;
  color:#FFF;
}
.year-toggle a.active{
  background:#1cd6d7;
}
.resource-table{
  font-size: 14px;
  display: inline-block;
  width:100%;
  /*height:255px;*/
  /*height:444px;*/
  height: 306px;
  overflow-y: auto;
  overflow-x: hidden;
}
.resource-table::-webkit-scrollbar {
  width: 4px;
}

/* 滚动槽 */
.resource-table::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
  border-radius: 10px;
}

/* 滚动条滑块 */
.resource-table::-webkit-scrollbar-thumb {
  border-radius: 2px;
  background: rgba(24, 247, 248, 0.3);
  -webkit-box-shadow: inset 0 0 6px rgba(22, 223, 226, 0.5);
}
.resource-table tr{
  height:30px;
}
.resource-table th{
  width:30%;
  font-size:12px;
}
.tr-header{
  background-image: linear-gradient(to right, rgba(28, 214, 215, 0),rgba(28, 214, 215, .5), rgba(28, 214, 215, 0));
}
.resource-table td{
  line-height:30px;
  text-align: center;
  font-size:12px;
}
.tr-body:nth-child(odd){
  background-image: linear-gradient(to right, rgba(28, 214, 215, 0),rgba(28, 214, 215, .2), rgba(28, 214, 215, 0));
}
.panel-total{
  height:32px;
  width:100%;
  background:rgba(28, 214, 215, .1);
  position:relative;
  color:#FFF;
  line-height:32px;
  font-size:12px;
  margin-bottom:10px;
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
.panel-total strong{
  width:6px;
  height:5px;
  position: absolute;
  border-width: 1px;
  border-style: solid;
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
</style>
