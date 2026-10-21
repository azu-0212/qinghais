<template>
  <div class="NaturalMapper">
    <!-- 标题-->
    <modelTitle :titleInfo="titleInfo" :dataType="type"/>

    <div class="panel-total">
      <span class="name">{{ panelResult.className }}成果</span>
      <span class="class"><b>{{ panelResult.classNum }}</b>类</span>
      <span class="volumn"><b>{{ (panelResult.classVolumn).toFixed(2) }}</b>GB</span>
      <strong/>
      <strong/>
      <strong/>
      <strong/>
    </div>
    <table class="resource-table" cellspacing="0">
      <tr class="tr-header">
        <th>{{ Name }}</th>
        <th>{{ Num }}</th>
        <th>{{ Volumn }}</th>
      </tr>
      <!--      第一类-->
      <tr class="tr-body" v-if="resourceListTree[0].children">
        <td align="center">{{ resourceListTree[0].children[0].nodeName }}</td>
        <td align="center">
          <span v-if="resourceListTree[0].children[0].children">{{ resourceListTree[0].children[0].children.length }}类</span>
          <span v-else>无</span>
        </td>
        <td align="center">{{ totalCH.toFixed(2) }}GB</td>
      </tr>
      <tr v-else></tr>
      <!--第二类-->
      <tr class="tr-body" v-if="resourceListTree[0].children">
        <td align="center">{{ resourceListTree[0].children[1].nodeName }}</td>
        <td align="center">
          <span v-if="resourceListTree[0].children[1].children">{{ resourceListTree[0].children[1].children.length }}类</span>
          <span v-else>无</span>
        </td>
        <td align="center">{{ totalXZ.toFixed(2) }}GB</td>
      </tr>
      <tr v-else></tr>
      <!--      第三类-->
      <tr class="tr-body" v-if="resourceListTree[0].children">
        <td align="center">{{ resourceListTree[0].children[2].nodeName }}</td>
        <td align="center">
          <span v-if="resourceListTree[0].children[2].children">{{ resourceListTree[0].children[2].children.length }}类</span>
          <span v-else>无</span>
        </td>
        <td align="center">{{ totalGH.toFixed(2) }}GB</td>
      </tr>
      <tr v-else></tr>
      <!--    第四类  -->
      <tr class="tr-body" v-if="resourceListTree[0].children">
        <td align="center">{{ resourceListTree[0].children[3].nodeName }}</td>
        <td align="center">
          <span v-if="resourceListTree[0].children[3].children">{{ resourceListTree[0].children[3].children.length }}类</span>
          <span v-else>无</span>
        </td>
        <td align="center">{{ totalGL.toFixed(2) }}GB</td>
      </tr>
      <tr v-else></tr>

      <!--    第五类  -->
      <tr class="tr-body" v-if="resourceListTree[0].children">
        <td align="center">{{ resourceListTree[0].children[4].nodeName }}</td>
        <td align="center">
          <span v-if="resourceListTree[0].children[4].children">{{ resourceListTree[0].children[4].children.length }}类</span>
          <span v-else>无</span>
        </td>
        <td align="center">{{ totalGL.toFixed(2) }}GB</td>
      </tr>
      <tr v-else></tr>
      <!--      <tr class="tr-body" v-if="resourceListTree[0].children" v-for="item in resourceListTree[0].children">
              <td align="center">{{ item.nodeName }}</td>
              <td align="center">
                <span v-if="item.children">{{ item.children.length }}类</span>
                <span v-else>无</span>
              </td>
              <td align="center">{{ item.total }}GB</td>
            </tr>
            <tr v-else></tr>-->
    </table>
  </div>
</template>

