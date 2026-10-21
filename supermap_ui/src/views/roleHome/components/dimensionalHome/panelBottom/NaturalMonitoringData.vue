<template>
  <div class="naturalMonitoringWrapper">
    <div class="sider-component-item">
      <div class="component-header">
        <div class="title">{{ title }}</div>
        <div class="data">
        <span class="type">
          <span class="value">{{ resourceData.type.value }}</span>
          <span class="unit">{{ resourceData.type.unit }}</span>
        </span>
          <span class="line">|</span>
          <span class="size">
          <span class="value">{{ resourceData.size.value }}</span>
          <span class="unit">{{ resourceData.size.unit }}</span>
        </span>
        </div>
      </div>
      <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
      <div id="monitoring-charts" class="monitoring-charts-wrap"></div>
    </div>
  </div>
</template>

<script>
let echarts = require('echarts/lib/echarts');
export default {
  name: "NaturalMonitoringData",
  data() {
    return {
      title: "自然资源监测数据",
      resourceData: {
        type: {
          value: 4,
          unit: "类",
        },
        size: {
          value: 0,
          unit: "MB",
        },
      },
    }
  },
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
              数值: ${a.value} <br/>
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
            radius: [25, 100],
            center: ['51%', '45%'],
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
              { value: 0, name: '地理要素数据', itemStyle:{color: '#0263ff'}, label:{color:"#0263ff"} },
              { value: 0, name: '应急监测成果', itemStyle:{ color: '#f3961c'}, label:{color:"#f3961c"} },
              { value: 0, name: '地理国情监测影像', itemStyle:{color: '#8e30ff'},label:{color:"#8e30ff"} },
              { value: 0, name: '地表覆盖数据', itemStyle:{color: '#0cc3ff'},label:{color:"#0cc3ff"} },
            ]
          }
        ]
      };
    }
  },
  methods: {
    drawChart(){
      let chartDom = document.getElementById('monitoring-charts');
      let myChart = echarts.init(chartDom);
      this.options && myChart.setOption(this.options);
    }
  },
  mounted() {
    this.drawChart();
  }
}
</script>

<style lang="scss" scoped>
.naturalMonitoringWrapper{
  height:100%;
  float: left;
  width: 540px;
  margin-right: 20px;
}
.naturalMonitoringWrapper .sider-component-item{
  height: 100%;
}
.monitoring-charts-wrap{
  width:100%;
  height:255px;
}
@import "~@/assets/homeIndex/dimensionalStype/commonStyle.scss";
</style>
