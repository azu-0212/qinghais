<template>
  <!-- 三维立体时空数据库 -->
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
import { getRolesResourceList } from "@/api/system/resource";
import {mapState} from "vuex";
import MapUtils from "@/utils/supermap";

export default {
  name: "DimensionalSpaceTree",
  data(){
    return{
      treeFilterText:"",
      dataTree: [],
      sourceTree:[],
      resourceCheckedKey:[],
      markers: [
        require("../../../../assets/image/mapMarker/pos_0.png"),
        require("../../../../assets/image/mapMarker/pos_1.png"),
        require("../../../../assets/image/mapMarker/pos_2.png"),
        require("../../../../assets/image/mapMarker/pos_3.png"),
      ],
      checkedKey:[],
      userRole:this.$store.state.user.roles,
      role: ''
    }
  },
  mounted() {
    this.role = this.appCfg.screenTitles[1].name;
    getRolesResourceList(this.role).then(res => {
      if (res.code === 200) {
        this.dataTree = this.handleTree(res.rows, "resourceId", 'parentId', 'children',res.rows[0].parentId);
      } else {
        this.$message({
          message: "未找到数据资源",
          type: "warning",
        });
      }
    });
  },
  methods:{
    filterNode(value, data) {
      console.log('value',value);
      if (!value) return true;
      return data.nodeName.indexOf(value) !== -1;
    },
    handleCheckChange(data, checked) {
      // 树结构的单选
      if(checked){
        if(!data.children){
          const arr = [data.resourceId];
          this.checkedKey = arr;
          //this.$refs.dataTree.setCheckedKeys(arr,true);  //取消单选
        }
      }
      if (data && data.dataType !== -1) {
        data.checked = !data.checked;
        this.handleResource(data);
      }

    },
    handleResource(data) {
      let dataId;
      if(data && data.resource){
        data = data.resource;
        data.checked = !data.checked;
      }
      dataId = `${data.nodeName}-${data.resourceId}`;
      switch (data.dataType) {
        case 0:
          console.log(data.checked ? "打开" : "关闭", "  :  影像图层");
          MapUtils.SceneUtil.addImageLayerByUrl(
            dataId,
            data.serverUrl,
            data.checked
          );
          break;
        case 1:
          //console.log(data.checked ? "打开" : "关闭", "  :  scp图层");
          MapUtils.SceneUtil.addS3MTilesLayerByScp(
            dataId,
            data.serverUrl,
            data.checked
          );
          break;
        case 2:
          //console.log(data.checked ? "打开" : "关闭", "  :  矢量图层");
          // 先全幅青海
          MapUtils.SceneUtil.entireView();
          MapUtils.SceneUtil.addMvtByUrl(
            dataId,
            data.serverUrl,
            data.checked,
            {
              width:300,
              height:218,
              marker: this.markers[3],
              html:`<div id="bubble" class="bubble" style="bottom:0;top: 0;">
                      <div id="tools" style="text-align : right;margin-bottom: 10px;">
                          <span style="color: #fff;padding: 5px;position: absolute;left: 10px;top: 4px;">对象属性</span>
                          <span class="fui-cross" title="关闭" id="close" style="color: #fff;padding:5px"></span>
                      </div>
                      <div style="overflow-y:auto;" id="tableContainer">
                          <table id="tab"></table>
                      </div>
                  </div>`,
              offsets:[150, 230]
            },
            "MVTType"
          );
          break;
        case 8:
          console.log(data.checked ? "打开" : "关闭", "  :  栅格图层");
          MapUtils.SceneUtil.addImageGridLayerByUrl(
            dataId,
            data.serverUrl,
            data.checked,
            data,
            "gridType",
            {
              width:300,
              height:218,
              marker: this.markers[3],
              html:`<div id="bubble" class="bubble" style="bottom:0;top: 0;">
                      <div id="tools" style="text-align : right;margin-bottom: 10px;">
                          <span style="color: #fff;padding: 5px;position: absolute;left: 10px;top: 4px;">对象属性</span>
                          <span class="fui-cross" title="关闭" id="close" style="color: #fff;padding:5px"></span>
                      </div>
                      <div style="overflow-y:auto;" id="tableContainer">
                          <table id="tab"></table>
                      </div>
                  </div>`,
              offsets:[150, 230]
            }
          );
          break;
        case 9: // 行政区划
          //console.log(data.checked ? "打开" : "关闭", "  :  场景");
          MapUtils.SceneUtil.add3DSceneLayerByScp(
            data,
            data.checked
          );
          break;
        default:
          break;
      }
    },
  },
  computed: {
    ...mapState({
      appCfg: state => state.app.appCfg
    })
  },
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
  margin-left:54px;
}
.treeSearch {
  background:#fff;
  border-radius: 2px;
  width: 90%;
  margin: 16px 15px 0;
}

.treeSearch >>> .el-input__inner {
  background: transparent;
  /*color: #16dfe2;*/
}

.treeDiv {
  height: calc(100% - 55px);
  width: 100%;
  padding: 0 4px 0 10px;
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
.el-checkbox{
  margin-left:24px;
  color:#fff;
}
</style>
