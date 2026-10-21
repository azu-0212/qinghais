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
    <div class="year-container">
      <span
        v-for="year in years"
        :key="year"
        :class="['year-item', { 'year-active': curYear === year.yearName },{ 'no-data': year.isNull }]"
        @click="changeYear(year)"
        >{{ year.yearName }}</span
      >
    </div>
    <div id="landChart" class="chart"></div>
  </div>
</template>

<script>
// 图表按需引入
let echarts = require('echarts/lib/echarts');
import {TooltipComponent} from 'echarts/components';
import {BarChart} from 'echarts/charts';
echarts.use([TooltipComponent, BarChart]);
export default {
  name: "LandResource",
  data() {
    return {
      title: "土地资源",
      curYear: 2015,
      resourceData: {
        type: {
          value: 3,
          unit: "类",
        },
        size: {
          value: 12,
          unit: "GB",
        },
      },
      years: [
        {
          yearName: 2015,
          isNull: false
        },
        {
          yearName: 2016,
          isNull: true
        },
        {
          yearName: 2017,
          isNull: true
        },
        {
          yearName: 2018,
          isNull: true
        },
        {
          yearName: 2019,
          isNull: true
        }
      ],
      landChart: null,
      options: {
        color: ["rgba(0, 245, 255, 0.7)"],
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "none", // 默认为直线，可选为：'line' | 'shadow'
          },
          borderColor: 'rgba(1,91,175,0.3)',
          formatter: function (params) {
            let tar = params[0];
            return '<div style="color:#fff;">' + tar.name + '<br/> 土地数据：' + tar.value + '个</div>';
          },
          backgroundColor: 'rgba(1,91,175,0.3)'
        },
        grid: {
          left: "5%",
          top: "4%",
          right: "5%",
          bottom: "3%",
          containLabel: true,
        },
        textStyle: {
          color: "rgba(255,255,255,0.9)",
        },
        xAxis: {
          type: "category",
          data: ["国土调查", "耕地资源", "永久基本", "高标准农"],
          axisTick: {
            show: false,
          },
          axisLine: {
            lineStyle: {
              color: "rgba(255,255,255,0.6)",
            },
          },
          axisLabel: {
            interval: 0,
          },
        },
        yAxis: {
          type: "value",
          min: 0,
          max: 40,
          interval: 10,
          axisTick: {
            show: false,
          },
          axisLine: {
            show: false,
          },
          splitLine: {
            lineStyle: {
              type: "dashed",
              color: "rgba(255,255,255,0.4)",
              width: 0.5,
            },
          }
        },
        series: [{
            data: [37, 4, 22, 0],
            type: "bar",
            symbol: "circle",
            showSymbol: false,
            barWidth: "23%",
            label: {
              position: "top",
              color: "rgba(0, 255, 255,1)",
              fontWeight: "bolder",
            },
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: "rgba(0, 192, 255, 1)",
                },
                {
                  offset: 1,
                  color: "rgba(0, 192, 255, 0.1)",
                },
              ]),
            },
          }],
      },
    };
  },
  mounted() {
    this.landChart = echarts.init(document.getElementById("landChart"));
    this.landChart.setOption(this.options);
    // window.addEventListener('resize', () => {
    //   this.landChart.resize()
    // })
    // document.getElementById('landChart').addEventListener('resize', () => {
    //   this.landChart.resize()
    // })
  },
  methods: {
    changeYear(year) {
      this.curYear = year;
      this.options.series[0].data = [18, 20, 13, 17,22]
      this.landChart.setOption(this.options)
    },

    getTip(obj) {
       return `${obj.axisValueLabel} <br /> ${obj.seriesName}：${this.getLevel(obj.data)}`;
    },
  },
};
</script>

<style lang="scss" scoped>
.year-container span.no-data{
  color: #ccc;
  background: #eaeaea;
  cursor: default;
}
@import "~@/assets/homeIndex/dimensionalStype/commonStyle.scss";
</style>
