<template>
  <div class="moduleWrapper">
    <!-- 标题 -->
    <p class="module-title">城市级</p>
    <div class="moduleContent">
      <!-- 年份切换 -->
      <year-switch/>
      <!-- 行政区划分类 -->
      <division/>
      <!-- DSM模型 -->
      <model/>
      <!--   底部标题   -->
      <div class="charts-title">倾斜摄影模型</div>
    </div>
  </div>
</template>

<script>
import yearSwitch from "./city/YearSwitch";
import division from "./city/Division";
import model from "./city/DSMModel";
export default {
  name: "City",
  components: {
    yearSwitch,
    division,
    model
  }
}
</script>

<style scoped>
.moduleWrapper{
  width: 44.1%;
  margin-right: 10px;
  float: left;
  position: relative;
}
.module-title{
  line-height: 30px;
  font-size: 14px;
  padding-left: 10px;
  color: #fff;
  border: 1px solid #1cd9da;
  width: 100%;
  background-image: linear-gradient(to left, rgba(28, 214, 215, 0) 10%,rgba(28, 214, 215, .3) 70%);
}
.charts-title{
  position: absolute;
  bottom: -6px;
  left: 225px;
  margin: auto;
  color:#FFF;
  font-size:16px;
  text-align: center;
  line-height: 30px;
}
p{
  margin: 0;
}
</style>
