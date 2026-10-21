<template>
  <div>
    <div class="content-year">
      <div class="year-toggle">
        <a href="#" v-for="(btn, index) in years"
           :key="index * 1.1"
           :class="[{ active: btn.choose || btn.active },{'no-data': btn.isNull}]"
           @mouseover="btn.active = true"
           @mouseout="btn.active = false"
           @click="handleSwitchYear(btn)"
        >{{ btn.yearName }}
        </a>
      </div>
      <div class="panel-total">
        <span class="name">三维模型总量</span>
        <span class="class"><b>{{ datas.modelTotal.num }}</b>个</span>
        <span class="volumn"><b>{{ datas.modelTotal.size }}</b>GB</span>
        <strong/>
        <strong/>
        <strong/>
        <strong/>
      </div>
      <div class="panel-total">
        <span class="name">三维模型成果</span>
        <span class="class"><b>{{ datas.modelResult.num }}</b>处</span>
        <span class="volumn"><b>{{ datas.modelResult.size }}</b>GB</span>
        <strong/>
        <strong/>
        <strong/>
        <strong/>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "YearSwitch",
  data() {
    return {
      datas:
        {
          modelTotal: {
            num: 66,
            size: 540
          },
          modelResult: {
            num: 21,
            size: 215
          }
        }
      ,
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
          isNull: false
        }
      ]
    }
  },
  methods: {
    handleSwitchYear(year){
      this.years.map((item) => {
        item.choose = false;
        item.active = false;
      });
      year.choose = true;
      if(year.yearName === 2017){
        this.datas.modelTotal.num = 33;
        this.datas.modelTotal.size = 425;
        this.datas.modelResult.num = 10;
        this.datas.modelResult.size = 124;
      }else{
        this.datas.modelTotal.num = 66;
        this.datas.modelTotal.size = 540;
        this.datas.modelResult.num = 21;
        this.datas.modelResult.size = 215;
      }
    }
  }
}
</script>

<style scoped>
.content-year{
  width: 256px;
  float: left;
}
.year-toggle{
  pointer-events: auto;
  overflow-x: scroll;
  overflow-y: hidden;
  white-space: nowrap;
  width:100%;
  margin-top: 10px;
  margin-bottom:12px;
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
  margin-left:30px;
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
.panel-total{
  height:32px;
  width:98%;
  background:rgba(28, 214, 215, .1);
  position:relative;
  color:#FFF;
  line-height:32px;
  font-size:14px;
  margin-bottom:20px;
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
</style>
