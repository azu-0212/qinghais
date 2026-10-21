<template>
  <!-- 数据总量资源 -->
  <div class="totalResourceWrapper">
    <div class="totalImage">
      <img class="totalIcon" :src="icon" alt="">
    </div>
    <div class="totalTitle">
      <p class="title">数据资源总量</p>
      <p class="value"><span><b>8</b> 大类</span>&nbsp;&nbsp;<span><b>739</b> MB</span></p>
    </div>
  </div>
</template>

<script>
export default {
  name: "TotalDataInfo",
  data() {
    return {
      icon: require("@/assets/image/roleHome/dimensionalHome/total-icon.png")
    }
  }
}
</script>

<style scoped>
.totalResourceWrapper {
  position: absolute;
  top: 85px;
  width: 329px;
  left: 410px;
}
.totalImage {
  float: left;
  width: 74px;
  height: 74px;
  background: #e27c42;
  margin-right: 3px;
}
.totalIcon{
  padding: 14px 0 0 12px;
}
.totalTitle {
  width: 250px;
  font-size: 15px;
  color:#FFF;
  float: left;
}
.totalTitle p {
  margin: 0;
  padding-left: 20px;
  font-style: italic;
}
.totalResourceWrapper .totalTitle p.title {
  margin-bottom: 3px;
  line-height: 30px;
  background-image: linear-gradient(to left, rgba(224, 121, 66, .1),rgba(224, 121, 66, .9));
}
.value {
  padding-top: 9px;
  background-image: linear-gradient(to left, rgba(224, 121, 66, .1),rgba(224, 121, 66, .9));
}
.value b {
  font-size: 28px;
  font-style: normal;
}
</style>
