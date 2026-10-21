<template>
  <div class="dropSelect" ref="dropSelect">
    <!--    <div style="border:1px solid red;width:400px;height:300px;">
          <iframe src="http://10.63.1.71:8080/datong201611/StreetView.html" id="iframe"></iframe>
        </div>-->
    <transition name="el-zoom-in-bottom">
      <div v-show="true" class="menuTools">
        <!--西宁市按钮-->
        <div :class="['toolsBtn',{'active':pointBtn.active}]"
             @mouseover="pointBtn.hover=true"
             @mouseout="pointBtn.hover=false"
             @click="dropTitleClick">
          <span class="toolsName">{{pointBtn.name}}</span>
          <img v-if="pointBtn.hover || pointBtn.active" :class="['panelsBtn',{'img-active':pointBtn.active}]" :src="pointBtn.icon_hover" alt="">
          <img class="panelsBtn" v-else :src="pointBtn.icon" alt="">
        </div>
        <!--地理单元按钮-->
        <div :class="['toolsBtn',{'active':unitBtn.active}]"
             @mouseover="unitBtn.hover=true"
             @mouseout="unitBtn.hover=false"
             @click="dropUnitClick"
        >
          <span class="toolsName">{{unitBtn.name}}</span>
          <img v-if="unitBtn.hover || unitBtn.active" :class="['panelsBtn',{'img-active':unitBtn.active}]" :src="unitBtn.icon_hover" alt="">
          <img class="panelsBtn" v-else :src="unitBtn.icon" alt="">
        </div>
        <!--测量按钮-->
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
    <!--西宁市弹出框-->
    <transition name="el-fade-in-linear">
      <div v-show="showContent" class="dropContent">
        <div class="regionCity">
          <div class="regionCityName">青海省</div>
          <div>
            <el-button class="regionOption" @click="selectedChanged('全幅')"
            >全幅</el-button
            >
            <el-button class="regionOption" @click="selectedChanged('清除')"
            >清除</el-button
            >
          </div>
        </div>
        <div
          class="regionCity"
          v-for="group in options[0].children"
          :key="group.id"
        >
          <div class="regionCityName">{{group.name }}</div>
          <div>
            <el-button
              v-for="item in group.children"
              @click="
                selectedChanged(item.code, '', `-${item.name}`, `${group.name}`, pointBtn.type)
              "
              @mouseenter.native="selOptionEnter($event, item, group.name)"
              class="regionOption"
              :key="item.code"
              slot="reference"
            >{{ item.name }}
            </el-button>
          </div>
        </div>
        <div
          class="townWrapper"
          v-show="townWrapperShow"
          @mouseleave="selOptionOut"
          :style="`transform:translateY(${townWrapperTop + 20}px)`"
        >
          <div
            class="trianglePointer"
            :style="`transform:translateX(${townWrapperPointLeft}px)`"
          ></div>
          <el-button
            v-for="item in townContent"
            :key="item.code"
            class="regionOption"
            @click="
              selectedChanged(
                item.code,
                `-${item.name}`,
                `-${countryName}`,
                `${cityName}`
              )
            "
          >
            {{ item.name }}
          </el-button>
        </div>
      </div>
    </transition>
    <!--地理单元弹出框-->
    <transition name="el-fade-in-linear">
      <div v-show="showUnitContent" class="dropContent dropUnit">
        <div class="regionCity">
          <div class="regionCityName">青海省</div>
          <div>
            <el-button class="regionOption" @click="selectedUnitChanged('全幅')"
            >全幅</el-button
            >
            <el-button class="regionOption" @click="selectedUnitChanged('清除')"
            >清除</el-button
            >
          </div>
        </div>
        <div class="regionCity">
          <div
            class="regionCity"
            v-for="group in unitOptions"
            :key="group.id"
          >
            <div class="regionCityName">{{ group.nodeName }}</div>
            <!--          <hr class="regionSplit" />-->
            <div>
              <el-button
                v-for="item in group.children"
                @click="
                selectedUnitChanged('',item, `-${item.nodeName}`,unitBtn.type)
              "
                @mouseenter.native="selOptionEnter($event, item, group.nodeName)"
                class="regionOption"
                :key="item.code"
                slot="reference"
              >{{ item.nodeName }}
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { listDivision } from "@/api/system/division";
import { listUnit } from "@/api/system/unit"
import MapUtil from "../../../utils/supermap/index";
import $ from "jquery";
import sceneUtil from "@/utils/supermap/map3D";
import MapUtils from "@/utils/supermap";
import bus from "@/utils/bus";
import store from "@/store";

