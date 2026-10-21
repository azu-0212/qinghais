<template>
  <div>
    <div class="moduleWrapper">
      <!-- 标题 -->
      <p class="module-title">
        其它场景
        <span class="btns">
           <a href="#" class="btn" v-for="(btn, index) in btns"
              :key="index * 1.1"
              :class="{ active: btn.choose || btn.active }"
              @mouseover="btn.active = true"
              @mouseout="btn.active = false"
              @click="handleClickScene(btn)"
           >{{ btn.name }}
        </a>
        </span>
      </p>
      <Panorama v-if="chartPanShow"/>
      <Street v-if="chartStreeShow"/>
    </div>
  </div>
</template>

<script>
import Street from "./other/Street";
import Panorama from "./other/Panorama"

export default {
  name: "Other",
  data() {
    return {
      btns: [
        {
          name: '全景',
          active: true,
          choose: true
        },
        {
          name: '街景',
          active: false,
          choose: false
        }
      ],
      chartPanShow: true,
      chartStreeShow: false
    }
  },
  components: {
    Street,
    Panorama
  },
  methods: {
    handleClickScene(btns){
      this.btns.map((item) => {
        item.choose = false;
        item.active = false;
      });
      btns.choose = true;
      if(btns.name.indexOf('街景')!==-1){
        this.chartStreeShow = true;
        this.chartPanShow = false;
      }else{
        this.chartStreeShow = false;
        this.chartPanShow = true;
      }
    }
  }
}
</script>

<style scoped>
.moduleWrapper{
  width: 19.8%;
  float: left;
  height:240px;
}
.module-title{
  position: relative;
  line-height: 30px;
  font-size: 14px;
  padding-left: 10px;
  color: #fff;
  border: 1px solid #1cd9da;
  width: 100%;
  background-image: linear-gradient(to left, rgba(28, 214, 215, 0) 10%,rgba(28, 214, 215, .3) 70%);
}
.module-title .btns{
  position: absolute;
  right: 5px;
  top:1px;
}
.module-title .btn{
  display: inline-block;
  width: 40px;
  height:22px;
  border: 1px solid #3f6078;
  color:#FFF;
  font-size: 14px;
  line-height: 22px;
  text-align: center;
}

.module-title .btn.active{
  border: 1px solid #1cdadb;
  color: #1cdadb;
}
p{
  margin: 0;
}
</style>
