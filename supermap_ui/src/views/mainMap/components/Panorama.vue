<template>
  <div class="fullScene">
    <el-button v-show="btnShow" class="exitPanorama" @click="exitPanorama"> 退出 </el-button>
    <iframe class="fullSceneFrame" :src="panoramaUrl" frameborder="0"></iframe>
  </div>
</template>

<script>
  export default {
    name: 'panorama',
    data(){
      return{
        btnShow:false
      }
    },
    computed:{
      panoramaUrl(){
        return this.$store.getters.panorama_url
      }
    },
    mounted() {
      setTimeout(()=>{
        this.btnShow = true
      },3000)
    },
    methods:{
      exitPanorama(){
        this.$store.commit('supermap/SET_PANORAMA_SHOW',false)
        this.btnShow = false
      }
    }
  }
</script>

<style scoped>
  .fullScene{
    position: absolute;
    top: 0;
    width: 100%;
    height: 100%;
    z-index: 1001;
    pointer-events: auto;
  }
  .exitPanorama{
    position: absolute;
    left: 570px;
    top: 60px;
    background: rgba(1, 1, 20, 0.6);
    box-shadow: 0 0 5px rgba(1, 1, 20, 0.7);
    border: 0;
    color: white;
  }
  .exitPanorama:hover{
    color: #16dfe0;
    transform: scale(1.1);
  }
  .fullSceneFrame{
    width: 100%;
    height: 100%;
  }
</style>
