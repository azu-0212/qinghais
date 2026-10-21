<template>
  <div>
<!--  地物实体模块  -->
    <!-- 标题-->
    <modelTitle :titleInfo="titleInfo"/>
    <div class="year-toggle">
      <a href="#" v-for="(btn, index) in years"
         :key="index * 1.1"
         :class="[{ active: btn.choose || btn.active },{'no-data': btn.isNull}]"
         @mouseover="btn.active = true"
         @mouseout="btn.active = false"
      >{{ btn.yearName }}
      </a>
    </div>
    <div class="panel-total">
      <span class="name">地物实体</span>
      <span class="class"><b>6</b>类</span>
      <span class="volumn"><b>84</b>MB</span>
      <strong/>
      <strong/>
      <strong/>
      <strong/>
    </div>
    <table class="table" cellspacing="0">
      <tr class="tr-header">
        <th>类名</th>
        <th>类别数量</th>
        <th>类别体量</th>
      </tr>
      <tr class="tr-body">
        <td align="center">水系</td>
        <td align="center">2类</td>
        <td align="center">84MB</td>
      </tr>
      <tr class="tr-body">
        <td align="center">交通</td>
        <td align="center">0类</td>
        <td align="center">0MB</td>
      </tr>
      <tr class="tr-body">
        <td align="center">建筑物及场地设施</td>
        <td align="center">0类</td>
        <td align="center">0MB</td>
      </tr>
      <tr class="tr-body">
        <td align="center">管线</td>
        <td align="center">0类</td>
        <td align="center">0MB</td>
      </tr>
      <tr class="tr-body">
        <td align="center">地名地址</td>
        <td align="center">0类</td>
        <td align="center">0MB</td>
      </tr>
      <tr class="tr-body">
        <td align="center">院落</td>
        <td align="center">0类</td>
        <td align="center">0MB</td>
      </tr>
    </table>

<!--  地理单元模块  -->
    <div class="panel-total">
      <span class="name">地理单元</span>
      <span class="class"><b>2</b>类</span>
      <span class="volumn"><b>65</b>MB</span>
      <strong/>
      <strong/>
      <strong/>
      <strong/>
    </div>
    <table class="table" cellspacing="0">
      <tr class="tr-header">
        <th>类名</th>
        <th>类别数量</th>
        <th>类别体量</th>
      </tr>
      <tr class="tr-body">
        <td align="center">行政区划</td>
        <td align="center">3类</td>
        <td align="center">65MB</td>
      </tr>
      <tr class="tr-body">
        <td align="center">自然地理单元</td>
        <td align="center">0类</td>
        <td align="center">0MB</td>
      </tr>
    </table>
  </div>
</template>

<script>
import modelTitle from "@/views/mainHome/components/modelTitle";
export default {
  name: "GeographicalEntity",
  data() {
    return {
      titleInfo: {
        title: '地理实体',
        title_en: 'GEOGRAPHICAL ENTITY'
      },
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
  },
  components: {
    modelTitle
  }
}
</script>

<style scoped>
.panel-total{
  height:32px;
  width:98%;
  background:rgba(28, 214, 215, .1);
  position:relative;
  color:#FFF;
  line-height:32px;
  font-size:14px;
  margin: 0 auto;
  margin-top:10px;
  margin-bottom:8px;
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

/* 滚动条滑块 */
.year-toggle::-webkit-scrollbar-thumb {
  border-radius: 2px;
  background: rgba(24, 247, 248, 0.3);
  -webkit-box-shadow: inset 0 0 6px rgba(22, 223, 226, 0.5);
}
.year-toggle a{
  display: inline-block;
  margin-left:35px;
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
</style>
