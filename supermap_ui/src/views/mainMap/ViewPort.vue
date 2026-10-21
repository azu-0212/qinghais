<template>
  <div class="viewport">
     <div id="mapScreen3D" />
      <MapHeader/>

      <!--测量按钮-->
      <div class="dropSelect">
        <transition name="el-zoom-in-bottom">
          <div v-show="true" class="menuTools">
            <div :class="['toolsBtn',{'active':btn.active}]"
                 v-for="(btn,index) in toolsBtn"
                 :key="index*1.4"
                 @mouseover="btn.hover=true"
                 @mouseout="btn.hover=false"
                 @click="handlerBtnClick(btn)"
            >
              <img v-if="btn.hover||btn.active" :src="btn.icon_active" alt="">
              <img v-else :src="btn.icon" alt="">
              <span>{{btn.name}}</span>
            </div>
          </div>
        </transition>
      </div>

     <div class="returnBtn" @click="back">
      <span>{{ returnPanel }}</span>
     </div>

    <!-- 左侧目录 -->
     <div class="panelWrapper left">
      <div class="panelContent">
        <hr class="regionSplit"/>
        <div class="pTitle">
          {{ panelTitle }}
        </div>
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
            @check-change="handleCheckLeft"
          >
        <span slot-scope="{ data }" class="custom-tree-node">
          <span>{{ data.nodeName }}</span>
          <span v-if="data.children">{{ data.children.length}}</span>
          <span v-else></span>
        </span>
          </el-tree>
        </div>
        </div>
      </div>
    </div>

    <!-- 右侧目录 -->
    <div class="panelWrapper right">
      <div class="panelContent">
        <hr class="regionSplit"/>
        <div class="pTitle">
          {{ panelTitle }}
        </div>
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
              @check-change="handleCheckRight"
            >
        <span slot-scope="{ data }" class="custom-tree-node">
          <span>{{ data.nodeName }}</span>
          <span v-if="data.children">{{ data.children.length}}</span>
          <span v-else></span>
        </span>
            </el-tree>
          </div>
        </div>
        <!--        <el-collapse v-model="activeName" accordion @change="handleChange">
                  <el-collapse-item
                    v-for="(item,index) in contrastParentList"
                    :title="item.nodeName"
                    :name="item.id"
                    :key="index">
                    <div class="radioBtns">
                      <el-checkbox @change="handleByImg">{{ dataType[0].val }}</el-checkbox>
                      <el-checkbox @change="handleByMVT">{{ dataType[1].val }}</el-checkbox>
                    </div>
                    <div v-if="!item.children.length">
                      <p class="warningMessage">{{ message }}</p>
                    </div>
                  </el-collapse-item>
                </el-collapse>-->
      </div>
    </div>

    <!--主办信息-->
    <hostInfo/>
  </div>
</template>

