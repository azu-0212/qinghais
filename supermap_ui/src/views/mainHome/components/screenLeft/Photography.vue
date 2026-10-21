<template>
  <div class="photography">
    <!-- 标题-->
    <modelTitle :titleInfo="titleInfo" :dataType="type"/>

    <div class="panel-total">
      <span class="name">{{ panelResult.className }}总量</span>
      <span class="class"><b>{{ panelResult.classNum }}</b>个</span>
      <span class="volumn" v-if="panelResult.classVolumn > 1024"><b>{{(panelResult.classVolumn/1024).toFixed(2)}}</b>TB</span>
      <span class="volumn" v-else><b>{{(panelResult.classVolumn).toFixed(2)}}</b>GB</span>
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
    <div class="panel-total" @click="openPhotography">
      <span class="name">{{ result.className }}</span>
      <span class="class"><b>{{ result.classNum }}</b>处</span>
      <span class="volumn"><b>{{ result.classVolumn }}</b>GB</span>
      <strong/>
      <strong/>
      <strong/>
      <strong/>
    </div>
    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="charts" class="charts-wrap"></div>
  </div>
</template>

<script>
import {
  getResourceByDataType,
  getResourceByDataYear,
  getResourceByDataYearList,
  getResourceRegionByGroup,
  getResourceYearByGroup
} from "@/api/system/resource";
let echarts = require('echarts/lib/echarts');
// 按需引入
import {ToolboxComponent, LegendComponent} from 'echarts/components';
import {PieChart} from 'echarts/charts';
import {CanvasRenderer} from 'echarts/renderers';
import modelTitle from "@/views/mainHome/components/modelTitle";
import MapUtil from "@/utils/supermap";
import MapUtils from "@/utils/supermap";
echarts.use([ToolboxComponent, LegendComponent, PieChart, CanvasRenderer]);
export default {
  name: "Photography",
  props: {
    dataType:Number,
  },
  data(){
    return{
      type:"photography",
      titleInfo: {
        title: '倾斜摄影',
        title_en: 'OBLIQUE PHOTOGRAPHY'
      },
      panelResult:{
        className: '三维模型',
        classNum: '0',
        classVolumn: '0'
      },
      photographyByYearList:[],
      flag:false,
      years:[],
      groupByRegion:[],
      photographyList:[],
      result:{
        className: '三维模型成果',
        classNum: '0',
        classVolumn: '0',
        choose: true

      },
      colors:[
        {color: '#0263ff'},
        {color: '#ff7723'},
        {color: '#8e30ff'},
        {color: '#819b66'},
        {color: '#3b8c3e'},
        {color: '#e841c9'},
        {color: '#10d02a'},
        {color: '#f34040'}
      ]
    }
  },
  components:{modelTitle},
  mounted() {
    this.$nextTick(() => {
      let dataType = this.dataType;
      // 倾斜摄影扇形图统计
      getResourceRegionByGroup(dataType).then(res => {
        /* this.groupByRegion = res.map((item,index) => {
           var groupObj = {};
           var color = this.colors[index];
           groupObj.value = parseInt(item.num);
           groupObj.name = item.dictLabel;
           groupObj.itemStyle = color;
           return groupObj;
         })*/
        //根据不同年份展示扇形图  默认
        getResourceYearByGroup({dataType:dataType,dataYear:this.years[0].yearName}).then( res => {
          this.groupByRegion = res.map((item,index) => {
            var groupObj = {};
            var color = this.colors[index];
            groupObj.value = parseInt(item.num);
            groupObj.name = item.dictLabel;
            groupObj.itemStyle = color;
            return groupObj;
          });
          this.drawChart();
        })
      })
    })
  },
  created() {
    let dataType = this.dataType;
    // 查询倾斜摄影年份
    getResourceByDataYear(dataType).then(res => {
      let yearsArr = [];
      res.forEach((item) => {
        if(item != null){
          let str = `{"yearName":`+item+`,"active":false,"choose":false}`;
          let strObj = JSON.parse(str);
          yearsArr.push(strObj);
        }
      })
      this.years = yearsArr;
      this.years[0].active = true;
      this.years[0].choose = true;
      this.initGetResource(dataType);
    });
    // 查询所有倾斜摄影数据列表
    getResourceByDataType(dataType).then(response => {
      this.photographyList = response.rows;
      this.dataTotal();
      this.volumeTotal();
    });
  },

  computed: {
    options(){
      return {
        toolbox: {
          show: true
        },
        series: [
          {
            name: '面积模式',
            type: 'pie',
            radius: [13, 85],
            center: ['51%', '54%'],
            roseType: 'area',
            itemStyle: {
              normal: {
                label:{
                  show: true,
                  position: 'inner',
                  formatter: '{b} {c}',
                  color:'#fff',
                },
                labelLine: {
                  length: .001,
                }
              }
            },
            data: []
          }
        ]
      };
    }
  },
  methods:{
    dataTotal(){
      this.panelResult.classNum = this.photographyList.length;
    },
    volumeTotal(){
      let total = 0;
      this.photographyList.forEach((item) => {
        total += parseFloat(item.dataVolume);
      });
      this.panelResult.classVolumn = total.toFixed(2);
    },
    initGetResource(dataType){
      let total = 0;
      // 根据不同年份统计
      getResourceByDataYearList(dataType,this.years[0].yearName).then(res => {
        this.photographyByYearList = res.rows;
        this.result.classNum = this.photographyByYearList.length;
        this.photographyByYearList.forEach((item) => {
          total += parseFloat(item.dataVolume);
        });
        this.result.classVolumn = total.toFixed(2);
      });
    },
    yearClick(btn,index){
      // 清除所有倾斜摄影
      MapUtils.SceneUtil.removeScpLayersAll();
      this.years.map((item) => {
        item.choose = false;
        item.active = false;
      });
      btn.choose = true;
      //根据年份 类型查询数据
      let dataType = this.dataType;
      let total = 0;
      getResourceByDataYearList(dataType,btn.yearName).then(res => {
        this.photographyByYearList = res.rows;
        this.result.classNum = this.photographyByYearList.length;
        this.photographyByYearList.forEach((item) => {
          total += parseFloat(item.dataVolume);
        });
        this.result.classVolumn = total.toFixed(2);
      });
      //根据不同年份展示扇形图
      getResourceYearByGroup({dataType:dataType,dataYear:btn.yearName}).then( res => {
        this.groupByRegion = res.map((item,index) => {
          var groupObj = {};
          var color = this.colors[index];
          groupObj.value = parseInt(item.num);
          groupObj.name = item.dictLabel;
          groupObj.itemStyle = color;
          return groupObj;
        });
        this.drawChart();
      })
      this.flag = !this.flag;
      MapUtil.SceneUtil.popup.closeAllPopup();
      MapUtil.SceneUtil.clearEntity();
      MapUtil.SceneUtil.entireView();
    },
    openPhotography(){
      this.flag = !this.flag;
      let markerIcon = require("@/assets/image/mapMarker/pos_0.png");
      if(this.photographyByYearList && this.photographyByYearList.length>0){
        MapUtil.SceneUtil.add3DMarkersToMap(this.photographyByYearList,markerIcon,this.flag); //没有勾选项
        MapUtil.SceneUtil.entireView();//跳转全幅青海
        return;
      }else {
        this.$alert("暂无点位,请通过后台导入!", '提示信息')
      }
    },
    drawChart(){
      this.options.series[0].data = this.groupByRegion;
      var chartDom = document.getElementById('charts');
      var myChart = echarts.init(chartDom);
      this.options && myChart.setOption(this.options);
    }
  }
}
</script>

<style scoped>
.photography{
  position: absolute;
  width:89%;
  top:30%;
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
.panel-total{
  height:32px;
  width:100%;
  background:rgba(28, 214, 215, .1);
  position:relative;
  color:#FFF;
  line-height:32px;
  font-size:12px;
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
  left:106px;
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
.year-toggle{
  pointer-events: auto;
  overflow-x: scroll;
  overflow-y: hidden;
  white-space: nowrap;
  /* display: flex;
   justify-content: space-around;*/
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
.charts-wrap{
  width:100%;
  height:185px;
  margin-top: -20px;
}
</style>
