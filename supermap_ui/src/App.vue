<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
  import request from '@/utils/request'

  export default {
    name: 'App',
    data(){
      return {
        title:""
      }
    },
    created() {
      // 初始化用户不登陆
      this.$store.state.user.login = '';
      request({
        url: '/baseCfg',
        headers:{
          isToken:false
        },
        method: 'get',
      }).then(res=>{
        if(res.code === 200 && res.data){
          const appCfg = JSON.parse(res.data.configJson);
          this.$store.commit('app/SET_APP_CFG', appCfg);
          this.title = appCfg.title||"超图项目";
          document.title = this.title;
        }
      })
    }
  }
</script>

<style>
  .regionPopoverContent {
    padding: 5px;
    background: rgba(1, 1, 20, 0.8);
    border: 0;
  }

  .el-popover.regionPopoverContent .popper__arrow::after {
    border-bottom-color: rgba(1, 1, 20, 0.8);
  }

  .regionPopoverContent button {
    padding: 5px 10px;
    font-size: 14px;
    color: white;
    background: transparent;
    border: 0;
  }

  .regionPopoverContent button:hover, .regionPopoverContent button:focus {
    color: rgba(24, 247, 248, 0.8);
    background: rgba(1, 1, 20, 0.6);
    box-shadow: 0 0 5px rgba(24, 247, 248, 0.2);
    border: 0;
  }
</style>
