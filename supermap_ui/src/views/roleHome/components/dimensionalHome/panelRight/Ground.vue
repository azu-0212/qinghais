<template>
  <div class="sider-component-item groundWrapper">
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
    <div class="panel-total">
      <span class="name">地物实体</span>
      <span class="class"><b>4</b>类</span>
      <span class="volumn"><b>64</b>MB</span>
      <strong/>
      <strong/>
      <strong/>
      <strong/>
    </div>
    <table class="table" cellspacing="0">
      <tr class="tr-body">
        <td align="center">地表基质调查</td>
        <td align="center">0景</td>
      </tr>
      <tr class="tr-body">
        <td align="center">地质调查</td>
        <td align="center">1个</td>
      </tr>
      <tr class="tr-body">
        <td align="center">矿山地质环境</td>
        <td align="center">0个</td>
      </tr>
      <tr class="tr-body">
        <td align="center">地质灾害</td>
        <td align="center">0个</td>
      </tr>
    </table>
  </div>
</template>

<script>
export default {
  name: "GroundResource",
  data() {
    return {
      title: "地表基质",
      resourceData: {
        type: {
          value: 4,
          unit: "类",
        },
        size: {
          value: 64,
          unit: "MB",
        },
      },
      resultData: {
        value: 0,
        unit: 'TB',
        name: '成果数据'
      },
      groundData: [
        {
          id: 'type-1',
          name: '地表基质调查',
          value: 0,
          unit: '景'
        },
        {
          id: 'type-2',
          name: '地质调查',
          value: 1,
          unit: '个'
        },
        {
          id: 'type-3',
          name: '矿山地质环境',
          value: 0,
          unit: '个'
        },
        {
          id: 'type-4',
          name: '地质灾害',
          value: 20,
          unit: '个'
        }
      ],
    };
  },
};
</script>

<style lang="scss" scoped>
.groundWrapper{
  margin-top:20px;
}
.panel-total{
  height:32px;
  width:94%;
  background:rgba(28, 214, 215, .1);
  position:relative;
  color:#FFF;
  line-height:32px;
  font-size:14px;
  margin: 20px auto;
}
.volumn, .name, .class{
  position:absolute;
}
.volumn{
  right:10px;
}
.name{
  left:10px;
}
.class{
  left:124px;
}
.panel-total span b{
  font-size:18px;
  color:#1cd6d7;
  margin-right:3px;
  font-weight:normal;
}
.panel-total strong{
  width:6px;
  height:5px;
  position: absolute;
  border-width: 1px;
  border-style: solid;
}
.panel-total strong:nth-of-type(1){
  border-color: #1cd6d7 transparent transparent #1cd6d7;
  top:-4px;
  left:-3px;
}
.panel-total strong:nth-of-type(2){
  border-color: #1cd6d7 #1cd6d7 transparent transparent;
  top:-4px;
  right:-3px;
}
.panel-total strong:nth-of-type(3){
  border-color: transparent transparent #1cd6d7 #1cd6d7;
  bottom:-4px;
  left:-3px;
}
.panel-total strong:nth-of-type(4){
  border-color: transparent #1cd6d7 #1cd6d7 transparent;
  bottom:-4px;
  right:-3px;
}
.year-toggle{
  pointer-events: auto;
  overflow-x: scroll;
  overflow-y: hidden;
  white-space: nowrap;
  width:100%;
}
.year-toggle::-webkit-scrollbar {
  height: 5px;
}
/* 滚动槽 */
.year-toggle::-webkit-scrollbar-track {
  /* -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);*/
  border-radius: 10px;
}
.table{
  font-size: 14px;
  display: inline-block;
  width:100%;
  height: 151px;
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
.table td{
  width:20%;
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

@import "~@/assets/homeIndex/dimensionalStype/commonStyle.scss";
</style>
