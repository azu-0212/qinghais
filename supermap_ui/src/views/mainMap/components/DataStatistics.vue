<template>
  <div class="dataCatalog">
    <div
      class="btn"
      @click="panelShow=!panelShow"
    >
      <img
        :src="dcIcon"
        alt=""
      >
      <span>数据目录</span>
    </div>
    <transition name="el-fade-in-linear">
      <div
        v-show="panelShow"
        class="panel">
<!--        <div :class="['iconData',{'active':item.active||item.hover}]" v-for="item in dataList"
             :key="item.id"
             @mouseover="item.hover=true"
             @mouseout="item.hover=false"
             @click="dlClick(item)"
        >
          <img v-if="item.active||item.hover" :src="item.icon_active" alt="">
          <img v-else :src="item.icon" alt="">
          <p>{{item.name}}<br>{{item.data}}</p>
          <span class="el-icon-arrow-right"></span>
        </div>-->
        <!--倾斜摄影-->
        <div :class="['iconData',{'active':active||hover}]" @click="qxClick('qxList')">
          <img v-if="active|| hover" :src="icon_active1" alt="">
          <img v-else :src="icon1" alt="">
          <p>倾斜摄影数据<br>{{this.qxList.length}}个点/{{this.dataVolumeXSum}}GB</p>
          <span class="el-icon-arrow-right"></span>
        </div>
        <div :class="['iconData',{'active':active||hover}]" @click="qxClick('qjList')">
          <img v-if="active|| hover" :src="icon_active2" alt="">
          <img v-else :src="icon2" alt="">
          <p>全景<br>{{this.qjList.length}}个点/{{this.dataVolumeJSum}}GB</p>
          <span class="el-icon-arrow-right"></span>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
  import DCIcon from '../../../assets/image/dataCatalog/btnIcon.png'
  import MapUtil from '../../../utils/supermap/index'
  import { listInfo } from "@/api/system/info";
  import { listResource } from "@/api/system/resource"

  export default {
    name: 'DataStatistics',
    created() {
      listResource().then( res =>{
        let data = res.rows;
        let qArr = [];
        let jArr = [];
        var dataVolumeXSum = 0;
        var dataVolumeJSum = 0;
        data.forEach((item, index) => {
          if(item.dataType === 1){
            qArr.push(item);
          }
          if(item.dataType === 3){
            jArr.push(item);
          }
        })
        for(let i=0;i<qArr.length;i++){
          dataVolumeXSum += parseFloat(qArr[i].dataVolume);
        }
        for(let i=0;i<jArr.length;i++){
          dataVolumeJSum += parseFloat(jArr[i].dataVolume);
        }

        this.dataVolumeXSum = dataVolumeXSum.toFixed(1);
        this.dataVolumeJSum = dataVolumeJSum.toFixed(1);
        this.qxList = qArr;
        this.qjList = jArr;

        /*this.dataList.map((item,index) => {
          console.log(22,res);
            item.children = res.rows.filter(rst=>{
              return parseInt(rst.dataType) === 1
            })
            console.log(item);
            if(item.children.length>0){
              let dataSize = item.children
                .map(item=>{return parseFloat(item.dataVolume)*1000})
                .reduce((a,b)=>{return a+b})/1000
              item.data = `${item.children.length}个点/${dataSize}GB`
            }else {
              item.data = `0个点/0GB`
            }
        })*/
        //console.log(111,res);
      } )
     /* listInfo().then(res=>{
        this.dataList.map((item,index)=>{
          item.children = res.rows.filter(rst=>{
            return parseInt(rst.type) === index
          })
          console.log(item);
          if(item.children.length>0){
            let dataSize = item.children
              .map(item=>{return parseFloat(item.dataVolume)*1000})
              .reduce((a,b)=>{return a+b})/1000
            item.data = `${item.children.length}个点/${dataSize}GB`
          }else {
            item.data = `0个点/0GB`
          }
        })
      })*/
    },
    data() {
      return {
        panelShow: false,
        dcIcon: DCIcon,
        active:"false",
        hover:"true",
        icon1:require('@/assets/image/dataCatalog/obliquePhoto.png'),
        icon_active1:require('@/assets/image/dataCatalog/obliquePhoto_active.png'),
        icon2:require('@/assets/image/dataCatalog/panorama.png'),
        icon_active2:require('@/assets/image/dataCatalog/panorama_active.png'),
        qxList:[],
        qjList:[],
        dataVolumeXSum:0,
        dataVolumeJSum:0,
        dataList: [
          {
            id: 0,
            name: '倾斜摄影数据',
            icon: require('@/assets/image/dataCatalog/obliquePhoto.png'),
            icon_active: require('@/assets/image/dataCatalog/obliquePhoto_active.png'),
            data: '37个县/900GB',
            active:false,
            hover:false
          },
          {
            id: 1,
            name: '全景数据',
            icon: require('@/assets/image/dataCatalog/panorama.png'),
            icon_active: require('@/assets/image/dataCatalog/panorama_active.png'),
            data: '155个点/75.5GB',
            active:false,
            hover:false
          },
          {
            id: 2,
            name: '街景数据',
            icon: require('@/assets/image/dataCatalog/streetScape.png'),
            icon_active: require('@/assets/image/dataCatalog/streetScape_active.png'),
            data: '0个点/240GB',
            active:false,
            hover:false
          }
        ],
        markers:[
          require("../../../assets/image/mapMarker/pos_0.png"),
          require("../../../assets/image/mapMarker/pos_1.png"),
          require("../../../assets/image/mapMarker/pos_2.png")
        ]
      }
    },
    methods:{
     /* dlClick(item){
        this.panelShow = false;
       /!* if (item.active){
          item.active = false
          MapUtil.SceneUtil.clearEntity()
          return
        }*!/
        this.dataList.map(dl=>{dl.active=(dl.id===item.id)})
        this.panelShow = false;
        let markerIcon = this.markers[item.id]
        let markerData = this.dataList[item.id].children;
        if(markerData&&markerData.length>0){
          MapUtil.SceneUtil.add3DMarkersToMap(markerData,markerIcon)
          MapUtil.SceneUtil.entireView()
        }else {
          item.active = false
          this.$alert("暂无点位,请通过后台导入!", '提示信息')
        }
        //描点
      },*/
      qxClick(type){
        this.panelShow = false;
        let markerIcon;
        let markerData;
        if(type === 'qxList'){
          markerIcon = require("../../../assets/image/mapMarker/pos_0.png");
          markerData = this.qxList;
        }else{
          markerIcon = require("../../../assets/image/mapMarker/pos_1.png");
          markerData = this.qjList;
        }
        if(markerData && markerData.length>0){
          MapUtil.SceneUtil.add3DMarkersToMap(markerData,markerIcon,true); //没有勾选项
          MapUtil.SceneUtil.entireView();//跳转全幅青海
        }else {
          //item.active = false
          this.$alert("暂无点位,请通过后台导入!", '提示信息')
        }
      }
    }
  }
</script>

<style scoped>
  .dataCatalog {
    position: absolute;
    display: flex;
    left: 20px;
    top: 180px;
    pointer-events: none;
  }

  .btn {
    width: 60px;
    height: 60px;
    font-size: 12px;
    text-align: center;
    color: #16dfe0;
    border-radius: 8px;
    background: rgba(1, 1, 20, 0.6);
    box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
    pointer-events: auto;
    cursor: pointer;
  }

  .btn img {
    margin: 7px 17px 7px;
  }

  .panel {
    margin-left: 20px;
    width: 230px;
    height: 130px;
    border-radius: 8px;
    background: rgba(1, 1, 20, 0.6);
    box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
    pointer-events: auto;
    cursor: pointer;
  }

  .iconData {
    margin: 5px 0;
    display: flex;
    justify-content: space-between;
    font-size: 14px;
    color: white;
    height: 60px;
  }

  .iconData.active{
    color: #16dfe0;
  }

  .iconData img {
    padding: 12px 10px;
    margin: 0 5px;
  }
  .iconData span{
    margin: 0 5px;
    line-height: 60px;
  }
</style>