<script>
import modelTitle from "@/views/mainHome/components/modelTitle";
import {getResourceByCodeTree, getResourceByDataType, listResourceByTree} from "@/api/system/resource";
export default {
  name: "natural",
  props:{
    typeCode:Number,
    dataType:Number
  },
  data(){
    return{
      type:'natural',
      titleInfo: {
        title: '自然资源',
        title_en: 'NATURAL RESOURCES'
      },
      panelResult:{
        className: '现状数据',
        classNum: 0,
        classVolumn: 0
      },
      totalCH:0.0,
      totalXZ:0.0,
      totalGH:0.0,
      totalGL:0.0,
      total:0,
      Name: '类名',
      Num: '类别数量',
      Volumn: '类别体量',
      resourceList:[],
      resourceListTree:[],
      index:0,
      resourceTable:[
        {
          className: '自然资源成果',
          classNum: '2',
          classVolumn: '3',
        },
        {
          className: '规划数据',
          classNum: '2',
          classVolumn: '3',
        },
        {
          className: '重要控制线',
          classNum: '3',
          classVolumn: '4',
        },
      ]
    }
  },
  components:{
    modelTitle
  },
  created() {
    listResourceByTree().then((res) => {
      if (res.code === 200) {
        this.resourceListTree = this.handleTreeRoot(res.rows, "resourceId",'parentId','children',0);
        // 过滤掉全景数据、倾斜摄影、视频、街景、场景数据等
        this.resourceListTree = this.resourceListTree[0].children.filter((item)=>{
          return item.parentCode === 3;
        });
        //重新排序树结构
        //this.resourceListTree = this.handleTreeRoot(this.resourceListTree, "resourceId",'parentId','children',0);
        this.resourceListTree[0].children.forEach(item => {
          // 设置种类初始体体量
          item.total = 0;
        })
        //console.log('11111111111111this.resourceListTree',this.resourceListTree[0].children);
        this.dataTotal();
        this.dataVolume();
        this.tableVolumeTotal();
      } else {
        this.$message({
          message: "未找到数据资源",
          type: "warning",
        });
      }
    });
  },
  methods:{
    dataTotal(){
      this.panelResult.classNum = this.resourceListTree[0].children.length;
    },
    dataVolume(){
      let dataTotalList = this.resourceListTree[0];
      this.run(dataTotalList);
      this.panelResult.classVolumn = this.total;
    },
    tableVolumeTotal(){
      let listCH = this.resourceListTree[0].children[0];
      this.getTotalCH(listCH);
      let listXZ = this.resourceListTree[0].children[1];
      this.getTotalXZ(listXZ);
      let listGH = this.resourceListTree[0].children[2];
      this.getTotalGH(listGH);
      let listGL = this.resourceListTree[0].children[3];
      this.getTotalGL(listGL);
    },
    getTotalCH(childrenList){
      childrenList.children.forEach((item) => {
        if(!item.children && item.dataVolume !== null && item.nodeStatus > 0){
          return this.totalCH += parseFloat(item.dataVolume);
        }
        this.getTotalCH(item);
        //console.log('this.totalCH',this.totalCH);
      })
    },
    getTotalXZ(childrenList){
      childrenList.children.forEach((item) => {
        if(!item.children && item.dataVolume !== null && item.nodeStatus > 0){
          return this.totalXZ += parseFloat(item.dataVolume);
        }
        this.getTotalXZ(item);
        //console.log('this.totalXZ',this.totalXZ);
      })
    },
    getTotalGH(childrenList){
      childrenList.children.forEach((item) => {
        if(!item.children && item.dataVolume !== null && item.nodeStatus > 0){
          return this.totalGH += parseFloat(item.dataVolume);
        }
        this.getTotalGH(item);
        //console.log('this.totalGH',this.totalGH);
      })
    },
    getTotalGL(childrenList){
      childrenList.children.forEach((item) => {
        if(!item.children && item.dataVolume !== null && item.nodeStatus > 0){
          return this.totalGL += parseFloat(item.dataVolume);
        }
        this.getTotalGL(item);
        //console.log('this.totalGL',this.totalGL);
      })
    },
    /*tableTotal(childrenList){
      childrenList.forEach((item,index) => {
        this.index = index;
        console.log('children',item,this.index);
        if(!item.children && item.dataVolume !== null){
          console.log(1,this.index,this.resourceListTree[0].children);
          return this.resourceListTree[0].children[this.index].total += parseFloat(item.dataVolume);
        }
        console.log(2);
        this.tableTotal(item.children);
        console.log('totalzz',this.index,this.resourceListTree[0].children[this.index].total);
      })

    },*/
    run(childrenList){
      childrenList.children.forEach((item) => {
        if(!item.children && item.dataVolume !== null && item.nodeStatus > 0){
          return this.total += parseFloat(item.dataVolume);
        }
        this.run(item);
      })
    }
  }
}
</script>

<style scoped>
.NaturalMapper{
  position: absolute;
  width: 89%;
  /*height:222px;*/
}
.panel-total{
  height:32px;
  width:100%;
  background:rgba(28, 214, 215, .1);
  position:relative;
  color:#FFF;
  line-height:32px;
  font-size:12px;
  margin-bottom:10px;
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
  left:105px;
  margin:auto;
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
.resource-table{
  font-size: 14px;
  display: inline-block;
  width:100%;
  /* height:126px;*/
  height:155px;
  overflow-y: auto;
  overflow-x: hidden;
}
.resource-table::-webkit-scrollbar {
  width: 4px;
}

/* 滚动槽 */
.resource-table::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
  border-radius: 10px;
}

/* 滚动条滑块 */
.resource-table::-webkit-scrollbar-thumb {
  border-radius: 2px;
  background: rgba(24, 247, 248, 0.3);
  -webkit-box-shadow: inset 0 0 6px rgba(22, 223, 226, 0.5);
}
.resource-table tr{
  height:30px;
}
.resource-table th{
  width:25%;
  font-size:12px;
}
.tr-header{
  background-image: linear-gradient(to right, rgba(28, 214, 215, 0),rgba(28, 214, 215, .5), rgba(28, 214, 215, 0));
}
.resource-table td{
  line-height:30px;
  font-size:12px;
}
.tr-body:nth-child(odd){
  background-image: linear-gradient(to right, rgba(28, 214, 215, 0),rgba(28, 214, 215, .2), rgba(28, 214, 215, 0));
}

</style>
