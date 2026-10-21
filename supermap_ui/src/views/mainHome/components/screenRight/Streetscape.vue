<template>
  <div class="Streetscape">
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
    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="Streetcharts" class="charts-wrap"></div>
  </div>
</template>

<script>
import modelTitle from "@/views/mainHome/components/modelTitle";
let echarts = require('echarts/lib/echarts');
import {TooltipComponent,GridComponent} from 'echarts/components';
import {BarChart} from 'echarts/charts';
import {CanvasRenderer} from 'echarts/renderers';
import {getResourceByDataType, getResourceRegionByGroup} from "@/api/system/resource";

echarts.use([TooltipComponent,GridComponent, BarChart, CanvasRenderer]);
export default {
  name: "Streetscape",
  props:{
    dataType:Number
  },
  data(){
    return{
      type:'street',
      titleInfo: {
        title: '街景数据',
        title_en: 'IMAGE DATA'
      },
      panelResult:{
        className: '覆盖区县',
        classNum: '0',
        classVolumn: '0'
      },
      streetList:[],
      volumns:[],
      regionType:[]
    }
  },
  created() {
    let dataType = this.dataType;
    // 查询所有街景数据列表
    getResourceByDataType(dataType).then(response => {
      this.streetList = response.rows;
      this.dataTotal();
      this.volumeTotal();
    });
  },
  components:{modelTitle},
  mounted() {
    this.$nextTick(() => {
      let dataType = this.dataType;
      // 街景扇形图统计
      getResourceRegionByGroup(dataType).then(res => {
        this.volumns = res.map((item) => {
          return item;
        })
        this.GroupDatas = res;
        this.drawStreetChart();
      })
      // 获取市区
      this.getDicts('sys_region_type').then(response => {
        this.regionType = response.data;
      });

    })
  },
  computed:{
    options(){
      return {
        tooltip: {
          trigger: 'axis',
          axisPointer: {           // 坐标轴指示器，坐标轴触发有效
            type: 'shadow',        // 默认为直线，可选为：'line' | 'shadow'
          },
          borderColor:'none',
          formatter: function (params) {
            var tar = params[0];
            return '<div style="color:#fff">'+tar.name + '<br/> 街景数据：' + tar.value + 'GB</div>';
          },
          backgroundColor: 'rgba(51,86,78,.5)'
        },
        chart: {
          type: 'bar',
          showAxes: true,
        },
        grid:{
          height:180,
          width:200,
          left: '13% ',
          top: '4%',
        },
        xAxis: {
          type: 'value',
          min: 0,
          max: 40,
          splitLine :{    //网格线
            lineStyle:{
              type:'dashed',    //设置网格线类型 dotted：虚线   solid:实线
              color:  'rgba(255,255,255,.4)'
            },
            show:true //隐藏或显示
          },
          axisLabel: {
            textStyle: {
              color:  '#fff' , //坐标值得具体的颜色
            }
          },
        },
        yAxis: {
          data: ['海东', '海西', '黄南', '海北', '果洛', '玉树', '海西', '西宁'],
          type: 'category',
          axisLabel: {
            textStyle: {
              color:  '#fff' , //坐标值得具体的颜色
            }
          }
        },
        series: [{
          data: [0, 0, 0, 0, 0, 0, 0, 0],
          type:'bar',
          barCategoryGap:'50%',
          itemStyle: {
            barBorderColor:'rgba(255, 255, 255, .7)',
            color: new echarts.graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 1,
              color: '#1cd9da'
            }, {
              offset: .4,
              color: '#1ecbcb'
            }, {
              offset: 0,
              color: 'rgba(28, 214, 215, 0)'
            }]),
          },
        }]
      };
    }
  },
  methods:{
    dataTotal(){
      this.panelResult.classNum = this.streetList.length;
    },
    volumeTotal(){
      let total = 0;
      this.streetList.forEach((item) => {
        total += parseFloat(item.dataVolume);
      });
      this.panelResult.classVolumn = total.toFixed(2);
    },
    drawStreetChart(){
      //this.options.series[0].data = this.volumns;
      this.GroupDatas.map(item=>{
        if(item.dictLabel.indexOf("西宁市")!==-1){
          this.options.series[0].data[7] = parseFloat(item.num);
        }
      })
      let chartDom = document.getElementById('Streetcharts');
      let myChart = echarts.init(chartDom);
      this.options && myChart.setOption(this.options);
    }
  }
}
</script>

<style scoped>
.Streetscape{
  height:31%;
  position: absolute;
  width:89%;
  /*bottom: -43px;*/
  bottom:20px
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
.charts-wrap{
  width:100%;
  height:100%;
}
</style>