<script>
  import MapUtil from "@/utils/supermap";
  import MapHeader from "../../layout/mapLayout/components/MapHeader";
  import {Cesium} from "@/utils/supermap/consts";
  import {listContrast, listContrastByType} from "@/api/system/contrast";
  import {getResourceByCode, listResourceByTree} from "@/api/system/resource";
  import hostInfo from "@/views/mainMap/components/hostInfo/index";
  import MapUtils from "@/utils/supermap";

  export default {
    name: "ViewPort",
    components:{
      hostInfo,
      MapHeader
    },
    data(){
      return{
        dataType:[
          {key:"0",val:"影像数据",},
          {key:"2",val:"矢量数据",}
        ],
        markers: [
          require("@/assets/image/mapMarker/pos_0.png"),
          require("@/assets/image/mapMarker/pos_1.png"),
          require("@/assets/image/mapMarker/pos_2.png"),
        ],
        dataTree:[],
        dataList:[],
        treeFilterText: "",
        dataTypeId:0,
        contrastType:0,
        returnPanel:"返回",
        panelTitle:"[资源对比]",
        activeName: '',
        contrastParentList:[],
        message:'未找到数据',
        checked: true,
        checkedLeft:true,
        checkedRight:true,
        contrastOptions:[],
        contrastStrArr:[],
        flag:false,
        toolsBtn: [
          {
            name: '测距',
            icon: require('@/assets/image/mapTools/mDis.png'),
            icon_active: require('@/assets/image/mapTools/mDis_active.png'),
            active: false,
            hover: false
          },
          {
            name: '测面积',
            icon: require('@/assets/image/mapTools/mArea.png'),
            icon_active: require('@/assets/image/mapTools/mArea_active.png'),
            active: false,
            hover: false
          },
          {
            name: '测高',
            icon: require('@/assets/image/mapTools/mHigh.png'),
            icon_active: require('@/assets/image/mapTools/mHigh_active.png'),
            active: false,
            hover: false
          },
          {
            name: '清除',
            icon: require('@/assets/image/mapTools/mEntire.png'),
            icon_active: require('@/assets/image/mapTools/mEntire_active.png'),
            active: false,
            hover: false
          }
        ],
      }

    },
    watch: {
      treeFilterText(val) {
        this.$refs.dataTree.filter(val);
      },
    },
    mounted() {
      MapUtil.SceneScreenUtil.initScene()
      MapUtil.SceneScreenUtil.initMultiViewPort();
      listResourceByTree().then((res) => {
        if (res.code === 200) {
          this.dataTree = this.handleTreeRoot(res.rows, "resourceId",'parentId','children',0);
          // 过滤掉全景数据
          this.dataTree = this.dataTree[0].children.filter((item)=>{
            return item.parentCode !== 2 && item.parentCode !== 5 && item.parentCode !== 9;
          });

          //重新排序树结构
          this.dataTree = this.handleTreeRoot(this.dataTree, "resourceId",'parentId','children',0);
        } else {
          this.$message({
            message: "未找到数据资源",
            type: "warning",
          });
        }
      });
    },
    created() {
      // 获取对比列表
      listContrast().then(response => {
        this.contrastParentList = this.handleTree(response.rows, 'id');
      });

    },
    methods:{
      filterNode(value, data) {
        if (!value) return true;
        return data.nodeName.indexOf(value) !== -1;
      },
      handleCheckLeft(data){
        if (data && data.dataType !== -1) {
          data.checkedLeft = !data.checkedLeft;
          this.handleResourceLeft(data);
        }
      },
      handleCheckRight(data){
        if (data && data.dataType !== -1) {
          data.checkedRight = !data.checkedRight;
          this.handleResourceRight(data);
        }
      },
      handleResourceLeft(data){
        let _this = this;
        let dataArray = [];
        dataArray.push(data);
        MapUtil.SceneScreenUtil.clearEntity();
        let dataId = `${data.nodeName}-${data.resourceId}`;
        let port = 1;
        switch (data.dataType) {
          case 1:
            console.log(data.checkedLeft ? "打开1" : "关闭1", "  :  scp图层");
            setTimeout(function () {
              MapUtil.SceneScreenUtil.add3DMarkersToMap(dataArray, _this.markers[0], data.checkedLeft); //加图标
            }, 100)
            MapUtils.SceneScreenUtil.addS3MTilesLayerByScp(
              data,
              JSON.parse(data.position),
              dataId,
              data.serverUrl,
              data.checkedLeft,
              port
            );
            break;
          case 2:
            console.log(data.checkedLeft ? "打开2" : "关闭2", "  :  矢量图层");
            // 先全幅青海
            //MapUtils.SceneScreenUtil.entireView();
            MapUtils.SceneScreenUtil.addMvtByUrl(dataId, data.serverUrl, data.checkedLeft,port);
            break;
          case 0:
            console.log(data.checked ? "打开0" : "关闭0", "  :  影像图层");
            // 全副青海
            //MapUtils.SceneScreenUtil.entireView();
            MapUtils.SceneScreenUtil.addImageLayerByUrl(
              dataId,
              data.serverUrl,
              data.checkedLeft,
              port
            );
        }
      },
      handleResourceRight(data){
        let _this = this;
        let dataArray = [];
        dataArray.push(data);
        MapUtil.SceneScreenUtil.clearEntity();
        let dataId = `${data.nodeName}-${data.resourceId}`;
        let port = 0;
        switch (data.dataType) {
          case 1:
            console.log(data.checkedRight ? "打开2" : "关闭2", "  :  scp图层");
            setTimeout(function () {
              MapUtil.SceneScreenUtil.add3DMarkersToMap(dataArray, _this.markers[0], data.checkedRight); //加图标
            }, 100)
            MapUtils.SceneScreenUtil.addS3MTilesLayerByScp(
              dataId,
              data.serverUrl,
              data.checkedRight,
              port
            );
            break;
          case 2:
            console.log(data.checkedRight ? "打开2" : "关闭2", "  :  矢量图层");
            // 先全幅青海
            //MapUtils.SceneScreenUtil.entireView();
            MapUtils.SceneScreenUtil.addMvtByUrl(dataId, data.serverUrl, data.checkedRight);
            break;
          case 0:
            //console.log(data.checked ? "打开0" : "关闭0", "  :  影像图层");
            console.log(data.checkedRight ? "打开0" : "关闭0", "  :  影像图层");
            MapUtils.SceneScreenUtil.addImageLayerByUrl(
              dataId,
              data.serverUrl,
              data.checkedRight,
              port
            );
        }
      },
      // 清除工具active
      removeToolsBtns(){
        this.toolsBtn.filter(item=>{
          return item.name.indexOf('时间轴')===-1
        }).map(item => {
          item.active = false;
        })
      },

      handlerBtnClick(btn) {
        this.removeToolsBtns();
        switch (btn.name) {
          case '测距':
            MapUtil.SceneUtil.measure3DUtil.activateMeasure3DControl("DIS")
            btn.active = true
            break
          case '测面积':
            MapUtil.SceneUtil.measure3DUtil.activateMeasure3DControl("AREA")
            btn.active = true
            break
          case '测高':
            MapUtil.SceneUtil.measure3DUtil.activateMeasure3DControl("HEIGHT")
            btn.active = true
            break
          case '清除':
            MapUtil.SceneUtil.measure3DUtil.deActiveAllMeasure3DControl()
            btn.active = true
            break
          default:
            break
        }
      },


      handleByImg(flag){
        let dataType = this.dataType[0].key;
        this.handleByList(dataType,flag);
      },
      handleByMVT(flag){
        let dataType = this.dataType[1].key;
        this.handleByList(dataType,flag);
      },
      handleByList(dataType,flag){
        let parentId = this.contrastType;
        listContrastByType(parentId,dataType).then(response => {
          this.contrastStrArr = response.rows;
          MapUtil.SceneScreenUtil.multiViewPort(this.contrastStrArr,flag);
        })
      },
      handleChange(typeValue) {
        this.contrastType = typeValue;
      },


      back(){
        this.$router.push({ path: '/home/index' });
      }
    }
  }
