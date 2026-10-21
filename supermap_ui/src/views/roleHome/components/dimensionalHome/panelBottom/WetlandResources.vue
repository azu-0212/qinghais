<template>
  <div class="wetlandResourceWrapper">
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
          @click="changeYear(year)">{{ year.yearName }}
        </span>
      </div>
      <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
      <div id="wetland-charts" class="wetland-charts-wrap"></div>
      <div class="textContent">
        <p class="title">{{text.title}}</p>
        <p class="count">{{text.count}}</p>
      </div>
    </div>
  </div>
</template>

<script>
let echarts = require('echarts/lib/echarts');
// 图表按需引入
import { LegendComponent, TooltipComponent} from 'echarts/components';
import {PieChart} from 'echarts/charts';
import {CanvasRenderer} from 'echarts/renderers';
import { LabelLayout } from 'echarts/features';
echarts.use([ LegendComponent, PieChart, TooltipComponent, LabelLayout]);

export default {
  name: "WetlandResources",
  data() {
    return {
      title: "湿地资源",
      resourceData: {
        type: {
          value: 3,
          unit: "类",
        },
        size: {
          value: 36,
          unit: "MB",
        },
      },
      text:{
        title:"湿地资源(GB)",
        count: 0
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
        }
      ],
      curYear: 2015,
    }
  },
  computed: {
    options(){
      return {
        legend: {
          bottom: 45,
          textStyle:{
            color:'white',
          },
          itemGap:25,
          icon:'circle'
        },
        series: [
          {
            name: '湿地资源',
            type: 'pie',
            radius: [45, 70],
            center: ['50%', '35%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center',
            },
            emphasis: {
              label: {
                show: false,
                fontSize: '16',
                fontWeight: 'bold'
              },
              itemStyle:{
                borderWidth:5,
                borderColor:"rgb(5,35,46)"
              }
            },
            data: [
              {
                value: 0,
                name: '湿地资源调查',
                yearOnYear:275.7,
                itemStyle:{
                  color:"rgb(78, 121, 196)",
                }
              },
              {
                value: 0,
                name: '典型湿地',
                yearOnYear:275.7,
                itemStyle:{
                  color:"rgb(196,101,78)",
                }
              },
              {
                value: 0,
                name: '重点湿地资源',
                yearOnYear:36.3,
                itemStyle:{
                  color:"rgb(79,195,198)",
                }
              },
            ]
          }
        ]
      };
    }
  },
  methods: {
    changeYear(year) {
      this.curYear = year;
      this.options.series[0].data = [18, 20, 13, 17, 22];
    },
    drawChart(){
      let chartDom = document.getElementById('wetland-charts');
      let myChart = echarts.init(chartDom);
      this.options && myChart.setOption(this.options);
      myChart.on('mouseover',e=>{
        this.text = {
          title:`${e.data.name}(GB)`,
          count: e.data.value
        }
      })
      myChart.on('highlight',e=>{
        this.text = {
          title:`${e.data.name}(GB)`,
          count: e.data.value
        }
      })
    }
  },
  mounted() {
    this.drawChart();
  }
}
</script>

<style lang="scss" scoped>
.wetlandResourceWrapper{
  height:100%;
  float: left;
  width: 370px;
  position: relative;
}
.wetlandResourceWrapper .sider-component-item{
  height: 100%;
}
.wetland-charts-wrap{
  width:100%;
  height:220px;
}
.textContent{
  position: absolute;
  top: 53.5%;
  left: 0;
  right: 0;
  margin: auto;
  width: 30%;
  color: white;
  //background:red;
  text-align: center;
  p{
    margin: 0;
  }
  .title{
    font-size: 14px;
    line-height:20px;
  }
  .count{
    font-size: 1.5rem;
    line-height: 30px;
    color: #1cdadb;
    font-weight: 600;

  }
}
.year-container span.no-data{
  color: #ccc;
  background: #eaeaea;
  cursor: default;
}
@import "~@/assets/homeIndex/dimensionalStype/commonStyle.scss";
</style>
