<template>
    <div class="statusBar" v-show="posMsg">
      <span>纬度 : {{posMsg.lat}}</span>
      <span>经度 : {{posMsg.lng}}</span>
      <span>高程 : {{posMsg.alt}}</span>
    </div>
</template>

<script>
  export default {
    name: 'statusBar',
    data(){
      return {
        posMsg:false,
      }
    },
    mounted() {
      setInterval(()=>{
        this.posMsg = JSON.parse(sessionStorage.getItem("posMsg"))
      },100)
    }
  }
</script>

<style scoped>
  .statusBar{
    position: absolute;
    padding: 3px;
    margin-bottom: -3px;
    bottom: 0;
    left: 230px;
    width: 320px;
    height: 38px;
    color: white;
    line-height: 35px;
    font-size: 14px;
    border-radius: 5px;
    background: rgba(1, 1, 20, 0.6);
    box-shadow: 0 0 15px rgba(1, 1, 20, 0.7);
    border: 0;
  }
  .statusBar span{
    display: block;
    float: left;
    width: calc(33% - 1px);
    text-align: center;
    /*background: red;*/
    /*border-right: 1px solid red;*/
    pointer-events: none;
  }
</style>
