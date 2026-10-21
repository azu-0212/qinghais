<template>
  <!-- 用户统一认证登录页 -->
  <div class="login">

    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
    >
      <h3 class="title">{{"用户统一认证"}}</h3>
      <div class="contentWrapper">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            type="text"
            auto-complete="off"
            placeholder="账号"
          >
            <svg-icon
              slot="prefix"
              icon-class="user"
              class="el-input__icon input-icon"
            />
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            auto-complete="off"
            placeholder="密码"
            @keyup.enter.native="handleLogin"
          >
            <svg-icon
              slot="prefix"
              icon-class="password"
              class="el-input__icon input-icon"
            />
          </el-input>
        </el-form-item>
        <el-checkbox
          v-model="loginForm.rememberMe"
          style="margin:0px 0px 25px 0px;"
        >记住密码</el-checkbox>
        <el-form-item style="width:100%;">
          <el-button
            class="submitBtn"
            :loading="loading"
            size="medium"
            type="primary"
            style="width:100%;"
            @click.native.prevent="handleLogin"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登 录 中...</span>
          </el-button>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import { getCodeImg } from '@/api/login'
import Cookies from 'js-cookie'
import { authenticationLogin } from '@/api/authen_login'
import store from "../store";

export default {
  name: 'Login',
  data() {
    return {
      codeUrl: '',
      cookiePassword: '',
      loginForm: {
        username: '',
        password: '',
        rememberMe: false
      },
      loginRules: {
        username: [
          { required: true, trigger: 'blur', message: '用户名不能为空' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '密码不能为空' }
        ]
      },
      loading: false,
      redirect: '',
    }
  },
  created() {
    this.getCookie();
  },
  methods: {
    getCookie() {
      const username = Cookies.get('authenticationName')
      const password = Cookies.get('authenticationPassword')
      const rememberMe = Cookies.get('authenticationRememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : password,
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      }
    },
    handleLogin() {
      let _this = this;
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.loginForm.rememberMe) {
            Cookies.set('authenticationName', this.loginForm.username, { expires: 30 })
            Cookies.set('authenticationPassword', this.loginForm.password, { expires: 30 })
            Cookies.set('authenticationRememberMe', this.loginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove('authenticationName')
            Cookies.remove('authenticationPassword')
            Cookies.remove('authenticationRememberMe')
          }
          /* 账户认证  认证成功设置store login参数为OK */
          this.$store
            .dispatch('AuthLogin', this.loginForm)
            .then((res) => {
              if(res.msg.result === "ok"){
                this.$router.push({ path: '/login' })
                return;
              }else{
                _this.$message.error(res.msg.msg)
              }
              setTimeout(function(){
                _this.loading = false;
                _this.reset();
              },2000)
            })
            .catch(() => {
              this.loading = false
            })
        }
      })
    },
    reset(){
      this.loginForm = {
        username : undefined,
        password : undefined,
        rememberMe : undefined
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/image/login/authen_bg_img.jpg");
  background-size: cover;
}

.login-form {
  position: relative;
  top:-120px;
  border-radius: 6px;
  background: transparent;
  width: 900px;
  padding: 25px 25px 5px 25px;
  .title{
    position: absolute;
    top: 0;
    width: 100%;
    margin: 0 auto 30px auto;
    text-align: center;
    font-size: 42px;
    letter-spacing: 10px;
    color: #f2f2f2;
  }
  .contentWrapper{
    position: absolute;
    top: 80px;
    left: calc(50% - 130px);
    width: 300px;
    .el-input {
      height: 38px;
      input {
        height: 38px;
      }
    }
    .input-icon {
      height: 39px;
      width: 14px;
      margin-left: 2px;
    }
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
</style>