export default {
  name: "RegionSelect",
  data() {
    return {
      ready: false,
      options: [{ children: undefined }],
      unitOptions:[{ children: undefined }],
      regionValue: "西宁市",
      showContent: false,
      showUnitContent:false,
      menuButton: false,
      townWrapperShow: false,
      townWrapperTop: 0,
      townWrapperPointLeft: 0,
      townContent: [],
      cityName: "",
      countryName: "",
      pointBtn: {
        name: '西宁市',
        active: false,
        hover: false,
        icon: require('@/assets/image/mapTools/arrawRight.png'),
        icon_hover: require('@/assets/image/mapTools/arrawRight_active.png'),
        type: "point"
      },
      unitBtn:{
        name: '地理单元',
        active: false,
        hover: false,
        icon: require('@/assets/image/mapTools/arrawRight.png'),
        icon_hover: require('@/assets/image/mapTools/arrawRight_active.png'),
        type: "unit"
      },
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
          name: '对比',
          icon: require('@/assets/image/mapTools/mChange.png'),
          icon_active: require('@/assets/image/mapTools/mChange_active.png'),
          active: false,
          hover: false
        },
        {
          name: '清除',
          icon: require('@/assets/image/mapTools/mEntire.png'),
          icon_active: require('@/assets/image/mapTools/mEntire_active.png'),
          active: false,
          hover: false
        },
        {
          name: '全幅',
          icon: require('@/assets/image/mapTools/mapEntireView.png'),
          icon_active: require('@/assets/image/mapTools/mapEntireView_active.png'),
          active: false,
          hover: false
        }
      ],
      userRole:this.$store.state.user.roles
    };
  },
  mounted() {
    listDivision().then(res => {
      this.options = this.handleTree(res.data);
      this.ready = true;
    });
    listUnit().then(res => {
      this.unitOptions = this.handleTree(res.rows);
    });
    // 根据不同权限展示按钮
    // 判断当前用户是否已拉取完user_info信息
    /* 管理员权限 */
    if (this.userRole[0].indexOf("admin")!== -1) {
      return;
    } else {
      this.$refs['dropSelect'].style.right = '160px';
    }
  },
  methods: {
    /* 西宁市触发 */
    dropTitleClick() {
      this.showUnitContent = false;
      this.unitBtn.active = false;
      this.removeToolsBtns();

      this.showContent = !this.showContent;
      this.townWrapperShow = false;
      this.menuButton = !this.menuButton;
      this.menuButton = false;
      this.pointBtn.active = !this.pointBtn.active;
    },
    /* 地理单元触发 */
    dropUnitClick(){
      this.showContent = false;
      this.pointBtn.active = false;
      this.removeToolsBtns();

      this.showUnitContent = !this.showUnitContent;
      this.unitBtn.active = !this.unitBtn.active;
    },
    selectedChanged(val, town, county, city, type) {
      if (val === "全幅") {
        MapUtil.SceneUtil.entireView();
        MapUtil.SceneUtil.clearEntity();
        this.regionValue = "西宁市";
      } else if (val === "清除") {//一键清除
        //清除气泡
        MapUtil.SceneUtil.popup.closeAllPopup();
        //清除实体
        MapUtil.SceneUtil.clearEntity();
        //全幅青海
        MapUtil.SceneUtil.entireView();
        //清除选中的所有影像图层
        MapUtil.SceneUtil.removeImageLayersAll();
        this.pointBtn.name = "西宁市";
        this.regionValue = "请选择";
      } else {
        //清除选中的所有影像图层
        MapUtil.SceneUtil.removeImageLayersAll();
        MapUtil.SceneUtil.queryArea(val);
        this.regionValue = `${city}${county}${town}`;
        if(type === "point"){
          this.pointBtn.name = county.slice(1);
        }
      }
      // 取消已选中的结构树
      bus.$emit('clearChecked')
      //清楚所有照片库实体图标等信息
      MapUtil.SceneUtil.destroyDynamicCluser();

      this.showContent = false;
      this.showUnitContent = false;
      this.pointBtn.active = false;
      this.unitBtn.active = false;
    },
    selectedUnitChanged(val, data, county, type){
      let dataId;
      if (val === "全幅") {
        MapUtil.SceneUtil.entireView();
        MapUtil.SceneUtil.clearEntity();
        this.regionValue = "西宁市";
      } else if (val === "清除") {//一键清除
        //清除气泡
        MapUtil.SceneUtil.popup.closeAllPopup();
        //清除实体
        MapUtil.SceneUtil.clearEntity();
        //全幅青海
        MapUtil.SceneUtil.entireView();
        //清除选中的所有影像图层
        MapUtil.SceneUtil.removeImageLayersAll();
        // 遍历清除所有scp倾斜摄影
        MapUtils.SceneUtil.removeScpLayersAll();
        this.unitBtn.name = "地理单元";
        this.regionValue = "请选择";
      } else {
        //清除选中的所有影像图层
        MapUtil.SceneUtil.removeImageLayersAll();
        //全幅青海
        MapUtil.SceneUtil.entireView();

        dataId = `${data.nodeName}-${data.id}`;
        switch (data.dataType) {
          case 0:
            //console.log(data.checked ? "打开" : "关闭", "  :  影像图层");
            // 清除初始化行政区划
            MapUtils.SceneUtil.removeInitImageLayers();
            MapUtils.SceneUtil.addImageLayerByUrl(
              dataId,
              data.serverUrl,
              true
            );
            break;
          case 9: // 行政区划
            //console.log(data.checked ? "打开" : "关闭", "  :  场景");
            // 清除初始化行政区划
            MapUtils.SceneUtil.removeInitImageLayers();
            // 遍历清除所有scp倾斜摄影
            MapUtils.SceneUtil.removeScpLayersAll();
            MapUtils.SceneUtil.add3DSceneLayerByScp(
              data,
              true
            );
            break;
          default:
            break;
        }
        if (type === "unit") {
          this.unitBtn.name = county.slice(1);
        }
      }
      // 取消已选中的结构树
      bus.$emit('clearChecked')
      //清楚所有照片库实体图标等信息
      MapUtil.SceneUtil.destroyDynamicCluser();

      this.showContent = false;
      this.showUnitContent = false;
      this.pointBtn.active = false;
      this.unitBtn.active = false;
    },
    selOptionEnter(event, item, cityName) {
      this.cityName = cityName;
      this.countryName = item.name;
      if (item.children && item.children.length > 0) {
        this.townContent = item.children;
        this.townWrapperShow = true;
        this.townWrapperTop = event.target.offsetTop;
        this.townWrapperPointLeft =
          event.target.offsetLeft +
          Math.round(event.target.offsetWidth / 2) -
          30;
      } else {
        this.townContent = [];
        this.townWrapperShow = false;
      }
    },
    selOptionOut() {
      this.townWrapperShow = false;
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
      this.showUnitContent = false;
      this.unitBtn.active = false;
      this.showContent = false;
      this.pointBtn.active = false;
      if(btn.name === '对比'){
        this.$router.push({ path: '/view' })
      }
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
        case '对比':
          MapUtil.SceneScreenUtil.initMultiViewPort()
          btn.active = true
          break
        case '清除':
          //清楚测量工具
          MapUtil.SceneUtil.measure3DUtil.deActiveAllMeasure3DControl();
          // 清楚选中的所有MVT矢量瓦片
          //MapUtil.SceneUtil.removeMVTLayersAll();
          //清除选中的所有影像图层
          //MapUtil.SceneUtil.removeImageLayersAll();
          //清除选中的所有倾斜摄影
          //MapUtil.SceneUtil.removeScpLayersAll();
          // 全幅青海
          //MapUtil.SceneUtil.entireView();
          btn.active = true
          break
        case '全幅':
          // 全幅青海
          sceneUtil.entireView();
          btn.active = true
          break
        default:
          break
      }
    }
  }
};
</script>

