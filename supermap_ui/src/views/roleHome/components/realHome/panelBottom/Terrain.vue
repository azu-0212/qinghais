<template>
  <div class="moduleWrapper">
    <!--标题-->
    <p class="module-title">地形级</p>
    <div class="year-toggle">
      <a href="#" v-for="(btn, index) in years"
         :key="index * 1.1"
         :class="[{ active: btn.choose || btn.active },{'no-data': btn.isNull}]"
         @mouseover="btn.active = true"
         @mouseout="btn.active = false"
      >{{ btn.yearName }}
      </a>
    </div>
    <table class="table" cellspacing="0">
      <tr class="tr-header">
        <th>数字正射影像</th>
        <th>精度</th>
        <th>数字高程模型</th>
        <th>精度</th>
      </tr>
      <tr class="tr-body">
        <td align="center">全省影像</td>
        <td align="center">1m</td>
        <td align="center">全国地形数据</td>
        <td align="center">30m</td>
      </tr>
    </table>
  </div>
</template>

<script>
export default {
  name: "Terrain",
  data() {
    return {
      years: [
        {
          yearName: 2015,
          active: true,
          choose: true,
          isNull: false
        },
        {
          yearName: 2016,
          active: false,
          choose: false,
          isNull: true
        },
        {
          yearName: 2017,
          active: false,
          choose: false,
          isNull: true
        }
      ]
    }
  }
}
</script>

<style scoped>
.moduleWrapper{
  width: 19.14%;
  margin-right:10px;
  float: left;
}
.module-title{
  line-height: 30px;
  font-size:14px;
  padding-left:10px;
  color:#fff;
  border:1px solid #1cd9da;
  width:100%;
  background-image: linear-gradient(to left, rgba(28, 214, 215, 0) 10%,rgba(28, 214, 215, .3) 70%);
}
.year-toggle{
  pointer-events: auto;
  overflow-x: scroll;
  overflow-y: hidden;
  white-space: nowrap;
  width:100%;
  margin:8px 0;
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
  margin-left:46px;
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
.year-toggle a.no-data{
  color: #ccc;
  background: #eaeaea;
  cursor: default;
}
.table{
  font-size: 14px;
  display: inline-block;
  width:100%;
  height:130px;
  overflow-y: auto;
  overflow-x: hidden;
}
.table::-webkit-scrollbar {
  width: 4px;
}

/* 滚动槽 */
.table::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
  border-radius: 10px;
}

/* 滚动条滑块 */
.table::-webkit-scrollbar-thumb {
  border-radius: 2px;
  background: rgba(24, 247, 248, 0.3);
  -webkit-box-shadow: inset 0 0 6px rgba(22, 223, 226, 0.5);
}
.table th{
  width:20%;
  font-size:14px;
}
.tr-header{
  background-image: linear-gradient(to right, rgba(28, 214, 215, 0),rgba(28, 214, 215, .5), rgba(28, 214, 215, 0));
}
.table td,.table tr{
  line-height:30px;
  font-size:14px;
}
.tr-body:nth-child(odd){
  background-image: linear-gradient(to right, rgba(28, 214, 215, 0),rgba(28, 214, 215, .2), rgba(28, 214, 215, 0));
}
p{
  margin: 0;
}
</style>
