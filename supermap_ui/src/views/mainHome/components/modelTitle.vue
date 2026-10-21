<template>
  <div class="titleHeader">
    <p class="title">{{ titleInfo.title }}</p>
    <p class="title_en">
<!--      <span>{{ titleInfo.title_en }}</span>-->
      <a class="details-button" href="#" @click="linkTo">详情 ></a>
    </p>
  </div>
</template>

<script>
  import MapUtil from "@/utils/supermap";
  export default {
    name: "modelTitle",
    props:{
      titleInfo:Object,
      dataType:String
    },
    methods:{
      linkTo(){
        // 清除图标实体
        MapUtil.SceneUtil.clearEntity();
        //清楚所有照片库实体图标等信息
        MapUtil.SceneUtil.destroyDynamicCluser();
        //清除选中的所有倾斜摄影
        MapUtil.SceneUtil.removeScpLayersAll();
        this.$router.push({ path: '/home/index?type=' + this.dataType });

      }
    }
  }
</script>

<style scoped>
  .titleHeader{
    border-left:3px solid #1cd6d7;
    padding-left:5px;
    margin-bottom:20px;
    font-size:16px;
  }
  .title{
    font-weight:700;
    margin-bottom:3px;
  }
  .title_en{
    font-size:14px;
  }
  .title_en span{
    font-style: italic;
    display:inline-block;
    width:85%;
    overflow: hidden;
    text-overflow:ellipsis;
    white-space: nowrap;
    font-size:10px;
  }
  .details-button{
    float:right;
    color: #1cd6d7;
    margin-top:-20px;
  }
  p{
    margin:0;
  }
</style>
