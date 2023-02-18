<template>
  <div>
    <div>
      <el-container>
        <el-header>
          <!--          <img class="logo" src="../assets/logo.png" />-->
          <span style="font-size: 30px; font-weight: bold">科研数据管理系统</span>
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
            <el-form-item label="验证码">
              <div style="display: flex; justify-content: space-around">
                <el-input v-model="loginForm.authCode" placeholder="请输入验证码"></el-input>
                <el-button @click="getAuthCode()" class="genAuthCodeBut"> 刷新 </el-button>
                <el-input v-model="sysAuthCode" disabled class="sysAuthCode"></el-input>
              </div>
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
        uniqueLoginId: "",
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
  created() {
    this.getAuthCode()
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          // 登录处理逻辑
          login(this.loginForm).then((res) => {
            const userInfo = res.data.userInfo;
            const token = res.data.token;
            // 把用户信息和token共享出去
            this.$store.commit("SET_USERINFO", userInfo);
            this.$store.commit("SET_TOKEN", token);
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
      generateAuthCode().then((res) => {
        this.sysAuthCode = res.data.sysAuthCode;
        this.loginForm.uniqueLoginId = res.data.uniqueLoginId
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

.genAuthCodeBut {
  margin-left: 5px;
}
.sysAuthCode {
  margin-left: 5px;
  width: 140px;
}
</style>
