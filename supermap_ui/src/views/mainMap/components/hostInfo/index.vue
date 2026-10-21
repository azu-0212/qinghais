<template>
  <div class="hostInfo">
    <p>主办：{{ appCfg.hostPage.host }}    技术支持：{{appCfg.hostPage.support}}</p>
  </div>
</template>

<script>
  import { mapState } from 'vuex';
  export default {
    name: "hostInfo",
    data(){
      return{

      }
    },
    computed: {
      ...mapState({
        appCfg: state => state.app.appCfg
      })
    },
  }
</script>

<style scoped>
  .hostInfo{
    position:absolute;
    z-index: 10;
    color:#000;
    bottom:0;
    width:100%;
    height:26px;
    background-image: linear-gradient(to right, rgba(255, 255, 255, 0),rgba(255, 255, 255, .2), rgba(255, 255, 255, 0));
  }
  .hostInfo p{
    text-align: center;
    margin:0;
    line-height:26px;
    font-size:12px;
  }
</style>
