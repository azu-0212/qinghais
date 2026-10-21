<template>
  <div class="dropSearch" ref="dropSearch">
    <div v-show="true" class="menuTools">
      <el-input
        popper-class="my-autocomplete"
        v-model="state"
        clearable
        :fetch-suggestions="querySearch"
        @blur="blur"
        @focus="focus"
        @clear="clear"
        @select="handleSelect"
        placeholder="输入关键字搜索"
      >
        <el-button slot="append" icon="el-icon-search" @click="btnSelect"></el-button>
      </el-input>
      <transition name="el-fade-in-linear">
        <div v-show="showContent" class="dropContent">
          <div class="regionCity">
            <div class="regionItem" v-for="item in restaurants" @click="selectedThis(item.fieldValues[7],item)">
              <span class="regionCityName" >{{ item.fieldValues[7] }}</span>
<!--              <span class="regionOption">{{ item.fieldValues[15] }}</span>-->
            </div>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script>
import MapUtil from "@/utils/supermap";
import bus from "@/utils/bus";
import MapUtils from "@/utils/supermap";

export default {
  name: "RegionSearch",
  data(){
    return{
      restaurants: [],
      state: '',
      data:{},
      cbResults:[],
      interestPointList:[],
      obj:{},
      showContent:false,
      markers:[
        require("@/assets/image/mapMarker/pos_1.png")
      ],
      marker:"",
      userRole:this.$store.state.user.roles
    }
  },
  watch: {
    state(val) {
      if(val!=null && val === ""){
        //this.restaurants = this.interestPointList;
        //全幅青海
        MapUtil.SceneUtil.entireView();
        MapUtil.SceneUtil.clearEntity();
        return;
      }
      MapUtil.SceneUtil.getInterestPoint(val);
      setTimeout(() => {
        this.restaurants = MapUtil.SceneUtil.interestPoints;
        this.restaurants.map((item) => {
          if(this.restaurants.length > 0 && val!==item.fieldValues[7]){
            this.showContent = true;
            return;
          }
        })
      },1300)
     /* let restaurants = this.restaurants;
      let results = val ? restaurants.filter(this.createFilter(val)) : restaurants;
      // 设置建议列表数据
      this.restaurants = results;*/
    },
  },
  methods:{
    selectedThis(val, data){
      this.state = val;
      this.data = data;
      this.showContent = false;
      this.handleIconClick();
    },
    /*getRestatuants(){
      //查询兴趣点服务中的数据信息
      this.interestPointList = MapUtil.SceneUtil.interestPoints;
      //console.log('兴趣点',this.interestPointList);
      this.restaurants = this.interestPointList;
    },*/
    focus(queryString, cb){
      //清除所有实体图标
      MapUtil.SceneUtil.clearEntity();
      //清除所有照片库实体图标等信息
      MapUtil.SceneUtil.destroyDynamicCluser();
      // 清楚选中的所有MVT矢量瓦片
      MapUtil.SceneUtil.removeMVTLayersAll();
      //清除选中的所有影像图层
      MapUtil.SceneUtil.removeImageLayersAll();
      //清楚测量工具
      MapUtil.SceneUtil.measure3DUtil.deActiveAllMeasure3DControl();
      //清除选中的所有倾斜摄影
      MapUtil.SceneUtil.removeScpLayersAll();
      // 清除高亮
      MapUtil.SceneUtil.clearHighLightAll();
      // 禁止飞行
      if(MapUtils.SceneUtil.flyManager && MapUtils.SceneUtil.flyManager!=undefined){
        MapUtils.SceneUtil.flyManager.stop();
      }
      // 全幅青海
      MapUtils.SceneUtil.entireView();
      // 隐藏右侧面板
      bus.$emit('closeRightPanel')
      // 隐藏左侧面板
      bus.$emit('closeLeftPanel')
/*
      this.getRestatuants();
      if(this.restaurants.length > 0){
        this.showContent = true;
        return;
      }*/
    },
    blur(){
      this.showContent = false;
    },
    clear(){
      this.showContent = false;
    },
    //清除输入框内容
    removeState(){
      this.state = '';
    },
    querySearch(queryString, cb) {
      var restaurants = this.restaurants;
      var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
      // 设置建议列表数据
      this.cbResults = results;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (restaurant) => {
        if(restaurant.fieldValues[7].toLowerCase().search(queryString.toLowerCase()) !== -1){
          return restaurant.fieldValues[7].toLowerCase();
        }
      };
    },
    loadAll() {
      return [
        { "value": "敬业时代", "address": "陕西省显示雁塔区" },
      ];
    },
    handleSelect(item) {
      this.obj = {};
      this.obj = item;
    },
    btnSelect(){

    },
    handleIconClick() {
      //let typeCode = parseInt(this.data.fieldValues[34]);
      if(this.state === '' || this.state === null){
        this.$alert("暂无输入行政区划,请输入行政区划!", '提示信息');
        this.obj = {};
      }else if(this.restaurants.length < 1){
        this.$alert("无此行政区划,请输入正确行政区划!", '提示信息');
        this.obj = {};
      }else{
        let position = {
          lon:parseFloat(this.data.fieldValues[17]),
          lat:parseFloat(this.data.fieldValues[18]),
          alt:3500
        }
        this.data.position = position;
        this.data.text = this.data.fieldValues[7];
        this.data.name = this.data.fieldValues[7];
        this.getCurrentMarker();
        return;
      }
    },
    getCurrentMarker(){
      MapUtil.SceneUtil.addInterestMarkersToMap(this.data,require("@/assets/image/pointMarkers/Address_normal.png") ,true)
    },
  },
  mounted() {
    /* 管理员权限 */
    if(this.userRole[0].indexOf("admin")!==-1){
      return;
    }else{
      this.$refs['dropSearch'].style.right = '890px';
    }
    bus.$on('removeRegionSearch', this.removeState)
    //this.getRestatuants();
  }
}
</script>

