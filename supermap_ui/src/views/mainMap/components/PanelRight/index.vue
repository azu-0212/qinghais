<template>
  <div class="panelWrapper">
    <div class="buttonsWrapper">
      <div
        v-for="(btn, index) in buttons"
        v-if="btn.show"
        :key="index * 1.1"
        class="buttons"
        :class="{ active: btn.choose }"
        @mouseover="btn.active = true"
        @mouseout="btn.active = false"
        @click="btnClick(btn, index)"
      >
        <img v-if="btn.choose || btn.active" :src="btn.icon_active" />
        <img v-if="!(btn.choose || btn.active)" :src="btn.icon" />
        <p>{{ btn.name }}</p>
      </div>
    </div>
    <transition name="el-fade-in-linear">
      <div class="panelContent" v-show="panelShow">
        <hr class="regionSplit" />
        <div class="pTitle">
          {{button.name}}
          <!--          {{
                      buttons.filter((item) => {
                        return item.choose;
                      })[0].title
                    }}-->
          <span
            class="closeBtn el-icon-close"
            @click="panelShow = false"
          ></span>
        </div>
        <!--自然资源弹框-->
        <DataCatalogTree v-show="buttons[0].choose" :type="buttons[0].typeCode" />
        <!-- 权限分配 -->
        <!--新型基础测绘弹框-->
        <BasicMapTree v-if="buttons[1].choose"/>
        <!-- 三维立体时空数据库 -->
        <DimensionalSpaceTree v-if="buttons[2].choose"/>

        <!--热点飞行弹框-->
        <FlyRouteList v-show="buttons[3].choose" v-if="buttons[3].choose"/>
        <!--重点区域弹框-->
        <PointAreaList v-show="buttons[4].choose" v-if="buttons[4].choose"/>
        <!--三维分析弹框-->
        <Analysis3D v-if="buttons[5].choose"></Analysis3D>
        <!--        <RealEstate v-if="buttons[4].choose" />-->
        <div
          class="trianglePointer"
          :style="`transform:translateY(${selBtnIndex * 80}px)`"
        ></div>
      </div>
    </transition>
  </div>
</template>

<script>
import DataCatalogTree from "./DataCatalogTree";
import BasicMapTree from "./BasicMapTree";
import FlyRouteList from "./FlyRouteList";
import PointAreaList from "./PointAreaList";
import RealEstate from "./RealEstate";
import Analysis3D from "./Analysis3D";
import DimensionalSpaceTree from "./DimensionalSpaceTree";
import MapUtil from "@/utils/supermap";
import bus from "@/utils/bus";
import {listResourceByTree} from "@/api/system/resource";
import store from "@/store";

