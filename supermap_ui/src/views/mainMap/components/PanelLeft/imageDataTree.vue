<template>
  <div class="dataTreeContainer">
    <el-input
      class="treeSearch"
      prefix-icon="el-icon-search"
      v-model="treeFilterText"
      placeholder="输入您要查询的内容"
    />
    <div class="treeDiv">
      <el-tree
        ref="dataTree"
        :data="dataTree"
        node-key="resourceId"
        :default-expand-all="true"
        show-checkbox
        :expand-on-click-node="true"
        :filter-node-method="filterNode"
        @check-change="handleCheckChange"
      >
          <span slot-scope="{ data }" class="custom-tree-node">
            <span>{{ data.nodeName }}</span>
            <span v-if="data.children">{{ data.children.length}}</span>
            <span v-else></span>
          </span>
      </el-tree>
    </div>
  </div>
</template>

<script>
  import { getResource, listResourceByTree } from "@/api/system/resource";
  import MapUtils from "@/utils/supermap";
  import $ from "jquery";
  import bus from "@/utils/bus";

  export default {
    name: "imageDataTree",
    props:{
      type:Number,
    },
    data(){
      return{
        dataTree:[],
        checkedKey:[],
        treeFilterText: "",
        parentId:0,
        markers: [
          require("../../../../assets/image/mapMarker/pos_3.png"),
        ],
        parentNodeName:''
      }
    },
    mounted() {
      bus.$on('clearChecked',this.clearChecked)
    },
    created() {
      let typeCode = this.type;
      listResourceByTree().then((res) => {
        if (res.code === 200) {
          this.dataTree = this.handleTree(res.rows, "resourceId",'parentId','children',0);
          // 过滤掉全景数据、倾斜摄影、视频、街景、场景数据等
          this.dataTree = this.dataTree.filter((item)=>{
            return item.parentCode === typeCode;
          })[0].children;
        } else {
          this.$message({
            message: "未找到数据资源",
            type: "warning",
          });
        }
      });
    },
    watch: {
      treeFilterText(val) {
        this.$refs.dataTree.filter(val);
      },
    },
    methods:{
      filterNode(value, data) {
        if (!value) return true;
        return data.nodeName.indexOf(value) !== -1;
      },
      handleCheckChange(data, checked) {
        // 树结构的单选
        if(checked){
          if(!data.children){
            const arr = [data.resourceId];
            this.checkedKey = arr;
            this.$refs.dataTree.setCheckedKeys(arr);
          }
        }
        if (data && data.dataType !== -1) {
          data.checked = !data.checked;
          this.handleResource(data);
        }
      },
      handleResource(data) {
        let parentNodeName;
        getResource(data.parentId).then(res => {
          parentNodeName =  res.data.nodeName;
          if(data && data.resource){
            data = data.resource;
            data.checked = !data.checked;
          }
          let dataMergeName = `${parentNodeName}-${data.nodeName}`;
          switch (data.dataType) {
            case 7:
              //console.log(data.checked ? "打开" : "关闭", "  :  矢量图层");
              // 先全幅青海
              //MapUtils.SceneUtil.entireView();
              // 测试点位
              MapUtils.SceneUtil.initCluser(data.checked, dataMergeName);
              break;
            default:
              break;
          }
        })
      },
      clearChecked(){
        //取消选中
        if(this.checkedKey.length > 0){
          this.$refs.dataTree.setCheckedNodes(this.checkedKey,false);
        }
        this.checkedAll = false;
        // 清除所有实体
        MapUtils.SceneUtil.clearEntity();
      }
    }
  }
</script>

<style scoped>
  .dataTreeContainer {
    height: calc(100% - 55px);
  }
  >>> .el-tabs__item{
    color:#fff;
  }
  >>> .el-tabs__item.is-active{
    color:rgb(24, 247, 248);
  }
  >>> .el-tabs__active-bar{
    width:56px;
    background-color:rgb(24, 247, 248);
  }
  >>> .el-tabs__nav-wrap::after{
    background-color:transparent;
  }
  >>> .el-tabs__nav{
    margin-left:38px;
  }
  .treeSearch {
    background:#fff;
    border-radius: 2px;
    width: 90%;
    margin: 16px 15px 10px;
  }

  .treeSearch >>> .el-input__inner {
    background: transparent;
    /*color: #16dfe2;*/
  }

  .treeDiv {
    height: calc(100% - 55px);
    width: 100%;
    padding: 0 15px 0 10px;
    overflow-x: hidden;
    overflow-y: auto;
  }

  .treeDiv::-webkit-scrollbar {
    /*滚动条整体样式*/
    width: 5px;
    height: 1px;
  }

  .treeDiv::-webkit-scrollbar-thumb {
    /*滚动条里面小方块*/
    border-radius: 5px;
    background: rgba(1, 1, 20, 0.9);
  }

  .treeDiv::-webkit-scrollbar-track {
    /*滚动条里面轨道*/
    border-radius: 5px;
    background: transparent;
  }

  .el-tree {
    background: transparent;
    color: white;
    height: calc(100% - 250px);
  }

  .el-tree >>> .el-tree-node__content:hover,
  .el-tree >>> .el-tree-node.is-current > .el-tree-node__content {
    background: rgba(24, 247, 248, 0.2);
    color: rgb(24, 247, 248);
  }

  .el-tree >>> .el-tree-node__expand-icon {
    pointer-events: none;
  }

  .el-tree >>> .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
</style>
