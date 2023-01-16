<template>
  <div>
    <h2>管理员登录</h2>
    <div>
      <el-container>
        <el-header>
          <img class="logo" src="http://hexo.li98.cn/img/snail2.png" />
        </el-header>
        <el-main>
          <el-form
            :model="loginForm"
            :rules="rules"
            ref="loginForm"
            label-width="100px"
            class="demo-loginForm"
          >
            <el-form-item label="用户名" prop="username">
              <el-input v-model="loginForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" v-model="loginForm.password"></el-input>
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

export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        username: "",
        password: "",
      },
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
            console.log(res);
            const jwt = res.data.jwt;
            const userInfo = res.data.user;
            // 把token和用户信息共享出去
            this.$store.commit("SET_TOKEN", jwt);
            this.$store.commit("SET_USERINFO", userInfo);
            this.$router.push({ path: "/dashboard" });
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
  },
};
</script>

<style scoped>
.el-header,
.el-footer {
  /*background-color: #B3C0D1;*/
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  /*background-color: #E9EEF3;*/
  color: #333;
  margin-top: 100px;
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
  height: 200%;
  margin-top: 30px;
}

.demo-loginForm {
  max-width: 30%;
  margin-left: 32%;
}
</style>