export default {
  name: "PanelRight",
  components: {
    PointAreaList,
    FlyRouteList,
    DataCatalogTree,
    RealEstate,
    Analysis3D,
    BasicMapTree,
    DimensionalSpaceTree
  },
  data() {
    return {
      panelShow: false,
      resourceCheckedKey:[],
      button:{
        name:"[无内容]"
      },
      buttons: [
        {
          name: "自然资源",
          typeCode:3,
          icon: require("@/assets/image/mainPage/dataSource.png"),
          icon_active: require("@/assets/image/mainPage/dataSource_active.png"),
          active: false,
          choose: false,
          title: "[自然资源一体化数据]",
          show:false
        },
        {
          name: "新型基础测绘",
          typeCode:3,
          icon: require("@/assets/image/mainPage/sanweiIcon.png"),
          icon_active: require("@/assets/image/mainPage/sanweiIcon.png"),
          active: false,
          choose: false,
          title: "[新型基础测绘数据]",
          show:false
        },
        {
          name: "三维立体时空数据库",
          typeCode:3,
          icon: require("@/assets/image/mainPage/litiIcon.png"),
          icon_active: require("@/assets/image/mainPage/litiIcon.png"),
          active: false,
          choose: false,
          title: "[三维立体时空数据库数据]",
          show:false
        },
        {
          name: "热点飞行",
          icon: require("@/assets/image/mainPage/flyRoute.png"),
          icon_active: require("@/assets/image/mainPage/flyRoute_active.png"),
          active: false,
          choose: false,
          title: "[热点飞行数据]",
          show:true
        },
        {
          name: "重点区域",
          icon: require("@/assets/image/mainPage/pointArea.png"),
          icon_active: require("@/assets/image/mainPage/pointArea_active.png"),
          active: false,
          choose: false,
          title: "[重点区域]",
          show:true
        },
        {
          name: "三维分析",
          icon: require("@/assets/image/analysis/analysis3DIcon.png"),
          icon_active: require("@/assets/image/analysis/analysis3DIcon_active.png"),
          active: false,
          choose: false,
          title: "[三维分析面板]",
          show:true
        },
        /*{
          name: "不动产",
          // name: "不动产专题",
          icon: require("@/assets/image/analysis/bdc.png"),
          icon_active: require("@/assets/image/analysis/bdc_active.png"),
          active: false,
          choose: false,
          title: "不动产专题",
        },*/
      ],
      selBtnIndex: 0,
      userRole:this.$store.state.user.roles
    };
  },
  beforeMount() {
    // 根据不同权限展示不同按钮
    // 判断当前用户是否已拉取完user_info信息
    /* 管理员权限 */
    if(this.userRole[0].indexOf("admin")!==-1){
      this.buttons[0].show = true;
      this.buttons[1].show = true;
      this.buttons[2].show = true;
    }
    /* 自然资源权限 */
    if(this.userRole[0].indexOf("natural")!==-1){
      this.buttons[0].show = true;
    }
    /* 实景三维权限 */
    if(this.userRole[0].indexOf("real")!==-1){
      this.buttons[1].show = true;
    }
    /* 三维立体时空数据库权限 */
    if(this.userRole[0].indexOf("dimensionalSpace")!==-1){
      this.buttons[2].show = true;
    }
  },
  mounted() {
    bus.$on('closeRightPanel', this.closeRightPanel)
  },
  created() {
    this.getQuery();
  },
  methods: {
    btnClick(btn, index) {
      this.selBtnIndex = index;
      this.buttons.map((item) => {
        item.choose = false;
        item.active = false;
      });
      this.button.name = btn.title;
      if (btn.name === "热点飞行") {
        this.$store.commit("supermap/SET_FLY_ROUTE_SHOW", true);
      } else {
        this.$store.commit("supermap/SET_FLY_ROUTE_SHOW", false);
      }
      btn.choose = true;
      this.panelShow = true;
      //清楚弹出框
      MapUtil.SceneUtil.popup.closeAllPopup();
      // 清除所有实体
      MapUtil.SceneUtil.clearEntity();
      // 清楚选中的所有MVT矢量瓦片
      MapUtil.SceneUtil.removeMVTLayersAll();
      //清除选中的所有影像图层
      MapUtil.SceneUtil.removeImageLayersAll();
      //清楚测量工具
      MapUtil.SceneUtil.measure3DUtil.deActiveAllMeasure3DControl();
      if (btn.name !== "三维分析") {
        //清除选中的所有倾斜摄影
        //MapUtil.SceneUtil.removeScpLayersAll();
        // 取消已选中的结构树
        bus.$emit('clearChecked')
      }
      //清楚所有照片库实体图标等信息
      MapUtil.SceneUtil.destroyDynamicCluser();
      // 清除高亮
      MapUtil.SceneUtil.clearHighLightAll();
      //清除照片类型坐标
      MapUtil.SceneUtil.getPositionType = "";
      //清楚所有照片库实体图标等信息
      MapUtil.SceneUtil.destroyDynamicCluser();
      // 隐藏左侧面板
      bus.$emit('closeLeftPanel')
      // 清除兴趣点输入框内容
      bus.$emit('removeRegionSearch')
    },
    getQuery(){
      if(this.$route.query.type && this.$route.query.type.indexOf('natural') !==-1){
        this.buttons.map((item) => {
          item.choose = false;
          item.active = false;
        });
        this.buttons[0].choose = true;
        this.buttons[0].active = true;
        this.panelShow = true;
        this.button.name = "自然资源一体化数据";
        return;
      }
    },
    closeRightPanel(){
      this.panelShow = false;
      this.buttons.map((item) => {
        item.choose = false;
        item.active = false;
      });
    }
  },
};
</script>

<style scoped>
.panelWrapper {
  position: absolute;
  top: 0;
  right: 0;
  width: 396px;
  height: 100%;
  cursor: pointer;
  pointer-events: none;
}

@keyframes moveIn {
  from {
    right: -300px;
  }
  to {
    right: 0;
  }
}

@keyframes moveOut /* Safari 与 Chrome */ {
  from {
    right: 0;
  }
  to {
    right: -300px;
  }
}

.panelIn {
  animation: moveIn 0.4s ease forwards;
}

.panelOut {
  animation: moveOut 0.4s ease forwards;
}

.buttonsWrapper {
  margin-top: 70px;
  float: right;
  width: 88px;
  height: 100%;
}

.panelContent {
  float: left;
  width: 300px;
  margin-top: 100px;
  height: calc(100% - 110px);
  background: rgba(1, 1, 20, 0.6);
  box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
  pointer-events: auto;
}

.pTitle {
  position:relative;
  height: 40px;
  color: white;
  font-size: 16px;
  text-align: center;
  line-height: 40px;
  font-weight: bolder;
  letter-spacing: 2px;
}

.regionSplit {
  margin: 0;
  padding: 0;
  background: rgb(24, 247, 248);
  border: 0;
  height: 3px;
}

/*.pTitle:hover .closeBtn {
  opacity: 1;
}*/

.closeBtn {
  position:absolute;
  right:0;
  top:0;
  display: block;
  width: 40px;
  height: 40px;
  text-align: center;
  line-height: 40px;
  font-size: 26px;
  color: #18f7f8;
  opacity: 1;
}

.buttons {
  margin: 14px 0 0 2px;
  width: 60px;
  height: 60px;
  color: white;
  border-radius: 10px;
  background: rgba(1, 1, 20, 0.6);
  box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
  pointer-events: auto;
}

.buttons:nth-child(1) {
  margin-top: 30px;
}
.buttons:hover,
.buttons.active,
.buttons.choose {
  color: rgb(24, 247, 248);
}

.buttons img {
  margin: 5px 17px;
  width: 26px;
}

.buttons p {
  margin: 0;
  text-align: center;
  font-size: 10px;
}

/*.trianglePointer {
  position: absolute;
  right: 80px;
  top: 45px;
  transform: translate(50, 0);
  border-width: 10px;
  border-style: solid;
  border-color: transparent transparent transparent rgba(1, 1, 20, 0.6);
}*/
</style>