<style scoped>
.dropSelect {
  position: absolute;
  right: 290px;
  top: 20px;
}

.dropTitle,
.dropTitle:hover,
.dropTitle:focus {
  color: white;
  background: rgba(1, 1, 20, 0.6);
  box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
  border: 0;
  pointer-events: auto;
}

.dropContent {
  position: absolute;
  top: 60px;
  width: 350px;
  height: 450px;
  color: white;
  background: rgba(1, 1, 20, 0.6);
  box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
  border: 0;
  border-radius: 5px;
  pointer-events: auto;
  overflow-y: auto;
  overflow-x: hidden;
}
.dropContent.dropUnit{
  height:auto;
}

.dropContent::-webkit-scrollbar {
  width: 4px;
}

/* 滚动槽 */
.dropContent::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
  border-radius: 10px;
}

/* 滚动条滑块 */
.dropContent::-webkit-scrollbar-thumb {
  border-radius: 2px;
  background: rgba(24, 247, 248, 0.3);
  -webkit-box-shadow: inset 0 0 6px rgba(22, 223, 226, 0.5);
}

.regionCity {
  display: block;
  width: 350px;
  padding: 5px;
}

.regionCityName {
  padding: 5px 10px;
  font-size: 16px;
  color: #a2f8f8;
  background: transparent;
  border: 0;
  pointer-events: none;
}

