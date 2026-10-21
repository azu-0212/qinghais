<template>
  <div>
    <div class="content-division">
      <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
      <div id="division-charts" class="division-charts-wrap"></div>
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
  name: "Division",
  computed: {
    options(){
      return {
        toolbox: {
          show: true
        },
        tooltip: {
          trigger: 'item',
          formatter: (a) => {
            return `<div style="color:#fff">${a.name}<br/>
              数值: ${a.value} 个<br/>
              比例: ${a.percent} %<br/`
          },
          backgroundColor: 'rgba(51,86,78,.5)'
        },
        legend: {
          show: false,
        },
        series: [
          {
            name: '面积模式',
            type: 'pie',
            radius: [20, 70],
            center: ['51%', '54%'],
            roseType: 'area',
            emphasis: {
              label: {
                show: true,
                fontSize: '16',
                fontWeight: 'bold'
              },
              itemStyle: {
                borderWidth: 4,
                borderColor: "rgb(8, 46, 83)"
              }
            },
            data: [
              { value: 1, name: '西宁市', itemStyle:{color: '#0263ff'}, label:{color:"#0263ff"} },
              { value: 3, name: '海东市', itemStyle:{ color: '#ff7723'}, label:{color:"#ff7723"} },
              { value: 7, name: '海北藏族自治区', itemStyle:{color: '#8e30ff'},label:{color:"#8e30ff"} },
              { value: 22, name: '海南藏族自治区', itemStyle:{color: '#819b66'},label:{color:"#819b66"} },
              { value: 17, name: '果洛藏族自治区', itemStyle:{color: '#3b8c3e'},label:{color:"#3b8c3e"} },
              { value: 28, name: '海西藏族自治区', itemStyle:{color: '#e841c9'},label:{color:"#e841c9"} }
            ]
          }
        ]
      };
    }
  },
  methods: {
    drawChart(){
      let chartDom = document.getElementById('division-charts');
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
.content-division{
  width: 315px;
  float: left;
}
.division-charts-wrap{
  width:100%;
  height:190px;
}
</style>
