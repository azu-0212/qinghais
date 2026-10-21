<template>
  <div>
    <div class="content-model">
      <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
      <div id="dsm-charts" class="dsm-charts-wrap"></div>
      <!--   底部标题   -->
      <p class="charts-title">DSM模型</p>
    </div>
  </div>
</template>

<script>
let echarts = require('echarts/lib/echarts');
// 图表按需引入
import {ToolboxComponent, LegendComponent, TooltipComponent} from 'echarts/components';
import {PieChart} from 'echarts/charts';
import {CanvasRenderer} from 'echarts/renderers';
echarts.use([ToolboxComponent, LegendComponent, PieChart, CanvasRenderer,TooltipComponent]);
export default {
  name: "DSMModel",
  computed: {
    options(){
      return {
        toolbox: {
          show: true
        },
        tooltip: {
          trigger: 'item',
          formatter: (a) => {
            return `<div style="color:#fff">${a.name} 比例: ${a.percent} %<br/`
          },
          backgroundColor: 'rgba(51,86,78,.5)'
        },
        legend: {
          show: false,
        },
        series: [
          {
            name: '三次产业比例',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['51%', '54%'],
            avoidLabelOverlap: false,
            emphasis: {
              label: {
                show: true,
                fontSize: '16',
                fontWeight: 'bold'
              },
              itemStyle:{
                borderWidth: 4,
                borderColor: "rgb(8, 46, 83)"
              }
            },
            data: [
              { value: 70, name: `青海省12米`, itemStyle:{color: '#0263ff'}, label:{color:"#0263ff"} },
              { value: 30, name: `全国30米`, itemStyle:{ color: '#ff7723'}, label:{color:"#ff7723"} }
            ]
          }
        ]
      };
    }
  },
  methods: {
    drawChart(){
      let chartDom = document.getElementById('dsm-charts');
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
.content-model{
  width: 220px;
  float: left;
}
.dsm-charts-wrap{
  width:100%;
  height:190px;
}
.charts-title{
  color:#FFF;
  font-size:16px;
  text-align: center;
  margin: 0;
}
</style>
