<template>
  <div>
    <div>
      <el-container>
        <el-header>
          <!--          <img class="logo" src="../assets/logo.png" />-->

          <span style="font-size: 30px; font-weight: bold">地学综合平台管理系统</span>
        </el-header>
        <el-main>
          <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="100px" class="loginForm">
            <el-form-item label="用户名">
              <el-input v-model="loginForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码">
              <el-input
                  type="password"
                  v-model="loginForm.password"
              ></el-input>
            </el-form-item>
            <el-form-item >
              <div style="display: flex; justify-content: space-around">
                <el-input v-model="sysAuthCode" disabled></el-input>
                <el-button @click="getAuthCode()"> 生成验证码 </el-button>
              </div>
            </el-form-item>
            <el-form-item label="验证码">
              <el-input v-model="loginForm.authCode" placeholder="请输入系统生成的验证码"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleLogin()">
                登录
              </el-button>
              <el-button @click="resetForm()">重置</el-button>
            </el-form-item>
          </el-form>
        </el-main>
      </el-container>
    </div>
  </div>
</template>

<script>
import { login } from "@/api/login";
import { generateAuthCode } from "@/api/login";

export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        username: "admin",
        password: "111111",
        authCode: "",
      },
      sysAuthCode: "",
      rules: {
        name: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          {
            min: 3,
            max: 15,
            message: "长度在 3 到 15 个字符",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "change" },
        ],
      },
      loading: false,
    };
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          // 登录处理逻辑
          login(this.loginForm).then((res) => {
            const jwt = res.data.jwt;
            const userInfo = res.data.user;
            // 把token和用户信息共享出去
            this.$store.commit("SET_TOKEN", jwt);
            this.$store.commit("SET_USERINFO", userInfo);
            this.$router.push({ path: "/" });
            this.loading = false;
          });
        } else {
          console.log("登录失败!!!");
          return false;
        }
      });
    },
    resetForm() {
      this.$refs.loginForm.resetFields();
    },
    getAuthCode() {
      generateAuthCode(this.loginForm.username).then((res) => {
        this.sysAuthCode = res.data;
      });
    },
  },
};
</script>

<style scoped>
.el-header,
.el-footer {
  /*background-color: #b3c0d1;*/
  color: #000;
  margin-top: 5%;
  margin-bottom: 3%;
  text-align: center;
  line-height: 60px;
}

.el-main {
  /*background-color: #E9EEF3;*/
  color: #333;
  /*margin-top: 100px;*/
  text-align: center;
  line-height: 400px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.logo {
  height: 100%;
  margin-top: 30px;
}

.loginForm {
  max-width: 40%;
  margin-left: 26%;
}
</style>