<style scoped >
.regionCity {
  display: block;
  width: 350px;
  padding: 5px;
}
.regionItem{
  line-height:32px;
}
.regionOption {
  margin-left:6px;
  overflow: hidden;;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-size:12px;
  color: #BBDDFF;
  background: transparent;
  border: 0;
}

.regionCityName{
  padding: 5px 10px;
  font-size: 16px;
  color: #FFF;
  background: transparent;
  border: 0;
  pointer-events: none;
}
.dropContent {
  position: absolute;
  top: 60px;
  width: 350px;
  height: 300px;
  color: white;
  background: rgba(1, 1, 20, 0.6);
  box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
  border: 0;
  border-radius: 5px;
  pointer-events: auto;
  overflow-y: auto;
  overflow-x: hidden;
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


.el-input {
  width: 350px;
  border-radius: 5px;
  box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
}
.my-autocomplete li{
  line-height: 30px;
  /*padding: 7px;*/
}
.my-autocomplete .name{
  text-overflow: ellipsis;
  overflow: hidden;
  margin-right:5px;
}
.my-autocomplete .addr{
  font-size: 12px;
  color: #b4b4b4;
}
.my-autocomplete .highlighted .addr {
  color: #ddd;
}

.dropSearch {
  position: absolute;
  right: 1020px;
  top: 20px;
}
>>> .el-input-group__append{
  background: rgba(1, 1, 20, 0.6);
  border:none;
  /* box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);*/
}
>>> .el-input--medium .el-input__inner{
  height:48px;
  line-height: 48px;
}
>>> .el-input__inner:focus{
  border:none;
}
>>> .el-input__inner{
  background: rgba(1, 1, 20, 0.6);
  border: none;color:#FFF;
}
>>> .el-input__suffix{
  right:10px;
}
>>> .el-autocomplete-suggestion{
  background:red;
}
.my-autocomplete >>> .el-popper[x-placement^="bottom"] .popper__arrow{
  top:0;
}


/*>>> .el-input__inner::-webkit-input-placeholder{
  color:#FFF;
}
>>> .el-input__inner:-moz-placeholder{
  color:#FFF;
}
>>> .el-input__inner::-moz-placeholder{
  color:#FFF;
}
>>> .el-input__inner:-ms-input-placeholder{
  color:#FFF;
}
>>> .el-input__prefix{
  color:#fff;
}*/
.treeSearch {
  border-radius: 2px;
}
.menuTools {
  width:370px;
  height: inherit;
  border-radius: 5px;
  /*box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);*/
  pointer-events: auto;
  cursor: pointer;
}
</style>
