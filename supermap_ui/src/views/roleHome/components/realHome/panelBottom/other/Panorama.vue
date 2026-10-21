<template>
  <div>
    <div class="panel-total">
      <span class="name">覆盖区县</span>
      <span class="class"><b>114</b>个</span>
      <span class="volumn"><b>10</b>GB</span>
      <strong/>
      <strong/>
      <strong/>
      <strong/>
    </div>
    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="scene-charts" class="scene-charts-wrap"></div>
  </div>
</template>

<script>
// 图表按需引入
let echarts = require('echarts/lib/echarts');
import {TooltipComponent,GridComponent} from 'echarts/components';
import {BarChart} from 'echarts/charts';
import {CanvasRenderer} from 'echarts/renderers';
echarts.use([TooltipComponent,GridComponent, BarChart, CanvasRenderer]);
export default {
  name: "Pan",
  computed: {
    options() {
      return {
        tooltip: {
          trigger: 'axis',
          axisPointer: {           // 坐标轴指示器，坐标轴触发有效
            type: 'shadow',        // 默认为直线，可选为：'line' | 'shadow'
          },
          borderColor: 'none',
          formatter: function (params) {
            var tar = params[0];
            return '<div style="color:#fff">' + tar.name + '<br/> 全景数据：' + tar.value + '个</div>';
          },
          backgroundColor: 'rgba(51,86,78,.5)'
        },
        chart: {
          type: 'bar',
          showAxes: true,
        },
        grid: {
          height: 126,
          width: 330,
          left: '7% ',
          top: '12%'
        },
        yAxis: {
          type: 'value',
          min: 0,
          max: 50,
          splitNumber : 4,
          splitLine: {    //网格线
            lineStyle: {
              type: 'dashed',    //设置网格线类型 dotted：虚线   solid:实线
              color: 'rgba(255,255,255,.4)'
            },
            show: true //隐藏或显示
          },
          axisLabel: {
            textStyle: {
              color: '#fff', //坐标值得具体的颜色
            }
          },
        },
        xAxis: {
          data: ['西宁', '海东', '海西', '海南', '海北', '果洛', '玉树' ],
          type: 'category',
          axisLabel: {
            textStyle: {
              color: '#fff', //坐标值得具体的颜色
            }
          }
        },
        series: [{
          data: [42, 17, 17, 15, 7, 6, 7],
          type: 'bar',
          barCategoryGap: '50%',
          itemStyle: {
            /*barBorderColor: 'rgba(255, 255, 255, .7)',*/
            color: new echarts.graphic.LinearGradient(0, 0, 1, 1, [{
              offset: .1,
              color: '#1cd9da'
            }, {
              offset: .4,
              color: '#1ecbcb'
            }, {
              offset: 1,
              color: 'rgba(28, 214, 215, 0)'
            }]),
          },
        }]
      };
    }
  },
  methods: {
    drawChart(){
      let chartDom = document.getElementById('scene-charts');
      let myChart = echarts.init(chartDom);
      this.options && myChart.setOption(this.options);
    }
  },
  mounted() {
    this.drawChart();
  }
}
</script>

<style scoped>
.scene-charts-wrap{
  width:100%;
  height:172px;
}
.panel-total{
  height:32px;
  width:98%;
  background:rgba(28, 214, 215, .1);
  position:relative;
  color:#FFF;
  line-height:32px;
  font-size:14px;
  margin: 0 auto;
  margin-top:10px;
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
  left:158px;
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
p{
  margin: 0;
}
</style>
