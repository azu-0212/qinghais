<template>
  <div class="imageData">
    <div class="titleHeader">
      <p class="title">{{ titleInfo.title }}</p>
      <p class="title_en">
        <span>{{ titleInfo.title_en }}</span>
        <a class="details-button" href="#">详情 ></a>
      </p>
    </div>
    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="Imagecharts" class="charts-wrap"></div>
  </div>
</template>

<script>
  let echarts = require('echarts/lib/echarts');
  import {TooltipComponent,GridComponent} from 'echarts/components';
  import {BarChart} from 'echarts/charts';
  import {CanvasRenderer} from 'echarts/renderers';

  echarts.use([TooltipComponent,GridComponent, BarChart, CanvasRenderer]);
  export default {
    name: "InageData",
    data(){
      return {
        titleInfo: {
          title: '影像数据',
          title_en: 'IMAGE DATA'
        },
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.drawImageChart();
      })
    },
    methods:{
      drawImageChart(){
        var chartDom = document.getElementById('Imagecharts');
        var myChart = echarts.init(chartDom);
        var option;
        option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
              type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            borderColor:'none',
            formatter: function (params) {
              var tar = params[0];
              return '<div style="color:#fff">'+tar.name + '<br/> 影像数据：' + tar.value + 'GB</div>';
            },
            backgroundColor: 'rgba(51,86,78,.5)'
          },
          chart: {
            type: 'bar',
            showAxes: true,
          },
          yAxis: {
            type: 'value',
            min: 0,
            max: 4,
            splitLine :{    //网格线
              lineStyle:{
                type:'dashed'    //设置网格线类型 dotted：虚线   solid:实线
              },
              show:true //隐藏或显示
            },
            axisLabel: {
              textStyle: {
                color:  '#fff' , //坐标值得具体的颜色
              }
            },
          },
          xAxis: {
            data: ['2016', '2017', '2018', '2019', '2020'],
            type: 'category',
            axisLabel: {
              textStyle: {
                color:  '#fff' , //坐标值得具体的颜色

              }
            }
          },
          series: [{
            data: [2.5, 2, 2.8, 1.5, 2],
            type:'bar',
            barCategoryGap:'50%',
            itemStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(0, 0, 1, 1, [{
                  offset: 0,
                  color: '#1cd9da'
                }, {
                  offset: .4,
                  color: '#1ecbcb'
                }, {
                  offset: 1,
                  color: 'rgba(28, 214, 215, 0)'
                }]),
              }
            },
          }]
        };
        option && myChart.setOption(option);
      }
    }
  }
</script>

<style scoped>
  .imageData{
    margin-top:20px;
  }
  .titleHeader{
    border-left:3px solid #1cd6d7;
    padding-left:5px;
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
  .charts-wrap{
    width:100%;
    height:300px;
  }
</style>
