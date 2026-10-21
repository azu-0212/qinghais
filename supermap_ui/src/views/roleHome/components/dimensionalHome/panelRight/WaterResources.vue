<template>
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
    <div class="resource-targets">
<!--      <div class="target-item">
        <img class="resource-img" :src="targetIcon" alt="">
        <b class="resource-value">1401条</b>
        <span>冰川水资源</span>
      </div>-->
      <div class="target-item">
        <img class="resource-img" :src="targetIcon" alt="">
        <b class="resource-value">49MB</b>
        <span>水资源</span>
      </div>
    </div>
    <div id="waterChart" class="chart"></div>
  </div>
</template>

<script>
// 图表按需引入
let echarts = require('echarts/lib/echarts');
import { GridComponent } from 'echarts/components';
import { LineChart } from 'echarts/charts';
echarts.use([
  GridComponent,
  LineChart
]);

export default {
  name: "WaterResource",
  data() {
    return {
      title: "水资源",
      targetIcon : require("@/assets/image/roleHome/dimensionalHome/water_target_Icon.png"),
      resourceData: {
        type: {
          value: 4,
          unit: "类",
        },
        size: {
          value: 49,
          unit: "MB",
        },
      },
      waterChart: null,
      options: {
        color: ['rgba(29, 230, 232, 1)'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: 'none' // 默认为直线，可选为：'line' | 'shadow'
          },
          padding: 8,
          textStyle: {
            fontSize: 12
          },
          borderColor: 'rgba(1,91,175,0.3)',
          backgroundColor: 'rgba(25, 147, 255, 0.3)',
          formatter: function (params) {
            let tar = params[0];
            return '<div style="color:#fff;">' + tar.name + '<br/> 水资源：' + tar.value + 'MB</div>';
          },
        },
        grid: {
          left: "5%",
          top: "11%",
          right: "5%",
          bottom: "3%",
          containLabel: true
        },
        textStyle: {
          color: 'rgba(255,255,255,0.9)'
        },
        xAxis: {
          type: 'category',
          boundaryGap: false, // 不留白，从原点开始
          data: [ '2015', '2016', '2017', '2018' ],
          axisTick: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(255,255,255,0.6)'
            }
          },
          axisLabel: {
            interval: 0
          }
        },
        yAxis: {
          type: 'value',
          min: 0,
          max: 20,
          interval: 5,
          axisTick: {
            show: false
          },
          axisLine: {
            show: false,
          },
          splitLine: {
            lineStyle: {
              type: 'dashed',
              color: 'rgba(255,255,255,0.4)',
              width: 0.5
            }
          }
        },
        series: [{
          type: 'line',
          symbol:'circle',
          showSymbol: false,
          smooth: true,
          barWidth: '60%',
          label: {
            // show: true,
            position: 'top',
            color: 'rgba(0, 255, 255,1)',
            fontWeight: 'bolder'
          },
          data: [7, 17, 12, 13],
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
              offset: 0,
              color: 'rgba(0, 91, 175, .9)'
            }, {
              offset: 1,
              color: 'rgba(0, 91, 175, 0.2)'
            }])
          },
        }]
      },
    };
  },
  mounted() {
    this.waterChart = echarts.init(document.getElementById("waterChart"));
    this.waterChart.setOption(this.options);
  },
  methods: {
    changeYear(year) {
      this.curYear = year;
      this.options.series[0].data = [18, 20, 13, 17,22]
      this.waterChart.setOption(this.options)
    },
  },
};
</script>

<style lang="scss" scoped>
.resource-targets{
  width:82%;
  height: 36px;
  margin: 0 auto;
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
}
.target-item{
  height: 100%;
  width:46%;
  font-size: 14px;
  font-style: italic;
}
.resource-img{
  float: left;
  margin-right: 12px;
  border: 1px solid #1cdadb;
  border-radius: 6px;
  padding: 7px;
}
.resource-value{
  display: block;
  color: #1cdadb;
  font-style: normal;
}
@import "~@/assets/homeIndex/dimensionalStype/commonStyle.scss";
</style>