</script>

<style scoped>
  .dropSelect {
    position: absolute;
    left: 700px;
    top: 20px;
    z-index:10;
  }
  .viewport{
    position: absolute;
    width: 100%;
    height:100%;
    color:#fff;
    z-index: 0;
  }
  #mapScreen3D {
    position: absolute;
    width: 100%;
    height: 100%;
    z-index: 9;
  }
  .dataTreeContainer {
    height: calc(100% - 55px);
  }
  .treeSearch {
    background:#fff;
    border-radius: 2px;
    width: 90%;
    margin: 16px 15px 0;
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

  .menuTools {
    width:328px;
    height: 50px;
    border-radius: 5px;
    background: rgba(1, 1, 20, 0.6);
    box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
    pointer-events: auto;
    cursor: pointer;
  }
  .toolsBtn {
    display: flex;
    float:left;
    color: white;
    margin:16px 0;
    font-size: 14px;
    padding:0 16px;
    border-right:1px solid #5a5e61;
  }
  .menuTools .panelsBtn{
    margin-right:-10px;
  }
  .menuTools :last-child{
    border:none;
    padding-right:0;
  }
  .toolsBtn:hover, .menuBtn:hover,
  .toolsBtn.active, .menuBtn.active {
    color: #1ce5e6;
  }


  .returnBtn{
    display: flex;
    color: white;
    position: absolute;
    right:25px;
    top:10px;
    width: 60px;
    height: 35px;
    background: rgba(1, 1, 20, 0.6);
    box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
    border-radius: 5px;
    pointer-events: auto;
    cursor: pointer;
    z-index:999;
  }
  .returnBtn span{
    display: inline-block;
    margin: 0 auto;
    line-height: 35px;
    font-size: 14px;
    font-weight: bolder;
  }
  .returnBtn:hover{
    color: #1ce5e6;
  }
  .panelWrapper{
    position: absolute;
    top: 0;
    height: inherit;
    cursor: pointer;
    pointer-events: none;
    z-index:999;
  }
  .panelWrapper.left{
    left: 25px;
  }
  .panelWrapper.right{
    right: 25px;
  }
  .panelContent{
    float: left;
    width: 250px;
    margin-top: 75px;
    height: calc(100% - 80px);
    background: rgba(1, 1, 20, 0.6);
    box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
    pointer-events: auto;
  }
  .regionSplit{
    margin: 0;
    padding: 0;
    background: rgb(24, 247, 248);
    border: 0;
    height: 3px;
  }
  .pTitle {
    height: 40px;
    color: white;
    font-size: 16px;
    text-align: center;
    line-height: 40px;
    font-weight: bolder;
    letter-spacing: 2px;
  }

  .el-collapse{
    padding: 0 15px 20px 10px;
    border:0;
  }
  .el-collapse-item{
    background: transparent;
    color:#fff;
    height: calc(100% - 250px);
  }
  .el-collapse-item >>> .el-collapse-item__header{
    color:#fff;
    background: transparent;
    height:36px;
    line-height:36px;
    border:0;
  }
  .el-collapse-item >>> .el-collapse-item__header:hover{
    background: rgba(24, 247, 248, 0.2);
    color: rgb(24, 247, 248);
  }
  .el-collapse-item >>> .el-collapse-item__header{
    color:#fff;
    font-size:16px;
  }
  .el-collapse-item >>> .el-collapse-item__wrap{
    background-color:transparent;
    border:0;
  }
  .el-collapse-item >>> .el-collapse-item__wrap .el-collapse-item__content{
    padding:10px 0 -10px 0;
  }
  .warningMessage{
    color:#c3c3c3;
    text-align: center;
    font-size:14px;
  }
  .radioBtns{

  }
  .el-checkbox{
    color:#fff;
  }
</style>