.regionSplit {
  background-image: linear-gradient(
    to right,
    transparent,
    rgba(24, 247, 248, 0.26),
    transparent
  );
  border: 0;
  height: 3px;
}

.townWrapper {
  position: absolute;
  top: 10px;
  left: 15px;
  width: 320px;
  min-height: 70px;
  background: rgba(0, 0, 0, 0.8);
  box-shadow: 0 0 5px rgba(24, 247, 248, 0.3);
  border-radius: 2px;
  cursor: pointer;
}

.trianglePointer {
  width: 0;
  height: 0;
  margin-top: -16px;
  border-width: 8px;
  border-style: solid;
  border-color: transparent transparent rgba(1, 1, 20, 1) transparent;
  /*border-color: transparent transparent red transparent ;*/
}

.regionOption {
  margin: 3px 0;
  color: white;
  background: transparent;
  border: 0;
}

.regionOption:hover,
.regionOption:focus {
  color: rgba(24, 247, 248, 0.8);
  background: rgba(1, 1, 20, 0.6);
  box-shadow: 0 0 5px rgba(24, 247, 248, 0.2);
  border: 0;
}

.regionOption {
  padding: 5px 10px;
  font-size: 14px;
  /*background: rgba(54, 54, 108, 0.5);*/
  cursor: pointer;
}
.menuTools {
  /*width: 650px;*/
  width:740px;
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
.toolsName{
  display: block;
  width:60px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  word-break: break-all;
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
.img-active{
  transform: rotate(90deg);
  transition: all 0.5s;
  color: #1ce5e6;
}

.toolsBtn img{
  margin: 0 4px;
  transition: all 0.5s;
}
</style>
