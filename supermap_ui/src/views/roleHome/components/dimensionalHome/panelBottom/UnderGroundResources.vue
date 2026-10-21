<template>
  <div class="underGroundWrapper">
    <div class="sider-component-item bottom-component-item">
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
      <div class="data-list">
        <div class="item" v-for="item in underGroundData" :key="item.id">
          <img :src="item.imgUrl" alt="" />
          <div class="name-value">
            <span class="name">{{ item.name }}</span>
            <span class="value">{{ item.value }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
let imgUrl = require("@/assets/image/roleHome/dimensionalHome/underGroundIcon.png");
export default {
  name: "UnderGroundResource",
  data() {
    return {
      title: "地下资源",
      resourceData: {
        type: {
          value: 3,
          unit: "类",
        },
        size: {
          value: 256,
          unit: "MB",
        },
      },
      underGroundData: [
        {
          id: "data-1",
          name: "矿产资源储备",
          value: 177,
          imgUrl,
        },
        {
          id: "data-2",
          name: "矿产地",
          value: 1734,
          imgUrl,
        },
        {
          id: "data-3",
          name: "矿产资源潜力评价",
          value: 7,
          imgUrl,
        },
        {
          id: "data-4",
          name: "地下空间资源调查",
          value: 0,
          imgUrl,
        },
      ],
    };
  },
};
</script>

<style lang="scss" scoped>
.underGroundWrapper{
  height: 100%;
  float: left;
  width: 540px;
  margin-right: 20px;
}
.underGroundWrapper .sider-component-item{
  height: 100%;
}
@import "~@/assets/homeIndex/dimensionalStype/commonStyle.scss";
$color: #1cd9da;
.data-list {
  width: 88%;
  height: 88%;
  margin: 0 auto;
  color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  // overflow: hidden;
  .item {
    width: 47%;
    height: 48%;
    display: flex;
    align-items: center;
    img {
      width: 5.625rem;
      height: 3.3125rem;
      margin-right: 1.0625rem;
    }
    .name-value {
      display: flex;
      flex-direction: column;
      .name {
        font-size: .875rem;
        position: relative;
        &::after {
          content: '';
          width: 5rem;
          height: .625rem;
          background: linear-gradient(to right, rgba($color: #0a7fa1, $alpha: 0.6), rgba($color: #0a7fa1, $alpha: 0));
          position: absolute;
          transform: skewX(-30deg);
          bottom: 0;
          left: 0;
        }
      }
      .value {
        font-size: 1.5rem;
        color: $color;
        font-weight: 600;
      }
    }
  }
}
</style>
